package com.wealthmetric.desktop

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.wealthmetric.app.ui.screens.MainScreen

fun main() = application {
    val windowState = rememberWindowState(size = DpSize(1280.dp, 850.dp))
    Window(
        onCloseRequest = ::exitApplication,
        title = "WealthMetric - Desktop Edition",
        state = windowState
    ) {
        MainScreen()
    }
}
