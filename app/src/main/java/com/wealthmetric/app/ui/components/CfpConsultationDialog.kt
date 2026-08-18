package com.wealthmetric.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.wealthmetric.app.ui.theme.*

@Composable
fun CfpConsultationCard(
    onOpenBookingDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = IndigoContainer.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.5.dp, IndigoAccent.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = IndigoAccent,
                modifier = Modifier.size(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = "CFP Advisor Icon",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Book CFP® Advisory Session",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Get 1-on-1 personalized guidance on annuity selection, rate lock-in & tax-free pension planning.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                BounceButton(
                    onClick = onOpenBookingDialog,
                    containerColor = IndigoAccent,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Book Free Session",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CfpConsultationDialog(
    onDismiss: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var selectedTimeSlot by remember { mutableStateOf("Morning (10 AM - 1 PM)") }
    var selectedTopic by remember { mutableStateOf("Annuity Rate Lock & Selection") }
    var isSubmitted by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val timeSlots = listOf(
        "Morning (10 AM - 1 PM)",
        "Afternoon (2 PM - 5 PM)",
        "Evening (6 PM - 9 PM)"
    )

    val topics = listOf(
        "Annuity Rate Lock & Selection",
        "Retirement Corpus Acceleration",
        "Tax-Efficient Pension Structuring"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = CardSurface,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Close Button Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = null,
                            tint = IndigoAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSubmitted) "Booking Confirmed" else "CFP® Advisory Call",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = CardBorder)
                Spacer(modifier = Modifier.height(16.dp))

                if (!isSubmitted) {
                    Text(
                        text = "Schedule a complimentary 15-min phone session with a SEBI-registered / CFP financial planner.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Full Name Input
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            showError = false
                        },
                        label = { Text("Full Name") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = IndigoAccent)
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Phone Number Input
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                            showError = false
                        },
                        label = { Text("Mobile Number (WhatsApp)") },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = IndigoAccent)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (showError) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Please enter your name and phone number.",
                            color = RoseLoss,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Advisory Topic Selector
                    Text(
                        text = "Primary Focus Area",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    topics.forEach { topic ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                        ) {
                            RadioButton(
                                selected = (selectedTopic == topic),
                                onClick = { selectedTopic = topic },
                                colors = RadioButtonDefaults.colors(selectedColor = IndigoAccent)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = topic,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Preferred Slot Selector
                    Text(
                        text = "Preferred Time Slot",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    timeSlots.forEach { slot ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(36.dp)
                        ) {
                            RadioButton(
                                selected = (selectedTimeSlot == slot),
                                onClick = { selectedTimeSlot = slot },
                                colors = RadioButtonDefaults.colors(selectedColor = IndigoAccent)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = slot,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    BounceButton(
                        onClick = {
                            if (fullName.isNotBlank() && phoneNumber.isNotBlank()) {
                                isSubmitted = true
                            } else {
                                showError = true
                            }
                        },
                        containerColor = IndigoAccent,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Confirm Booking",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                } else {
                    // Success Screen State
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = EmeraldPrimary,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Session Booked Successfully!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Thank you, $fullName! A certified financial advisor will reach out to you at $phoneNumber during your preferred slot ($selectedTimeSlot).",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        BounceButton(
                            onClick = onDismiss,
                            containerColor = DarkButton,
                            contentColor = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        ) {
                            Text("Done", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
