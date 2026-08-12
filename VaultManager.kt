package com.calcvault.app

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.MessageDigest

object VaultManager {
    private const val PREF_FILE = "vault_secure"
    private const val KEY_PIN = "pin_hash"
    private const val KEY_VAULT_CODE = "vault_code"
    private const val DEFAULT_PIN = "1234"
    private const val DEFAULT_VAULT_CODE_SUFFIX = "="

    private var securePrefs: SharedPreferences? = null

    fun init(context: Context) {
        if (securePrefs != null) return
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            securePrefs = EncryptedSharedPreferences.create(
                context,
                PREF_FILE,
                masterKey,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            securePrefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        }
        if (!securePrefs!!.contains(KEY_PIN)) {
            securePrefs!!.edit()
                .putString(KEY_PIN, hash(DEFAULT_PIN))
                .putString(KEY_VAULT_CODE, hash(DEFAULT_PIN + DEFAULT_VAULT_CODE_SUFFIX))
                .apply()
        }
    }

    fun getPrefs(context: Context): SharedPreferences {
        init(context)
        return securePrefs!!
    }

    fun setPin(context: Context, newPin: String) {
        init(context)
        securePrefs!!.edit()
            .putString(KEY_PIN, hash(newPin))
            .putString(KEY_VAULT_CODE, hash(newPin + DEFAULT_VAULT_CODE_SUFFIX))
            .apply()
    }

    fun matchVaultCode(expr: String): Boolean {
        val prefs = securePrefs ?: return false
        val expected = prefs.getString(KEY_VAULT_CODE, null) ?: return false
        return hash(expr) == expected
    }

    fun matchPin(pin: String): Boolean {
        val prefs = securePrefs ?: return false
        val expected = prefs.getString(KEY_PIN, null) ?: return false
        return hash(pin) == expected
    }

    private fun hash(s: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(s.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
