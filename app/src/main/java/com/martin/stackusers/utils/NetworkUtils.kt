package com.martin.stackusers.utils

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

class NetworkUtils {
    companion object {
        fun isInternetAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager? ?: return false

            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                cm.activeNetworkInfo != null
            } else {
                cm.activeNetwork != null
            }
        }
    }
}