package com.strangeworkings.phonepass

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import java.util.UUID
import android.util.Log
import android.widget.Toast
import java.nio.charset.Charset
import kotlin.with


class BluetoothScanner {

    var bladapter: BluetoothAdapter?
    var isScanning: Boolean = false
    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val advertiseSettings = AdvertiseSettings.Builder()
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
        .setConnectable(true)
        .build();


    private val advertiseCallback = object: AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.e("Advertiser", "STARTING ADVERTISEMENT")
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)
            Log.e("Advertiser", "ADVERTISEMENT FAILED $errorCode")
        }
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            with(result.device) {
                Log.i(
                    "ScanCallback",
                    "Found BLE device! Name: ${name ?: "Unnamed"}, address: $address"
                )
                if (address == "CDB7950D-73F1-4D4D-8E47-C090502DBD63") {
                    Log.i("Scanner","TARGET FOUND")
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("ScanCallback", "onScanFailed: code $errorCode")
        }

    }

    constructor(adapter: BluetoothAdapter?) {
        bladapter = adapter
    }


    fun scan() {
        bladapter?.bluetoothLeScanner?.startScan(null, scanSettings, scanCallback)
        isScanning = true
    }

    fun advertise() {
        val pUuid: ParcelUuid = ParcelUuid( UUID.fromString( "CDB7950D-73F1-4D4D-8E47-C090502DBD63" ) )
        val data = AdvertiseData.Builder().setIncludeDeviceName(false)
            .addServiceUuid(pUuid)
            //.addServiceData(pUuid, "Data".toByteArray(Charset.forName("UTF-8")))
            .build()

        bladapter?.bluetoothLeAdvertiser?.startAdvertising(advertiseSettings,data,advertiseCallback)
    }
}

