package com.example.digitracksdk.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val hasInternet : Boolean

    val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
      hasInternet = when {
          networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
          networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
          networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return hasInternet
}