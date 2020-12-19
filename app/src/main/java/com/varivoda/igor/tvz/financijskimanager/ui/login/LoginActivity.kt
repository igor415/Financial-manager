package com.varivoda.igor.tvz.financijskimanager.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AnticipateOvershootInterpolator
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.databinding.ActivityLoginBinding
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import com.varivoda.igor.tvz.financijskimanager.util.showSelectedToast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var preferences: Preferences? = null
    private val loginViewModel by viewModels<LoginViewModel> {
        LoginViewModelFactory((applicationContext as App).preferences,(applicationContext as App).loginRepository)
    }
    private val constraintSetLoginProgress = ConstraintSet()
    private val constraintFirst = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.viewModel = loginViewModel
        constraintSetLoginProgress.clone(this,R.layout.activity_login_alt)
        constraintFirst .clone(this,R.layout.activity_login)
        observeLoginSuccess()
        preferences = Preferences(applicationContext)
        doAnimation()
    }

    private fun doAnimation() {
        loginViewModel.doAnimation.observe(this, Observer {
            if(it==null) return@Observer
            val transition = ChangeBounds()
            transition.interpolator = AnticipateOvershootInterpolator(0.5f)
            TransitionManager.beginDelayedTransition(root,transition)
            constraintSetLoginProgress.applyTo(root)
            guidelineHorizontal.setGuidelinePercent(0.4f)
            loginViewModel.doAnimation.value = null
        })
    }

    private fun backToFirstLayout(){
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(0.5f)
        TransitionManager.beginDelayedTransition(root,transition)
        constraintFirst.applyTo(root)
        guidelineHorizontal.setGuidelinePercent(0.47f)
    }

    private fun observeLoginSuccess() {
        loginViewModel.loginSuccess.observe(this, Observer {
            if(it==null) return@Observer
            when(it){
                is NetworkResult.Success -> {
                    if(loginViewModel.rememberMe){
                        preferences?.setCachedPassword(loginViewModel.currentPassword)
                        preferences?.setCachedUsername(loginViewModel.currentUsername)
                        preferences?.setRememberMe(loginViewModel.rememberMe)
                    }
                    startActivity(Intent(this,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                }
                is NetworkResult.NoNetworkConnection -> {
                    showSelectedToast(this,getString(R.string.no_internet))
                    backToFirstLayout()
                }
                is NetworkResult.Error -> {
                    if(it.exception.message=="401"){
                        showSelectedToast(this,getString(R.string.wrong_username_or_password))
                    }else{
                        showSelectedToast(this,getString(R.string.general_error))
                    }
                    listOf("username key","password key","remember me").map { item -> preferences?.clear(item) }
                    backToFirstLayout()
                }

                else -> {
                    showSelectedToast(this,getString(R.string.general_error))
                    listOf("username key","password key","remember me").map { item ->preferences?.clear(item) }
                    backToFirstLayout()
                }
            }
            loginViewModel.loginSuccess.value = null
        })
    }
}