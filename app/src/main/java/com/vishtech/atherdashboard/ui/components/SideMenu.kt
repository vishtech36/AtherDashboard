package com.vishtech.atherdashboard.ui.components

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishtech.atherdashboard.R

@Composable
fun SideMenu(
    icons: List<Any>,
    middleIcons: List<Int>,
    selectedIndex: Int,
    selectedNavIndex: Int,
    focusRequesters: List<FocusRequester>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.Start // Aligns content inside the Column
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, top = 40.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                icons.forEachIndexed { index, item ->
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
                                middleIcons.forEachIndexed { i, icon ->
                                    Box(
                                        modifier = Modifier
                                            .size(50.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (selectedNavIndex == i) Color.White else Color(
                                                    0x003A3F4B
                                                )
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(icon),
                                            contentDescription = "Icon $icon",
                                            tint = if (selectedNavIndex == i) Color.Gray else
                                                if (selectedIndex == index) Color.White else Color.Gray,
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
                                    tint = if (selectedIndex == index) Color.White else Color.Gray,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .size(32.dp)
                                        .align(Alignment.Center)
                                )
                            }
                            if (index != 0) {
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
                    if (index != icons.size - 1)
                        Spacer(
                            modifier = Modifier
                                .height(24.dp)
                                .weight(1f)
                        ) // Space between items
                }
            }
        }
    }
}
