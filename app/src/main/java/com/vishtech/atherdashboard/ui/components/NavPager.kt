package com.vishtech.atherdashboard.ui.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NavPager(pagerState: PagerState) {
    val flingBehavior = PagerDefaults.flingBehavior(
        state = pagerState,
        pagerSnapDistance = PagerSnapDistance.atMost(1),
        lowVelocityAnimationSpec = tween(
            easing = FastOutLinearInEasing,
            durationMillis = 5000
        ),
        highVelocityAnimationSpec = rememberSplineBasedDecay(),
        snapAnimationSpec = tween(
            easing = FastOutSlowInEasing,
            durationMillis = 1000
        ),
    )

    Column {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            pageSize = object : PageSize {
                override fun Density.calculateMainAxisPageSize(
                    availableSpace: Int,
                    pageSpacing: Int
                ): Int {
                    return ((availableSpace - 2 * pageSpacing) * 0.7f).toInt() // Reduced size for adjacent items to be visible
                }
            },
            beyondBoundsPageCount = 3,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally
        ) { page ->
            val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
            val scale = animateFloatAsState(
                targetValue = 1f - (0.4f * kotlin.math.abs(pageOffset)), // Scale effect
                animationSpec = tween(durationMillis = 100)
            ).value

            Column(
                modifier = Modifier
                    .padding(start = 120.dp, top = 8.dp, bottom = 8.dp)
                    .background(Color(0xFF292E3A), shape = RoundedCornerShape(12.dp))
                    .padding(12.dp)
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        alpha = 1f - (0.3f * kotlin.math.abs(pageOffset)) // Optional fade effect
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(300.dp)
                        .fillMaxHeight()
                        .padding(0.dp)
                ) {
                    Text(
                        text = "Page: $page",
                        modifier = Modifier
                            .background(if (page % 2 == 0) Color.Gray else Color.Green)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun PreviewNavPager() {
    val pagerState = rememberPagerState(pageCount = { 3 })
    NavPager(pagerState)
}
