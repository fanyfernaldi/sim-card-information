package com.example.deviceinformation.ui.screen.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.deviceinformation.ui.model.DeviceInformation

class MainViewModel : ViewModel() {
    private val _deviceInformation = MutableLiveData<DeviceInformation>()
    val deviceInformation: LiveData<DeviceInformation> get() = _deviceInformation

    fun updateDeviceInformation(newValue: DeviceInformation) {
        _deviceInformation.value = newValue
    }
}