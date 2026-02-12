package com.example.niftyscanner

object SignalEngine {

    /**
     * Very conservative, rule-based guidance.
     * This is NOT investment advice; it is a decision checklist.
     */
    fun buildSignal(spot: Double, support: Double, resistance: Double): Signal {
        val pivot = (support + resistance) / 2.0

        // Range / no trade zone (premium-decay trap)
        if (spot in (support + 40.0)..(resistance - 40.0)) {
            return Signal(
                bias = "RANGE",
                suggested = "NO TRADE",
                entryCondition = "Wait. Spot is inside the range (${fmt(support)} - ${fmt(resistance)}).",
                stopLoss = "—",
                targets = listOf("—"),
                notes = "Options decay is high in mid-range. Trade only on clean breakout/breakdown."
            )
        }

        if (spot >= resistance) {
            return Signal(
                bias = "BULLISH",
                suggested = "BUY ATM CE",
                entryCondition = "Buy only if spot holds above ${fmt(resistance)} for 5 minutes after 9:25.",
                stopLoss = "Exit if spot falls below ${fmt(resistance - 40.0)}.",
                targets = listOf(fmt(resistance + 80.0), fmt(resistance + 160.0)),
                notes = "Prefer ATM or 1-step ITM. Avoid deep OTM." 
            )
        }

        if (spot <= support) {
            return Signal(
                bias = "BEARISH",
                suggested = "BUY ATM PE",
                entryCondition = "Buy only if spot holds below ${fmt(support)} for 5 minutes after 9:25.",
                stopLoss = "Exit if spot rises above ${fmt(support + 40.0)}.",
                targets = listOf(fmt(support - 80.0), fmt(support - 160.0)),
                notes = "Prefer ATM or 1-step ITM. Avoid deep OTM." 
            )
        }

        // Near edges: small edge-bias but still cautious
        return if (spot > pivot) {
            Signal(
                bias = "BULLISH (EDGE)",
                suggested = "WAIT or small CE on confirmation",
                entryCondition = "Wait for breakout above ${fmt(resistance)}. No breakout = no trade.",
                stopLoss = "—",
                targets = listOf("—"),
                notes = "Price is leaning up but still inside range." 
            )
        } else {
            Signal(
                bias = "BEARISH (EDGE)",
                suggested = "WAIT or small PE on confirmation",
                entryCondition = "Wait for breakdown below ${fmt(support)}. No breakdown = no trade.",
                stopLoss = "—",
                targets = listOf("—"),
                notes = "Price is leaning down but still inside range." 
            )
        }
    }

    private fun fmt(v: Double): String = String.format("%.0f", v)
}
