package com.wealthmetric.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun YearlyTableSchedule(
    result: CalculationResult,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 0
    }

    val horizontalScrollState = rememberScrollState()

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
            Text(
                text = "ANNUAL GROWTH & INFLATION SCHEDULE",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Detailed year-by-year breakdown of ${result.plan.name}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

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
                    TableHeaderCell("Nominal Value", 110.dp)
                    TableHeaderCell("Interest Earned", 110.dp)
                    TableHeaderCell("Real Value", 110.dp)
                    TableHeaderCell("Depreciation", 110.dp)
                    TableHeaderCell("Real Gain", 110.dp)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Table Rows
                result.yearlyBreakdowns.forEachIndexed { index, row ->
                    val bgColor = if (index % 2 == 0) CardSurface else AppBackground
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(bgColor)
                            .padding(vertical = 8.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TableCell("Y${row.year}", 45.dp, TextPrimary, FontWeight.Bold)
                        TableCell(currencyFormatter.format(row.nominalValue), 110.dp, IndigoDark)
                        TableCell(currencyFormatter.format(row.cumulativeInterest), 110.dp, IndigoAccent)
                        TableCell(currencyFormatter.format(row.realValue), 110.dp, EmeraldDark, FontWeight.Bold)
                        TableCell(currencyFormatter.format(row.depreciation), 110.dp, OrangeDepreciation)
                        TableCell(
                            currencyFormatter.format(row.realGain),
                            110.dp,
                            if (row.realGain >= 0) EmeraldDark else RoseLoss,
                            FontWeight.SemiBold
                        )
                    }
                    HorizontalDivider(color = CardBorder.copy(alpha = 0.4f), thickness = 0.5.dp)
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
