package com.vishtech.atherdashboard.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomePopupNotification(key: String?, showPopup: Boolean, onDismiss: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(showPopup) {
        if (showPopup) {
            coroutineScope.launch {
                delay(3000) // Auto-dismiss after 3 seconds
                onDismiss()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth(0.5f),
        contentAlignment = Alignment.BottomCenter // Align at the bottom
    ) {
        AnimatedVisibility(
            visible = showPopup,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) // Space from bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF1A1D21)) // Dark background
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = key,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Home",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "1.2 km • 16 min",
                        fontSize = 14.sp,
                        color = Color(0xFFB0B3B8)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomePopup() {
    var showPopup by remember { mutableStateOf(true) } // Always visible in preview
    HomePopupNotification(key = "Home", showPopup = showPopup, onDismiss = {})
}
