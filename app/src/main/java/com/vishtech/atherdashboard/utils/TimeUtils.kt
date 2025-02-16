package com.vishtech.atherdashboard.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date())
}

fun main() {
    println(getCurrentTime()) // Example output: 10:10 AM
}
