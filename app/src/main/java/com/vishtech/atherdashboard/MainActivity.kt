package com.vishtech.atherdashboard

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
    val topIcon = listOf(R.drawable.music) // Separate Music Icon
    val middleIcons = listOf(R.drawable.location, R.drawable.bluetooth, R.drawable.subtract) // Grouped Icons
    val bottomIcon = listOf(R.drawable.gauge) // Gauge as last item

    val allIcons = topIcon + listOf("group") + bottomIcon // "group" represents the middle section
    val focusRequesters = List(allIcons.size) { FocusRequester() }
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
                            if (selectedIndex < allIcons.size - 1) {
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

        // **Side Menu**
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp, top = 40.dp)
        ) {
            allIcons.forEachIndexed { index, item ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // **Selection Line**
                    if (selectedIndex == index) {
                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .height(if (item == "group") 140.dp else 40.dp)
                                .background(Color.White, shape = RoundedCornerShape(50))
                        )
                    } else {
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Spacer(modifier = Modifier.width(12.dp)) // Space between line and icon

                    if (item == "group") {
                        // **Grouped Middle Icons**
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFF292E3A))
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                                .focusRequester(focusRequesters[index])
                                .focusable()
                        ) {
                            middleIcons.forEach { icon ->
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF3A3F4B))
                                        .padding(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(icon),
                                        contentDescription = "Icon $icon",
                                        tint = Color.White,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp)) // Space between grouped icons
                            }
                        }
                    } else {
                        // **Other Icons**
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF292E3A))
                                .focusRequester(focusRequesters[index])
                                .focusable()
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (index == 0) R.drawable.music else R.drawable.gauge
                                ),
                                contentDescription = "Icon $index",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(32.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp)) // Space between items
            }
        }

        // **Speedometer (Center)**
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "0",
                fontSize = 100.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "km/h",
                fontSize = 20.sp,
                color = Color.Gray
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .width(40.dp)
                .height(6.dp)
                .background(Color.Green, shape = RoundedCornerShape(50))
                .align(Alignment.TopCenter)

        )
        // **Battery & ECO Mode (Top-Right)**
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.bluetooth),
                    contentDescription = "Bluetooth",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.network),
                    contentDescription = "Signal",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // **Range & ECO Info (Right)**
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 24.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "99 km",
                fontSize = 24.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            DistanceProgressBar()
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "92%",
                fontSize = 16.sp,
                color = Color.Green
            )
            Spacer(modifier = Modifier.height(20.dp))
            Icon(
                painter = painterResource(R.drawable.arr),
                contentDescription = "Signal",
                tint = Color.White,
                modifier = Modifier.size(72.dp)
            )
        }

        // **ODO Info (Bottom-Left)**
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ODO",
                color = Color.Gray,
                fontSize = 16.sp
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
fun DistanceProgressBar() {
    LinearProgressIndicator(
        progress = { 0.92f },
        modifier = Modifier
            .width(50.dp)
            .height(10.dp),
        color = Color.Green,
        trackColor = Color.Gray,
    )
}

@Preview
@Composable
private fun PreviewDashboard() {
    DashboardUI()
}