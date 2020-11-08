package com.varivoda.igor.tvz.financijskimanager.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.databinding.ActivityLoginBinding
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.Result
import com.varivoda.igor.tvz.financijskimanager.util.showSelectedToast
import com.varivoda.igor.tvz.financijskimanager.util.toast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginViewModelFactory: LoginViewModelFactory
    private val constraintSetLoginProgress = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        loginViewModelFactory = LoginViewModelFactory(Preferences(this))
        loginViewModel = ViewModelProvider(this,loginViewModelFactory).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel
        constraintSetLoginProgress.clone(this,R.layout.activity_login_alt)
        observeLoginSuccess()
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

    private fun observeLoginSuccess() {
        loginViewModel.loginSuccess.observe(this, Observer {
            if(it==null) return@Observer
            when(it){
                is Result.Success -> {
                    startActivity(Intent(this,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                }
                is Result.NoNetworkConnection -> {
                    showSelectedToast(this,getString(R.string.no_internet))
                }
                is Result.Error -> {
                    showSelectedToast(this,getString(R.string.general_error))
                }

            }
            loginViewModel.loginSuccess.value = null
        })
    }
}