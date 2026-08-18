package com.wealthmetric.app.model

data class YearlyBreakdown(
    val year: Int,
    val nominalValue: Double,
    val cumulativeInterest: Double,
    val realValue: Double,
    val depreciation: Double,
    val realGain: Double
)
