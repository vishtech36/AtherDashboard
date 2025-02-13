package com.vishtech.atherdashboard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishtech.atherdashboard.ui.theme.cardBackgroundColor

@Composable
fun ControlsCard() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .width(250.dp)
            .height(screenHeight)
            .background(cardBackgroundColor, shape = RoundedCornerShape(12.dp)) // Dark background
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        // Title
        Text(
            text = "Controls",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Airflow Section
        SectionTitle(title = "⚙ Airflow")
        ControlButton(label = "Intensity", value = "LOW", isHighlighted = true)
        ControlButton(label = "Auto Switch", value = "ON")

        Spacer(modifier = Modifier.height(16.dp))

        // About Vehicle Section
        SectionTitle(title = "🚙 About vehicle")
        ControlButton(label = "Trickle", value = "ON")
        ControlButton(label = "AutoLight", value = "ON")
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color.LightGray
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ControlButton(label: String, value: String, isHighlighted: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isHighlighted) Color.White else Color(0xFF343841),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = if (isHighlighted) Color.Black else Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = if (isHighlighted) Color.Black else Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Preview
@Composable
fun PreviewControlsCard() {
    ControlsCard()
}
