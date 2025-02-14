package com.vishtech.atherdashboard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun GoogleMapScreen(newLocation: LatLng? = null) {
    val defaultLocation = LatLng(18.6748101, 73.8573322)
    var selectedLocation by remember { mutableStateOf(newLocation ?: defaultLocation) }
    val markerState = rememberMarkerState(position = selectedLocation)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation, 15f)
    }

    // Move the camera and update marker when newLocation changes
    LaunchedEffect(newLocation) {
        newLocation?.let {
            selectedLocation = it
            markerState.position = it
            cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(0.8f)
                .clip(RoundedCornerShape(12.dp))
                .background(com.vishtech.atherdashboard.ui.theme.cardBackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(com.vishtech.atherdashboard.ui.theme.cardBackgroundColor),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = markerState,
                    title = "Destination",
                    snippet = "Lat: ${selectedLocation.latitude}, Lng: ${selectedLocation.longitude}"
                )
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun PreviewGoogleMap() {
    GoogleMapScreen(LatLng(18.5204, 73.8567)) // Example location
}
