package com.wealthmetric.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Share
import com.wealthmetric.app.calculator.ReportExporter
import com.wealthmetric.app.model.RetirementCalculationResult
import com.wealthmetric.app.model.RetirementPlanInfo
import com.wealthmetric.app.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnnuityScheduleTable(
    planInfo: RetirementPlanInfo,
    result: RetirementCalculationResult,
    hasCalculated: Boolean,
    onGoToPlanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
            maximumFractionDigits = 0
        }
    }

    val context = LocalContext.current
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    if (!hasCalculated) {
        // EMPTY STATE VIEW
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = CardSurface),
            border = BorderStroke(1.dp, CardBorder),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = IndigoContainer,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.TableChart,
                            contentDescription = null,
                            tint = IndigoAccent,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No Annuity Plan Submitted Yet",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Select an annuity plan, enter your financial details in the Annuity Planner, and click 'Calculate Annuity Plan' to view your 60-year projection matrix.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                BounceButton(
                    onClick = onGoToPlanner,
                    modifier = Modifier.shadow(4.dp, RoundedCornerShape(12.dp)),
                    containerColor = DarkButton,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Go to Annuity Planner",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    } else {
        // FULL 60-YEAR VERTICALLY & HORIZONTALLY SCROLLABLE TABLE VIEW
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                border = BorderStroke(1.dp, CardBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(verticalScrollState)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "60-YEAR ANNUITY TIMELINE & PENSION SCHEDULE",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Year-by-year accumulation & pension payout projection for ${planInfo.title}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedButton(
                            onClick = {
                                val csvFile = ReportExporter.generateCsvReport(
                                    context = context,
                                    resultState = result,
                                    planTitle = planInfo.title
                                )
                                ReportExporter.shareFile(context, csvFile, "text/csv", "Export Annuity Schedule CSV")
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, PurpleAccent),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Export CSV",
                                tint = PurpleAccent,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Export CSV",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = PurpleAccent
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Horizontally Scrollable Table
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(horizontalScrollState)
                    ) {
                        // Table Header Row
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(CardSurfaceVariant)
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TableHeaderCell("Yr", 45.dp)
                            TableHeaderCell("Age", 45.dp)
                            TableHeaderCell("Phase", 110.dp)
                            TableHeaderCell("Starting Corpus (₹)", 135.dp)
                            TableHeaderCell("Growth / Pension (₹)", 135.dp)
                            TableHeaderCell("Annual Savings (₹)", 125.dp)
                            TableHeaderCell("Ending Corpus (₹)", 135.dp)
                            TableHeaderCell("Target Exp (₹)", 125.dp)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Table Rows
                        result.scheduleRows.forEachIndexed { index, row ->
                            val bgColor = if (row.isAccumulationPhase) {
                                if (index % 2 == 0) CardSurface else AppBackground
                            } else {
                                if (index % 2 == 0) IndigoContainer.copy(alpha = 0.4f) else AppBackground
                            }

                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(bgColor)
                                    .padding(vertical = 8.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TableCell("Y${row.year}", 45.dp, TextPrimary, FontWeight.Bold)
                                TableCell("${row.age}", 45.dp, IndigoDark, FontWeight.Bold)
                                TableCell(
                                    text = if (row.isAccumulationPhase) "Accumulation" else "Pension Payout",
                                    width = 110.dp,
                                    color = if (row.isAccumulationPhase) EmeraldDark else IndigoAccent,
                                    fontWeight = FontWeight.Bold
                                )
                                TableCell(currencyFormatter.format(row.startingCorpus), 135.dp, TextPrimary)
                                TableCell(
                                    text = currencyFormatter.format(row.interestOrPayout),
                                    width = 135.dp,
                                    color = if (row.isAccumulationPhase) EmeraldDark else AmberWarning,
                                    fontWeight = FontWeight.Bold
                                )
                                TableCell(
                                    text = if (row.annualAddition > 0) currencyFormatter.format(row.annualAddition) else "-",
                                    width = 125.dp,
                                    color = IndigoDark
                                )
                                TableCell(currencyFormatter.format(row.endingCorpus), 135.dp, TextPrimary, FontWeight.Bold)
                                TableCell(currencyFormatter.format(row.targetExpenditure), 125.dp, TextSecondary)
                            }
                            HorizontalDivider(color = CardBorder.copy(alpha = 0.4f), thickness = 0.5.dp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun TableHeaderCell(text: String, width: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier.width(width),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun TableCell(
    text: String,
    width: androidx.compose.ui.unit.Dp,
    color: Color = TextPrimary,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Box(
        modifier = Modifier.width(width),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = fontWeight,
            color = color,
            fontSize = 12.sp
        )
    }
}
