package com.example.bluetoothdetector.common.domain

import android.Manifest
import com.example.bluetoothdetector.R

enum class ModuleType {
    MAPS,
    DETECTOR,
}

data class Module(
    val moduleType: ModuleType,
    val description: Int,
    val permissions: Permissions = listOf()
)

val Modules = listOf(
    Module(
        ModuleType.MAPS,
        R.string.maps_module_description,
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    ),

    // Handle the bluetooth permission changes of api 31
    if (android.os.Build.VERSION.SDK_INT < 31) {
        Module(
            ModuleType.DETECTOR,
            R.string.detector_module_description,
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                // TODO : INVESTIGATE -> ADD / REMOVE
                // https://developer.android.com/guide/topics/connectivity/bluetooth/permissions
                // Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        )
    } else {
        Module(
            ModuleType.DETECTOR,
            R.string.detector_module_description,
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                // TODO : INVESTIGATE -> ADD / REMOVE
                // https://developer.android.com/guide/topics/connectivity/bluetooth/permissions
                // Manifest.permission.ACCESS_BACKGROUND_LOCATION
                Manifest.permission.BLUETOOTH_CONNECT, // API 31+
                Manifest.permission.BLUETOOTH_SCAN, // API 31+
            )
        )
    }
)

fun List<Module>.getAllPermissions(): List<String> {
    return map { it.permissions }.flatten()
}