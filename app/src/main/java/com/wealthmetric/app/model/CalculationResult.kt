package com.wealthmetric.app.model

data class CalculationResult(
    val plan: InvestmentPlan,
    val interestEarned: Double,
    val futureValue: Double,
    val realValue: Double,
    val depreciation: Double,
    val realGain: Double,
    val realReturnPercentage: Double,
    val yearlyBreakdowns: List<YearlyBreakdown>
)
