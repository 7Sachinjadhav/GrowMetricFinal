package com.wealthmetric.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.wealthmetric.app.model.RetirementPlanData
import com.wealthmetric.app.viewmodel.WealthMetricUiState
import com.wealthmetric.app.viewmodel.WealthMetricViewModel

@Composable
fun RetirementMainScreen(
    uiState: WealthMetricUiState,
    viewModel: WealthMetricViewModel,
    modifier: Modifier = Modifier
) {
    val selectedPlan = uiState.customPlanInfo ?: RetirementPlanData.getPlan(uiState.selectedRetirementPlan)

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = uiState.retirementStep,
            transitionSpec = {
                if (targetState > initialState) {
                    // Forward step: Slide in from right, slide out to left
                    (slideInHorizontally(animationSpec = tween(350)) { fullWidth -> fullWidth } + fadeIn(animationSpec = tween(350))) togetherWith
                            (slideOutHorizontally(animationSpec = tween(350)) { fullWidth -> -fullWidth } + fadeOut(animationSpec = tween(350)))
                } else {
                    // Backward step: Slide in from left, slide out to right
                    (slideInHorizontally(animationSpec = tween(350)) { fullWidth -> -fullWidth } + fadeIn(animationSpec = tween(350))) togetherWith
                            (slideOutHorizontally(animationSpec = tween(350)) { fullWidth -> fullWidth } + fadeOut(animationSpec = tween(350)))
                }
            },
            label = "RetirementStepSlideAnimation"
        ) { step ->
            when (step) {
                0 -> {
                    // STEP 0: Welcome Landing Page (Build Financial Plans You Love)
                    RetirementWelcomeScreen(
                        onExplorePlans = {
                            viewModel.setRetirementStep(1)
                        }
                    )
                }
                1 -> {
                    // STEP 1: Dual Plan Selection Screen (Bank Pension Plans vs Mutual Fund Growth)
                    RetirementPlanSelectionScreen(
                        planCategory = uiState.planCategory,
                        onSelectPlanCategory = { category ->
                            viewModel.setPlanCategory(category)
                        },
                        onSelectPlan = { planType ->
                            viewModel.selectRetirementPlan(planType)
                        },
                        onSelectFundForSip = { cagr, fundName ->
                            viewModel.applyMutualFundToSip(cagr, fundName)
                        },
                        onSelectFundObject = { fund ->
                            viewModel.selectMutualFundForRetirement(fund)
                        },
                        onGoToHome = {
                            viewModel.setRetirementStep(0)
                        }
                    )
                }
                2 -> {
                    // STEP 2: Dedicated Full Page Plan & NAV Information Screen
                    RetirementPlanInfoScreen(
                        plan = selectedPlan,
                        mutualFund = uiState.selectedMutualFundDetails,
                        isFetchingNav = uiState.isFetchingNav,
                        onBack = {
                            viewModel.setRetirementStep(1)
                        },
                        onCalculateNow = {
                            viewModel.setRetirementStep(3)
                        }
                    )
                }
                3 -> {
                    // STEP 3: Calculator & Results Screen
                    RetirementCalculatorScreen(
                        planInfo = selectedPlan,
                        inputState = uiState.retirementInput,
                        resultState = uiState.retirementResult,
                        hasCalculated = uiState.hasCalculatedAnnuity,
                        onBack = {
                            viewModel.setRetirementStep(2)
                        },
                        onSubmitCalculation = {
                            viewModel.submitRetirementCalculation()
                        },
                        onInputChanged = { currentAge, retirementAge, currentCorpus, annualSavings, annualExpenditure, annualInterest, annualInflation, expectedAnnuityRate, payoutFrequency ->
                            viewModel.updateRetirementInput(
                                currentAge = currentAge,
                                retirementAge = retirementAge,
                                currentCorpus = currentCorpus,
                                annualSavings = annualSavings,
                                annualExpenditure = annualExpenditure,
                                annualInterest = annualInterest,
                                annualInflation = annualInflation,
                                expectedAnnuityRate = expectedAnnuityRate,
                                payoutFrequency = payoutFrequency
                            )
                        }
                    )
                }
            }
        }
    }
}
