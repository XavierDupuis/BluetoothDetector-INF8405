package com.example.bluetoothdetector.common.domain

import android.Manifest
import com.example.bluetoothdetector.ui.theme.DETECTOR_MODULE_DESCRIPTION
import com.example.bluetoothdetector.ui.theme.MAPS_MODULE_DESCRIPTION

enum class ModuleType {
    MAPS,
    DETECTOR,
}

data class Module(
    val moduleType: ModuleType,
    val description: String = moduleType.name,
    val permissions: Permissions = listOf()
)

val MapsModule = Module(
    ModuleType.MAPS,
    MAPS_MODULE_DESCRIPTION,
    listOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
)

val Modules = listOf(
    MapsModule,
    // Handle the bluetooth permission changes of api 31
    if (android.os.Build.VERSION.SDK_INT < 31) {
        Module(
            ModuleType.DETECTOR,
            DETECTOR_MODULE_DESCRIPTION,
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
            )
        )
    } else {
        Module(
            ModuleType.DETECTOR,
            DETECTOR_MODULE_DESCRIPTION,
            listOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT, // API 31+
                Manifest.permission.BLUETOOTH_SCAN, // API 31+
            )
        )
    }
)

fun List<Module>.getAllPermissions(): List<String> {
    return map { it.permissions }.flatten()
}