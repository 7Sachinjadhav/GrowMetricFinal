package com.wealthmetric.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.calculator.SipCalculationResult
import com.wealthmetric.app.calculator.SipCalculator
import com.wealthmetric.app.model.RetirementStatus
import com.wealthmetric.app.ui.components.AnimatedCurrencyText
import com.wealthmetric.app.ui.components.BounceButton
import com.wealthmetric.app.ui.components.PulsingBadge
import com.wealthmetric.app.ui.theme.*
import com.wealthmetric.app.viewmodel.WealthMetricUiState
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SipAndDeficitOptimizerScreen(
    uiState: WealthMetricUiState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
            maximumFractionDigits = 0
        }
    }

    // Default duration from retirement calculation if available
    val remainingYears = uiState.retirementResult.yearsRemaining.coerceAtLeast(5)

    var monthlySipText by remember { mutableStateOf("15000") }
    var stepUpText by remember { mutableStateOf("10") }
    var returnRateText by remember { mutableStateOf("12.0") }
    var durationText by remember { mutableStateOf(remainingYears.toString()) }
    var hasCalculatedSip by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.presetSipRate) {
        uiState.presetSipRate?.let { rate ->
            returnRateText = String.format(Locale.US, "%.1f", rate)
            hasCalculatedSip = true
        }
    }

    val monthlySip = monthlySipText.toDoubleOrNull() ?: 15000.0
    val stepUp = stepUpText.toDoubleOrNull() ?: 10.0
    val returnRate = returnRateText.toDoubleOrNull() ?: 12.0
    val duration = durationText.toIntOrNull() ?: remainingYears

    val sipResult: SipCalculationResult = remember(monthlySip, stepUp, returnRate, duration) {
        SipCalculator.calculate(
            monthlyInvestment = monthlySip,
            annualStepUpPercent = stepUp,
            expectedReturnRate = returnRate,
            durationYears = duration
        )
    }

    // Retirement Annuity Plan Deficit calculations
    val isAnnuityDeficit = uiState.retirementResult.status == RetirementStatus.DEFICIT
    val annuityDeficitYearly = if (isAnnuityDeficit) -uiState.retirementResult.difference else 0.0
    val annuityRate = uiState.retirementInput.expectedAnnuityRate / 100.0
    val requiredAdditionalCorpus = if (annuityRate > 0 && isAnnuityDeficit) annuityDeficitYearly / annuityRate else 0.0

    // Exact Monthly SIP required to cover the additional corpus
    val recommendedMonthlySip = remember(requiredAdditionalCorpus, returnRate, duration, stepUp) {
        if (requiredAdditionalCorpus > 0) {
            SipCalculator.calculateRequiredSip(
                targetCorpus = requiredAdditionalCorpus,
                expectedReturnRate = returnRate,
                durationYears = duration,
                annualStepUpPercent = stepUp
            )
        } else 0.0
    }

    // Goal status check: Does current SIP corpus cover required additional corpus?
    val isGoalAchieved = if (requiredAdditionalCorpus > 0) {
        sipResult.finalCorpus >= (requiredAdditionalCorpus - 1.0)
    } else true

    val shortfallCorpus = (requiredAdditionalCorpus - sipResult.finalCorpus).coerceAtLeast(0.0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // SCREEN HEADER CARD (With Refresh/Reset Button)
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, CardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PurpleContainer,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = PurpleAccent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "SIP & Wealth Deficit Optimizer",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Step-Up SIP compounding & retirement shortfall fixer in Rupees (₹)",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(onClick = {
                    monthlySipText = "15000"
                    stepUpText = "10"
                    returnRateText = "12.0"
                    durationText = remainingYears.toString()
                    hasCalculatedSip = false
                }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Defaults",
                        tint = TextSecondary
                    )
                }
            }
        }

        // INPUT CONTROLS CARD
        Card(
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, CardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "SIP Investment Parameters",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Monthly Investment
                OutlinedTextField(
                    value = monthlySipText,
                    onValueChange = {
                        monthlySipText = it
                        hasCalculatedSip = false
                    },
                    label = { Text("Monthly SIP Amount (₹)") },
                    leadingIcon = { Text("₹ ", fontWeight = FontWeight.Bold, color = PurpleDark) },
                    trailingIcon = { Text("/ month", style = MaterialTheme.typography.labelSmall, color = TextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick SIP preset chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(5000, 10000, 15000, 25000, 50000).forEach { amt ->
                        FilterChip(
                            selected = monthlySip == amt.toDouble(),
                            onClick = {
                                monthlySipText = amt.toString()
                                hasCalculatedSip = false
                            },
                            label = { Text("₹${amt / 1000}k", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PurpleContainer,
                                selectedLabelColor = PurpleDark,
                                containerColor = CardSurfaceVariant,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Annual Step-Up % and Horizon (Side by side)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = stepUpText,
                        onValueChange = {
                            stepUpText = it
                            hasCalculatedSip = false
                        },
                        label = { Text("Annual Step-Up (%)") },
                        trailingIcon = { Text("%", style = MaterialTheme.typography.labelSmall, color = TextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = durationText,
                        onValueChange = {
                            durationText = it
                            hasCalculatedSip = false
                        },
                        label = { Text("Duration (Years)") },
                        trailingIcon = { Text("Yrs", style = MaterialTheme.typography.labelSmall, color = TextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Expected Return Rate
                OutlinedTextField(
                    value = returnRateText,
                    onValueChange = {
                        returnRateText = it
                        hasCalculatedSip = false
                    },
                    label = { Text("Expected Return Rate (% p.a.)") },
                    trailingIcon = { Text("%", style = MaterialTheme.typography.labelSmall, color = TextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Asset Class Preset Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        "Debt Fund (7.5%)" to 7.5,
                        "Balanced (10.0%)" to 10.0,
                        "Equity Index (12.0%)" to 12.0,
                        "Small Cap (15.0%)" to 15.0
                    ).forEach { (label, rate) ->
                        FilterChip(
                            selected = returnRate == rate,
                            onClick = {
                                returnRateText = rate.toString()
                                hasCalculatedSip = false
                            },
                            label = { Text(label, fontSize = 10.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = IndigoContainer,
                                selectedLabelColor = IndigoDark,
                                containerColor = CardSurfaceVariant,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // EXPLICIT "CALCULATE SIP & WEALTH PLAN" BOUNCE BUTTON
                BounceButton(
                    onClick = {
                        hasCalculatedSip = true
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
                        text = "Calculate SIP & Wealth Plan",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }

        // RESULTS CONTAINER (Revealed ONLY AFTER clicking "Calculate SIP & Wealth Plan")
        AnimatedVisibility(
            visible = hasCalculatedSip,
            enter = slideInVertically(initialOffsetY = { it / 2 }) + expandVertically() + fadeIn(),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // DYNAMIC GOAL DEFICIT & COVERAGE STATUS CARD
                if (!isGoalAchieved) {
                    // DEFICIT / SHORTFALL CARD (If SIP corpus is less than required goal)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = RoseLoss.copy(alpha = 0.06f)),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.5.dp, RoseLoss.copy(alpha = 0.4f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = RoseLoss,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    PulsingBadge(
                                        text = "Retirement Deficit Detected",
                                        backgroundColor = RoseLoss.copy(alpha = 0.15f),
                                        textColor = RoseLoss
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Shortfall of ${currencyFormat.format(shortfallCorpus)} in target retirement corpus",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = CardBorder)
                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "💡 Smart Recommendation:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "You should invest ${currencyFormat.format(recommendedMonthlySip)}/month in SIP (with ${stepUp.toInt()}% annual step-up) so you can fully reach your goal!",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            BounceButton(
                                onClick = {
                                    monthlySipText = recommendedMonthlySip.toLong().toString()
                                    hasCalculatedSip = true
                                    coroutineScope.launch {
                                        scrollState.animateScrollTo(scrollState.maxValue)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp),
                                containerColor = PurpleAccent,
                                contentColor = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Auto-Fill Suggested SIP (${currencyFormat.format(recommendedMonthlySip)}/mo)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                } else {
                    // SUCCESS CARD (Now it's perfect & good!)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = EmeraldContainer),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.5.dp, EmeraldPrimary),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldDark,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                PulsingBadge(
                                    text = "Goal Fully Achieved! Now it's Perfect!",
                                    backgroundColor = EmeraldPrimary,
                                    textColor = Color.White
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                val surplusText = if (requiredAdditionalCorpus > 0) {
                                    val surplus = sipResult.finalCorpus - requiredAdditionalCorpus
                                    "Your SIP of ${currencyFormat.format(monthlySip)}/mo generates ${currencyFormat.format(sipResult.finalCorpus)}, completely covering your goal with a surplus of ${currencyFormat.format(surplus)}!"
                                } else {
                                    "Your SIP of ${currencyFormat.format(monthlySip)}/mo generates a strong wealth corpus of ${currencyFormat.format(sipResult.finalCorpus)}!"
                                }
                                Text(
                                    text = surplusText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                // RESULTS SUMMARY CARDS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        border = BorderStroke(1.dp, CardBorder),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Total Invested",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currencyFormat.format(sipResult.totalInvested),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = EmeraldContainer),
                        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Wealth Gain",
                                style = MaterialTheme.typography.labelSmall,
                                color = EmeraldDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "+${currencyFormat.format(sipResult.totalWealthGain)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldDark
                            )
                        }
                    }
                }

                // FINAL CORPUS HIGHLIGHT CARD
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.5.dp, PurpleAccent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Projected Total Corpus",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PurpleDark
                            )
                            PulsingBadge(
                                text = String.format("%.2fx Growth", sipResult.wealthMultiplier),
                                backgroundColor = PurpleContainer,
                                textColor = PurpleDark
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        AnimatedCurrencyText(
                            targetValue = sipResult.finalCorpus,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    }
                }

                // YEAR-BY-YEAR PROJECTION TABLE
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "SIP Growth Timeline (${sipResult.durationYears} Years)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Yearly contribution, interest compounding, and corpus accumulation schedule",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(horizontalScrollState)
                        ) {
                            // Header Row
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(CardSurfaceVariant)
                                    .padding(vertical = 10.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                SipTableCell("Yr", 45.dp, TextPrimary, FontWeight.Bold)
                                SipTableCell("Monthly SIP (₹)", 125.dp, TextPrimary, FontWeight.Bold)
                                SipTableCell("Annual Added (₹)", 135.dp, TextPrimary, FontWeight.Bold)
                                SipTableCell("Cum. Invested (₹)", 135.dp, TextPrimary, FontWeight.Bold)
                                SipTableCell("Ending Corpus (₹)", 140.dp, PurpleDark, FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            sipResult.yearlyBreakdowns.forEachIndexed { idx, row ->
                                val bg = if (idx % 2 == 0) CardSurface else AppBackground
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(bg)
                                        .padding(vertical = 8.dp, horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    SipTableCell("Y${row.year}", 45.dp, TextPrimary, FontWeight.Medium)
                                    SipTableCell(currencyFormat.format(row.monthlyPayment), 125.dp, TextSecondary, FontWeight.Normal)
                                    SipTableCell(currencyFormat.format(row.totalInvestedInYear), 135.dp, TextSecondary, FontWeight.Normal)
                                    SipTableCell(currencyFormat.format(row.cumulativeInvested), 135.dp, TextSecondary, FontWeight.Normal)
                                    SipTableCell(currencyFormat.format(row.endingCorpus), 140.dp, IndigoDark, FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun SipTableCell(
    text: String,
    width: androidx.compose.ui.unit.Dp,
    color: Color,
    fontWeight: FontWeight
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = fontWeight,
        color = color,
        modifier = Modifier.width(width),
        fontSize = 11.sp
    )
}
