package com.example.niftyscanner

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var store: SecureStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        store = SecureStore(this)

        val etUid = findViewById<EditText>(R.id.etUserId)
        val etPwd = findViewById<EditText>(R.id.etPassword)
        val etVc = findViewById<EditText>(R.id.etVendorCode)
        val etApiKey = findViewById<EditText>(R.id.etApiKey)
        val tvHint = findViewById<TextView>(R.id.tvHint)

        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnClear = findViewById<Button>(R.id.btnClear)

        store.loadShoonyaConfig()?.let {
            etUid.setText(it.uid)
            etPwd.setText(it.pwd)
            etVc.setText(it.vc)
            etApiKey.setText(it.apiKey)
        }

        btnSave.setOnClickListener {
            val uid = etUid.text.toString().trim()
            val pwd = etPwd.text.toString().trim()
            val vc = etVc.text.toString().trim()
            val apiKey = etApiKey.text.toString().trim()

            if (uid.isEmpty() || pwd.isEmpty() || vc.isEmpty() || apiKey.isEmpty()) {
                tvHint.text = "Please fill all fields."
                return@setOnClickListener
            }

            store.saveShoonyaConfig(ShoonyaConfig(uid, pwd, vc, apiKey))
            tvHint.text = "Saved. Demo mode still active until Shoonya API wiring is added."
        }

        btnClear.setOnClickListener {
            store.clearShoonyaConfig()
            etUid.setText("")
            etPwd.setText("")
            etVc.setText("")
            etApiKey.setText("")
            tvHint.text = "Cleared."
        }
    }
}
