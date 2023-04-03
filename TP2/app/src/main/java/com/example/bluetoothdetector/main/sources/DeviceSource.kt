package com.example.bluetoothdetector.main.sources

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bluetoothdetector.main.domain.DeviceConverter
import com.example.bluetoothdetector.main.domain.DeviceDao
import com.example.bluetoothdetector.main.model.Device
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Database(entities = [Device::class], version = 6)
@TypeConverters(DeviceConverter::class)
abstract class DeviceSource : RoomDatabase() {

    protected abstract val deviceDao: DeviceDao

    companion object {
        val Name: String = DeviceSource::javaClass.name
    }

    fun populateDevices(devices: MutableMap<String, Device>) {
        CoroutineScope(Dispatchers.IO).launch {
            val savedDevices = getAll().associateBy { device ->
                device.macAddress
            }
            safeOperation {
                devices.putAll(savedDevices)
            }
        }
    }

    fun observeDeviceCount(): Flow<Int> {
        return deviceDao.observeDeviceCount()
    }

    suspend fun getAll(): List<Device> {
        return deviceDao.getAll()
    }

    suspend fun delete(device: Device) {
        safeOperation {
            deviceDao.delete(device)
        }
    }

    suspend fun deleteAll() {
        safeOperation {
            deviceDao.deleteAll()
        }
    }

    suspend fun insert(device: Device) {
        safeOperation {
            deviceDao.insert(device)
        }
    }

    private suspend fun safeOperation(
        operation: suspend () -> Unit
    ) {
        try {
            operation()
        } catch (exception: Exception) {
            println("COULD NOT EXECUTE $operation")
            exception.printStackTrace()
        }
    }
}
