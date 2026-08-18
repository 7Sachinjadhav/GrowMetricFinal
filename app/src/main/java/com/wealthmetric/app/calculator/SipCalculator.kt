package com.wealthmetric.app.calculator

import kotlin.math.pow

data class SipYearlyBreakdown(
    val year: Int,
    val monthlyPayment: Double,
    val totalInvestedInYear: Double,
    val cumulativeInvested: Double,
    val interestEarnedInYear: Double,
    val endingCorpus: Double
)

data class SipCalculationResult(
    val monthlyInvestment: Double,
    val annualStepUpPercent: Double,
    val expectedReturnRate: Double,
    val durationYears: Int,
    val totalInvested: Double,
    val totalWealthGain: Double,
    val finalCorpus: Double,
    val wealthMultiplier: Double,
    val yearlyBreakdowns: List<SipYearlyBreakdown>
)

object SipCalculator {

    fun calculate(
        monthlyInvestment: Double,
        annualStepUpPercent: Double = 0.0,
        expectedReturnRate: Double = 12.0,
        durationYears: Int = 15
    ): SipCalculationResult {
        val p = monthlyInvestment.coerceAtLeast(0.0)
        val stepUp = (annualStepUpPercent.coerceAtLeast(0.0)) / 100.0
        val r = (expectedReturnRate.coerceAtLeast(0.0)) / 100.0
        val monthlyRate = r / 12.0
        val years = durationYears.coerceAtLeast(1)

        val breakdownList = mutableListOf<SipYearlyBreakdown>()
        var currentCorpus = 0.0
        var cumulativeInvested = 0.0

        for (y in 1..years) {
            val currentMonthlyPayment = p * (1.0 + stepUp).pow((y - 1).toDouble())
            var corpusAtStartOfYear = currentCorpus
            var investedInYear = 0.0

            for (m in 1..12) {
                currentCorpus += currentMonthlyPayment
                investedInYear += currentMonthlyPayment
                val monthlyInterest = currentCorpus * monthlyRate
                currentCorpus += monthlyInterest
            }

            cumulativeInvested += investedInYear
            val interestEarnedInYear = currentCorpus - corpusAtStartOfYear - investedInYear

            breakdownList.add(
                SipYearlyBreakdown(
                    year = y,
                    monthlyPayment = currentMonthlyPayment,
                    totalInvestedInYear = investedInYear,
                    cumulativeInvested = cumulativeInvested,
                    interestEarnedInYear = interestEarnedInYear,
                    endingCorpus = currentCorpus
                )
            )
        }

        val totalInvested = cumulativeInvested
        val finalCorpus = currentCorpus
        val totalWealthGain = (finalCorpus - totalInvested).coerceAtLeast(0.0)
        val wealthMultiplier = if (totalInvested > 0) finalCorpus / totalInvested else 1.0

        return SipCalculationResult(
            monthlyInvestment = p,
            annualStepUpPercent = annualStepUpPercent,
            expectedReturnRate = expectedReturnRate,
            durationYears = years,
            totalInvested = totalInvested,
            totalWealthGain = totalWealthGain,
            finalCorpus = finalCorpus,
            wealthMultiplier = wealthMultiplier,
            yearlyBreakdowns = breakdownList
        )
    }

    /**
     * Calculates the exact monthly SIP required to achieve a target corpus over a duration.
     */
    fun calculateRequiredSip(
        targetCorpus: Double,
        expectedReturnRate: Double = 12.0,
        durationYears: Int = 15,
        annualStepUpPercent: Double = 0.0
    ): Double {
        if (targetCorpus <= 0 || durationYears <= 0) return 0.0
        // Use binary search / linear ratio to find exact monthly payment
        val testResult = calculate(1000.0, annualStepUpPercent, expectedReturnRate, durationYears)
        if (testResult.finalCorpus <= 0) return 0.0
        val ratio = targetCorpus / testResult.finalCorpus
        return 1000.0 * ratio
    }
}
