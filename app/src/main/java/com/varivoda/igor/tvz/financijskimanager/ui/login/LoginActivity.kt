package com.varivoda.igor.tvz.financijskimanager.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.databinding.ActivityLoginBinding
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.Result
import com.varivoda.igor.tvz.financijskimanager.util.toast


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loginViewModelFactory: LoginViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        loginViewModelFactory = LoginViewModelFactory(Preferences(this))
        loginViewModel = ViewModelProvider(this,loginViewModelFactory).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel
        observeLoginSuccess()
    }

    private fun observeLoginSuccess() {
        loginViewModel.loginSuccess.observe(this, Observer {
            if(it==null) return@Observer
            when(it){
                is Result.Success -> {
                    startActivity(Intent(this,HomeActivity::class.java))
                    finish()
                }
                is Result.NoNetworkConnection -> {
                    this.toast(getString(R.string.no_internet))
                }
                is Result.Error -> {
                    this.toast(getString(R.string.general_error))
                }

            }
            loginViewModel.loginSuccess.value = null
        })
    }
}