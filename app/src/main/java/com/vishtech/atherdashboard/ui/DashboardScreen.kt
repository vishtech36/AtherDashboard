package com.vishtech.atherdashboard.ui

import DashboardViewModel
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun DashboardUI(viewModel: DashboardViewModel = remember { DashboardViewModel() }) {
    val focusRequesters = List(viewModel.allIcons.size) { FocusRequester() }
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequesters[viewModel.selectedIndex.intValue].requestFocus()
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
                            if (viewModel.navMenuVisible.value) {
                                if (viewModel.selectedNavIndex.intValue < viewModel.navMenuItems - 1) {
                                    viewModel.selectedNavIndex.intValue++
                                }
                            } else {
                                if (viewModel.selectedIndex.intValue < viewModel.allIcons.size - 1) {
                                    viewModel.selectedIndex.intValue++
                                    focusRequesters[viewModel.selectedIndex.intValue].requestFocus()
                                }
                            }
                            true
                        }

                        KeyEvent.KEYCODE_DPAD_UP -> {
                            if (viewModel.navMenuVisible.value) {
                                if (viewModel.selectedNavIndex.intValue > 0) {
                                    viewModel.selectedNavIndex.intValue--
                                }
                            } else {
                                if (viewModel.selectedIndex.intValue > 0) {
                                    viewModel.selectedIndex.intValue--
                                    focusRequesters[viewModel.selectedIndex.intValue].requestFocus()
                                }
                            }
                            true
                        }

                        KeyEvent.KEYCODE_DPAD_LEFT -> {
                            if (viewModel.navMenuVisible.value) {
                                viewModel.navMenuVisible.value = false
                                viewModel.showSelectedPage.value = false
                                viewModel.pageSelector.intValue = -1
                                viewModel.selectedNavIndex.intValue = -1
                            }
                            true
                        }


                        KeyEvent.KEYCODE_DPAD_RIGHT -> {
                            if (!viewModel.navMenuVisible.value && viewModel.allIcons[viewModel.selectedIndex.intValue] == "group") {
                                viewModel.navMenuVisible.value = true
                                viewModel.selectedNavIndex.intValue =
                                    0 // Highlight first navigation icon
                            } else if (viewModel.navMenuVisible.value) {
                                viewModel.showSelectedPage.value = true // Open the selected page
                                viewModel.pageSelector.intValue = viewModel.selectedNavIndex.intValue
                                viewModel.pageUpdater.intValue++
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
        LaunchedEffect(viewModel.selectedNavIndex.intValue) {
            coroutineScope.launch {
                pagerState.animateScrollToPage(
                    (viewModel.selectedNavIndex.intValue).coerceAtLeast(0) // Prevents going below 0
                )
            }
        }

        // **Time Display (Top-Left)**
        TimeDisplay()

        // **Side Menu**
        SideMenu(
            isAnimating = viewModel.isAnimating.value,
            icons = viewModel.allIcons,
            middleIcons = viewModel.middleIcons,
            selectedIndex = viewModel.selectedIndex.intValue,
            navMenuVisible =  viewModel.navMenuVisible.value,
            modifier = Modifier,
            selectedNavIndex = viewModel.selectedNavIndex.intValue,
            focusRequesters = focusRequesters
        ) {
            viewModel.isAnimating.value = it
        }

        // **Speedometer (Center)**
        Speedometer(viewModel)

        // Battery
        Battery()

        // **Battery & ECO Mode (Top-Right)**
        BatteryEcoInfo()

        // **Range & ECO Info (Right)**
        RangeEcoInfo()
    }

    AnimatedVisibility(
        visible = viewModel.selectedNavIndex.intValue == 0 && viewModel.navMenuVisible.value,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        ) { fullWidth -> fullWidth } + fadeIn(animationSpec = tween(800)), // Moves in from right
        exit = slideOutHorizontally(
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        ) { fullWidth -> fullWidth } + fadeOut(animationSpec = tween(800)) // Moves out to left
    ) {
        viewModel.newChangedLocation.value?.let { GoogleMapScreen(newLocation = it) }
    }
    LaunchedEffect(pagerState.currentPage) {
        if(viewModel.navMenuVisible.value) {
            viewModel.selectedNavIndex.intValue = pagerState.currentPage
        }
    }
    AnimatedVisibility(
        visible = viewModel.navMenuVisible.value,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        ) { it } + fadeIn(animationSpec = tween(800)),
        exit = slideOutVertically(
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        ) { it } + fadeOut(animationSpec = tween(800))
    ) {
        NavPager(pagerState, viewModel.pageSelector.intValue, viewModel.pageUpdater.intValue) { newLocation ->
            viewModel.newChangedLocation.value = newLocation
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