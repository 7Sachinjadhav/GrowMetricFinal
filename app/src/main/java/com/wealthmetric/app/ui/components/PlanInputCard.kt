package com.wealthmetric.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wealthmetric.app.model.InterestType
import com.wealthmetric.app.model.InvestmentPlan
import com.wealthmetric.app.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanInputCard(
    plan: InvestmentPlan,
    onPlanChanged: (principal: Double?, durationYears: Int?, interestRate: Double?, inflationRate: Double?, interestType: InterestType?, name: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedDropdown by remember { mutableStateOf(false) }
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
            maximumFractionDigits = 0
        }
    }

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
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Plan Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = plan.name,
                    onValueChange = { onPlanChanged(null, null, null, null, null, it) },
                    label = { Text("Plan Label") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IndigoAccent,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            // 1. Principal Amount Input (₹)
            var principalText by remember(plan.principal) {
                mutableStateOf(if (plan.principal == 0.0) "" else String.format(Locale.US, "%.0f", plan.principal))
            }

            OutlinedTextField(
                value = principalText,
                onValueChange = { input ->
                    principalText = input
                    val parsed = input.toDoubleOrNull()
                    if (parsed != null && parsed >= 0) {
                        onPlanChanged(parsed, null, null, null, null, null)
                    }
                },
                label = { Text("Principal Amount (₹)") },
                prefix = { Text("₹ ", color = IndigoDark, fontWeight = FontWeight.Bold) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IndigoAccent,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // 2. Duration (Years)
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Duration (Years)", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text(
                        "${plan.durationYears} Years",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = IndigoDark
                    )
                }
                Slider(
                    value = plan.durationYears.toFloat(),
                    onValueChange = { onPlanChanged(null, it.toInt(), null, null, null, null) },
                    valueRange = 1f..40f,
                    steps = 38,
                    colors = SliderDefaults.colors(
                        thumbColor = IndigoDark,
                        activeTrackColor = IndigoAccent,
                        inactiveTrackColor = CardSurfaceVariant
                    )
                )
            }

            // 3. Interest Rate (%) & Inflation Rate (%) side by side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = String.format(Locale.US, "%.1f", plan.interestRate),
                    onValueChange = { val str = it.toDoubleOrNull(); if (str != null) onPlanChanged(null, null, str, null, null, null) },
                    label = { Text("Interest Rate (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IndigoAccent,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = String.format(Locale.US, "%.1f", plan.inflationRate),
                    onValueChange = { val str = it.toDoubleOrNull(); if (str != null) onPlanChanged(null, null, null, str, null, null) },
                    label = { Text("Inflation Rate (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AmberWarning,
                        unfocusedBorderColor = CardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            // 4. Interest Type Dropdown
            Column {
                Text("Interest Calculation Type", style = MaterialTheme.typography.labelLarge, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    OutlinedTextField(
                        value = plan.interestType.displayName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IndigoAccent,
                            unfocusedBorderColor = CardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false },
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                    ) {
                        InterestType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    onPlanChanged(null, null, null, null, type, null)
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
