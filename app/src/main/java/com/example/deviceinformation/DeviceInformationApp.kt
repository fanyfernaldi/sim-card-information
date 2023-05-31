package com.example.deviceinformation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.deviceinformation.ui.misc.fromJsonApi
import com.example.deviceinformation.ui.model.DeviceInformation
import com.example.deviceinformation.ui.model.ListDeviceInformation
import com.example.deviceinformation.ui.navigation.Screen
import com.example.deviceinformation.ui.screen.detail.DetailScreen
import com.example.deviceinformation.ui.screen.main.MainScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceInformationApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = Screen.Main.route) {
                MainScreen(
                    navigateToDetail = { deviceInformation ->
                        navController.navigate(Screen.Detail.createRoute(deviceInformation))
                    }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("deviceInformation") {
                    type = NavType.StringType
                })
            ) {
                it.arguments?.getString("deviceInformation")?.let { jsonListDeviceInformationString ->
                    val listDeviceInformation = jsonListDeviceInformationString.fromJsonApi(ListDeviceInformation::class.java)
                    DetailScreen(listDeviceInformation = listDeviceInformation) {

                    }
                }
//                it.arguments?.getStringArrayList("deviceInformation")
//                    ?.let { jsonListDeviceInformationString ->
//                        var listDeviceInformation = mutableListOf<DeviceInformation>()
//                        jsonListDeviceInformationString.forEach { jsonDeviceInformationString ->
//                            listDeviceInformation.add(
//                                jsonDeviceInformationString.fromJsonApi(
//                                    DeviceInformation::class.java
//                                )
//                            )
//                        }
////                    val deviceInformation = jsonDeviceInformationString.fromJsonApi(
////                        DeviceInformation::class.java
////                    )
//                        DetailScreen(
//                            deviceInformation = listDeviceInformation,
//                            navigateBack = {
//                                navController.navigateUp()
//                            }
//                        )
//                    }
            }
        }
    }
}
