package com.wealthmetric.app.calculator

import com.wealthmetric.app.model.CalculationResult
import com.wealthmetric.app.model.InterestType
import com.wealthmetric.app.model.InvestmentPlan
import com.wealthmetric.app.model.YearlyBreakdown
import kotlin.math.pow

object FinancialCalculator {

    fun calculate(plan: InvestmentPlan): CalculationResult {
        val p = plan.principal.coerceAtLeast(0.0)
        val t = plan.durationYears.coerceAtLeast(1)
        val r = plan.interestRate / 100.0
        val inf = plan.inflationRate / 100.0

        val interestEarned: Double
        val futureValue: Double

        if (plan.interestType == InterestType.SIMPLE) {
            interestEarned = p * r * t
            futureValue = p + interestEarned
        } else {
            val n = plan.interestType.compoundingFrequencyPerYear().toDouble()
            futureValue = p * (1.0 + r / n).pow(n * t)
            interestEarned = futureValue - p
        }

        val inflationFactor = (1.0 + inf).pow(t.toDouble())
        val realValue = if (inflationFactor > 0) futureValue / inflationFactor else futureValue
        val depreciation = (futureValue - realValue).coerceAtLeast(0.0)
        val realGain = realValue - p
        val realReturnPercent = if (p > 0) (realGain / p) * 100.0 else 0.0

        val breakdowns = mutableListOf<YearlyBreakdown>()
        for (year in 1..t) {
            val yrInterestEarned: Double
            val yrFutureValue: Double
            if (plan.interestType == InterestType.SIMPLE) {
                yrInterestEarned = p * r * year
                yrFutureValue = p + yrInterestEarned
            } else {
                val n = plan.interestType.compoundingFrequencyPerYear().toDouble()
                yrFutureValue = p * (1.0 + r / n).pow(n * year)
                yrInterestEarned = yrFutureValue - p
            }

            val yrInflationFactor = (1.0 + inf).pow(year.toDouble())
            val yrRealValue = if (yrInflationFactor > 0) yrFutureValue / yrInflationFactor else yrFutureValue
            val yrDepreciation = (yrFutureValue - yrRealValue).coerceAtLeast(0.0)
            val yrRealGain = yrRealValue - p

            breakdowns.add(
                YearlyBreakdown(
                    year = year,
                    nominalValue = yrFutureValue,
                    cumulativeInterest = yrInterestEarned,
                    realValue = yrRealValue,
                    depreciation = yrDepreciation,
                    realGain = yrRealGain
                )
            )
        }

        return CalculationResult(
            plan = plan,
            interestEarned = interestEarned,
            futureValue = futureValue,
            realValue = realValue,
            depreciation = depreciation,
            realGain = realGain,
            realReturnPercentage = realReturnPercent,
            yearlyBreakdowns = breakdowns
        )
    }
}
