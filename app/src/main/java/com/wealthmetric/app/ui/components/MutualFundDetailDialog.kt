package com.wealthmetric.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ShowChart
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.model.MutualFundData
import com.wealthmetric.app.ui.theme.*
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToInt



@Composable
fun CagrTile(
    label: String,
    value: String,
    textColor: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.ExtraBold, color = textColor, fontSize = 12.sp)
        }
    }
}

@Composable
fun InteractiveNavTrajectoryCanvas(
    navPoints: List<com.wealthmetric.app.model.NavPoint>,
    modifier: Modifier = Modifier
) {
    val navValues = remember(navPoints) { navPoints.map { it.nav } }
    val minNav = remember(navValues) { (navValues.minOrNull() ?: 10.0) * 0.95 }
    val maxNav = remember(navValues) { (navValues.maxOrNull() ?: 100.0) * 1.05 }

    var activeIndex by remember { mutableStateOf<Int?>(null) }
    val activePoint = activeIndex?.let { if (it in navPoints.indices) navPoints[it] else null }

    Column(modifier = modifier) {
        // FLOATING TOUCH/HOVER TOOLTIP BANNER
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (activePoint != null) PurpleContainer else CardSurfaceVariant,
            border = BorderStroke(1.dp, if (activePoint != null) PurpleAccent else Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (activePoint != null) {
                    Text(
                        text = "📅 Date: ${activePoint.date}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PurpleDark,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "💰 NAV: ₹${String.format(Locale.US, "%.2f", activePoint.nav)}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = PurpleDark,
                        fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = "👆 Touch or drag across graph to inspect daily NAV & date",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(navPoints) {
                        detectTapGestures(
                            onPress = { offset ->
                                val count = navPoints.size
                                if (count > 1 && size.width > 0) {
                                    val ratio = (offset.x / size.width.toFloat()).coerceIn(0f, 1f)
                                    activeIndex = (ratio * (count - 1)).roundToInt().coerceIn(0, count - 1)
                                }
                            }
                        )
                    }
                    .pointerInput(navPoints) {
                        detectDragGestures(
                            onDrag = { change, _ ->
                                change.consume()
                                val count = navPoints.size
                                if (count > 1 && size.width > 0) {
                                    val ratio = (change.position.x / size.width.toFloat()).coerceIn(0f, 1f)
                                    activeIndex = (ratio * (count - 1)).roundToInt().coerceIn(0, count - 1)
                                }
                            }
                        )
                    }
            ) {
                val width = size.width
                val height = size.height
                val pointsCount = navValues.size

                if (pointsCount < 2) return@Canvas

                val path = Path()
                val fillPath = Path()

                var activeX = 0f
                var activeY = 0f

                navValues.forEachIndexed { i, nav ->
                    val x = (i / (pointsCount - 1).toFloat()) * width
                    val normalizedY = ((nav - minNav) / (maxNav - minNav)).toFloat()
                    val y = height - (normalizedY * height)

                    if (i == activeIndex) {
                        activeX = x
                        activeY = y
                    }

                    if (i == 0) {
                        path.moveTo(x, y)
                        fillPath.moveTo(x, height)
                        fillPath.lineTo(x, y)
                    } else {
                        path.lineTo(x, y)
                        fillPath.lineTo(x, y)
                    }
                }

                fillPath.lineTo(width, height)
                fillPath.close()

                // Gradient Fill under curve
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            PurpleAccent.copy(alpha = 0.35f),
                            PurpleAccent.copy(alpha = 0.02f)
                        )
                    )
                )

                // Line Curve Stroke
                drawPath(
                    path = path,
                    color = PurpleAccent,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Active touch/hover vertical indicator line & glowing dot
                if (activeIndex != null && activeIndex!! in navPoints.indices) {
                    // Vertical dashed guideline
                    drawLine(
                        color = PurpleDark.copy(alpha = 0.7f),
                        start = Offset(activeX, 0f),
                        end = Offset(activeX, height),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    // Outer halo
                    drawCircle(
                        color = PurpleAccent.copy(alpha = 0.3f),
                        radius = 10.dp.toPx(),
                        center = Offset(activeX, activeY)
                    )

                    // Solid inner dot
                    drawCircle(
                        color = PurpleDark,
                        radius = 5.dp.toPx(),
                        center = Offset(activeX, activeY)
                    )
                }
            }
        }
    }
}
