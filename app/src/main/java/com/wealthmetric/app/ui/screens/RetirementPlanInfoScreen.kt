package com.wealthmetric.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.model.MutualFundData
import com.wealthmetric.app.model.RetirementPlanInfo
import com.wealthmetric.app.model.RetirementPlanType
import com.wealthmetric.app.ui.components.BounceButton
import com.wealthmetric.app.ui.components.CagrTile
import com.wealthmetric.app.ui.components.DualPhaseCashFlowChart
import com.wealthmetric.app.ui.components.InteractiveNavTrajectoryCanvas
import com.wealthmetric.app.ui.components.PlanStatTileGrid
import com.wealthmetric.app.ui.components.PulsingBadge
import com.wealthmetric.app.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RetirementPlanInfoScreen(
    plan: RetirementPlanInfo,
    mutualFund: MutualFundData? = null,
    isFetchingNav: Boolean = false,
    onBack: () -> Unit,
    onCalculateNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val tableScrollState = rememberScrollState()

    val currencyFormat = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
            maximumFractionDigits = 2
        }
    }

    val isMutualFund = mutualFund != null

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // NAVIGATION HEADER WITH BACK BUTTON & STEP BREADCRUMB
        Surface(
            color = CardSurface,
            shadowElevation = 3.dp,
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column {
                // Step Breadcrumb
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "STEP 2 OF 3",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = IndigoDark
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isMutualFund) "•  Fund Overview & NAV Trajectory" else "•  Plan Overview & Cash Flow",
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
                            contentDescription = "Back to Plans",
                            tint = TextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isMutualFund && mutualFund != null) {
                                PulsingBadge(
                                    text = mutualFund.amcProvider.displayName,
                                    backgroundColor = PurpleContainer,
                                    textColor = PurpleDark
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = CardSurfaceVariant
                                ) {
                                    Text(
                                        text = "SFIN: ${mutualFund.sfinCode}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp,
                                        color = TextSecondary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            } else {
                                PulsingBadge(
                                    text = "Bank Pension Plan",
                                    backgroundColor = EmeraldContainer,
                                    textColor = EmeraldDark
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = plan.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (isMutualFund && mutualFund != null) "${mutualFund.category} • Benchmark: ${mutualFund.benchmarkIndex}" else plan.shortDescription,
                            style = MaterialTheme.typography.labelSmall,
                            color = IndigoAccent,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // SCROLLABLE PAGE BODY
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isMutualFund && mutualFund != null) {
                // LATEST LIVE NAV BANNER
                Card(
                    colors = CardDefaults.cardColors(containerColor = AppBackground),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Latest Live NAV (${mutualFund.navDate})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = currencyFormat.format(mutualFund.currentNav),
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    val isPos = mutualFund.dailyChangePercent >= 0
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isPos) EmeraldContainer else RoseLoss.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "${if (isPos) "+" else ""}${String.format(Locale.US, "%.2f", mutualFund.dailyChangePercent)}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isPos) EmeraldDark else RoseLoss,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }

                            if (isFetchingNav) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp,
                                    color = PurpleAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = CardBorder.copy(alpha = 0.6f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Inception NAV: ", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 11.sp)
                                Text(text = "₹${String.format(Locale.US, "%.2f", mutualFund.inceptionNav)}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextSecondary, fontSize = 11.sp)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Launch Date: ", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 11.sp)
                                Text(text = mutualFund.launchDate, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = TextSecondary, fontSize = 11.sp)
                            }
                        }
                    }
                }

                // 4-PERIOD CAGR TILES
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CagrTile("1Y CAGR", "${mutualFund.cagr1Year}%", IndigoDark, IndigoContainer, Modifier.weight(1f))
                    CagrTile("3Y CAGR", "${mutualFund.cagr3Year}%", PurpleDark, PurpleContainer, Modifier.weight(1f))
                    CagrTile("5Y CAGR", "${mutualFund.cagr5Year}%", EmeraldDark, EmeraldContainer, Modifier.weight(1f))
                    CagrTile("Inception", "${mutualFund.sinceInceptionReturn}%", CyanAccent, CyanContainer, Modifier.weight(1f))
                }

                // ASSET ALLOCATION PILL BAR
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Asset Allocation",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Equity: ${mutualFund.assetAllocation.equityPercent}% | Cash: ${mutualFund.assetAllocation.cashPercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(CardSurfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth((mutualFund.assetAllocation.equityPercent / 100.0).toFloat())
                                .background(IndigoAccent)
                        )
                        if (mutualFund.assetAllocation.debtPercent > 0) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth((mutualFund.assetAllocation.debtPercent / 100.0).toFloat())
                                    .background(CyanAccent)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxSize()
                                .background(EmeraldLight)
                        )
                    }
                }

                // BIG NAV CHART ORGANIZED
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ShowChart,
                                    contentDescription = null,
                                    tint = PurpleAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "5-Year NAV Trajectory Curve",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            PulsingBadge(text = "Interactive", backgroundColor = PurpleContainer, textColor = PurpleDark)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (mutualFund.navHistory5Y.isNotEmpty()) {
                            InteractiveNavTrajectoryCanvas(
                                navPoints = mutualFund.navHistory5Y,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .background(AppBackground, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = PurpleAccent)
                            }
                        }
                    }
                }

                // HISTORICAL NAV SCHEDULE TABLE
                Card(
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Historical NAV Schedule",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 140.dp)
                                .verticalScroll(tableScrollState)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(CardSurfaceVariant)
                                    .padding(vertical = 6.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Date", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                Text("NAV (₹)", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            }

                            mutualFund.navHistory5Y.reversed().take(30).forEachIndexed { idx, point ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (idx % 2 == 0) CardSurface else AppBackground)
                                        .padding(vertical = 5.dp, horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(point.date, style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, color = TextSecondary)
                                    Text("₹${String.format(Locale.US, "%.2f", point.nav)}", style = MaterialTheme.typography.bodySmall, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                                }
                            }
                        }
                    }
                }
            } else {
                // BANK PENSION PLAN SPECIFIC DETAILS
                Card(
                    colors = CardDefaults.cardColors(containerColor = EmeraldContainer),
                    border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = EmeraldDark,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Annuity Rate: ",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldDark
                                )
                                PulsingBadge(
                                    text = "${plan.defaultAnnuityRate}% p.a.",
                                    backgroundColor = EmeraldPrimary,
                                    textColor = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Best For: ${plan.bestFor}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }

                val payoutHorizonText = when (plan.type) {
                    RetirementPlanType.IMMEDIATE_PENSION -> "Immediate"
                    RetirementPlanType.GUARANTEED_FUTURE_PENSION -> "At Retirement"
                    RetirementPlanType.RETIREMENT_WEALTH_BUILDER -> "At Retirement"
                    RetirementPlanType.FLEXIBLE_PENSION -> "Custom Frequency"
                }

                PlanStatTileGrid(
                    annuityRate = plan.defaultAnnuityRate,
                    payoutHorizon = payoutHorizonText
                )

                DualPhaseCashFlowChart()
            }

            // COMMON PLAN OVERVIEW SECTIONS
            InfoSectionCard(
                title = "About this Plan",
                icon = Icons.Default.Info,
                iconTint = IndigoAccent,
                content = plan.about
            )

            InfoSectionCard(
                title = "How it Works",
                icon = Icons.Default.Lightbulb,
                iconTint = IndigoDark,
                content = plan.howItWorks
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Benefits & Features",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    plan.benefits.forEach { benefit ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "• ",
                                color = EmeraldPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = benefit,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // AT THE VERY LAST: CALCULATE PLAN BUTTON FOR ALL FUNDS & PLANS
            BounceButton(
                onClick = onCalculateNow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(6.dp, RoundedCornerShape(16.dp)),
                containerColor = DarkButton,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Calculate Plan",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun InfoSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    content: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 22.sp
            )
        }
    }
}
