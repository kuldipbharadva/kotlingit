package com.kotlingithub.deeplink

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlingithub.R
import android.content.Intent
import kotlinx.android.synthetic.main.activity_deeplink.*

class DeepLinkActivity : AppCompatActivity() {

    private var linkId: String? = null
    private var eventId: String? = null
    private val walletData: String? = null
    private var type: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deeplink)
        initBasicTask()
    }

    private fun initBasicTask() {
        btnShareLink.setOnClickListener { shareLink() }
    }

    private fun shareLink() {
        try {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/html"
            share.putExtra(Intent.EXTRA_SUBJECT, "Deeplink")
            share.putExtra(Intent.EXTRA_TEXT, "https://google.com/")
            startActivity(Intent.createChooser(share, "Share"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /* you can get deeplink data with the help oh this method */
    private fun getDeepLinkData() {
        val appLinkIntent = intent
        try {
            val appLinkData = appLinkIntent.data
            if (appLinkData != null) {
                if (appLinkIntent.data!!.query!!.contains("eid"))
                    eventId = appLinkIntent.data!!.getQueryParameter("eid")
                else if (appLinkIntent.data!!.query!!.contains("linkid"))
                    linkId = appLinkIntent.data!!.getQueryParameter("linkid")
            } else if (appLinkIntent.extras != null && appLinkIntent.extras!!.containsKey("eid") && appLinkIntent.extras!!.get(
                    "eid"
                )!!.toString().trim { it <= ' ' }.isNotEmpty()
            ) {
                eventId = appLinkIntent.extras!!.get("eid")!!.toString()
            } else if (appLinkIntent.extras != null && appLinkIntent.extras!!.containsKey("WALLET_DATA") && appLinkIntent.extras!!.get(
                    "WALLET_DATA"
                ) != null
            ) {
                //                walletData = appLinkIntent.getExtras().get("WALLET_DATA");
            } else if (appLinkIntent.extras != null && appLinkIntent.extras!!.containsKey("TYPE") && appLinkIntent.extras!!.get(
                    "TYPE"
                ) != null
            ) {
                type = appLinkIntent.extras!!.getInt("TYPE")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}