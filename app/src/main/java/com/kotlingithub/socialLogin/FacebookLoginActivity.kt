package com.kotlingithub.socialLogin

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.andylib.socialLogin.FacebookLogin
import com.andylib.socialLogin.SocialItem
import com.andylib.socialLogin.SocialResultCallback
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.activity_gmail.*
import java.security.MessageDigest


class FacebookLoginActivity : AppCompatActivity() {

    private var facebookLogin: FacebookLogin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.kotlingithub.R.layout.activity_gmail)
        init()
    }

    private fun init() {
        facebookLogin = FacebookLogin(this@FacebookLoginActivity, socialInterface)
        btnLogin.setOnClickListener { facebookLogin!!.signIn() }
        btnLogout.setOnClickListener { logout() }
        getHashKey()
    }

    private val socialInterface = object : SocialResultCallback {
        override fun onSuccess(socialModel: SocialItem) {
        }

        override fun onError(errorMessage: String) {
            Log.d("fbLogin", "onError: $errorMessage")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookLogin?.onActivityResult(requestCode, resultCode, data)
    }

    private fun logout() {
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null) {
            LoginManager.getInstance().logOut()
            Log.d("TAG", "logout: ")
        }
    }

    private fun getHashKey() {
        try {
            @SuppressLint("PackageManagerGetSignatures") val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val sign = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.d("TAG", "hash key : $sign")
            }
        } catch (e: Exception) {
            Log.d("TAG", "getHashKey: exception : " + e.localizedMessage)
        }
    }
}