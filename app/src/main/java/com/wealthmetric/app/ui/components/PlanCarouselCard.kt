package com.wealthmetric.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.model.RetirementPlanData
import com.wealthmetric.app.model.RetirementPlanInfo
import com.wealthmetric.app.model.RetirementPlanType
import com.wealthmetric.app.ui.theme.*

/**
 * Horizontal Pager Hero Carousel showing featured plan cards with swipe indicators.
 */
@Composable
fun PlanHeroCarousel(
    onSelectPlan: (RetirementPlanType) -> Unit,
    modifier: Modifier = Modifier
) {
    val plans = remember { RetirementPlanData.plans }
    val pagerState = rememberPagerState(pageCount = { plans.size })

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp),
            pageSpacing = 12.dp
        ) { page ->
            val plan = plans[page]
            CarouselHeroTile(
                plan = plan,
                onSelectPlan = onSelectPlan
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Pager Indicator Dots
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(plans.size) { i ->
                val isSelected = pagerState.currentPage == i
                val width by animateDpAsState(
                    targetValue = if (isSelected) 22.dp else 8.dp,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    label = "DotWidth"
                )
                Box(
                    modifier = Modifier
                        .height(8.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(if (isSelected) PurpleAccent else CardBorder)
                )
            }
        }
    }
}

@Composable
private fun CarouselHeroTile(
    plan: RetirementPlanInfo,
    onSelectPlan: (RetirementPlanType) -> Unit
) {
    val (primaryColor, containerColor, icon) = when (plan.type) {
        RetirementPlanType.IMMEDIATE_PENSION -> Triple(EmeraldDark, EmeraldContainer, Icons.Default.AccountBalanceWallet)
        RetirementPlanType.GUARANTEED_FUTURE_PENSION -> Triple(IndigoDark, IndigoContainer, Icons.Default.Security)
        RetirementPlanType.RETIREMENT_WEALTH_BUILDER -> Triple(AmberWarning, PurpleContainer, Icons.Default.TrendingUp)
        RetirementPlanType.FLEXIBLE_PENSION -> Triple(CyanAccent, CyanContainer, Icons.Default.Tune)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectPlan(plan.type) }
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        border = BorderStroke(1.5.dp, primaryColor.copy(alpha = 0.4f))
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
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = containerColor,
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = plan.title,
                            tint = primaryColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Surface(
                    color = primaryColor,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${plan.defaultAnnuityRate}% Yield",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = plan.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plan.shortDescription,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 18.sp,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            BounceButton(
                onClick = { onSelectPlan(plan.type) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                containerColor = DarkButton,
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Explore ${plan.title}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
