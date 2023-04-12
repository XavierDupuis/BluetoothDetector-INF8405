package com.example.bluetoothdetector.main.repository

import android.bluetooth.BluetoothManager
import android.content.Context
import android.hardware.SensorManager




class SensorRepository(private val context: Context) {
    private val mSensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager


    fun getSensorList() {
        println("----------------------------------------------------------------AAA----------------------------------------------------------------")
        println(mSensorManager.getSensorList(android.hardware.Sensor.TYPE_ALL))
        println("----------------------------------------------------------------BBB----------------------------------------------------------------")

    }

}