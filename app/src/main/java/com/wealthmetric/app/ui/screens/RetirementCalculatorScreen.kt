package com.wealthmetric.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.model.*
import com.wealthmetric.app.ui.components.AnimatedCurrencyText
import com.wealthmetric.app.ui.components.BounceButton
import com.wealthmetric.app.ui.components.DualPhaseCashFlowChart
import com.wealthmetric.app.ui.components.PulsingBadge
import com.wealthmetric.app.ui.components.RetirementInfoDialog
import com.wealthmetric.app.ui.components.RetirementInfoType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TableChart
import com.wealthmetric.app.calculator.ReportExporter
import com.wealthmetric.app.ui.components.CfpConsultationCard
import com.wealthmetric.app.ui.components.CfpConsultationDialog
import com.wealthmetric.app.ui.components.LiveAnnuityRatesCard
import com.wealthmetric.app.ui.theme.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RetirementCalculatorScreen(
    planInfo: RetirementPlanInfo,
    inputState: RetirementInputState,
    resultState: RetirementCalculationResult,
    hasCalculated: Boolean,
    onBack: () -> Unit,
    onSubmitCalculation: () -> Unit,
    onInputChanged: (
        currentAge: Int?,
        retirementAge: Int?,
        currentCorpus: Double?,
        annualSavings: Double?,
        annualExpenditure: Double?,
        annualInterest: Double?,
        annualInflation: Double?,
        expectedAnnuityRate: Double?,
        payoutFrequency: PayoutFrequency?
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var activeInfoDialog by remember { mutableStateOf<RetirementInfoType?>(null) }
    var showCfpDialog by remember { mutableStateOf(false) }

    // Currency Formatter in Rupees (₹)
    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
            maximumFractionDigits = 0
        }
    }

    // Local text states for smooth text field editing
    var ageText by remember(inputState.currentAge) { mutableStateOf(inputState.currentAge.toString()) }
    var retirementAgeText by remember(inputState.retirementAge) { mutableStateOf(inputState.retirementAge.toString()) }
    var corpusText by remember(inputState.currentCorpus) { mutableStateOf(inputState.currentCorpus.toLong().toString()) }
    var savingsText by remember(inputState.annualSavings) { mutableStateOf(inputState.annualSavings.toLong().toString()) }
    var expenditureText by remember(inputState.annualExpenditure) { mutableStateOf(inputState.annualExpenditure.toLong().toString()) }
    var interestText by remember(inputState.annualInterest) { mutableStateOf(inputState.annualInterest.toString()) }
    var inflationText by remember(inputState.annualInflation) { mutableStateOf(inputState.annualInflation.toString()) }
    var annuityRateText by remember(inputState.expectedAnnuityRate) { mutableStateOf(inputState.expectedAnnuityRate.toString()) }

    // Dialog trigger helper
    activeInfoDialog?.let { dialogType ->
        RetirementInfoDialog(
            infoType = dialogType,
            onDismiss = { activeInfoDialog = null }
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Navigation Header
        Surface(
            color = CardSurface,
            shadowElevation = 2.dp
        ) {
            Column {
                // Step 3 Breadcrumb
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STEP 3 OF 3",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = IndigoDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•  Calculate Annuity & Pension",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                HorizontalDivider(color = CardBorder)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = planInfo.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Annuity & Pension Calculator (Rupees ₹)",
                            style = MaterialTheme.typography.labelSmall,
                            color = IndigoAccent
                        )
                    }
                    IconButton(onClick = {
                        onInputChanged(
                            35,
                            60,
                            2500000.0,
                            100000.0,
                            600000.0,
                            7.0,
                            6.0,
                            planInfo.defaultAnnuityRate,
                            PayoutFrequency.YEARLY
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset Defaults",
                            tint = TextSecondary
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // FINANCIAL INPUTS CARD
            Card(
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, CardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Enter Your Details (in ₹)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Adjust your ages, current savings, annual contributions, and annual expenses in Rupees (₹).",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 1. Current Age & Target Retirement Age (Side by Side)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            RetirementInputField(
                                label = "Current Age",
                                value = ageText,
                                onValueChange = { str ->
                                    ageText = str
                                    str.toIntOrNull()?.let { onInputChanged(it, null, null, null, null, null, null, null, null) }
                                },
                                trailingText = "Yrs",
                                keyboardType = KeyboardType.Number,
                                onInfoClick = null
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            RetirementInputField(
                                label = "Retirement Age",
                                value = retirementAgeText,
                                onValueChange = { str ->
                                    retirementAgeText = str
                                    str.toIntOrNull()?.let { onInputChanged(null, it, null, null, null, null, null, null, null) }
                                },
                                trailingText = "Yrs",
                                keyboardType = KeyboardType.Number,
                                onInfoClick = null
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 2. Current Corpus in Rupees (₹)
                    RetirementInputField(
                        label = "Current Corpus (Retirement Savings)",
                        value = corpusText,
                        onValueChange = { str ->
                            corpusText = str
                            str.toDoubleOrNull()?.let { onInputChanged(null, null, it, null, null, null, null, null, null) }
                        },
                        prefixText = "₹ ",
                        keyboardType = KeyboardType.Number,
                        onInfoClick = { activeInfoDialog = RetirementInfoType.CURRENT_CORPUS }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 3. Ongoing Annual Contribution in Rupees (₹/Year)
                    RetirementInputField(
                        label = "Annual Additional Contribution / Savings",
                        value = savingsText,
                        onValueChange = { str ->
                            savingsText = str
                            str.toDoubleOrNull()?.let { onInputChanged(null, null, null, it, null, null, null, null, null) }
                        },
                        prefixText = "₹ ",
                        trailingText = "/ Yr",
                        keyboardType = KeyboardType.Number,
                        onInfoClick = null
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 4. Current Annual Expenditure in Rupees (₹)
                    RetirementInputField(
                        label = "Current Annual Expenditure",
                        value = expenditureText,
                        onValueChange = { str ->
                            expenditureText = str
                            str.toDoubleOrNull()?.let { onInputChanged(null, null, null, null, it, null, null, null, null) }
                        },
                        prefixText = "₹ ",
                        keyboardType = KeyboardType.Number,
                        onInfoClick = { activeInfoDialog = RetirementInfoType.ANNUAL_EXPENDITURE }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = CardBorder)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Pre-filled Annuity Assumptions",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = IndigoDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // 5. Annual Interest on Corpus (%)
                    RetirementInputField(
                        label = "Annual Interest on Corpus (%)",
                        value = interestText,
                        onValueChange = { str ->
                            interestText = str
                            str.toDoubleOrNull()?.let { onInputChanged(null, null, null, null, null, it, null, null, null) }
                        },
                        trailingText = "%",
                        keyboardType = KeyboardType.Decimal,
                        onInfoClick = { activeInfoDialog = RetirementInfoType.ANNUAL_INTEREST }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 6. Annual Inflation Rate (%)
                    RetirementInputField(
                        label = "Annual Inflation Rate (%)",
                        value = inflationText,
                        onValueChange = { str ->
                            inflationText = str
                            str.toDoubleOrNull()?.let { onInputChanged(null, null, null, null, null, null, it, null, null) }
                        },
                        trailingText = "%",
                        keyboardType = KeyboardType.Decimal,
                        onInfoClick = { activeInfoDialog = RetirementInfoType.ANNUAL_INFLATION }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 7. Expected Annuity Rate (%)
                    RetirementInputField(
                        label = "Expected Annuity Rate after Retirement",
                        value = annuityRateText,
                        onValueChange = { str ->
                            annuityRateText = str
                            str.toDoubleOrNull()?.let { onInputChanged(null, null, null, null, null, null, null, it, null) }
                        },
                        trailingText = "%",
                        keyboardType = KeyboardType.Decimal,
                        onInfoClick = { activeInfoDialog = RetirementInfoType.EXPECTED_ANNUITY_RATE }
                    )

                    // 8. Optional Payout Frequency for Flexible Pension Plan
                    if (planInfo.type == RetirementPlanType.FLEXIBLE_PENSION) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Pension Payout Frequency",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PayoutFrequency.values().forEach { freq ->
                                FilterChip(
                                    selected = inputState.payoutFrequency == freq,
                                    onClick = { onInputChanged(null, null, null, null, null, null, null, null, freq) },
                                    label = { Text(freq.label, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IndigoContainer,
                                        selectedLabelColor = IndigoDark,
                                        containerColor = CardSurfaceVariant,
                                        labelColor = TextSecondary
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // EXPLICIT "CALCULATE ANNUITY PLAN" BOUNCE BUTTON
                    BounceButton(
                        onClick = {
                            onSubmitCalculation()
                            coroutineScope.launch {
                                scrollState.animateScrollTo(scrollState.maxValue)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(4.dp, RoundedCornerShape(14.dp)),
                        containerColor = DarkButton,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Calculate Annuity Plan",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }

            // RESULTS CONTAINER (Spring Reveal Animation when calculated)
            AnimatedVisibility(
                visible = hasCalculated,
                enter = slideInVertically(initialOffsetY = { it / 2 }) + expandVertically() + fadeIn(),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Visual Lifetime Dual Phase Graph
                    DualPhaseCashFlowChart()

                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(
                            1.5.dp,
                            when (resultState.status) {
                                RetirementStatus.ACHIEVED, RetirementStatus.EXACT -> EmeraldPrimary
                                RetirementStatus.DEFICIT -> RoseLoss
                            }
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // Retirement Status Header
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = when (resultState.status) {
                                        RetirementStatus.ACHIEVED, RetirementStatus.EXACT -> Icons.Default.CheckCircle
                                        RetirementStatus.DEFICIT -> Icons.Default.Cancel
                                    },
                                    contentDescription = null,
                                    tint = when (resultState.status) {
                                        RetirementStatus.ACHIEVED, RetirementStatus.EXACT -> EmeraldPrimary
                                        RetirementStatus.DEFICIT -> RoseLoss
                                    },
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    val statusTitle = when (resultState.status) {
                                        RetirementStatus.ACHIEVED -> "Goal Achieved"
                                        RetirementStatus.EXACT -> "Goal Exactly Achieved"
                                        RetirementStatus.DEFICIT -> "Deficit - Extra Savings Needed"
                                    }

                                    PulsingBadge(
                                        text = statusTitle,
                                        backgroundColor = when (resultState.status) {
                                            RetirementStatus.ACHIEVED, RetirementStatus.EXACT -> EmeraldContainer
                                            RetirementStatus.DEFICIT -> RoseLoss.copy(alpha = 0.15f)
                                        },
                                        textColor = when (resultState.status) {
                                            RetirementStatus.ACHIEVED, RetirementStatus.EXACT -> EmeraldDark
                                            RetirementStatus.DEFICIT -> RoseLoss
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    val diffText = if (resultState.difference >= 0) {
                                        "+${currencyFormat.format(resultState.difference)} Yearly Surplus"
                                    } else {
                                        "-${currencyFormat.format(-resultState.difference)} Yearly Deficit"
                                    }
                                    Text(
                                        text = diffText,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = CardBorder)
                            Spacer(modifier = Modifier.height(16.dp))

                            // Annuity Plan Summary
                            Text(
                                text = "Annuity Plan Summary (Rupees ₹)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = IndigoAccent
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            SummaryRow("Current Age", "${inputState.currentAge} Years")
                            SummaryRow("Years Until Retirement (Age ${inputState.retirementAge})", "${resultState.yearsRemaining} Years")
                            SummaryRowAnimated("Current Corpus", inputState.currentCorpus)
                            SummaryRowAnimated("Annual Contribution", inputState.annualSavings)
                            SummaryRowAnimated("Current Annual Expenditure", inputState.annualExpenditure)
                            SummaryRowAnimated("Corpus at Age ${inputState.retirementAge}", resultState.corpusAtRetirement, isHighlight = true)
                            SummaryRow("Annual Interest on Corpus", "${inputState.annualInterest}%")
                            SummaryRow("Annual Inflation Rate", "${inputState.annualInflation}%")
                            SummaryRow("Expected Annuity Rate", "${inputState.expectedAnnuityRate}%")

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = CardBorder)
                            Spacer(modifier = Modifier.height(16.dp))

                            // Annual Income Details
                            Text(
                                text = "Annual Pension Details (Rupees ₹)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = IndigoAccent
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            SummaryRowAnimated("Annual Income Required at Age ${inputState.retirementAge}", resultState.annualIncomeRequired)
                            SummaryRowAnimated("Annual Income from Annuity", resultState.annualIncomeFromAnnuity, isHighlight = true)

                            if (inputState.payoutFrequency != PayoutFrequency.YEARLY) {
                                SummaryRowAnimated(
                                    label = "${inputState.payoutFrequency.label} Pension Payout",
                                    valueDouble = resultState.periodPensionAmount,
                                    isHighlight = true
                                )
                            }

                            SummaryRow(
                                label = "Difference (Annuity - Required)",
                                value = (if (resultState.difference >= 0) "+" else "") + currencyFormat.format(resultState.difference),
                                valueColor = if (resultState.difference >= 0) EmeraldDark else RoseLoss,
                                isHighlight = true
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // REPORT EXPORTING ACTION CARD
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "Export & Share Financial Report",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Download a branded PDF summary dossier or export the full yearly schedule matrix as a CSV file.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val pdfFile = ReportExporter.generatePdfReport(
                                            context = context,
                                            inputState = inputState,
                                            resultState = resultState,
                                            planTitle = planInfo.title
                                        )
                                        ReportExporter.shareFile(context, pdfFile, "application/pdf", "Share PDF Financial Report")
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, IndigoAccent),
                                    modifier = Modifier.weight(1f).height(44.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = null,
                                        tint = IndigoAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "PDF Report",
                                        fontWeight = FontWeight.Bold,
                                        color = IndigoAccent,
                                        fontSize = 12.sp
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        val csvFile = ReportExporter.generateCsvReport(
                                            context = context,
                                            resultState = resultState,
                                            planTitle = planInfo.title
                                        )
                                        ReportExporter.shareFile(context, csvFile, "text/csv", "Share CSV Annuity Schedule")
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, PurpleAccent),
                                    modifier = Modifier.weight(1f).height(44.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TableChart,
                                        contentDescription = null,
                                        tint = PurpleAccent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "CSV Matrix",
                                        fontWeight = FontWeight.Bold,
                                        color = PurpleAccent,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ACTIONABLE FINANCIAL SUGGESTIONS CARD
                    RetirementSuggestionsCard(
                        inputState = inputState,
                        resultState = resultState,
                        currencyFormat = currencyFormat
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // LIVE ANNUITY RATES CARD
                    LiveAnnuityRatesCard(
                        currentAppliedRate = inputState.expectedAnnuityRate,
                        onApplyRate = { newRate, _ ->
                            onInputChanged(
                                null, null, null, null, null, null, null, newRate, null
                            )
                            onSubmitCalculation()
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // CFP ADVISOR CONSULTATION CARD
                    CfpConsultationCard(
                        onOpenBookingDialog = { showCfpDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showCfpDialog) {
            CfpConsultationDialog(onDismiss = { showCfpDialog = false })
        }
    }
}

@Composable
private fun RetirementSuggestionsCard(
    inputState: RetirementInputState,
    resultState: RetirementCalculationResult,
    currencyFormat: NumberFormat
) {
    val isAchieved = resultState.status == RetirementStatus.ACHIEVED || resultState.status == RetirementStatus.EXACT

    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, if (isAchieved) EmeraldPrimary.copy(alpha = 0.5f) else AmberWarning.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = if (isAchieved) EmeraldPrimary else AmberWarning,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isAchieved) "Financial Recommendations & Next Steps" else "Smart Recommendations to Bridge Deficit",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isAchieved) {
                SuggestionBullet(
                    title = "Lock In Guaranteed Annuity Rates",
                    description = "Consider locking in your annuity rate guarantees early to protect your post-retirement cash flows from falling interest rate cycles."
                )
                SuggestionBullet(
                    title = "Tax-Efficient Pension Structuring",
                    description = "Structure your annual pension disbursements within tax-exempt threshold limits to maximize net take-home income."
                )
                SuggestionBullet(
                    title = "Estate & Wealth Transfer Planning",
                    description = "Your projected surplus of ${currencyFormat.format(resultState.difference)} per year can be allocated towards legacy planning or family trusts."
                )
            } else {
                val annualDeficit = -resultState.difference
                val annuityRate = inputState.expectedAnnuityRate / 100.0
                val additionalCorpusNeeded = if (annuityRate > 0.0) annualDeficit / annuityRate else 0.0
                val r = inputState.annualInterest / 100.0
                val n = resultState.yearsRemaining

                val compoundingFactor = when {
                    n <= 0 -> 1.0
                    r > 0.0 -> ((1.0 + r).pow(n.toDouble()) - 1.0) / r
                    else -> n.toDouble()
                }

                val additionalAnnualSavingsNeeded = if (compoundingFactor > 0.0) additionalCorpusNeeded / compoundingFactor else additionalCorpusNeeded
                val additionalMonthlySavingsNeeded = additionalAnnualSavingsNeeded / 12.0

                SuggestionBullet(
                    title = "1. Increase Monthly Savings by ${currencyFormat.format(additionalMonthlySavingsNeeded)}",
                    description = "Saving an additional ${currencyFormat.format(additionalMonthlySavingsNeeded)} per month (${currencyFormat.format(additionalAnnualSavingsNeeded)}/yr) will build the required extra corpus of ${currencyFormat.format(additionalCorpusNeeded)} over ${resultState.yearsRemaining} years."
                )
                SuggestionBullet(
                    title = "2. Extend Accumulation Horizon by 2-3 Years",
                    description = "Working until age 62 or 63 allows your corpus to compound longer without early drawdowns, drastically shrinking your deficit."
                )
                SuggestionBullet(
                    title = "3. Optimize Pre-Retirement Asset Yield",
                    description = "Allocating a portion of pre-retirement savings to higher-yielding growth instruments (e.g. 1-2% higher return) can bridge the remaining gap."
                )
            }
        }
    }
}

@Composable
private fun SuggestionBullet(
    title: String,
    description: String
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.TrendingUp,
                contentDescription = null,
                tint = IndigoAccent,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            lineHeight = 18.sp,
            modifier = Modifier.padding(start = 22.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RetirementInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    prefixText: String? = null,
    trailingText: String? = null,
    keyboardType: KeyboardType = KeyboardType.Number,
    onInfoClick: (() -> Unit)? = null
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            if (onInfoClick != null) {
                IconButton(
                    onClick = onInfoClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = IndigoAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            prefix = prefixText?.let { { Text(it, color = TextPrimary, fontWeight = FontWeight.Bold) } },
            suffix = trailingText?.let { { Text(it, color = TextMuted) } },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CardSurfaceVariant,
                unfocusedContainerColor = CardSurfaceVariant,
                focusedBorderColor = IndigoAccent,
                unfocusedBorderColor = CardBorder,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    isHighlight: Boolean = false,
    valueColor: androidx.compose.ui.graphics.Color = if (isHighlight) IndigoDark else TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isHighlight) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) TextPrimary else TextSecondary
        )
        Text(
            text = value,
            style = if (isHighlight) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isHighlight) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = valueColor
        )
    }
}

@Composable
private fun SummaryRowAnimated(
    label: String,
    valueDouble: Double,
    isHighlight: Boolean = false,
    valueColor: androidx.compose.ui.graphics.Color = if (isHighlight) IndigoDark else TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = if (isHighlight) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Normal,
            color = if (isHighlight) TextPrimary else TextSecondary
        )
        AnimatedCurrencyText(
            targetValue = valueDouble,
            style = if (isHighlight) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isHighlight) FontWeight.ExtraBold else FontWeight.SemiBold,
            color = valueColor
        )
    }
}
