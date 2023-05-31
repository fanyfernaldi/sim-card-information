package com.example.deviceinformation.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Detail : Screen("detail/{deviceInformation}") {
        fun createRoute(deviceInformation: String) = "detail/$deviceInformation"
    }
}