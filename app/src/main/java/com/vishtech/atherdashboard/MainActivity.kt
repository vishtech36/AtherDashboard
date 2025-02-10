package com.vishtech.atherdashboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(color = 0xFF13171F)) // Match the background
    ) {
        // Top-left time
        Text(
            text = "12:30 PM",
            color = Color.White,
            fontFamily = FontFamily.Default,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )
        Column(modifier = Modifier.padding(top = 40.dp, start = 16.dp)
            .align(Alignment.TopStart)
            .clip(shape = RoundedCornerShape(30.dp))
            .background(color = Color(color = 0x60FCFDFF))
            .padding(8.dp)) {
            Icon(
                painter = painterResource(R.drawable.music),
                contentDescription = "Navigation",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Top-right icons
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
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Center Speedometer
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "0", // Dynamic speed value
                color = Color.White,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "km/h",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // Bottom-left navigation icon and ODO
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(color = Color(color = 0x60FCFDFF))
                .padding(8.dp)) {
                Icon(
                    painter = painterResource(R.drawable.gauge),
                    contentDescription = "Navigation",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ODO 1249 km",
                color = Color.White,
                fontSize = 14.sp
            )
        }

        // Middle Navigation
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .background(color = Color(color = 0x60FCFDFF))
                .padding(8.dp),

            horizontalAlignment = Alignment.End // Align text to the right

        ) {
            Icon(
                painter = painterResource(R.drawable.location),
                contentDescription = "Navigation",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(16.dp))
            Icon(
                painter = painterResource(R.drawable.bluetooth),
                contentDescription = "Navigation",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.height(16.dp))
            Icon(
                painter = painterResource(R.drawable.subtract),
                contentDescription = "Navigation",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Bottom-right stats
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Dashboard()
}

@Composable
fun SimpleProgressBar() {
    LinearProgressIndicator(
        progress = {
            0.92f // 50% progress
        },
        modifier = Modifier
            .width(50.dp)
            .height(10.dp),
        color = Color.Green,
        trackColor = Color.Gray,
    )
}
