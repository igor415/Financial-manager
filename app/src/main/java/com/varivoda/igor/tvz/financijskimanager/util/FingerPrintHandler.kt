package com.varivoda.igor.tvz.financijskimanager.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.CancellationSignal
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.varivoda.igor.tvz.financijskimanager.ui.login.LoginViewModel


class FingerprintHandler(
    mContext: Context,
    private val loginViewModel: LoginViewModel
) :
    FingerprintManager.AuthenticationCallback() {

    private var cancellationSignal: CancellationSignal? = null
    private val context: Context = mContext

    fun startAuth(
        manager: FingerprintManager,
        cryptoObject: FingerprintManager.CryptoObject?
    ) {
        cancellationSignal = CancellationSignal()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.USE_FINGERPRINT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null)
    }

    override fun onAuthenticationError(
        errMsgId: Int,
        errString: CharSequence
    ) {
        //context.toast("Authentication error\n$errString")
    }

    //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//
    override fun onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed -> fingerprint doesn’t match with any of the fingerprints registered on the device", Toast.LENGTH_LONG).show()
    }

    //onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error,
    //so to provide the user with as much feedback as possible I’m incorporating this information into my toast//
    override fun onAuthenticationHelp(
        helpMsgId: Int,
        helpString: CharSequence
    ) {
        Toast.makeText(context, "Authentication help\n$helpString", Toast.LENGTH_LONG).show()
    }

    //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
    override fun onAuthenticationSucceeded(
        result: FingerprintManager.AuthenticationResult
    ) {
        loginViewModel.fingerPrintAuthenticationSuccess()
    }

}