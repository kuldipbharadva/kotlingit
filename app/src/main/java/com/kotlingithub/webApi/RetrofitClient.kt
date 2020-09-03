package com.demo.guruji24kotlin.webApi


import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

import java.util.HashMap
import java.util.concurrent.TimeUnit

class RetrofitClient {

    private val baseUrl = "http://sagarsinh-001-site1.ctempurl.com/api/"
    private var retrofit: Retrofit? = null

    private//TODO 60 to 30 second at everywhere
    val retrofitInstance: Retrofit
        get() {
            if (retrofit == null) {
                val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return this.retrofit!!
        }

    internal val instanceNew: ApiServiceInterface
        get() = retrofitInstance.create(ApiServiceInterface::class.java)

    interface MethodType {
        companion object {
            const val GET = "GET"
            const val POST = "POST"
        }
    }

    interface ApiPostUrl {
        companion object {
            const val Authenticate = "Authenticate/Authenticate"
        }
    }

    interface ApiServiceInterface {

        @GET
        abstract fun getServiceCall(@Url url: String, @HeaderMap header: Map<String, String>): Call<ResponseBody>

        @POST
        abstract fun postServiceCall(@Url url: String, @HeaderMap header: Map<String, String>, @Body requestBody: Any): Call<ResponseBody>
    }

    /* this function set header param value and return map object */
    fun makeHeaderValue(context: Context): Map<String, String> {
//        map.put("Authorization", MySharedPreference.getPreference(context, Constants.PREF_NAME_AUTHENTICATE, Constants.PREF_KEY_TOKEN, "").toString());
        return HashMap()
    }
}
