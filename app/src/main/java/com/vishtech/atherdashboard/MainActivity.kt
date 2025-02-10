package com.vishtech.atherdashboard

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardUI()
        }
    }
}

@Composable
fun DashboardUI() {
    val icons = listOf(
        R.drawable.music,
        R.drawable.location,
        R.drawable.bluetooth,
        R.drawable.subtract
    )

    val focusRequesters = List(icons.size) { FocusRequester() }
    var selectedIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        focusRequesters[selectedIndex].requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF13171F))
            .onKeyEvent { event ->
                if (event.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    when (event.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_DOWN -> {
                            if (selectedIndex < icons.size - 1) {
                                selectedIndex++
                                focusRequesters[selectedIndex].requestFocus()
                            }
                            true
                        }
                        KeyEvent.KEYCODE_DPAD_UP -> {
                            if (selectedIndex > 0) {
                                selectedIndex--
                                focusRequesters[selectedIndex].requestFocus()
                            }
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
    ) {
        // **Time Display (Top-Left)**
        Text(
            text = "12:30 PM",
            color = Color.White,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 16.dp)
        )

        // **Side Menu with Icons & Selection Line**
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp, top = 50.dp)
        ) {
            icons.forEachIndexed { index, icon ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // **Selection Indicator**
                    if (selectedIndex == index) {
                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .height(40.dp)
                                .background(Color.White, shape = RoundedCornerShape(50))
                        )
                    } else {
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp)) // Space between line and icon

                    // **Icon**
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = "Icon $index",
                        tint = Color.White,
                        modifier = Modifier
                            .size(32.dp)
                            .focusRequester(focusRequesters[index])
                            .focusable()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp)) // Spacing between items
            }
        }

        // **Top-right status icons (Bluetooth, Network)**
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.bluetooth),
                contentDescription = "Bluetooth",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.network),
                contentDescription = "Network",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // **Speedometer (Center)**
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "0",
                color = Color.White,
                fontSize = 160.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif
            )
            Text(
                text = "km/h",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // **Battery & ECO Mode UI (Right)**
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        ) {
            Text(
                text = "99 km",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            SimpleProgressBar()
            Text(
                text = "92%",
                color = Color(0xFF4CAF50),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "ECO R",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(R.drawable.network),
                contentDescription = "Navigation",
                tint = Color.White,
                modifier = Modifier.size(72.dp)
            )
        }

        // **ODO Reading (Bottom Left)**
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ODO",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "1249 km",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun SimpleProgressBar() {
    LinearProgressIndicator(
        progress = { 0.92f },
        modifier = Modifier
            .width(50.dp)
            .height(10.dp),
        color = Color.Green,
        trackColor = Color.Gray,
    )
}
