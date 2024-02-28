package com.example.androiddb.utils

import android.util.Log

fun Any.logd(message: Any? = "No message provided!") {
    Log.d("Main", message.toString())
}