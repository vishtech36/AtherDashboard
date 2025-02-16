package com.vishtech.atherdashboard.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishtech.atherdashboard.utils.getCurrentTime
import kotlinx.coroutines.delay

@Composable
fun TimeDisplay(modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf(getCurrentTime()) }

    // Updates the time every 1 minute
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = getCurrentTime()
            delay(60_000L) // 1 minute delay
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.Start // Aligns content inside the Column
    ) {
        Text(
            text = currentTime,
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }

}

@Preview
@Composable
private fun PreviewTimeDisplay() {
    TimeDisplay()
}