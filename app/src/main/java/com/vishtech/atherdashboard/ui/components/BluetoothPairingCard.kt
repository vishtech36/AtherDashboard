package com.vishtech.atherdashboard.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishtech.atherdashboard.R
import com.vishtech.atherdashboard.ui.theme.cardBackgroundColor
import kotlinx.coroutines.delay
import kotlin.coroutines.ContinuationInterceptor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BluetoothPairingCard(shouldFocus: Boolean, pagerState: PagerState) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            pagerState.animateScrollToPage(1)
        }
    }

    Column(
        modifier = Modifier
            .width(250.dp)
            .height(screenHeight)
            .background(cardBackgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Bluetooth",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(R.drawable.qr),
            contentDescription = "QR Code",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (isFocused) Color.White else Color(0xFF343841)) // Change color on focus
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }.onKeyEvent { event ->
                if (event.key == Key.DirectionLeft) {
                    println("Left key pressed, clearing focus")
                    isFocused = false
                    return@onKeyEvent true
                }
                false
            }
                .focusable(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Request to Pair",
                color = if (isFocused) Color.Black else Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    // Auto-focus "Request to Pair" button when shouldFocus is true
   LaunchedEffect(shouldFocus) {
        if (shouldFocus) {
            delay(300) // Small delay ensures focus request works correctly
            focusRequester.requestFocus()
        }
    }
}

@Preview
@Composable
fun PreviewBluetoothPairingCard() {
    Column {
        Text("Without Focus")
        //BluetoothPairingCard(shouldFocus = false) // Nothing is selected

        Spacer(modifier = Modifier.height(16.dp))

        Text("With Focus")
        //BluetoothPairingCard(shouldFocus = true) // "Request to Pair" button is focused
    }
}
