@file:Suppress("CAST_NEVER_SUCCEEDS")

package com.demo.guruji24kotlin.webApi

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import com.kotlingithub.utilities.NetworkCheck
import com.kotlingithub.R
import com.kotlingithub.utilities.CustomDialog
import com.kotlingithub.utilities.Functions
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ApiCalling {

    //    private var call: Call<*>? = null
    private var dialog: Dialog? = null

    fun makeApiCall(
        context: Context,
        url: String,
        method: String,
        headerMap: Map<*, *>,
        requestBody: Any,
        isProgressDialogNeeded: Boolean,
        apiSuccessInterface: ApiSuccessInterface
    ) {
        try {
            if (NetworkCheck.isNetworkAvailable(context)) {
                try {
                    if (isProgressDialogNeeded) {
                        CustomDialog.displayProgress(context)
                    }
                } catch (e: Exception) {
                    e.localizedMessage
                }

                var call: Call<ResponseBody>? = null
                if (method.equals(RetrofitClient.MethodType.GET, ignoreCase = true)) {
                    call = RetrofitClient().instanceNew.getServiceCall(url, headerMap as Map<String, String>)
                } else if (method.equals(RetrofitClient.MethodType.POST, ignoreCase = true)) {
                    call = RetrofitClient().instanceNew.postServiceCall(url, headerMap as Map<String, String>, requestBody)
                }

                call!!.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Log.d("makeApiCall", "onResponse: " + response.message())
                        try {
                            if (isProgressDialogNeeded)
                                CustomDialog.dismissProgress(context)
                        } catch (e: Exception) {
                            e.localizedMessage
                        }

                        if (response.code() == 401 || response.code() == 403 || response.code() == 404 || response.code() == 500) {
                            Functions.showSnackbar(
                                context,
                                if (response.message().isEmpty()) context.getString(R.string.msg_unexpected_error) else response.message()
                            )
                        } else if (response.code() == 401) {
                        } else {
                            try {
                                var res: String? = null
                                if (response.body() != null) {
                                    res = String(response.body()!!.bytes())
                                }
                                apiSuccessInterface.onSuccess(response.code(), response.message(), res)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Log.d("makeApiCall", "onFailure: " + t.localizedMessage)
                        try {
                            if (isProgressDialogNeeded) CustomDialog.dismissProgress(context)
                            apiSuccessInterface.onFailure(t)
                        } catch (e: Exception) {
                            e.localizedMessage
                        }
                    }
                })
            } else {
                if (dialog == null) {
                    dialog = Dialog(context, R.style.Dialog_Full_Screen)
                    CustomDialog.noInternetDialog(
                        context,
                        dialog!!,
                        R.layout.dialog_no_internet,
                        R.id.ok,
                        false,
                        CustomDialog.CustomDialogInterface.match_parent,
                        object : CustomDialog.NoInterNetDialogInterface {
                            override fun onOkClicked(d: Dialog, view: View) {
                                if (NetworkCheck.isNetworkAvailable(context))
                                    if (dialog!!.isShowing) dialog!!.dismiss()
                                ApiCalling().makeApiCall(
                                    context,
                                    url,
                                    method,
                                    headerMap,
                                    requestBody,
                                    true,
                                    object : ApiSuccessInterface {
                                        override fun onSuccess(resCode: Int, resMsg: String, apiResponse: String?) {
                                            apiSuccessInterface.onSuccess(resCode, resMsg, apiResponse)
                                        }

                                        override fun onFailure(t: Throwable) {
                                            apiSuccessInterface.onFailure(t)
                                        }
                                    })
                            }
                        })
                }
            }
        } catch (e: Exception) {
            e.localizedMessage
            Log.d("makeApiCall", "catch: " + e.localizedMessage)
        }
    }

    interface ApiSuccessInterface {
        fun onSuccess(resCode: Int, resMsg: String, apiResponse: String?)

        fun onFailure(t: Throwable)
    }
}

//recvfrom failed: ECONNRESET (Connection reset by peer)