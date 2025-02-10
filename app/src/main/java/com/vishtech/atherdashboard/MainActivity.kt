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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishtech.atherdashboard.ui.theme.AtherDashboardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AtherDashboardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Dashboard()
                    innerPadding
                }
            }
        }
    }
}

@Composable
fun Dashboard() {
    val icons = listOf(
        R.drawable.location,
        R.drawable.bluetooth,
        R.drawable.subtract
    )

    val focusRequesters = List(icons.size + 2) { FocusRequester() } // Extra slots for Music and Gauge
    var selectedIndex by remember { mutableStateOf(0) }

    // Auto-focus first item on launch
    LaunchedEffect(Unit) {
        focusRequesters[selectedIndex].requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF13171F))
            .onKeyEvent { event ->
                if (event.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) { // Prevents double jumps
                    when (event.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_DOWN -> {
                            if (selectedIndex < focusRequesters.size - 1) {
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
        // **Independent Music Icon (Top Center)**
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0x60FCFDFF))
                .padding(16.dp)
                .focusRequester(focusRequesters[0]) // Assign D-pad focus
                .focusable(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.music),
                contentDescription = "Music",
                tint = if (selectedIndex == 0) Color.Yellow else Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        // **D-pad Controlled Icons (Left)**
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0x60FCFDFF))
                .padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            icons.forEachIndexed { index, icon ->
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Icon $index",
                    tint = if (selectedIndex == index + 1) Color.Yellow else Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(8.dp)
                        .focusRequester(focusRequesters[index + 1])
                        .focusable()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // **D-pad Controlled Gauge Icon (Bottom Left)**
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0x60FCFDFF))
                .padding(16.dp)
                .focusRequester(focusRequesters[focusRequesters.size - 1]) // Assign last focus slot
                .focusable(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.gauge),
                contentDescription = "Gauge",
                tint = if (selectedIndex == focusRequesters.size - 1) Color.Yellow else Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // **Top-right icons (Battery, Bluetooth, Network)**
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

        // **Bottom-right stats**
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp)
        ) {
            Text(
                text = "99 km", // Dynamic range value
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            SimpleProgressBar()
            Text(
                text = "92%", // Dynamic battery value
                color = Color.White,
                fontSize = 16.sp
            )
            Icon(
                painter = painterResource(R.drawable.arr),
                contentDescription = "Navigation",
                tint = Color.White,
                modifier = Modifier.size(72.dp)
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Dashboard()
}
