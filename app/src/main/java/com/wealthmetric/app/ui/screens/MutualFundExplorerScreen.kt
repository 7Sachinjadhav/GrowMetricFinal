package com.wealthmetric.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import com.wealthmetric.app.model.AmcProvider
import com.wealthmetric.app.model.MutualFundData
import com.wealthmetric.app.model.PreconfiguredMutualFunds
import com.wealthmetric.app.ui.components.BounceButton
import com.wealthmetric.app.ui.components.PulsingBadge
import com.wealthmetric.app.ui.theme.*
import java.util.Locale

@Composable
fun MutualFundExplorerScreen(
    onSelectFundForSip: (cagrRate: Double, fundName: String) -> Unit = { _, _ -> },
    onSelectFundObject: (MutualFundData) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedAmc by remember { mutableStateOf(AmcProvider.SBI) }

    val currentFunds = remember(selectedAmc) {
        PreconfiguredMutualFunds.getFundsForAmc(selectedAmc)
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // AMC PROVIDER SEGMENTED SELECTION TABS
        Surface(
            color = CardSurface,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp)) {
                Text(
                    text = "Select Mutual Fund Provider",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AmcProvider.values().forEach { provider ->
                        val isSelected = selectedAmc == provider
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedAmc = provider },
                            label = {
                                Text(
                                    text = provider.displayName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PurpleContainer,
                                selectedLabelColor = PurpleDark,
                                containerColor = CardSurfaceVariant,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }
        }

        // TOP 5 TRUSTED FUNDS LIST FOR SELECTED BANK PROVIDER
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Top 5 Trusted Funds (${selectedAmc.displayName})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    PulsingBadge(
                        text = "Live NAVs",
                        backgroundColor = EmeraldContainer,
                        textColor = EmeraldDark
                    )
                }
            }

            items(currentFunds, key = { it.schemeCode }) { fund ->
                MutualFundCard(
                    fund = fund,
                    onClickCard = {
                        onSelectFundObject(fund)
                    }
                )
            }
        }
    }
}

@Composable
private fun MutualFundCard(
    fund: MutualFundData,
    onClickCard: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClickCard,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = fund.fundName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${fund.category} • Code: ${fund.sfinCode}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = EmeraldContainer
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "5Y CAGR",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            color = EmeraldDark
                        )
                        Text(
                            text = "${fund.cagr5Year}%",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = CardBorder)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Live NAV",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 10.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹${String.format(Locale.US, "%.2f", fund.currentNav)}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+${fund.dailyChangePercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldDark,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onClickCard,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, PurpleAccent),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = "5Y NAV",
                            tint = PurpleAccent,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "5Y NAV Chart",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PurpleAccent,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = onClickCard,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkButton),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = "Calculate Plan",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
