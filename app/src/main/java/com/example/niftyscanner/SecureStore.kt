package com.example.niftyscanner

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SecureStore(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs = EncryptedSharedPreferences.create(
        "nifty_scanner_secure",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveShoonyaConfig(cfg: ShoonyaConfig) {
        prefs.edit()
            .putString(KEY_UID, cfg.uid)
            .putString(KEY_PWD, cfg.pwd)
            .putString(KEY_VC, cfg.vc)
            .putString(KEY_APIKEY, cfg.apiKey)
            .apply()
    }

    fun loadShoonyaConfig(): ShoonyaConfig? {
        val uid = prefs.getString(KEY_UID, null) ?: return null
        val pwd = prefs.getString(KEY_PWD, null) ?: return null
        val vc = prefs.getString(KEY_VC, null) ?: return null
        val apiKey = prefs.getString(KEY_APIKEY, null) ?: return null
        return ShoonyaConfig(uid, pwd, vc, apiKey)
    }

    fun clearShoonyaConfig() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_UID = "uid"
        private const val KEY_PWD = "pwd"
        private const val KEY_VC = "vc"
        private const val KEY_APIKEY = "apiKey"
    }
}
