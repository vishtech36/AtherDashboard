import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.maps.model.LatLng
import com.vishtech.atherdashboard.R
import com.vishtech.atherdashboard.data.Direction

class DashboardViewModel : ViewModel() {
    val topIcon = listOf(R.drawable.music)
    val middleIcons = listOf(R.drawable.location, R.drawable.bluetooth, R.drawable.subtract)
    val bottomIcon = listOf(R.drawable.gauge)
    val allIcons = topIcon + listOf("group") + bottomIcon

    val selectedIndex = mutableIntStateOf(0)
    val navMenuVisible = mutableStateOf(false)
    var selectedNavIndex = mutableIntStateOf(-1)
    val showSelectedPage = mutableStateOf(false)
    val isAnimating = mutableStateOf(false)
    val pageSelector = mutableIntStateOf(-1)
    val pageUpdater = mutableIntStateOf(-1)
    val newChangedLocation = mutableStateOf<LatLng?>(null)

    val navMenuItems = 3
}
