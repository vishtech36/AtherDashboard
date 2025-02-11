package com.vishtech.atherdashboard

import android.os.Bundle
import android.util.Log
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
import com.vishtech.atherdashboard.ui.theme.LimeColor

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
    var navPanelSelected by remember { mutableStateOf(true) }
    var navMenuVisible by remember { mutableStateOf(false) }
    var selectedNavIndex by remember { mutableStateOf(0) }
    var showSelectedPage by remember { mutableStateOf(false) }

    val navMenuItems = listOf("Navigation", "Trip Info", "Settings", "Battery Info")


    LaunchedEffect(Unit) {
        focusRequesters[selectedIndex].requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF13171F))
            .padding(top = 8.dp, bottom = 8.dp)
            .onKeyEvent { event ->
                if (event.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    when (event.nativeKeyEvent.keyCode) {
                        KeyEvent.KEYCODE_DPAD_DOWN -> {
                            if (navMenuVisible) {
                                if (selectedNavIndex < navMenuItems.size - 1) {
                                    selectedNavIndex++
                                }
                            } else {
                                if (selectedIndex < allIcons.size - 1) {
                                    selectedIndex++
                                    focusRequesters[selectedIndex].requestFocus()
                                }
                            }
                            true
                        }

                        KeyEvent.KEYCODE_DPAD_UP -> {
                            if (navMenuVisible) {
                                if (selectedNavIndex > 0) {
                                    selectedNavIndex--
                                }
                            } else {
                                if (selectedIndex > 0) {
                                    selectedIndex--
                                    focusRequesters[selectedIndex].requestFocus()
                                }
                            }
                            true
                        }

                        KeyEvent.KEYCODE_DPAD_LEFT -> {
                            if (navMenuVisible) {
                                navMenuVisible = false
                                showSelectedPage = false
                            }
                            true
                        }


                        KeyEvent.KEYCODE_DPAD_RIGHT -> {
                            if (!navMenuVisible && allIcons[selectedIndex] == "group") {
                                navMenuVisible = true
                                selectedNavIndex = 0 // Highlight first navigation icon
                            } else if (navMenuVisible) {
                                showSelectedPage = true // Open the selected page
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
        Row(modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(start = 8.dp, top = 40.dp)){
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                allIcons.forEachIndexed { index, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // **Selection Line**
                        if (selectedIndex == index) {
                            Log.d("AA", "Selected Index: $selectedIndex");
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
                                            .background(Color(0x003A3F4B))
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(icon),
                                            contentDescription = "Icon $icon",
                                            tint = if(selectedIndex ==index) Color.White else Color.Gray,
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
                                    tint = if(selectedIndex ==index) Color.White else Color.Gray,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .size(32.dp)
                                        .align(Alignment.Center)
                                )
                            }
                            if(index != 0) {
                                Row(
                                    modifier = Modifier
                                        .padding(start = 16.dp, bottom = 16.dp, top = 8.dp),
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
                    }
                    if(index != allIcons.size-1)
                        Spacer(modifier = Modifier.height(24.dp).weight(1f)) // Space between items
                }
            }
            // **ODO Info (Bottom-Left)**
            // **Navigation Panel on Right**
            if (navMenuVisible) {
                Column(
                    modifier = Modifier
                        .padding(start = 20.dp, top = 40.dp)
                        .background(Color(0xFF292E3A), shape = RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    navMenuItems.forEachIndexed { index, item ->
                        Text(
                            text = item,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (index == selectedNavIndex) Color.White else Color.Gray,
                            modifier = Modifier
                                .padding(8.dp)
                                .focusable()
                        )
                    }
                }
            }
        }

        // **Selected Page Content**
        if (showSelectedPage) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You selected: ${navMenuItems[selectedNavIndex]}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }


        // **Speedometer (Center)**
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "0",
                fontSize = 120.sp,
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
                .background(LimeColor, shape = RoundedCornerShape(50))
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
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.network),
                    contentDescription = "Signal",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
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
            Row {
                Text(
                    text = "99",
                    fontSize = 48.sp,
                    color = Color.White
                )
                Text(
                    text = "km",
                    fontSize = 24.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            DistanceProgressBar()
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "92%",
                fontSize = 16.sp,
                color = LimeColor,
                modifier = Modifier.align(Alignment.Start).width(90.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Icon(
                painter = painterResource(R.drawable.arr),
                contentDescription = "Signal",
                tint = Color.White,
                modifier = Modifier.size(72.dp)
            )
        }
    }
}



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

@Preview
@Composable
private fun PreviewDashboard() {
    DashboardUI()
}