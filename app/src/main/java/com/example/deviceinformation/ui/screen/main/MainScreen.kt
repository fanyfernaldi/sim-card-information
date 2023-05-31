package com.example.deviceinformation.ui.screen.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.deviceinformation.ui.misc.toJsonApi
import com.example.deviceinformation.ui.model.DeviceInformation
import com.example.deviceinformation.ui.model.ListDeviceInformation

@SuppressLint("MissingPermission")
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    navigateToDetail: (String) -> Unit,
) {
    // Unused in this case
    val deviceInformation: DeviceInformation? by viewModel.deviceInformation.observeAsState()

    var carrierName = ""
    var displayName = ""
    var slotIndex = 0
    var number = ""
    var countryIso = ""
    var imsi = ""
    var simId = ""

    val context = LocalContext.current

    val telephonyManager: TelephonyManager =
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    val subscriptionManager =
        context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
    val sm = subscriptionManager.activeSubscriptionInfoList

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val permissions = arrayOf(
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_NUMBERS,
            Manifest.permission.READ_PHONE_STATE,
        )

        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
            if (areGranted) {
                var listDeviceInformation = mutableListOf<DeviceInformation>()
                sm.forEach { subscriptionInfo ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && subscriptionInfo != null) {
                        carrierName = subscriptionInfo.carrierName.toString()
                        displayName = subscriptionInfo.displayName.toString()
                        slotIndex = subscriptionInfo.simSlotIndex
                        number = subscriptionInfo.number.orEmpty()
                        imsi = subscriptionInfo.subscriptionId.toString()
                        simId = subscriptionInfo.iccId
                        if (subscriptionInfo.countryIso != null && subscriptionInfo.countryIso.isNotEmpty()) {
                            countryIso = subscriptionInfo.countryIso
                        } else if (telephonyManager.simCountryIso != null) {
                            countryIso = telephonyManager.simCountryIso
                        }
                        val newDeviceInformation = DeviceInformation(
                            carrierName,
                            displayName,
                            slotIndex,
                            number,
                            countryIso,
                            imsi,
                            simId,
                        )
                        listDeviceInformation.add(newDeviceInformation)
                    } else {
                        if (telephonyManager.simOperator != null) {
                            carrierName = telephonyManager.simOperatorName

                        }
                        if (telephonyManager.simOperator != null) {
                            displayName = telephonyManager.simOperatorName

                        }
                        if (telephonyManager.simCountryIso != null) {
                            countryIso = telephonyManager.simCountryIso
                        }
                        if (telephonyManager.subscriberId != null) {
                            imsi = telephonyManager.subscriberId
                        }

                        if (telephonyManager.simSerialNumber != null) {
                            simId = telephonyManager.simSerialNumber
                        }

                        if (telephonyManager.line1Number != null && telephonyManager.line1Number.isNotEmpty()) {
                            number = telephonyManager.line1Number
                        }
                        val newDeviceInformation = DeviceInformation(
                            carrierName,
                            displayName,
                            slotIndex,
                            number,
                            countryIso,
                            imsi,
                            simId,
                        )

                        listDeviceInformation.add(newDeviceInformation)
                    }
                }

                val listDeviceInformationJsonString =
                    ListDeviceInformation(listDeviceInformation = listDeviceInformation).toJsonApi()
                if (listDeviceInformationJsonString != null) {
                    navigateToDetail(listDeviceInformationJsonString)
                }
            } else {
                // show dialog
            }
        }
        Button(onClick = {
            checkAndRequestSimInfoPermissions(
                context,
                permissions,
                launcherMultiplePermissions,
                navigateToDetail = navigateToDetail
            )
        }) {
            Text(text = "Check Device Information")
        }
    }

}

@SuppressLint("ServiceCast", "HardwareIds")
fun checkAndRequestSimInfoPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    navigateToDetail: (String) -> Unit,
) {
    if (
        permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    ) {

        var carrierName = ""
        var displayName = ""
        var slotIndex = 0
        var number = ""
        var countryIso = ""
        var imsi = ""
        var simId = ""
        val telephonyManager: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val subscriptionManager =
            context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        val sm = subscriptionManager.activeSubscriptionInfoList
        var listDeviceInformation = mutableListOf<DeviceInformation>()
        sm.forEach { subscriptionInfo ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 && subscriptionInfo != null) {
                carrierName = subscriptionInfo.carrierName.toString()
                displayName = subscriptionInfo.displayName.toString()
                slotIndex = subscriptionInfo.simSlotIndex
                number = subscriptionInfo.number.orEmpty()
                imsi = subscriptionInfo.subscriptionId.toString()
                simId = subscriptionInfo.iccId
                if (subscriptionInfo.countryIso != null && subscriptionInfo.countryIso.isNotEmpty()) {
                    countryIso = subscriptionInfo.countryIso
                } else if (telephonyManager.simCountryIso != null) {
                    countryIso = telephonyManager.simCountryIso
                }
                val newDeviceInformation = DeviceInformation(
                    carrierName,
                    displayName,
                    slotIndex,
                    number,
                    countryIso,
                    imsi,
                    simId,
                )
                listDeviceInformation.add(newDeviceInformation)
            } else {
                if (telephonyManager.simOperator != null) {
                    carrierName = telephonyManager.simOperatorName

                }
                if (telephonyManager.simOperator != null) {
                    displayName = telephonyManager.simOperatorName

                }
                if (telephonyManager.simCountryIso != null) {
                    countryIso = telephonyManager.simCountryIso
                }
                if (telephonyManager.subscriberId != null) {
                    imsi = telephonyManager.subscriberId
                }

                if (telephonyManager.simSerialNumber != null) {
                    simId = telephonyManager.simSerialNumber
                }

                if (telephonyManager.line1Number != null && telephonyManager.line1Number.isNotEmpty()) {
                    number = telephonyManager.line1Number
                }
                val newDeviceInformation = DeviceInformation(
                    carrierName,
                    displayName,
                    slotIndex,
                    number,
                    countryIso,
                    imsi,
                    simId,
                )

                listDeviceInformation.add(newDeviceInformation)
            }
        }

        val listDeviceInformationJsonString =
            ListDeviceInformation(listDeviceInformation = listDeviceInformation).toJsonApi()
        if (listDeviceInformationJsonString != null) {
            navigateToDetail(listDeviceInformationJsonString)
        }
    } else {
        // Request Permissions
        launcher.launch(permissions)
    }
}