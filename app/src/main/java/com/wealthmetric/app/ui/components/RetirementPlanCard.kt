package com.wealthmetric.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.model.RetirementPlanInfo
import com.wealthmetric.app.model.RetirementPlanType
import com.wealthmetric.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RetirementPlanCard(
    plan: RetirementPlanInfo,
    onSelectPlan: (RetirementPlanType) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    val (primaryColor, containerColor, icon, featureTag) = when (plan.type) {
        RetirementPlanType.IMMEDIATE_PENSION -> Quadruple(
            EmeraldDark, EmeraldContainer, Icons.Default.AccountBalanceWallet, "Immediate Monthly Payouts"
        )
        RetirementPlanType.GUARANTEED_FUTURE_PENSION -> Quadruple(
            IndigoDark, IndigoContainer, Icons.Default.Security, "Guaranteed Capital Lock-In"
        )
        RetirementPlanType.RETIREMENT_WEALTH_BUILDER -> Quadruple(
            AmberWarning, PurpleContainer, Icons.Default.TrendingUp, "7.2% High Yield Compounding"
        )
        RetirementPlanType.FLEXIBLE_PENSION -> Quadruple(
            CyanAccent, CyanContainer, Icons.Default.Tune, "Custom Payout Frequency"
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardSurface
        ),
        border = BorderStroke(1.5.dp, primaryColor.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            // Header Row: Icon, Plan Title, Feature Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = containerColor,
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = plan.title,
                            tint = primaryColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plan.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextPrimary,
                        fontSize = 17.sp
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Surface(
                        color = containerColor,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = featureTag,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor,
                            fontSize = 10.sp
                        )
                    }
                }

                Surface(
                    color = primaryColor,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${plan.defaultAnnuityRate}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Explanation
            Text(
                text = plan.shortDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Annuity Yield Rate Gauge
            AnnuityRateGauge(
                ratePercent = plan.defaultAnnuityRate,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Best For Tag Line
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = CardSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Best For: ",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = primaryColor
                    )
                    Text(
                        text = plan.bestFor,
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Expandable Drawer Toggle Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Hide Key Highlights" else "Show Key Highlights & Rules",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(if (isExpanded) 180f else 0f)
                )
            }

            // Expandable Drawer Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 6.dp)
                ) {
                    HorizontalDivider(color = CardBorder)
                    Spacer(modifier = Modifier.height(10.dp))
                    plan.benefits.forEach { benefit ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = primaryColor,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = benefit,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tactile BounceButton: VIEW PLAN
            BounceButton(
                onClick = { onSelectPlan(plan.type) },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(12.dp)),
                containerColor = DarkButton,
                contentColor = Color.White,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Select & Calculate Plan",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp,
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
        }
    }
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)
