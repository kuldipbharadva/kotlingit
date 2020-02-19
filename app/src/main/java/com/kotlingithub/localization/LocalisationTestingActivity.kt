package com.kotlingithub.localization

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_localization.*
import android.util.Log.d
import com.kotlingithub.R
import com.kotlingithub.utilities.MySharedPreference
import java.util.*

class LocalisationTestingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localization)
        initBasicTask()
    }

    private fun initBasicTask() {
        btnEng.setOnClickListener {
            setLocale("en")
            reopenActivity()
        }
        btnSpain.setOnClickListener {
            setLocale("es")
            reopenActivity()
        }
        btnArabic.setOnClickListener {
            setLocale("ar")
            reopenActivity()
        }
    }

    /* this function set given locale language and set in preference */
    private fun setLocale(lang: String) {
        try {
            MySharedPreference.setPreference(
                this@LocalisationTestingActivity,
                "prefLang",
                "lang",
                lang
            )
            val myLocale = Locale(lang)
            Locale.setDefault(myLocale)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.setLocale(myLocale)
            conf.setLayoutDirection(myLocale)
            res.updateConfiguration(conf, dm)
            onConfigurationChanged(conf)
        } catch (e: Exception) {
            d("localLang", "setLocale: " + e.localizedMessage)
        }

    }

    /* this function reopen current activity */
    private fun reopenActivity() {
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onResume() {
        super.onResume()
        val myLang = MySharedPreference.getPreference(
            this@LocalisationTestingActivity,
            "prefLang",
            "lang",
            "en"
        ) as String
        setLocale(myLang)
    }

    override fun onStart() {
        super.onStart()
        d("lifeCycle", "onStart: ")
    }

    override fun onPause() {
        super.onPause()
        d("lifeCycle", "onPause: ")
    }

    override fun onRestart() {
        super.onRestart()
        reopenActivity()
        d("lifeCycle", "onRestart: ")
    }

    override fun onStop() {
        super.onStop()
        d("lifeCycle", "onStop: ")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        tvName.text = getString(R.string.fullName)
        tvCity.text = getString(R.string.city)
        tvAddress.text = getString(R.string.address)
        tvMobile.text = getString(R.string.mobile)
    }
}