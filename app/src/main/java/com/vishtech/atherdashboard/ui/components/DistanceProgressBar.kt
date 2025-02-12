package com.vishtech.atherdashboard.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vishtech.atherdashboard.ui.theme.LimeColor

@Composable
fun DistanceProgressBar() {
    LinearProgressIndicator(
        progress = { 0.92f },
        modifier = Modifier
            .width(90.dp)
            .height(10.dp),
        color = LimeColor,
        trackColor = Color.Gray,
    )
}