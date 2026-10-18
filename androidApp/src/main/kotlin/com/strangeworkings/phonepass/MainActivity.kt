package com.strangeworkings.phonepass

import android.Manifest
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val factory = DriverFactory(this)
        val profiledatabase = ProfileDB(factory.createDriver())
        val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 101
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(onAndroidAction = {
                if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ) {
                    val bluetoothManager: BluetoothManager =
                        getSystemService(BluetoothManager::class.java)
                    val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
                    val notificationChannel = NotificationChannel(
                        "com.strangewokrings.phonepass.newpass",
                        "Notification for a new pass",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    val notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.createNotificationChannel(notificationChannel)
                    if (bluetoothAdapter?.isEnabled == false) {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        val REQUEST_ENABLE_BT = 0
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                    }
                    //val CHANNEL = "com.strangeworkings.phonepass/foreground"
                    val intent = Intent(this, BackgroundScanner::class.java)
                    if (this.isServiceRunning(BackgroundScanner::class.java)) {
                        stopService(intent)
                    }
                    startForegroundService(intent)
                } else {
                    Toast.makeText(
                        this,
                        "No Permission",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //start scanning
            }, sumbitProfile = {
                val intent = Intent(this, BackgroundScanner::class.java)
                stopService(intent)
            }, profdb = profiledatabase)
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS,
                //Manifest.permission.BLUETOOTH_PRIVILEGED,
            ),
            READ_EXTERNAL_STORAGE_PERMISSION_CODE
        )


        //val tester = Profiles("jane doe", "she/her", bio = "yes")
        //Toast.makeText(this, tester.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//            val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
//            val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
//            if (bluetoothAdapter?.isEnabled == false) {
//                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                val REQUEST_ENABLE_BT = 0
//                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
//            }
//            val scanner = BluetoothScanner(bluetoothAdapter, this, Profiles("test","they/them","The quick brown fox"))
//            scanner.advertise()
//            scanner.scan()
//        }

    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() {
//    AppNavigation(onAndroidAction = {
//        Toast.makeText(
//            this,
//            "Hello from Android",
//            Toast.LENGTH_SHORT
//        ).show()
//    }))
//}

fun <T> Context.isServiceRunning(service: Class<T>): Boolean {
    return (getSystemService(ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Integer.MAX_VALUE)
        .any { it -> it.service.className == service.name }
}
