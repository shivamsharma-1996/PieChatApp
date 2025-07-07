package com.shivam.piechatapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.format(pattern: String = "MMM dd, hh:mm a"): String {
    return SimpleDateFormat(pattern, Locale.getDefault()).format(this)
}