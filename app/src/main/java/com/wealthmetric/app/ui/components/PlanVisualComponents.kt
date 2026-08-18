package com.wealthmetric.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.ui.theme.*

/**
 * Mini animated rate gauge bar for plan cards showing rate percentage (e.g., 6.8%).
 */
@Composable
fun AnnuityRateGauge(
    ratePercent: Double,
    modifier: Modifier = Modifier,
    maxRate: Double = 10.0
) {
    val progressTarget = (ratePercent / maxRate).toFloat().coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "RateGaugeAnimation"
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Annuity Yield Gauge",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
            Text(
                text = "$ratePercent% p.a.",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = IndigoDark,
                fontSize = 11.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(CardSurfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(3.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(IndigoAccent, EmeraldPrimary)
                        )
                    )
            )
        }
    }
}

/**
 * 3-Stat Metric Tile Grid for Plan Overview screens.
 */
@Composable
fun PlanStatTileGrid(
    annuityRate: Double,
    payoutHorizon: String,
    taxBenefit: String = "Tax Exempt (10(10A))",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatTileItem(
            modifier = Modifier.weight(1f),
            label = "Default Rate",
            value = "$annuityRate%",
            accentColor = IndigoDark
        )
        StatTileItem(
            modifier = Modifier.weight(1.2f),
            label = "Payout Mode",
            value = payoutHorizon,
            accentColor = EmeraldDark
        )
        StatTileItem(
            modifier = Modifier.weight(1.1f),
            label = "Tax Status",
            value = "Tax Saver",
            accentColor = CyanAccent
        )
    }
}

@Composable
private fun StatTileItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    accentColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, CardBorder),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 10.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = accentColor,
                fontSize = 13.sp,
                maxLines = 1
            )
        }
    }
}

/**
 * Mini dual-phase graph illustrating pre-retirement accumulation (growth curve)
 * transitioning into post-retirement annuity pension drawdowns up to age 80.
 */
@Composable
fun DualPhaseCashFlowChart(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LIFETIME CASH FLOW TRAJECTORY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Surface(
                    color = IndigoContainer,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Accumulation $\\rightarrow$ Payout",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = IndigoDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Canvas Dual-Phase Growth & Payout Graph
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            ) {
                val w = size.width
                val h = size.height

                // Phase 1: Accumulation Curve (Upward to Age 60)
                val curvePath = Path().apply {
                    moveTo(0f, h * 0.9f)
                    cubicTo(w * 0.2f, h * 0.8f, w * 0.4f, h * 0.35f, w * 0.55f, h * 0.1f)
                }

                drawPath(
                    path = curvePath,
                    brush = Brush.horizontalGradient(listOf(EmeraldPrimary, IndigoAccent)),
                    style = Stroke(width = 3.5.dp.toPx())
                )

                // Fill under accumulation
                val fillCurve = Path().apply {
                    addPath(curvePath)
                    lineTo(w * 0.55f, h)
                    lineTo(0f, h)
                    close()
                }

                drawPath(
                    path = fillCurve,
                    brush = Brush.verticalGradient(
                        colors = listOf(EmeraldPrimary.copy(alpha = 0.2f), Color.Transparent)
                    )
                )

                // Phase 2: Steady Payout Bars (Age 60 to 80)
                val barWidth = (w * 0.40f) / 6f
                for (i in 0..5) {
                    val barX = w * 0.58f + (i * barWidth * 1.15f)
                    val barH = h * 0.45f
                    val topY = h * 0.50f
                    drawRect(
                        color = IndigoAccent,
                        topLeft = Offset(barX, topY),
                        size = androidx.compose.ui.geometry.Size(barWidth * 0.8f, barH)
                    )
                }

                // Retirement Age 60 Divider Line
                drawLine(
                    color = AmberWarning,
                    start = Offset(w * 0.55f, 0f),
                    end = Offset(w * 0.55f, h),
                    strokeWidth = 2.dp.toPx()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pre-Retirement Accumulation (Age 35-60)", style = MaterialTheme.typography.labelSmall, color = EmeraldDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("Pension Payout Phase (Age 60-80)", style = MaterialTheme.typography.labelSmall, color = IndigoDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
