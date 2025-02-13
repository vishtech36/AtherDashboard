import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishtech.atherdashboard.ui.theme.cardBackgroundColor

@Composable
fun SavedRoutesCard() {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    Column(
        modifier = Modifier
            .width(250.dp)
            .height(screenHeight)
            .background(cardBackgroundColor, shape = RoundedCornerShape(12.dp)) // Dark background
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Saved Routes",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "⭐ Favourites", fontSize = 14.sp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalScrollableRow(items = listOf("House", "Hostel","House", "Hostel","House", "Hostel"))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "🕒 Frequents", fontSize = 14.sp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalScrollableRow(items = listOf("Ashirvad Supermarket", "GoNative", ))

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "⚡ Chargers", fontSize = 14.sp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalScrollableRow(items = listOf("Mantri Skyvilla", "Athena"))
    }
}

@Composable
fun HorizontalScrollableRow(items: List<String>) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
    ) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .background(Color(0xFF343841), shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(text = item, color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Preview
@Composable
fun PreviewSavedRoutesCard() {
    SavedRoutesCard()
}
