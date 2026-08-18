package com.wealthmetric.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Savings
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
import com.wealthmetric.app.model.RetirementPlanData
import com.wealthmetric.app.model.RetirementPlanType
import com.wealthmetric.app.ui.components.PlanHeroCarousel
import com.wealthmetric.app.ui.components.RetirementPlanCard
import com.wealthmetric.app.ui.theme.*

@Composable
fun RetirementPlanSelectionScreen(
    planCategory: Int = 0,
    onSelectPlanCategory: (Int) -> Unit = {},
    onSelectPlan: (RetirementPlanType) -> Unit,
    onSelectFundForSip: (cagrRate: Double, fundName: String) -> Unit = { _, _ -> },
    onSelectFundObject: (MutualFundData) -> Unit = {},
    onGoToHome: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    var selectedFilterIndex by remember { mutableStateOf(0) }

    val filterCategories = listOf("All Plans", "Immediate", "Guaranteed", "Wealth Builder", "Flexible")
    val filterScrollState = rememberScrollState()

    val filteredPlans = remember(selectedFilterIndex) {
        when (selectedFilterIndex) {
            1 -> RetirementPlanData.plans.filter { it.type == RetirementPlanType.IMMEDIATE_PENSION }
            2 -> RetirementPlanData.plans.filter { it.type == RetirementPlanType.GUARANTEED_FUTURE_PENSION }
            3 -> RetirementPlanData.plans.filter { it.type == RetirementPlanType.RETIREMENT_WEALTH_BUILDER }
            4 -> RetirementPlanData.plans.filter { it.type == RetirementPlanType.FLEXIBLE_PENSION }
            else -> RetirementPlanData.plans
        }
    }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    Column(modifier = modifier.fillMaxSize()) {
        // PROMINENT PLAN CATEGORY SELECTOR HEADER
        Surface(
            color = CardSurface,
            shadowElevation = 3.dp,
            border = BorderStroke(1.dp, CardBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "SELECT PLAN TYPE",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 10.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(CardSurfaceVariant)
                        .padding(5.dp)
                ) {
                    // BANK PENSION PLANS TAB
                    Surface(
                        onClick = { onSelectPlanCategory(0) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (planCategory == 0) CardSurface else Color.Transparent,
                        shadowElevation = if (planCategory == 0) 3.dp else 0.dp,
                        border = if (planCategory == 0) BorderStroke(1.dp, PurpleAccent.copy(alpha = 0.5f)) else null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Savings,
                                    contentDescription = null,
                                    tint = if (planCategory == 0) PurpleDark else TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Bank Pension Plans",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = if (planCategory == 0) PurpleDark else TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Guaranteed Income",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = if (planCategory == 0) TextSecondary else TextMuted
                            )
                        }
                    }

                    // MUTUAL FUND GROWTH PLANS TAB
                    Surface(
                        onClick = { onSelectPlanCategory(1) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (planCategory == 1) CardSurface else Color.Transparent,
                        shadowElevation = if (planCategory == 1) 3.dp else 0.dp,
                        border = if (planCategory == 1) BorderStroke(1.dp, PurpleAccent.copy(alpha = 0.5f)) else null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = if (planCategory == 1) PurpleDark else TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Mutual Fund Plans",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = if (planCategory == 1) PurpleDark else TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "SBI • ICICI • LIC Funds",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = if (planCategory == 1) TextSecondary else TextMuted
                            )
                        }
                    }
                }
            }
        }

        if (planCategory == 1) {
            // CATEGORY 2: MUTUAL FUND GROWTH EXPLORER (SBI, ICICI, LIC)
            MutualFundExplorerScreen(
                onSelectFundForSip = onSelectFundForSip,
                onSelectFundObject = onSelectFundObject
            )
        } else {
            // CATEGORY 1: BANK PENSION PLANS
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Step Breadcrumb Banner with Home Navigation
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PurpleContainer,
                        border = BorderStroke(1.dp, PurpleLight.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "STEP 1 OF 3",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PurpleDark
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "•  Explore Annuity & Pension Plans",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }

                            IconButton(
                                onClick = onGoToHome,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Back to Hero Home",
                                    tint = PurpleDark,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                // FEATURED PLAN HERO SWIPEABLE CAROUSEL
                item {
                    Column {
                        Text(
                            text = "Featured Plan Highlights",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        PlanHeroCarousel(onSelectPlan = onSelectPlan)
                    }
                }

                // CATEGORY FILTER PILL BAR
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(filterScrollState),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filterCategories.forEachIndexed { index, category ->
                            FilterChip(
                                selected = selectedFilterIndex == index,
                                onClick = { selectedFilterIndex = index },
                                label = { Text(category, fontWeight = FontWeight.SemiBold) },
                                shape = RoundedCornerShape(20.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PurpleAccent,
                                    selectedLabelColor = Color.White,
                                    containerColor = CardSurfaceVariant,
                                    labelColor = TextSecondary
                                )
                            )
                        }
                    }
                }

                // ALL ANNUITY PLAN CARDS LIST (SPRING ENTER ANIMATION)
                itemsIndexed(filteredPlans, key = { _, plan -> plan.type.name }) { index, plan ->
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(animationSpec = tween(400, delayMillis = index * 100)) +
                                slideInVertically(
                                    initialOffsetY = { 80 },
                                    animationSpec = tween(400, delayMillis = index * 100)
                                )
                    ) {
                        RetirementPlanCard(
                            plan = plan,
                            onSelectPlan = { onSelectPlan(plan.type) }
                        )
                    }
                }
            }
        }
    }
}
