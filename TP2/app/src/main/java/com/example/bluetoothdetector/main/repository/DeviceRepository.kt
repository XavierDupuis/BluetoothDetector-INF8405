package com.example.bluetoothdetector.main.repository

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.example.bluetoothdetector.main.model.Device
import com.example.bluetoothdetector.main.sources.DeviceSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
    private val deviceSource: DeviceSource,
) {
    //  TODO : UPDATE / REMOVE
    val a = "SomeRepositoryValue"

    val devices: MutableMap<String, Device> = mutableStateMapOf(
        "FAKE_MAC_ADDRESS_1" to Device(),
        "FAKE_MAC_ADDRESS_2" to Device()
    )

    fun share(device: Device) {
        deviceSource.share(device)
    }
}