package com.naga.unlockclock

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import java.util.*

class NotificationService : Service() {

    private val CHANNEL_ID = "UnlockClockChannel"
    private var timer: Timer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(1, buildNotification())

        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, buildNotification())
            }
        }, 60000, 60000)  // delay 60s, then every 60s

        return START_STICKY
    }

    private fun buildNotification(): Notification {
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        val lastUnlock = prefs.getLong("last_unlock", System.currentTimeMillis())
        val minutesAgo = (System.currentTimeMillis() - lastUnlock) / 60000

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Last Unlock")
            .setContentText("$minutesAgo min ago")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "UnlockClock Notification",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}