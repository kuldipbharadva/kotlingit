package com.kotlingithub.socialLogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.andylib.socialLogin.GoogleLogin
import com.andylib.socialLogin.SocialItem
import com.andylib.socialLogin.SocialResultCallback
import com.andylib.util.CommonConfig
import com.kotlingithub.R
import kotlinx.android.synthetic.main.activity_gmail.*


class GmailLoginActivity : AppCompatActivity() {

    private var gmailLogin : GoogleLogin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gmail)
        initBasicTask()
    }

    private fun initBasicTask() {
        gmailLogin = GoogleLogin(this@GmailLoginActivity, object : SocialResultCallback{
            override fun onSuccess(result: SocialItem?) {
                Toast.makeText(this@GmailLoginActivity, "${result?.getId()}", Toast.LENGTH_LONG).show()
            }

            override fun onError(error: String?) {
                Toast.makeText(this@GmailLoginActivity, error, Toast.LENGTH_LONG).show()
            }

        })

        btnLogin.setOnClickListener { gmailLogin?.signIn() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            //            case AppConfig.SocialLoginCode.GOOGLE_SIGN_IN:
            CommonConfig.GOOGLE_SIGN_IN -> gmailLogin?.onActivityResult(
                requestCode,
                resultCode,
                data
            )
        }
    }
}