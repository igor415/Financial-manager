package com.varivoda.igor.tvz.financijskimanager.ui.login

import android.Manifest
import android.app.KeyguardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.varivoda.igor.tvz.financijskimanager.App
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import com.varivoda.igor.tvz.financijskimanager.databinding.ActivityLoginBinding
import com.varivoda.igor.tvz.financijskimanager.ui.home.HomeActivity
import com.varivoda.igor.tvz.financijskimanager.util.FingerprintHandler
import com.varivoda.igor.tvz.financijskimanager.util.NetworkResult
import com.varivoda.igor.tvz.financijskimanager.util.showSelectedToast
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey


const val KEY = "my_key"
const val FINGERPRINT_PERMISSION = 111

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferences: Preferences
    private val loginViewModel by viewModels<LoginViewModel> {
        LoginViewModelFactory(
            (applicationContext as App).preferences,
            (applicationContext as App).loginRepository,
            applicationContext
        )
    }
    private val constraintSetLoginProgress = ConstraintSet()
    private val constraintFirst = ConstraintSet()

    private var cipher: Cipher? = null
    private lateinit var keyStore: KeyStore
    private lateinit var keyGenerator: KeyGenerator
    private var cryptoObject: FingerprintManager.CryptoObject? = null
    private var fingerprintManager: FingerprintManager? = null
    private var keyguardManager: KeyguardManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = loginViewModel
        constraintSetLoginProgress.clone(this, R.layout.activity_login_alt)
        constraintFirst.clone(this, R.layout.activity_login)
        observeLoginSuccess()
        preferences = Preferences(applicationContext)
        doAnimation()
        checkIfFingerPrintIsPossible()
    }

    private fun checkIfFingerPrintIsPossible() {
        keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        try {
            fingerprintManager = getSystemService(FINGERPRINT_SERVICE) as FingerprintManager
        } catch (ex: java.lang.Exception) {
            Timber.d(ex)
            return
        }
        if (fingerprintManager!!.isHardwareDetected()) {

            if (fingerprintManager!!.hasEnrolledFingerprints() && keyguardManager!!.isKeyguardSecure() && preferences.getFingerprintOption()) {
                orLayout.visibility = View.VISIBLE
                fingerPrintText.visibility = View.VISIBLE
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.USE_FINGERPRINT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.USE_FINGERPRINT),
                        FINGERPRINT_PERMISSION
                    )
                } else {
                    generateAndStartAuthentication()
                }
            }
        }


        //Check that the user has registered at least one fingerprint//
        /*  if (!fingerprintManager.hasEnrolledFingerprints()) {
              // If the user hasn’t configured any fingerprints, then display the following message//
              textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
          }*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == FINGERPRINT_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                generateAndStartAuthentication()
            }
        } else {
            showSelectedToast(
                this,
                "You need to give permission for fingerprint if you want to authenticate in that way."
            )
        }
    }

    private fun generateAndStartAuthentication() {
        try {
            generateKey()
        } catch (ex: Exception) {
            Timber.d(ex)
        }
        if (initCipher()) {
            //If the cipher is initialized successfully, then create a CryptoObject instance//
            cryptoObject = FingerprintManager.CryptoObject(cipher!!)

            // Here, I’m referencing the FingerprintHandler class that we’ll create in the next section. This class will be responsible
            // for starting the authentication process (via the startAuth method) and processing the authentication process events//
            val helper = FingerprintHandler(this, loginViewModel)
            helper.startAuth(fingerprintManager!!, cryptoObject)
        }
    }

    private fun doAnimation() {
        loginViewModel.doAnimation.observe(this, Observer {
            if (it == null) return@Observer
            val transition = ChangeBounds()
            transition.interpolator = AnticipateOvershootInterpolator(0.5f)
            TransitionManager.beginDelayedTransition(root, transition)
            constraintSetLoginProgress.applyTo(root)
            guidelineHorizontal.setGuidelinePercent(0.4f)
            loginViewModel.doAnimation.value = null
        })
    }

    private fun backToFirstLayout() {
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(0.5f)
        TransitionManager.beginDelayedTransition(root, transition)
        constraintFirst.applyTo(root)
        guidelineHorizontal.setGuidelinePercent(0.44f)
        checkIfFingerPrintIsPossible()
    }

    private fun observeLoginSuccess() {
        loginViewModel.loginSuccess.observe(this, Observer {
            if (it == null) return@Observer
            when (it) {
                is NetworkResult.Success -> {
                    if(it.data){
                        if (loginViewModel.rememberMe) {
                            preferences?.setCachedPassword(loginViewModel.currentPassword)
                            preferences?.setCachedUsername(loginViewModel.currentUsername)
                            preferences?.setRememberMe(loginViewModel.rememberMe)
                        }

                        startActivity(
                            Intent(
                                this,
                                HomeActivity::class.java
                            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    }else{
                        showSelectedToast(this, getString(R.string.database_decryption_error))
                        backToFirstLayout()
                    }

                }
                is NetworkResult.NoNetworkConnection -> {
                    showSelectedToast(this, getString(R.string.no_internet))
                    backToFirstLayout()
                }
                is NetworkResult.Error -> {
                    if (it.exception.message == "401") {
                        showSelectedToast(this, getString(R.string.wrong_username_or_password))
                    } else {
                        showSelectedToast(this, getString(R.string.general_error))
                    }
                    listOf(
                        "username key",
                        "password key",
                        "remember me"
                    ).map { item -> preferences?.clear(item) }
                    backToFirstLayout()
                }

                else -> {
                    showSelectedToast(this, getString(R.string.general_error))
                    listOf(
                        "username key",
                        "password key",
                        "remember me"
                    ).map { item -> preferences?.clear(item) }
                    backToFirstLayout()
                }
            }
            loginViewModel.loginSuccess.value = null
        })
    }


    private fun generateKey() {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore")

            //Generate the key//
            keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                "AndroidKeyStore"
            )

            //Initialize an empty KeyStore//
            keyStore.load(null)

            //Initialize the KeyGenerator//
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEY,
                    KeyProperties.PURPOSE_ENCRYPT or
                            KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC) //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                        KeyProperties.ENCRYPTION_PADDING_PKCS7
                    )
                    .build()
            )
            keyGenerator.generateKey()

        } catch (ex: java.lang.Exception) {
            Timber.d(ex)
        }
    }

    private fun initCipher(): Boolean {
        cipher = try {
            Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }
        return try {
            keyStore.load(null)
            val key: SecretKey = keyStore.getKey(
                KEY,
                null
            ) as SecretKey
            cipher!!.init(Cipher.ENCRYPT_MODE, key)
            true
        } catch (e: java.lang.Exception) {
            Timber.d(e)
            false
        }
    }
}