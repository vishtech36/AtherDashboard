package com.vishtech.atherdashboard.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TimeDisplay(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.Start // Aligns content inside the Column
    ) {
        Text(
            text = "12:30 PM",
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