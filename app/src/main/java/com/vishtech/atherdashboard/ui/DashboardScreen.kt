package com.vishtech.atherdashboard.ui

import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.vishtech.atherdashboard.R
import com.vishtech.atherdashboard.data.Direction
import com.vishtech.atherdashboard.ui.components.Battery
import com.vishtech.atherdashboard.ui.components.BatteryEcoInfo
import com.vishtech.atherdashboard.ui.components.GoogleMapScreen
import com.vishtech.atherdashboard.ui.components.NavPager
import com.vishtech.atherdashboard.ui.components.RangeEcoInfo
import com.vishtech.atherdashboard.ui.components.SideMenu
import com.vishtech.atherdashboard.ui.components.Speedometer
import com.vishtech.atherdashboard.ui.components.TimeDisplay
import com.vishtech.atherdashboard.ui.theme.backgroundColor
import kotlinx.coroutines.launch


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
    val coroutineScope = rememberCoroutineScope()
    var isAnimating by remember { mutableStateOf(false) }
    var pageSelector by remember { mutableIntStateOf(-1) }
    val navMenuItems = 3
    var pageUpdater by remember { mutableStateOf(-1) }
    var newChangedLocation by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(Unit) {
        focusRequesters[selectedIndex].requestFocus()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                                pageSelector = -1
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
                                pageSelector = selectedNavIndex
                                pageUpdater++
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
        LaunchedEffect(selectedNavIndex) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(
                    (selectedNavIndex).coerceAtLeast(0) // Prevents going below 0
                )
            }
        }

        // **Time Display (Top-Left)**
        TimeDisplay()

        // **Side Menu**
        SideMenu(
            isAnimating = isAnimating,
            icons = allIcons,
            middleIcons = middleIcons,
            selectedIndex = selectedIndex,
            navMenuVisible = navMenuVisible,
            modifier = Modifier,
            selectedNavIndex = selectedNavIndex,
            focusRequesters = focusRequesters
        ) {
            isAnimating = it
        }

        // **Speedometer (Center)**
        Speedometer()

        // Battery
        Battery()

        // **Battery & ECO Mode (Top-Right)**
        BatteryEcoInfo()

        // **Range & ECO Info (Right)**
        RangeEcoInfo()
    }

    AnimatedVisibility(
        visible = selectedNavIndex == 0 && navMenuVisible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        ) { fullWidth -> fullWidth } + fadeIn(animationSpec = tween(800)), // Moves in from right
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        ) { fullWidth -> fullWidth } + fadeOut(animationSpec = tween(800)) // Moves out to left
    ) {
        GoogleMapScreen(newChangedLocation)
    }
    LaunchedEffect(pagerState.currentPage) {
        if(navMenuVisible) {
            selectedNavIndex = pagerState.currentPage
        }
    }
    AnimatedVisibility(
        visible = navMenuVisible,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        ) { it } + fadeIn(animationSpec = tween(800)),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        ) { it } + fadeOut(animationSpec = tween(800))
    ) {
        NavPager(pagerState, pageSelector, pageUpdater) { newLocation ->
            newChangedLocation = newLocation
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun PreviewDashboard() {
    DashboardUI()
}