package com.kotlingithub.utilities

import android.content.Context
import android.net.ConnectivityManager

object NetworkCheck {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivity.activeNetworkInfo
        return activeNetwork != null
    }
}
