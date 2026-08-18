package com.wealthmetric.app.calculator

import com.wealthmetric.app.model.AnnuityScheduleRow
import com.wealthmetric.app.model.PayoutFrequency
import com.wealthmetric.app.model.RetirementCalculationResult
import com.wealthmetric.app.model.RetirementInputState
import com.wealthmetric.app.model.RetirementStatus
import kotlin.math.pow
import kotlin.math.max

object RetirementCalculator {

    const val TOTAL_SCHEDULE_YEARS = 60 // 60 Years total projection timeline

    fun calculate(input: RetirementInputState): RetirementCalculationResult {
        val retirementAge = input.retirementAge.coerceAtLeast(input.currentAge + 1)
        val yearsRemaining = max(0, retirementAge - input.currentAge)

        // Inflation Multiplier: (1 + Inflation Rate %)^Years
        val inflationMultiplier = (1.0 + input.annualInflation / 100.0).pow(yearsRemaining.toDouble())
        val annualIncomeRequired = input.annualExpenditure * inflationMultiplier

        // Year-by-Year Accumulation Phase
        val scheduleRows = mutableListOf<AnnuityScheduleRow>()
        var currentCorpus = input.currentCorpus

        for (y in 1..yearsRemaining) {
            val age = input.currentAge + y - 1
            val interestEarned = currentCorpus * (input.annualInterest / 100.0)
            val addition = input.annualSavings
            val endingCorpus = currentCorpus + interestEarned + addition
            val targetExp = input.annualExpenditure * (1.0 + input.annualInflation / 100.0).pow((y - 1).toDouble())

            scheduleRows.add(
                AnnuityScheduleRow(
                    year = y,
                    age = age,
                    isAccumulationPhase = true,
                    startingCorpus = currentCorpus,
                    interestOrPayout = interestEarned,
                    annualAddition = addition,
                    endingCorpus = endingCorpus,
                    targetExpenditure = targetExp
                )
            )
            currentCorpus = endingCorpus
        }

        val corpusAtRetirement = if (yearsRemaining == 0) input.currentCorpus else currentCorpus

        // Annual Income from Annuity = Corpus at Retirement Age * Annuity Rate %
        val annualIncomeFromAnnuity = corpusAtRetirement * (input.expectedAnnuityRate / 100.0)

        // Difference = Annuity Income - Required Income
        val difference = annualIncomeFromAnnuity - annualIncomeRequired

        val status = when {
            difference > 0.01 -> RetirementStatus.ACHIEVED
            difference >= -0.01 -> RetirementStatus.EXACT
            else -> RetirementStatus.DEFICIT
        }

        val periodPensionAmount = annualIncomeFromAnnuity / input.payoutFrequency.perYearMultiplier

        // Post-Retirement Pension Phase (Completing 60 Total Schedule Years)
        var payoutCorpus = corpusAtRetirement
        val postRetirementYears = max(1, TOTAL_SCHEDULE_YEARS - yearsRemaining)
        
        for (pY in 1..postRetirementYears) {
            val age = retirementAge + pY - 1
            val annualPension = payoutCorpus * (input.expectedAnnuityRate / 100.0)
            val endingBalance = payoutCorpus
            val targetExp = annualIncomeRequired * (1.0 + input.annualInflation / 100.0).pow((pY - 1).toDouble())

            scheduleRows.add(
                AnnuityScheduleRow(
                    year = yearsRemaining + pY,
                    age = age,
                    isAccumulationPhase = false,
                    startingCorpus = payoutCorpus,
                    interestOrPayout = annualPension,
                    annualAddition = 0.0,
                    endingCorpus = endingBalance,
                    targetExpenditure = targetExp
                )
            )
        }

        return RetirementCalculationResult(
            yearsRemaining = yearsRemaining,
            corpusAtRetirement = corpusAtRetirement,
            annualIncomeRequired = annualIncomeRequired,
            annualIncomeFromAnnuity = annualIncomeFromAnnuity,
            difference = difference,
            status = status,
            periodPensionAmount = periodPensionAmount,
            inflationMultiplier = inflationMultiplier,
            scheduleRows = scheduleRows
        )
    }
}
