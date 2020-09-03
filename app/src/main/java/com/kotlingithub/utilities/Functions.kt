package com.kotlingithub.utilities

import android.app.Activity
import android.content.Context
import com.google.android.material.snackbar.Snackbar

/**
 * Created by KD on 8/14/2019.
 */
class Functions {

    companion object {
        fun showSnackbar(context: Context, message: String = "This is simple snackbar") {
            Snackbar.make((context as Activity).findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        }
    }
}