package com.wealthmetric.app.model

enum class InterestType(val displayName: String) {
    SIMPLE("Simple Interest"),
    COMPOUND_ANNUAL("Compound (Annually)"),
    COMPOUND_SEMI_ANNUAL("Compound (Semi-Annually)"),
    COMPOUND_QUARTERLY("Compound (Quarterly)"),
    COMPOUND_MONTHLY("Compound (Monthly)");

    fun compoundingFrequencyPerYear(): Int = when (this) {
        SIMPLE -> 1
        COMPOUND_ANNUAL -> 1
        COMPOUND_SEMI_ANNUAL -> 2
        COMPOUND_QUARTERLY -> 4
        COMPOUND_MONTHLY -> 12
    }
}

data class InvestmentPlan(
    val id: String = "1",
    val name: String = "Plan A",
    val principal: Double = 10000.0,
    val durationYears: Int = 10,
    val interestRate: Double = 8.5,
    val inflationRate: Double = 3.0,
    val interestType: InterestType = InterestType.COMPOUND_ANNUAL
)
