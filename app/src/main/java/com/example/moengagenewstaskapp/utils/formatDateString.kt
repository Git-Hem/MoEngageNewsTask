package com.example.moengagenewstaskapp.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun formatDateString(dateString: String, inputFormat: String, outputFormat: String): String? {
    return try {
        val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
        val date = inputDateFormat.parse(dateString)
        val outputDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())
        outputDateFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}