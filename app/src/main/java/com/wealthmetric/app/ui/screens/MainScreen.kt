package com.wealthmetric.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wealthmetric.app.model.RetirementPlanData
import com.wealthmetric.app.ui.components.AnnuityScheduleTable
import com.wealthmetric.app.ui.theme.*
import com.wealthmetric.app.viewmodel.WealthMetricViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: WealthMetricViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Hide bottom navigation bar on the 1st Landing Page (Step 0)
    val isLandingPage = uiState.activeTab == 0 && uiState.retirementStep == 0

    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 2.dp,
                color = PurpleGradientTop
            ) {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoGraph,
                                contentDescription = "GrowMetric Icon",
                                tint = PurpleAccent,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "GrowMetric",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Simulate. Plan. Prosper.",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PurpleDark,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PurpleGradientTop
                    )
                )
            }
        },
        bottomBar = {
            // Show bottom navigation ONLY after user clicks "Start planning" (Step >= 1 or Tab >= 1)
            AnimatedVisibility(
                visible = !isLandingPage,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                Surface(
                    shadowElevation = 8.dp,
                    color = CardSurface
                ) {
                    NavigationBar(
                        containerColor = CardSurface,
                        contentColor = IndigoAccent,
                        tonalElevation = 0.dp
                    ) {
                        NavTabItem(
                            selected = uiState.activeTab == 0,
                            onClick = { viewModel.setTab(0) },
                            icon = Icons.Default.Savings,
                            label = "Annuity & Wealth Planner"
                        )
                        NavTabItem(
                            selected = uiState.activeTab == 1,
                            onClick = { viewModel.setTab(1) },
                            icon = Icons.Default.TableChart,
                            label = "Annuity Schedule"
                        )
                    }
                }
            }
        },
        containerColor = AppBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.activeTab) {
                0 -> RetirementMainScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                1 -> Box(modifier = Modifier.fillMaxSize()) {
                    AnnuityScheduleTable(
                        planInfo = uiState.customPlanInfo ?: RetirementPlanData.getPlan(uiState.selectedRetirementPlan),
                        result = uiState.retirementResult,
                        hasCalculated = uiState.hasCalculatedAnnuity,
                        onGoToPlanner = { viewModel.setTab(0) }
                    )
                }
                else -> RetirementMainScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun RowScope.NavTabItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(imageVector = icon, contentDescription = label) },
        label = { Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = IndigoDark,
            selectedTextColor = IndigoDark,
            indicatorColor = IndigoContainer,
            unselectedIconColor = TextSecondary,
            unselectedTextColor = TextSecondary
        )
    )
}
