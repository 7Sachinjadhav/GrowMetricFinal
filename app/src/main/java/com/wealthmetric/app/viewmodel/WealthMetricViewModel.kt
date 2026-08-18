package com.wealthmetric.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.wealthmetric.app.calculator.FinancialCalculator
import com.wealthmetric.app.model.CalculationResult
import com.wealthmetric.app.model.InterestType
import com.wealthmetric.app.model.InvestmentPlan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import com.wealthmetric.app.calculator.RetirementCalculator
import com.wealthmetric.app.model.PayoutFrequency
import com.wealthmetric.app.model.RetirementCalculationResult
import com.wealthmetric.app.model.RetirementInputState
import com.wealthmetric.app.model.RetirementPlanData
import com.wealthmetric.app.model.RetirementPlanType

data class WealthMetricUiState(
    val planA: InvestmentPlan = InvestmentPlan(
        id = "A",
        name = "Plan A (Stock Index)",
        principal = 10000.0,
        durationYears = 10,
        interestRate = 9.0,
        inflationRate = 3.0,
        interestType = InterestType.COMPOUND_ANNUAL
    ),
    val planB: InvestmentPlan = InvestmentPlan(
        id = "B",
        name = "Plan B (Fixed Savings)",
        principal = 10000.0,
        durationYears = 10,
        interestRate = 4.5,
        inflationRate = 3.0,
        interestType = InterestType.SIMPLE
    ),
    val activeTab: Int = 0, // 0 = Single Plan, 1 = Plan Comparison, 2 = Annual Breakdown Schedule, 3 = Retirement & Annuity Planner
    val resultA: CalculationResult = FinancialCalculator.calculate(
        InvestmentPlan(
            id = "A",
            name = "Plan A (Stock Index)",
            principal = 10000.0,
            durationYears = 10,
            interestRate = 9.0,
            inflationRate = 3.0,
            interestType = InterestType.COMPOUND_ANNUAL
        )
    ),
    val resultB: CalculationResult = FinancialCalculator.calculate(
        InvestmentPlan(
            id = "B",
            name = "Plan B (Fixed Savings)",
            principal = 10000.0,
            durationYears = 10,
            interestRate = 4.5,
            inflationRate = 3.0,
            interestType = InterestType.SIMPLE
        )
    ),
    // Retirement & Annuity Planner State
    val selectedRetirementPlan: RetirementPlanType = RetirementPlanType.IMMEDIATE_PENSION,
    val retirementStep: Int = 0, // 0 = Welcome, 1 = Selection, 2 = Plan Info, 3 = Calculator
    val hasCalculatedAnnuity: Boolean = false,
    val retirementInput: RetirementInputState = RetirementInputState(
        expectedAnnuityRate = 6.8
    ),
    val retirementResult: RetirementCalculationResult = RetirementCalculator.calculate(
        RetirementInputState(expectedAnnuityRate = 6.8)
    ),
    // Mutual Fund & Dual Plan Architecture State
    val planCategory: Int = 0, // 0 = Bank Pension Plans, 1 = Mutual Fund Growth Plans
    val customPlanInfo: com.wealthmetric.app.model.RetirementPlanInfo? = null,
    val selectedMutualFundDetails: com.wealthmetric.app.model.MutualFundData? = null,
    val isFetchingNav: Boolean = false,
    val presetSipRate: Double? = null,
    val presetFundName: String? = null
)

class WealthMetricViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WealthMetricUiState())
    val uiState: StateFlow<WealthMetricUiState> = _uiState.asStateFlow()

    fun updatePlanA(
        principal: Double? = null,
        durationYears: Int? = null,
        interestRate: Double? = null,
        inflationRate: Double? = null,
        interestType: InterestType? = null,
        name: String? = null
    ) {
        _uiState.update { currentState ->
            val updatedPlan = currentState.planA.copy(
                principal = principal ?: currentState.planA.principal,
                durationYears = durationYears ?: currentState.planA.durationYears,
                interestRate = interestRate ?: currentState.planA.interestRate,
                inflationRate = inflationRate ?: currentState.planA.inflationRate,
                interestType = interestType ?: currentState.planA.interestType,
                name = name ?: currentState.planA.name
            )
            currentState.copy(
                planA = updatedPlan,
                resultA = FinancialCalculator.calculate(updatedPlan)
            )
        }
    }

    fun updatePlanB(
        principal: Double? = null,
        durationYears: Int? = null,
        interestRate: Double? = null,
        inflationRate: Double? = null,
        interestType: InterestType? = null,
        name: String? = null
    ) {
        _uiState.update { currentState ->
            val updatedPlan = currentState.planB.copy(
                principal = principal ?: currentState.planB.principal,
                durationYears = durationYears ?: currentState.planB.durationYears,
                interestRate = interestRate ?: currentState.planB.interestRate,
                inflationRate = inflationRate ?: currentState.planB.inflationRate,
                interestType = interestType ?: currentState.planB.interestType,
                name = name ?: currentState.planB.name
            )
            currentState.copy(
                planB = updatedPlan,
                resultB = FinancialCalculator.calculate(updatedPlan)
            )
        }
    }

    fun setTab(index: Int) {
        _uiState.update { it.copy(activeTab = index) }
    }

    fun applyPreset(presetName: String, isPlanB: Boolean = false) {
        val newPlan = when (presetName) {
            "S&P 500 Historical" -> InvestmentPlan(
                name = "S&P 500 Historical",
                principal = 10000.0,
                durationYears = 10,
                interestRate = 10.2,
                inflationRate = 3.2,
                interestType = InterestType.COMPOUND_ANNUAL
            )
            "Fixed Deposit Safe" -> InvestmentPlan(
                name = "Fixed Deposit Safe",
                principal = 10000.0,
                durationYears = 10,
                interestRate = 5.0,
                inflationRate = 3.0,
                interestType = InterestType.COMPOUND_ANNUAL
            )
            "High Inflation Scenario" -> InvestmentPlan(
                name = "High Inflation Scenario",
                principal = 10000.0,
                durationYears = 10,
                interestRate = 6.0,
                inflationRate = 7.5,
                interestType = InterestType.COMPOUND_ANNUAL
            )
            else -> return
        }

        if (isPlanB) {
            updatePlanB(
                principal = newPlan.principal,
                durationYears = newPlan.durationYears,
                interestRate = newPlan.interestRate,
                inflationRate = newPlan.inflationRate,
                interestType = newPlan.interestType,
                name = newPlan.name
            )
        } else {
            updatePlanA(
                principal = newPlan.principal,
                durationYears = newPlan.durationYears,
                interestRate = newPlan.interestRate,
                inflationRate = newPlan.inflationRate,
                interestType = newPlan.interestType,
                name = newPlan.name
            )
        }
    }

    // Retirement & Annuity Planner Actions
    fun selectRetirementPlan(type: RetirementPlanType) {
        val planInfo = RetirementPlanData.getPlan(type)
        _uiState.update { currentState ->
            val updatedInput = currentState.retirementInput.copy(
                expectedAnnuityRate = planInfo.defaultAnnuityRate,
                annualInterest = planInfo.defaultAnnuityRate
            )
            currentState.copy(
                selectedRetirementPlan = type,
                customPlanInfo = null,
                selectedMutualFundDetails = null,
                retirementInput = updatedInput,
                retirementResult = RetirementCalculator.calculate(updatedInput),
                hasCalculatedAnnuity = false,
                retirementStep = 2 // Navigate to Plan Info Screen
            )
        }
    }

    fun selectMutualFundForRetirement(fund: com.wealthmetric.app.model.MutualFundData) {
        val mfPlanInfo = com.wealthmetric.app.model.RetirementPlanData.createMutualFundPlanInfo(fund)
        _uiState.update { currentState ->
            val updatedInput = currentState.retirementInput.copy(
                expectedAnnuityRate = fund.cagr5Year,
                annualInterest = fund.cagr5Year
            )
            currentState.copy(
                customPlanInfo = mfPlanInfo,
                selectedMutualFundDetails = fund,
                isFetchingNav = true,
                retirementInput = updatedInput,
                retirementResult = RetirementCalculator.calculate(updatedInput),
                hasCalculatedAnnuity = false,
                retirementStep = 2, // Step 2 Overview (Full Page NAV Screen)
                activeTab = 0 // Tab 0 Annuity Planner
            )
        }

        viewModelScope.launch {
            val updatedFund = com.wealthmetric.app.repository.MutualFundRepository.fetchFundWith5YearNavHistory(fund)
            val updatedPlanInfo = com.wealthmetric.app.model.RetirementPlanData.createMutualFundPlanInfo(updatedFund)
            _uiState.update { currentState ->
                if (currentState.selectedMutualFundDetails?.schemeCode == fund.schemeCode) {
                    currentState.copy(
                        customPlanInfo = updatedPlanInfo,
                        selectedMutualFundDetails = updatedFund,
                        isFetchingNav = false
                    )
                } else {
                    currentState.copy(isFetchingNav = false)
                }
            }
        }
    }

    fun setRetirementStep(step: Int) {
        _uiState.update { it.copy(retirementStep = step) }
    }

    fun submitRetirementCalculation() {
        _uiState.update { currentState ->
            val result = RetirementCalculator.calculate(currentState.retirementInput)
            currentState.copy(
                hasCalculatedAnnuity = true,
                retirementResult = result
            )
        }
    }

    fun updateRetirementInput(
        currentAge: Int? = null,
        retirementAge: Int? = null,
        currentCorpus: Double? = null,
        annualSavings: Double? = null,
        annualExpenditure: Double? = null,
        annualInterest: Double? = null,
        annualInflation: Double? = null,
        expectedAnnuityRate: Double? = null,
        payoutFrequency: PayoutFrequency? = null
    ) {
        _uiState.update { currentState ->
            val updatedInput = currentState.retirementInput.copy(
                currentAge = currentAge ?: currentState.retirementInput.currentAge,
                retirementAge = retirementAge ?: currentState.retirementInput.retirementAge,
                currentCorpus = currentCorpus ?: currentState.retirementInput.currentCorpus,
                annualSavings = annualSavings ?: currentState.retirementInput.annualSavings,
                annualExpenditure = annualExpenditure ?: currentState.retirementInput.annualExpenditure,
                annualInterest = annualInterest ?: currentState.retirementInput.annualInterest,
                annualInflation = annualInflation ?: currentState.retirementInput.annualInflation,
                expectedAnnuityRate = expectedAnnuityRate ?: currentState.retirementInput.expectedAnnuityRate,
                payoutFrequency = payoutFrequency ?: currentState.retirementInput.payoutFrequency
            )
            currentState.copy(
                retirementInput = updatedInput,
                retirementResult = RetirementCalculator.calculate(updatedInput)
            )
        }
    }

    // Mutual Fund & Dual Plan Actions
    fun setPlanCategory(category: Int) {
        _uiState.update { it.copy(planCategory = category) }
    }

    fun applyMutualFundToSip(cagr: Double, fundName: String) {
        _uiState.update { currentState ->
            val updatedInput = currentState.retirementInput.copy(
                annualInterest = cagr,
                expectedAnnuityRate = cagr
            )
            currentState.copy(
                activeTab = 0,
                retirementStep = 3,
                retirementInput = updatedInput,
                retirementResult = RetirementCalculator.calculate(updatedInput),
                presetSipRate = cagr,
                presetFundName = fundName
            )
        }
    }
}

