package com.strangeworkings.phonepass

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
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
        val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 101
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                //Manifest.permission.BLUETOOTH_PRIVILEGED,
            ),
            READ_EXTERNAL_STORAGE_PERMISSION_CODE
        )
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val REQUEST_ENABLE_BT = 0
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
        setContent {
            App()
        }

        //val tester = Profiles("jane doe", "she/her", bio = "yes")
        //Toast.makeText(this, tester.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(this, permissions.contentToString(), Toast.LENGTH_LONG).show()
        print(permissions.contentToString())
        Toast.makeText(this, grantResults.contentToString(), Toast.LENGTH_LONG).show()
        print(grantResults.contentToString())

    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}