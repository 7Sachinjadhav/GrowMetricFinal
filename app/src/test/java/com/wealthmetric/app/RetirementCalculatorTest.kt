package com.wealthmetric.app

import com.wealthmetric.app.calculator.RetirementCalculator
import com.wealthmetric.app.model.RetirementInputState
import com.wealthmetric.app.model.RetirementStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class RetirementCalculatorTest {

    @Test
    fun testCustomRetirementAgeCalculation() {
        val input = RetirementInputState(
            currentAge = 30,
            retirementAge = 50,
            currentCorpus = 1000000.0,
            annualSavings = 50000.0,
            annualExpenditure = 300000.0,
            annualInterest = 8.0,
            annualInflation = 5.0,
            expectedAnnuityRate = 7.0
        )

        val result = RetirementCalculator.calculate(input)

        // Years remaining = 50 - 30 = 20 years
        assertEquals(20, result.yearsRemaining)
        
        // Schedule rows check
        val accumulationRows = result.scheduleRows.filter { it.isAccumulationPhase }
        assertEquals(20, accumulationRows.size)
        assertEquals(30, accumulationRows.first().age)
        assertEquals(49, accumulationRows.last().age)

        val postRetirementRows = result.scheduleRows.filter { !it.isAccumulationPhase }
        assertEquals(50, postRetirementRows.first().age)
    }
}
