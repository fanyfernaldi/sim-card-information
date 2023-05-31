package com.example.deviceinformation.ui.misc

import com.google.gson.Gson

fun <A> String.fromJsonApi(type: Class<A>): A {
    val convertedString = this.replace("+", "/").replace("-", "?")
    return Gson().fromJson(convertedString, type)
}

fun <A> A.toJsonApi(): String? {
    return Gson().toJson(this).toString().replace("/", "+").replace("?", "-")
}