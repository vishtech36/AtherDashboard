package com.vishtech.atherdashboard.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishtech.atherdashboard.R
import com.vishtech.atherdashboard.ui.theme.LimeColor

@Composable
fun RangeEcoInfo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.End, // Aligns content inside the Column
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
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
                modifier = Modifier
                    .align(Alignment.Start)
                    .width(90.dp)
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

@Preview
@Composable
private fun PreviewRangeEcoInfo() {
    RangeEcoInfo()
}