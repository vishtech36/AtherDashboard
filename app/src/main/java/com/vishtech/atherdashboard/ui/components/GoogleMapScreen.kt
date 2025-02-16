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
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*

@Composable
fun GoogleMapScreen(startLocation: LatLng = LatLng(18.6748101, 73.8573322), newLocation: LatLng? = null) {
    var selectedLocation by remember { mutableStateOf(newLocation ?: startLocation) }

    val startMarkerState = rememberMarkerState(position = startLocation)
    val endMarkerState = rememberMarkerState(position = selectedLocation)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(startLocation, 15f)
    }

    // Generate curved path points
    val curvePoints = generateCurvedPolyline(startLocation, selectedLocation, 0.2)

    LaunchedEffect(newLocation) {
        newLocation?.let {
            selectedLocation = it
            endMarkerState.position = it
            val bounds = LatLngBounds.builder()
                .include(startLocation)
                .include(it)
                .build()
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 100))
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
                    state = startMarkerState,
                    title = "Start Location",
                    snippet = "Lat: ${startLocation.latitude}, Lng: ${startLocation.longitude}",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )

                Marker(
                    state = endMarkerState,
                    title = "Destination",
                    snippet = "Lat: ${selectedLocation.latitude}, Lng: ${selectedLocation.longitude}"
                )

                // Draw a curved polyline
                Polyline(
                    points = curvePoints,
                    color = androidx.compose.ui.graphics.Color.Blue,
                    width = 5f
                )
            }
        }
    }
}

// Function to generate curved points
fun generateCurvedPolyline(start: LatLng, end: LatLng, curvature: Double): List<LatLng> {
    val midLat = (start.latitude + end.latitude) / 2
    val midLng = (start.longitude + end.longitude) / 2
    val curveHeight = curvature * 0.001  // Adjust this for more/less curve

    val controlPoint = LatLng(midLat + curveHeight, midLng + curveHeight)

    val points = mutableListOf<LatLng>()
    for (t in 0..100) { // Increase for smoother curve
        val u = t / 100.0
        val lat = (1 - u) * (1 - u) * start.latitude + 2 * (1 - u) * u * controlPoint.latitude + u * u * end.latitude
        val lng = (1 - u) * (1 - u) * start.longitude + 2 * (1 - u) * u * controlPoint.longitude + u * u * end.longitude
        points.add(LatLng(lat, lng))
    }
    return points
}

@Preview(
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
private fun PreviewGoogleMap() {
    GoogleMapScreen(
        startLocation = LatLng(18.6748101, 73.8573322),
        newLocation = LatLng(18.6758101, 73.8583322) // Example destination
    )
}
