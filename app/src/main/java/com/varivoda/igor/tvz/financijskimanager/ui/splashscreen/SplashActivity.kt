package com.varivoda.igor.tvz.financijskimanager.ui.splashscreen

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.databinding.SplashActivityBinding
import com.varivoda.igor.tvz.financijskimanager.ui.login.LoginActivity
import kotlinx.android.synthetic.main.splash_activity.*

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: SplashActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.splash_activity)

        logoAnimation()
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        }, 1300)
    }

    private fun logoAnimation() {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,1.5f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,1.5f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(binding.mainLogo,scaleX,scaleY)
        animator.apply {
            duration = 1000
        }.also {
            it.start()
        }
    }
}