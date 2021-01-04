package com.varivoda.igor.tvz.financijskimanager.util

import android.content.Context
import android.os.Build
import android.util.Base64
import com.varivoda.igor.tvz.financijskimanager.R
import com.varivoda.igor.tvz.financijskimanager.data.local.Preferences
import java.lang.Exception
import java.security.AlgorithmParameters
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

data class Storable(val iv: String, val key: String, val salt: String)

fun generateRandomKey(): ByteArray =
    ByteArray(32).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SecureRandom.getInstanceStrong().nextBytes(this)
        } else {
            SecureRandom().nextBytes(this)
        }
    }

private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

fun ByteArray.toHex(): String {
    val result = StringBuilder()
    forEach {
        val octet = it.toInt()
        val firstIndex = (octet and 0xF0).ushr(4)
        val secondIndex = octet and 0x0F
        result.append(HEX_CHARS[firstIndex])
        result.append(HEX_CHARS[secondIndex])
    }
    return result.toString()
}

private lateinit var rawByteKey: ByteArray
private var dbCharKey: CharArray? = null
private lateinit var myContext: Context

fun createNewKey(context: Context) {
    myContext = context
    // This is the raw key that we'll be encrypting + storing
    rawByteKey = generateRandomKey()
    // This is the key that will be used by Room
    dbCharKey = rawByteKey.toHex().toCharArray()
}

fun persistRawKey(userPasscode: CharArray) {
    val storable = toStorable(rawByteKey, userPasscode)
    // Implementation explained in next step
    Preferences(myContext).saveStorableObject(storable)
}


fun toStorable(rawDbKey: ByteArray, userPasscode: CharArray): Storable {
    // Generate a random 8 byte salt
    val salt = ByteArray(8).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SecureRandom.getInstanceStrong().nextBytes(this)
        } else {
            SecureRandom().nextBytes(this)
        }
    }
    val secret: SecretKey = generateSecretKey(userPasscode, salt)
    val iv = IvParameterSpec("iv1234567890".toByteArray())
    // Now encrypt the database key with PBE
    val cipher: Cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, secret,iv)
    val params: AlgorithmParameters = cipher.parameters
    val ciphertext: ByteArray = cipher.doFinal(rawDbKey)

    // Return the IV and CipherText which can be stored to disk
    return Storable(
        Base64.encodeToString(iv.iv, Base64.DEFAULT),
        Base64.encodeToString(ciphertext, Base64.DEFAULT),
        Base64.encodeToString(salt, Base64.DEFAULT)
    )
}

private fun generateSecretKey(passcode: CharArray, salt: ByteArray): SecretKey {
    // Initialize PBE with password
    val factory: SecretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    val spec: KeySpec = PBEKeySpec(passcode, salt, 65536, 256)
    val tmp: SecretKey = factory.generateSecret(spec)
    return SecretKeySpec(tmp.encoded, "AES")
}

fun getRawByteKey(passcode: CharArray, storable: Storable): ByteArray {
    val aesWrappedKey = Base64.decode(storable.key, Base64.DEFAULT)
    val iv = Base64.decode(storable.iv, Base64.DEFAULT)
    val salt = Base64.decode(storable.salt, Base64.DEFAULT)
    val secret: SecretKey = generateSecretKey(passcode, salt)
    val cipher = Cipher.getInstance("AES/GCM/NoPadding")
    cipher.init(Cipher.DECRYPT_MODE, secret, IvParameterSpec(iv))
    return try {
        cipher.doFinal(aesWrappedKey)
    }catch (ex: Exception){
        return byteArrayOf()
    }
}

fun getCharKey(passcode: CharArray, context: Context): CharArray {
    if (dbCharKey == null) {
        initKey(passcode, context)
    }
    return dbCharKey ?: error("Failed to decrypt database key")
}

fun clearDbCharKey(){
    dbCharKey = null
}

private fun initKey(passcode: CharArray, context: Context) {
    val storable = Preferences(context).getStorable()
    if (storable == null) {
        createNewKey(context)
        persistRawKey(passcode)
    } else {
        rawByteKey = getRawByteKey(passcode, storable)
        dbCharKey = rawByteKey.toHex().toCharArray()
    }
}


