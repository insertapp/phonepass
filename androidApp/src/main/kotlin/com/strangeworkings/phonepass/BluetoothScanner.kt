package com.strangeworkings.phonepass

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import android.os.Parcel
import android.os.ParcelUuid
import java.util.UUID
import android.util.Log
import android.util.SparseArray
import android.widget.Toast
import androidx.core.util.isNotEmpty
import java.nio.charset.Charset
import kotlin.with
import androidx.core.util.size
import java.nio.file.Files.writeString
import java.time.LocalDateTime
import java.util.Dictionary


class BluetoothScanner {

    var bladapter: BluetoothAdapter?
    val pUuid: ParcelUuid = ParcelUuid( UUID.fromString( "CDB7950D-73F1-4D4D-8E47-C090401DBD63" ) )
    var isScanning: Boolean = false

    private val foundusers: MutableList<OtherUserProfile> = mutableListOf()
    private val foundDeviecs: MutableList<String> = mutableListOf()
    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private val advertiseSettings = AdvertiseSettings.Builder()
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
        .setConnectable(true)
        .build();

    fun GetNewUsers(): MutableList<OtherUserProfile> {
        val tmp = foundusers.toMutableList()
        foundusers.clear()
        return tmp as MutableList<OtherUserProfile>
    }

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

    private val junkCallback = object: ScanCallback() {}
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            with(result.device) {
                if (foundDeviecs.contains(address)) {
                    return
                }

                Log.i(
                    "ScanCallback",
                    "Found BLE device! Name: ${name ?: "Unnamed"}, address: $address, uuid: ${uuids.contentToString()}"
                )
                bladapter?.bluetoothLeScanner?.stopScan(junkCallback)
                connectGatt(null,false,gattCallback)
            }

        }

        override fun onScanFailed(errorCode: Int) {
            Log.e("ScanCallback", "onScanFailed: code $errorCode")
        }

    }

    private val appcon: Context
    private val profile: String

    constructor(adapter: BluetoothAdapter?, ctx: Context, pf: UserProfile) {
        bladapter = adapter
        appcon = ctx
        profile = Json.encodeToString(OtherUserProfile(pf.name,pf.pronouns,pf.bio,0, LocalDateTime.now()))
    }


    private val gattCallback = object : BluetoothGattCallback() {
        private var queue: List<String> = "The Quick Brown Fox Jumps Over THe Lazy Dog Into the lazy river".chunked(12)
        private var offset: Int = 1
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            val deviceAddress = gatt.device.address
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    queue = profile.chunked(size = 12)
                    Log.w("BluetoothGattCallback", "Successfully connected to $deviceAddress")
                    gatt.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.w("BluetoothGattCallback", "Successfully disconnected from $deviceAddress")
                    gatt.close()
                }
            } else {
                Log.w(
                    "BluetoothGattCallback",
                    "Error $status encountered for $deviceAddress! Disconnecting..."
                )
                gatt.close()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            val characteristic = gatt.getService(service)?.getCharacteristic(chara)
            characteristic?.let {
                gatt.writeCharacteristic(
                    it,
                    queue[0].toByteArray(),
                    BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                )
            }

            //gatt.close()
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            if (offset >= queue.size) {
                characteristic?.let {
                    gatt?.writeCharacteristic(
                        it,
                        "\uEC2E".toByteArray(),
                        BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                    )
                }
                gatt?.close()
                scan()
                return
            }
            characteristic?.let {
                gatt?.writeCharacteristic(
                    it,
                    queue[offset].toByteArray(),
                    BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                )
            }
            offset += 1

        }
    }


    private val filter: ScanFilter = ScanFilter.Builder().setServiceUuid(
       pUuid
    ).build()

    fun scan() {
        bladapter?.bluetoothLeScanner?.startScan(listOf<ScanFilter>(filter), scanSettings, scanCallback)
        isScanning = true
    }

    fun advertise() {
        val data = AdvertiseData.Builder().setIncludeDeviceName(false)
            .addServiceUuid(pUuid)
            //.addServiceData(pUuid, "Data".toByteArray(Charset.forName("UTF-8")))
            .build()

        bladapter?.bluetoothLeAdvertiser?.startAdvertising(advertiseSettings,data,advertiseCallback)
        startGattServer(appcon)
    }

    private lateinit var gattServer: BluetoothGattServer
    private val service = UUID.fromString( "CDB7950D-73F1-4D4D-8E47-C090401DBD61" )
    private val chara = UUID.fromString( "CDB7950D-73F1-4D4D-8E47-C090401DBD62" )

    fun startGattServer(context: Context) {
        val manager = context.getSystemService(BluetoothManager::class.java)

        val characteristic = BluetoothGattCharacteristic(
            chara,
            BluetoothGattCharacteristic.PROPERTY_WRITE,
            BluetoothGattCharacteristic.PERMISSION_WRITE
        )

        val servicegatt = BluetoothGattService(
            service,
            BluetoothGattService.SERVICE_TYPE_PRIMARY
        ).apply {
            addCharacteristic(characteristic)

        }
        gattServer = manager.openGattServer(context, serverCallback)
        gattServer.addService(servicegatt)

    }
    private val inProgress: HashMap<String, String> = hashMapOf()
    private val serverCallback = object : BluetoothGattServerCallback() {
        override fun onCharacteristicWriteRequest(
            device: BluetoothDevice?,
            requestId: Int,
            characteristic: BluetoothGattCharacteristic?,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray?
        ) {
            super.onCharacteristicWriteRequest(
                device,
                requestId,
                characteristic,
                preparedWrite,
                responseNeeded,
                offset,
                value
            )
            val text = value?.let { String(it, Charsets.UTF_8) }
            if (text == "\uEC2E") {
                Log.e("HATT","WE DONE")
                foundDeviecs.add(device?.address!!)
                Log.i("TEST",foundDeviecs.toString())
                inProgress[device?.address!!]?.let { Log.e("HATT",it) }
                try {
                    val newuser: OtherUserProfile =
                        inProgress[device?.address!!]?.let { Json.decodeFromString(it) }!!
                    if (!foundusers.contains(newuser)) {
                        foundusers.add(newuser)
                    }
                } finally {
                    Log.i("H", "we did it")
                    inProgress[device?.address!!] = ""

                }

            } else {
                inProgress[device?.address!!] = inProgress.getOrDefault(device?.address!!, "")+text
            }
            text?.let { Log.e("HATT",it) }
            device?.let {
                gattServer.sendResponse(
                    it,
                    requestId,
                    BluetoothGatt.GATT_SUCCESS,
                    offset,
                    null
                )
            }


        }
    }
}

