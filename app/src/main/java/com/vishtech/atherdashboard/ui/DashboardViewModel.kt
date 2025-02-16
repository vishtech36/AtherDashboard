import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.vishtech.atherdashboard.R

class DashboardViewModel : ViewModel() {
    private val topIcon = listOf(R.drawable.music)
    val middleIcons = listOf(R.drawable.location, R.drawable.bluetooth, R.drawable.subtract)
    private val bottomIcon = listOf(R.drawable.gauge)
    val allIcons = topIcon + listOf("group") + bottomIcon

    val selectedIndex = mutableIntStateOf(0)
    val navMenuVisible = mutableStateOf(false)
    var selectedNavIndex = mutableIntStateOf(-1)
    val showSelectedPage = mutableStateOf(false)
    val isAnimating = mutableStateOf(false)
    val pageSelector = mutableIntStateOf(-1)
    val pageUpdater = mutableIntStateOf(-1)
    val newChangedLocation = mutableStateOf<LatLng?>(null)
    val currentSpeed = mutableIntStateOf(0)

    val navMenuItems = 3
}
