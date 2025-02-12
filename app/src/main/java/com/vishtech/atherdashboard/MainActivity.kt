package com.vishtech.atherdashboard

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishtech.atherdashboard.data.Direction
import com.vishtech.atherdashboard.ui.theme.LimeColor
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DashboardUI()
            //NavPager()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardUI() {
    val topIcon = listOf(R.drawable.music) // Separate Music Icon
    val middleIcons =
        listOf(R.drawable.location, R.drawable.bluetooth, R.drawable.subtract) // Grouped Icons
    val bottomIcon = listOf(R.drawable.gauge) // Gauge as last item

    val allIcons = topIcon + listOf("group") + bottomIcon // "group" represents the middle section
    val focusRequesters = List(allIcons.size) { FocusRequester() }
    var selectedIndex by remember { mutableIntStateOf(0) }
    var navMenuVisible by remember { mutableStateOf(false) }
    var selectedNavIndex by remember { mutableIntStateOf(-1) }
    var showSelectedPage by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 3 })
    var direction by remember { mutableStateOf(Direction.NONE) }
    var currentPage by remember { mutableIntStateOf(0) }
    val navMenuItems = 3


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
                                if (selectedNavIndex < navMenuItems - 1) {
                                    selectedNavIndex++
                                }
                                direction = Direction.DOWN
                                if (currentPage <= 3)
                                    currentPage += 1
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
                                direction = Direction.UP
                                if (currentPage >= 0)
                                    currentPage -= 1
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
                                selectedNavIndex = -1
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
        val coroutineScope = rememberCoroutineScope()
        LaunchedEffect(currentPage) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(
                    (currentPage).coerceAtLeast(0) // Prevents going below 0
                )
            }
        }
        // **Side Menu**
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp, top = 40.dp)
        ) {
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
                    if (index != allIcons.size - 1)
                        Spacer(
                            modifier = Modifier
                                .height(24.dp)
                                .weight(1f)
                        ) // Space between items
                }
            }
            // **ODO Info (Bottom-Left)**
            // **Navigation Panel on Right**

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
                    text = "You selected: ${navMenuItems}",
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
    if (navMenuVisible) {
        NavPager(pagerState)
    }
}


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

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun PreviewDashboard() {
    DashboardUI()
}