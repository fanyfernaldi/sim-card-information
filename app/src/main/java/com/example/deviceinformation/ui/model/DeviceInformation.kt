package com.example.deviceinformation.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeviceInformation(
    val carrierName: String = "",
    val displayName: String = "",
    val slotIndex: Int = 0,
    val number: String = "",
    val countryIso: String = "",
    val imsi: String = "",
    val simId: String = "",
) : Parcelable

@Parcelize
data class ListDeviceInformation(
    val listDeviceInformation: List<DeviceInformation>
) : Parcelable