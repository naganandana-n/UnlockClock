package com.naga.unlockclock

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start your background service
        val serviceIntent = Intent(this, NotificationService::class.java)
        startService(serviceIntent)

        // Compose content (blank for now)
        setContent {
            // You can put UI later here
        }
    }
}