package com.wealthmetric.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.ui.theme.*

enum class RetirementInfoType(
    val title: String,
    val explanation: String
) {
    CURRENT_CORPUS(
        title = "Current Corpus",
        explanation = "The money you have already saved for your retirement."
    ),
    ANNUAL_INTEREST(
        title = "Annual Interest on Corpus",
        explanation = "The yearly growth expected on your retirement savings before retirement."
    ),
    ANNUAL_INFLATION(
        title = "Annual Inflation Rate",
        explanation = "The yearly increase in living expenses due to rising prices."
    ),
    ANNUAL_EXPENDITURE(
        title = "Annual Expenditure",
        explanation = "The total amount you currently spend in one year."
    ),
    EXPECTED_ANNUITY_RATE(
        title = "Expected Annuity Rate",
        explanation = "The yearly return used to calculate your pension after retirement."
    )
}

@Composable
fun RetirementInfoDialog(
    infoType: RetirementInfoType,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = CardSurface,
        icon = {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = IndigoAccent,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = infoType.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = infoType.explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkButton,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Got it",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    )
}
