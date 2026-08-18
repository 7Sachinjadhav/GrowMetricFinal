package com.wealthmetric.app

import com.wealthmetric.app.calculator.SipCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SipCalculatorTest {

    @Test
    fun testSipCalculationWithoutStepUp() {
        val result = SipCalculator.calculate(
            monthlyInvestment = 10000.0,
            annualStepUpPercent = 0.0,
            expectedReturnRate = 12.0,
            durationYears = 10
        )

        // 10,000 * 12 * 10 = 1,200,000 invested
        assertEquals(1200000.0, result.totalInvested, 0.01)
        assertTrue(result.finalCorpus > result.totalInvested)
        assertTrue(result.totalWealthGain > 0.0)
    }

    @Test
    fun testSipCalculationWithStepUp() {
        val resultNoStepUp = SipCalculator.calculate(10000.0, 0.0, 12.0, 10)
        val resultWithStepUp = SipCalculator.calculate(10000.0, 10.0, 12.0, 10)

        assertTrue(resultWithStepUp.totalInvested > resultNoStepUp.totalInvested)
        assertTrue(resultWithStepUp.finalCorpus > resultNoStepUp.finalCorpus)
    }

    @Test
    fun testRequiredSipCalculation() {
        val requiredSip = SipCalculator.calculateRequiredSip(
            targetCorpus = 5000000.0,
            expectedReturnRate = 12.0,
            durationYears = 15,
            annualStepUpPercent = 0.0
        )

        val verifyResult = SipCalculator.calculate(requiredSip, 0.0, 12.0, 15)
        assertEquals(5000000.0, verifyResult.finalCorpus, 100.0)
    }
}
