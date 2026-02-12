package com.example.niftyscanner

data class ShoonyaConfig(
    val uid: String,
    val pwd: String,
    val vc: String,
    val apiKey: String
)

data class Signal(
    val bias: String, // BULLISH / BEARISH / RANGE / NO_TRADE
    val suggested: String,
    val entryCondition: String,
    val stopLoss: String,
    val targets: List<String>,
    val notes: String
)
