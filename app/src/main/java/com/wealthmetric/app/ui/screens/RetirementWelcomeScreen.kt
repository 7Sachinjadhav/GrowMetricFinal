package com.wealthmetric.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.ui.components.BounceButton
import com.wealthmetric.app.ui.theme.*

@OptIn(ExperimentalTextApi::class)
@Composable
fun RetirementWelcomeScreen(
    onExplorePlans: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Hero Icon Floating/Bobbing Motion
    val infiniteTransition = rememberInfiniteTransition(label = "HeroBobbingTransition")
    val heroScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "HeroScaleAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PurpleGradientTop,
                        PurpleGradientMiddle,
                        AppBackground
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ProjectionLab Icon Badge in Soft Purple Container with floating animation
            Surface(
                shape = RoundedCornerShape(22.dp),
                color = PurpleContainer,
                border = BorderStroke(1.5.dp, PurpleLight),
                modifier = Modifier
                    .size(76.dp)
                    .scale(heroScale)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AutoGraph,
                        contentDescription = "GrowMetric Logo",
                        tint = PurpleAccent,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Headline Styling: "Build Financial Plans You Love" (Light Purple + Cyan + Emerald Gradient)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val annotatedTitle = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = TextPrimary)) {
                        append("Build Financial\nPlans ")
                    }
                    withStyle(
                        style = SpanStyle(
                            brush = Brush.horizontalGradient(
                                colors = listOf(PurpleAccent, CyanAccent, EmeraldPrimary)
                            ),
                            fontWeight = FontWeight.Black
                        )
                    ) {
                        append("You Love")
                    }
                }

                Text(
                    text = annotatedTitle,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Simulate your financial future and chart a course toward your best life in Indian Rupees (₹).",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // ProjectionLab Solid Dark Action BounceButton: "Start planning"
            BounceButton(
                onClick = onExplorePlans,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(6.dp, RoundedCornerShape(14.dp)),
                containerColor = DarkButton,
                contentColor = Color.White,
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "Start planning",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Feature Highlight Cards (Clean White Surfaces with Soft Borders)
            WelcomeFeatureCard(
                icon = Icons.Default.Security,
                iconTint = PurpleAccent,
                iconBg = PurpleContainer,
                title = "Guaranteed Pension Income",
                description = "Explore immediate & deferred annuity plans to lock in lifetime monthly/annual pension streams in Rupees (₹)."
            )

            WelcomeFeatureCard(
                icon = Icons.Default.Savings,
                iconTint = EmeraldPrimary,
                iconBg = EmeraldContainer,
                title = "Inflation Impact Protection",
                description = "Calculate exact retirement living expenses at age 60 adjusted for compound inflation."
            )

            WelcomeFeatureCard(
                icon = Icons.Default.TableChart,
                iconTint = CyanAccent,
                iconBg = CyanContainer,
                title = "Year-by-Year Schedule Matrix",
                description = "Detailed timeline of your pre-retirement accumulation and post-retirement payouts up to age 80."
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun WelcomeFeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    iconBg: androidx.compose.ui.graphics.Color,
    title: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = iconBg,
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
