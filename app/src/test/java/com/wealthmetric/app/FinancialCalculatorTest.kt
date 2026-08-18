package com.wealthmetric.app

import com.wealthmetric.app.calculator.FinancialCalculator
import com.wealthmetric.app.model.InterestType
import com.wealthmetric.app.model.InvestmentPlan
import org.junit.Assert.assertEquals
import org.junit.Test

class FinancialCalculatorTest {

    @Test
    fun testSimpleInterestCalculation() {
        val plan = InvestmentPlan(
            principal = 10000.0,
            durationYears = 5,
            interestRate = 10.0,
            inflationRate = 0.0,
            interestType = InterestType.SIMPLE
        )

        val result = FinancialCalculator.calculate(plan)

        assertEquals(5000.0, result.interestEarned, 0.01)
        assertEquals(15000.0, result.futureValue, 0.01)
        assertEquals(15000.0, result.realValue, 0.01)
        assertEquals(0.0, result.depreciation, 0.01)
        assertEquals(5000.0, result.realGain, 0.01)
    }

    @Test
    fun testCompoundInterestWithInflation() {
        val plan = InvestmentPlan(
            principal = 10000.0,
            durationYears = 10,
            interestRate = 8.0,
            inflationRate = 3.0,
            interestType = InterestType.COMPOUND_ANNUAL
        )

        val result = FinancialCalculator.calculate(plan)

        // 10000 * (1.08)^10 = 21589.25
        assertEquals(21589.25, result.futureValue, 1.0)
        // Inflation factor = (1.03)^10 = 1.343916
        // Real Value = 21589.25 / 1.343916 = 16064.43
        assertEquals(16064.43, result.realValue, 1.0)
        assertEquals(5524.82, result.depreciation, 1.0)
        assertEquals(6064.43, result.realGain, 1.0)
    }
}
