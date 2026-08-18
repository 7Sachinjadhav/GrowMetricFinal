package com.wealthmetric.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.model.CalculationResult
import com.wealthmetric.app.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PlanComparisonCard(
    resultA: CalculationResult,
    resultB: CalculationResult,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 0
    }

    val diffRealValue = resultA.realValue - resultB.realValue
    val winnerName = if (diffRealValue >= 0) resultA.plan.name else resultB.plan.name
    val winnerDiff = kotlin.math.abs(diffRealValue)

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Winner Announcement Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = BorderStroke(1.5.dp, EmeraldPrimary),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Winner Trophy",
                    tint = AmberWarning,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "OPTIMAL PLAN ANALYZER",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$winnerName is superior",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Yields +${currencyFormatter.format(winnerDiff)} more real purchasing power after inflation.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        // Side by Side Comparison Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ComparisonColumn(
                modifier = Modifier.weight(1f),
                title = resultA.plan.name,
                result = resultA,
                isWinner = diffRealValue >= 0,
                formatter = currencyFormatter
            )
            ComparisonColumn(
                modifier = Modifier.weight(1f),
                title = resultB.plan.name,
                result = resultB,
                isWinner = diffRealValue < 0,
                formatter = currencyFormatter
            )
        }

        // Visual Comparison Bars
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = BorderStroke(1.dp, CardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Text(
                    text = "REAL VALUE COMPARISON (PURCHASING POWER)",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                val maxVal = maxOf(resultA.realValue, resultB.realValue, 1.0)
                val barAFraction = (resultA.realValue / maxVal).toFloat().coerceIn(0.05f, 1f)
                val barBFraction = (resultB.realValue / maxVal).toFloat().coerceIn(0.05f, 1f)

                // Bar A
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(resultA.plan.name, style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                        Text(currencyFormatter.format(resultA.realValue), style = MaterialTheme.typography.labelSmall, color = EmeraldDark, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(barAFraction)
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(EmeraldPrimary)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bar B
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(resultB.plan.name, style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                        Text(currencyFormatter.format(resultB.realValue), style = MaterialTheme.typography.labelSmall, color = IndigoDark, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(barBFraction)
                            .height(14.dp)
                            .clip(RoundedCornerShape(7.dp))
                            .background(IndigoAccent)
                    )
                }
            }
        }
    }
}

@Composable
private fun ComparisonColumn(
    modifier: Modifier = Modifier,
    title: String,
    result: CalculationResult,
    isWinner: Boolean,
    formatter: NumberFormat
) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(14.dp)),
        border = BorderStroke(
            width = if (isWinner) 1.5.dp else 1.dp,
            color = if (isWinner) EmeraldPrimary else CardBorder
        ),
        colors = CardDefaults.cardColors(
            containerColor = CardSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isWinner) 3.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
                if (isWinner) {
                    Surface(
                        color = EmeraldPrimary,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "BEST",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = DarkButtonText,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 9.sp
                        )
                    }
                }
            }

            HorizontalDivider(color = CardBorder, thickness = 0.5.dp)

            CompRow("Principal", formatter.format(result.plan.principal), TextSecondary)
            CompRow("Duration", "${result.plan.durationYears} Yrs", TextSecondary)
            CompRow("Interest", "${result.plan.interestRate}%", TextSecondary)
            CompRow("Inflation", "${result.plan.inflationRate}%", AmberWarning)

            HorizontalDivider(color = CardBorder, thickness = 0.5.dp)

            CompRow("Future Value", formatter.format(result.futureValue), IndigoDark)
            CompRow("Interest Earned", formatter.format(result.interestEarned), IndigoAccent)
            CompRow("Inflation Loss", formatter.format(result.depreciation), OrangeDepreciation)

            Spacer(modifier = Modifier.height(4.dp))

            Column {
                Text("REAL VALUE", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 9.sp)
                Text(
                    text = formatter.format(result.realValue),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isWinner) EmeraldDark else TextPrimary,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun CompRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 11.sp)
        Text(value, style = MaterialTheme.typography.labelSmall, color = valueColor, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
    }
}
