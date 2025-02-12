package com.vishtech.atherdashboard.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vishtech.atherdashboard.R
import kotlinx.coroutines.launch

@Composable
fun Arrow(isAnimating: Boolean, arrow2Offset: Float, arrow3Offset: Float) {
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.spacedBy(-28.dp, Alignment.End)
    ) {
        Image(
            painter = painterResource(id = R.drawable.arrow_3),
            contentDescription = "Arrow Lightest",
            modifier = Modifier
                .size(40.dp)
                 // Moves 3rd arrow
        )
        Image(
            painter = painterResource(id = R.drawable.arrow_2),
            contentDescription = "Arrow Light",
            modifier = Modifier
                .size(40.dp)
                .offset(x = arrow2Offset.dp) // Moves 2nd arrow
        )
        Image(
            painter = painterResource(id = R.drawable.arrow_1),
            contentDescription = "Arrow Dark",
            modifier = Modifier.size(40.dp)
                .offset(x = arrow3Offset.dp)// Darkest arrow stays fixed
        )
    }
}

@Composable
fun MovingLayout(arrow2Offset: Float, arrow3Offset: Float) {
    // This layout moves exactly like the arrows
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .offset(x = arrow3Offset.dp) // Moves with 3rd arrow
            .background(androidx.compose.ui.graphics.Color.Blue)
    )
}

@Composable
fun ArrowWithSyncAnimation(isAnimating: Boolean) {
    val scope = rememberCoroutineScope()

    // Create Animatable offsets for movement
    val arrow2Offset = remember { Animatable(0f) }
    val arrow3Offset = remember { Animatable(0f) }

    // Start animation if isAnimating = true
    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            scope.launch {
                while (true) {
                    arrow2Offset.animateTo(10f, animationSpec = tween(500, easing = LinearEasing))
                    arrow2Offset.animateTo(0f, animationSpec = tween(500, easing = LinearEasing))
                }
            }
            scope.launch {
                while (true) {
                    arrow3Offset.animateTo(20f, animationSpec = tween(500, easing = LinearEasing))
                    arrow3Offset.animateTo(0f, animationSpec = tween(500, easing = LinearEasing))
                }
            }
        } else {
            arrow2Offset.snapTo(0f)
            arrow3Offset.snapTo(0f)
        }
    }

    Column {
        Arrow(isAnimating, arrow2Offset.value, arrow3Offset.value)
        MovingLayout(arrow2Offset.value, arrow3Offset.value)
    }
}

@Preview
@Composable
private fun PreviewArrowWithSyncAnimation() {
    ArrowWithSyncAnimation(isAnimating = true)
}
