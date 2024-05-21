package com.example.moengagenewstaskapp.utils

import android.content.Intent

fun handleNotifications(intent: Intent){
    // Handle notification click
    intent.extras?.let {
        val data = it.getString("key1")
        // Process the data
    }
}