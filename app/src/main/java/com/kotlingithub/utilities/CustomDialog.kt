package com.kotlingithub.utilities

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.kotlingithub.R

import java.lang.ref.WeakReference

object CustomDialog {

    private var progressDialog: android.app.ProgressDialog? = null
    private var weakActivity: WeakReference<Activity>? = null

    fun displayProgress(context: Context) {
        weakActivity = WeakReference(context as Activity)

        if (weakActivity!!.get() != null && !weakActivity!!.get()!!.isFinishing) {
            if (progressDialog == null) {
                progressDialog = android.app.ProgressDialog(context)

            } else {
                if (progressDialog!!.isShowing)
                    progressDialog!!.dismiss()
            }
            progressDialog!!.setMessage(context.getString(R.string.please_wait))
            if (!context.isFinishing) {
                progressDialog!!.show()
            }
            progressDialog!!.setCancelable(false)
        }
    }

    fun dismissProgress(context: Context) {
        weakActivity = WeakReference(context as Activity)

        if (weakActivity!!.get() != null && !weakActivity!!.get()!!.isFinishing) {
            if (progressDialog != null && progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        }
    }

    fun customDialog(
        context: Context,
        dialog: Dialog,
        view: Int,
        positiveButtonId: Int,
        cancelable: Boolean,
        height: Int,
        customDialogInterface: NoInterNetDialogInterface
    ) {
        val viewLayout = LayoutInflater.from(context).inflate(view, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(viewLayout)
        if (height == CustomDialogInterface.wrap_content) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else if (height == CustomDialogInterface.match_parent) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        dialog.setCancelable(cancelable)
        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 20)
        dialog.window!!.setBackgroundDrawable(inset)
        dialog.show()
        dialog.findViewById<View>(positiveButtonId)
            .setOnClickListener { customDialogInterface.onOkClicked(dialog, viewLayout) }
    }

    /**
     * this function show no internet dialog with custom layout
     */
    fun noInternetDialog(
        context: Context,
        dialog: Dialog,
        view: Int,
        positiveButtonId: Int,
        cancelable: Boolean,
        height: Int,
        noInterNetDialogInterface: NoInterNetDialogInterface
    ) {
        val viewLayout = LayoutInflater.from(context).inflate(view, null)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(viewLayout)
        if (height == CustomDialogInterface.wrap_content) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else if (height == CustomDialogInterface.match_parent) {
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        dialog.setCancelable(cancelable)
        dialog.show()
        dialog.findViewById<View>(positiveButtonId)
            .setOnClickListener { noInterNetDialogInterface.onOkClicked(dialog, viewLayout) }
    }

    /**
     * this function open setting dialog
     */
    fun openSettingDialog(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }

    interface CustomDialogInterface {

        fun onOkClicked(dialog: Dialog, view: View)

        fun onCancelClicked(dialog: Dialog)

        fun onNeutralClicked(dialog: Dialog)

        companion object {

            const val wrap_content = 0
            const val match_parent = 1
        }
    }

    interface NoInterNetDialogInterface {

        fun onOkClicked(dialog: Dialog, view: View)

        companion object {

            const val wrap_content = 0
            const val match_parent = 1
        }

    }
}