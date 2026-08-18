package com.wealthmetric.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
 * Modern ProjectionLab-style interactive visual dashboard card with line curves,
 * stacked bar charts, and milestone tooltips matching the ProjectionLab hero graphic.
 */
@Composable
fun ProjectionLabChartPreview(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ChartGlowTransition")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "GlowAlpha"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Stats Bar (ProjectionLab Style)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = IndigoContainer,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AutoGraph,
                                contentDescription = null,
                                tint = IndigoAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Current Projections",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Accumulation & Drawdown Phase",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    }
                }

                Surface(
                    color = EmeraldContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = EmeraldDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "100% On Track",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldDark,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Floating Milestone Badge Tooltip (ProjectionLab Style)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CardSurfaceVariant,
                border = BorderStroke(1.dp, IndigoAccent.copy(alpha = pulseAlpha)),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(IndigoAccent)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Milestone: Age 60 Retirement",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "Corpus: ₹2,50,00,000",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = IndigoDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bezier Curve Line Graph Canvas
            val gradientBrush = Brush.horizontalGradient(
                colors = listOf(IndigoAccent, CyanAccent, EmeraldPrimary)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                val width = size.width
                val height = size.height

                // Draw Smooth Projection Path
                val path = Path().apply {
                    moveTo(0f, height * 0.85f)
                    cubicTo(
                        width * 0.35f, height * 0.70f,
                        width * 0.55f, height * 0.25f,
                        width, height * 0.05f
                    )
                }

                drawPath(
                    path = path,
                    brush = gradientBrush,
                    style = Stroke(width = 4.dp.toPx())
                )

                // Fill Area Under Path
                val fillPath = Path().apply {
                    addPath(path)
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            IndigoAccent.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                )

                // Draw Milestone Node Points
                drawCircle(
                    color = IndigoDark,
                    radius = 6.dp.toPx(),
                    center = Offset(width * 0.58f, height * 0.30f)
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.dp.toPx(),
                    center = Offset(width * 0.58f, height * 0.30f)
                )

                drawCircle(
                    color = EmeraldDark,
                    radius = 7.dp.toPx(),
                    center = Offset(width * 0.95f, height * 0.08f)
                )
                drawCircle(
                    color = Color.White,
                    radius = 3.5.dp.toPx(),
                    center = Offset(width * 0.95f, height * 0.08f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Stacked Bar Chart Growth Timeline (Accumulation -> Payout)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val barHeights = listOf(0.35f, 0.45f, 0.58f, 0.72f, 0.85f, 1.0f, 0.92f, 0.84f, 0.78f, 0.70f)
                val colors = listOf(
                    IndigoAccent, IndigoAccent, IndigoAccent, IndigoDark, EmeraldPrimary,
                    EmeraldDark, CyanAccent, CyanAccent, IndigoAccent, IndigoAccent
                )

                barHeights.forEachIndexed { i, h ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(18.dp)
                                .fillMaxHeight(h)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(colors[i])
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Timeline Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Age 35 (Now)", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 10.sp)
                Text("Age 50 (Growth)", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 10.sp)
                Text("Age 60 (Retire)", style = MaterialTheme.typography.labelSmall, color = IndigoDark, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                Text("Age 80 (Payouts)", style = MaterialTheme.typography.labelSmall, color = TextMuted, fontSize = 10.sp)
            }
        }
    }
}
