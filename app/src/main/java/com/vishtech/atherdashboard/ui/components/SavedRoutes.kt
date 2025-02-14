import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.vishtech.atherdashboard.locationMap
import com.vishtech.atherdashboard.ui.theme.cardBackgroundColor
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedRoutesCard(shouldFocus: Boolean, pagerState: PagerState, onLocationChanged: (LatLng) -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val firstItemFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .width(250.dp)
            .height(screenHeight)
            .background(cardBackgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Saved Routes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        SectionTitle("⭐ Favourites")
        FocusableRow(
            items = listOf("House", "Hostel", "Office"),
            firstItemFocusRequester,
            shouldFocus,
            pagerState,
            onLocationChanged
        )

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("🕒 Frequents")
        FocusableRow(items = listOf("Ashirvad Supermarket", "GoNative"), null, false, pagerState, onLocationChanged)

        Spacer(modifier = Modifier.height(16.dp))

        SectionTitle("⚡ Chargers")
        FocusableRow(items = listOf("Mantri Skyvilla", "Athena"), null, false, pagerState, onLocationChanged)
    }

    // If shouldFocus is true, request focus on the first item
    LaunchedEffect(shouldFocus) {
        if (shouldFocus) {
            delay(300)
            firstItemFocusRequester.requestFocus()
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = Color.LightGray
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FocusableRow(
    items: List<String>,
    firstItemFocusRequester: FocusRequester?,
    shouldFocus: Boolean,
    pagerState: PagerState,
    onLocationChanged: (LatLng) -> Unit
) {
    val focusRequesterList = remember { items.map { FocusRequester() } }
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState())
    ) {
        items.forEachIndexed { index, item ->
            var isFocused by remember { mutableStateOf(false) }


            LaunchedEffect(isFocused) {
                if (isFocused) {
                    pagerState.animateScrollToPage(0)
                    onLocationChanged(locationMap[item]!!)
                }
            }

            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(
                        if (isFocused) Color.White else Color(0xFF343841),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .focusRequester(
                        if (index == 0) firstItemFocusRequester
                            ?: focusRequesterList[index] else focusRequesterList[index]
                    )
                    .onFocusChanged { isFocused = it.isFocused }
                    .focusable()
            ) {
                Text(
                    text = item,
                    color = if (isFocused) Color.Black else Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSavedRoutesCard() {
    Column {
        Text("Without Focus")
        // SavedRoutesCard(shouldFocus = false, pagerState = ) // Nothing is selected

        Spacer(modifier = Modifier.height(16.dp))

        Text("With Focus")
        // SavedRoutesCard(shouldFocus = true) // First item of first row is selected
    }
}
