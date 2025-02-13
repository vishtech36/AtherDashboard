package com.vishtech.atherdashboard.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishtech.atherdashboard.ui.theme.cardBackgroundColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ControlsCard(shouldFocus: Boolean, pagerState: PagerState) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val firstButtonFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .width(250.dp)
            .height(screenHeight)
            .background(cardBackgroundColor, shape = RoundedCornerShape(12.dp))
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
        ControlButton(
            label = "Intensity",
            value = "LOW",
            focusRequester = firstButtonFocusRequester,
            pagerState
        )
        ControlButton(label = "Auto Switch", value = "ON", pagerState = pagerState)

        Spacer(modifier = Modifier.height(16.dp))

        // About Vehicle Section
        SectionTitle(title = "🚙 About vehicle")
        ControlButton(label = "Trickle", value = "ON", pagerState = pagerState)
        ControlButton(label = "AutoLight", value = "ON", pagerState = pagerState)
    }

    // Auto-focus the first control button when the screen loads
    LaunchedEffect(shouldFocus) {
        if (shouldFocus) {
            delay(300) // Small delay to ensure focus request
            firstButtonFocusRequester.requestFocus()
        }
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ControlButton(
    label: String,
    value: String,
    focusRequester: FocusRequester? = null,
    pagerState: PagerState
) {
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            pagerState.animateScrollToPage(2)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isFocused) Color.White else Color(0xFF343841),
                shape = RoundedCornerShape(20.dp)
            )
            .focusRequester(
                focusRequester ?: FocusRequester()
            ) // Apply focus requester only if passed
            .onFocusChanged { focusState -> isFocused = focusState.isFocused }
            .focusable()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = if (isFocused) Color.Black else Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = if (isFocused) Color.Black else Color.White,
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
    Column {
        Text("Without Focus")
        // ControlsCard(shouldFocus = false) // No focus initially

        Spacer(modifier = Modifier.height(16.dp))

        Text("With Focus")
        // ControlsCard(shouldFocus = true) // First control button will be focused
    }
}
