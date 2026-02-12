package com.example.niftyscanner

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var store: SecureStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store = SecureStore(this)

        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val tvSignal = findViewById<TextView>(R.id.tvSignal)
        val tvNotes = findViewById<TextView>(R.id.tvNotes)
        val btnSettings = findViewById<Button>(R.id.btnSettings)
        val btnRefresh = findViewById<Button>(R.id.btnRefresh)

        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        btnRefresh.setOnClickListener {
            render(tvStatus, tvSignal, tvNotes)
        }

        render(tvStatus, tvSignal, tvNotes)
    }

    private fun render(tvStatus: TextView, tvSignal: TextView, tvNotes: TextView) {
        val cfg = store.loadShoonyaConfig()
        if (cfg == null) {
            tvStatus.text = "Shoonya not configured (Demo mode)"
            val spot = 25728.0
            val support = 25600.0
            val resistance = 25850.0
            val sig = SignalEngine.buildSignal(spot, support, resistance)
            tvSignal.text = formatSignal(spot, support, resistance, sig)
            tvNotes.text = "Set Shoonya details in Settings. This demo does NOT fetch live data."
            return
        }

        // For now: we still run in demo mode, but we acknowledge config exists.
        // You (or your developer) can plug real Shoonya calls here.
        tvStatus.text = "Shoonya configured (Still demo data until API is wired)"

        val spot = 25728.0
        val support = 25600.0
        val resistance = 25850.0
        val sig = SignalEngine.buildSignal(spot, support, resistance)
        tvSignal.text = formatSignal(spot, support, resistance, sig)
        tvNotes.text = "Next step: connect Shoonya Noren API to fetch live spot + option chain."
    }

    private fun formatSignal(spot: Double, support: Double, resistance: Double, s: Signal): String {
        return buildString {
            append("Spot (demo): ").append(String.format("%.0f", spot)).append("\n")
            append("Support: ").append(String.format("%.0f", support)).append("\n")
            append("Resistance: ").append(String.format("%.0f", resistance)).append("\n\n")

            append("Bias: ").append(s.bias).append("\n")
            append("Suggested: ").append(s.suggested).append("\n\n")
            append("Entry: ").append(s.entryCondition).append("\n\n")
            append("SL: ").append(s.stopLoss).append("\n")
            append("Targets: ").append(s.targets.joinToString(", ")).append("\n\n")
            append("Notes: ").append(s.notes)
        }
    }
}
