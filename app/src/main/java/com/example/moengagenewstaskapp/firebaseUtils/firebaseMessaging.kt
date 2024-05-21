package com.example.moengagenewstaskapp.firebaseUtils

import com.google.firebase.messaging.FirebaseMessaging

fun firebaseMessaging(){
    // Get FCM token
    FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val token = task.result
            println("FCM Token: $token")
            // Send token to your server
        } else {
            println("Fetching FCM token failed: ${task.exception}")
        }
    }
}