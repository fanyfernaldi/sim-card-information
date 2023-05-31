package com.example.deviceinformation.ui.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.deviceinformation.ui.model.ListDeviceInformation

@Composable
fun DetailScreen(
    listDeviceInformation: ListDeviceInformation,
    navigateBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {

        listDeviceInformation.listDeviceInformation.forEachIndexed { index, deviceInformation ->
            Text(text = "SIM ${index + 1}", fontSize = 24.sp)
            Text(text = "carrierName: ${deviceInformation.carrierName}")
            Text(text = "displayName: ${deviceInformation.displayName}")
            Text(text = "slotIndex: ${deviceInformation.slotIndex}")
            Text(text = "countryIso: ${deviceInformation.countryIso}")
            Text(text = "imsi: ${deviceInformation.imsi}")
            Text(text = "simId: ${deviceInformation.simId}")
        }
    }
}