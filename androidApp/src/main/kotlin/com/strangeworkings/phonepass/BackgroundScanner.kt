package com.strangeworkings.phonepass

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.NotificationCompat

class BackgroundScanner: Service() {
    private val CHANNEL_ID = "phonepass_scanner"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Phonepass is passing!")
            .setContentText("Running in background")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()

        startForeground(6767, notification)
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        val scanner = BluetoothScanner(bluetoothAdapter, this, Profiles("test","they/them","The quick brown fox"))
        scanner.advertise()
        scanner.scan()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Background Scanning",
                NotificationManager.IMPORTANCE_LOW
            )
            serviceChannel.setSound(null, null)
            serviceChannel.setShowBadge(false)
            serviceChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
}