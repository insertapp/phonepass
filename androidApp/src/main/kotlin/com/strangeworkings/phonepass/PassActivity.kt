package com.strangeworkings.phonepass

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
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

class PassActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val factory = DriverFactory(this)
        val profiledatabase = ProfileDB(factory.createDriver())
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation(onAndroidAction = {

            }, sumbitProfile = {

            }, profdb = profiledatabase, start = "Passing")
        }


        //val tester = Profiles("jane doe", "she/her", bio = "yes")
        //Toast.makeText(this, tester.toString(), Toast.LENGTH_LONG).show()
    }
}