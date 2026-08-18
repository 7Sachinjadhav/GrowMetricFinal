package com.wealthmetric.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
fun ResultMetricsOverview(
    result: CalculationResult,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 0
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hero Highlight Card: Real Value (Purchasing Power)
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
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REAL VALUE (PURCHASING POWER)",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldDark,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = if (result.realGain >= 0) EmeraldContainer else RoseLoss.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (result.realGain >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = if (result.realGain >= 0) EmeraldDark else RoseLoss,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format(Locale.US, "%+.1f%% Real", result.realReturnPercentage),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (result.realGain >= 0) EmeraldDark else RoseLoss
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                AnimatedCurrencyText(
                    targetValue = result.realValue,
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar showing breakdown: Principal vs Real Gain vs Inflation Loss with width animation
                val totalValue = result.futureValue.coerceAtLeast(1.0)
                val targetPrincipalFrac = (result.plan.principal / totalValue).toFloat().coerceIn(0f, 1f)
                val targetRealGainFrac = (result.realGain.coerceAtLeast(0.0) / totalValue).toFloat().coerceIn(0f, 1f)
                val targetDeprecFrac = (result.depreciation / totalValue).toFloat().coerceIn(0f, 1f)

                val principalFraction by animateFloatAsState(targetValue = targetPrincipalFrac, animationSpec = tween(700), label = "PrincipalFrac")
                val realGainFraction by animateFloatAsState(targetValue = targetRealGainFrac, animationSpec = tween(700), label = "RealGainFrac")
                val depreciationFraction by animateFloatAsState(targetValue = targetDeprecFrac, animationSpec = tween(700), label = "DeprecFrac")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(CardSurfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(principalFraction.coerceAtLeast(0.01f))
                            .fillMaxHeight()
                            .background(IndigoAccent)
                    )
                    if (realGainFraction > 0.01f) {
                        Box(
                            modifier = Modifier
                                .weight(realGainFraction)
                                .fillMaxHeight()
                                .background(EmeraldPrimary)
                        )
                    }
                    if (depreciationFraction > 0.01f) {
                        Box(
                            modifier = Modifier
                                .weight(depreciationFraction)
                                .fillMaxHeight()
                                .background(OrangeDepreciation)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    LegendItem(color = IndigoAccent, label = "Principal")
                    LegendItem(color = EmeraldPrimary, label = "Real Gain")
                    LegendItem(color = OrangeDepreciation, label = "Inflation Loss")
                }
            }
        }

        // 2x2 Grid of Detailed Metrics
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricTileAnimated(
                modifier = Modifier.weight(1f),
                title = "Future Value (Nominal)",
                targetValue = result.futureValue,
                subtitle = "Raw End Total",
                accentColor = IndigoDark
            )
            MetricTileAnimated(
                modifier = Modifier.weight(1f),
                title = "Interest Earned",
                targetValue = result.interestEarned,
                subtitle = "Total Yield",
                accentColor = IndigoAccent
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricTileAnimated(
                modifier = Modifier.weight(1f),
                title = "Depreciation (Loss)",
                targetValue = result.depreciation,
                subtitle = "Erosion to Inflation",
                accentColor = OrangeDepreciation
            )
            MetricTileAnimated(
                modifier = Modifier.weight(1f),
                title = "Net Real Gain",
                targetValue = result.realGain,
                subtitle = if (result.realGain >= 0) "Above Principal" else "Below Principal",
                accentColor = if (result.realGain >= 0) EmeraldDark else RoseLoss
            )
        }
    }
}

@Composable
private fun MetricTileAnimated(
    modifier: Modifier = Modifier,
    title: String,
    targetValue: Double,
    subtitle: String,
    accentColor: Color
) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            AnimatedCurrencyText(
                targetValue = targetValue,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontSize = 10.sp
        )
    }
}
