package com.kotlingithub.localization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.content.Intent
import android.content.res.Configuration
import android.util.Log.d
import com.kotlingithub.R
import com.kotlingithub.utilities.MySharedPreference
import kotlinx.android.synthetic.main.activity_localization.*


class LocalizationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localization)
        initBasicTask()
    }

    private fun initBasicTask() {
        btnEng.setOnClickListener {
            setLocale("en")
            /* if there is no activity in back stack then no need to call reopenActivity */
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
        btnNext.setOnClickListener {
            startActivity(
                Intent(
                    this@LocalizationActivity,
                    LocalisationTestingActivity::class.java
                )
            )
        }
    }

    /* this function set given locale language and set in preference */
    private fun setLocale(lang: String) {
        MySharedPreference.setPreference(this@LocalizationActivity, "prefLang", "lang", lang)
        val myLocale = Locale(lang)
        Locale.setDefault(myLocale)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(myLocale)
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
        onConfigurationChanged(conf)
    }

    /* this function reopen current activity */
    private fun reopenActivity() {
        try {
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        } catch (e: Exception) {
            e.localizedMessage
        }

    }

    /* this function get lang from pref and set in locale */
    private fun setLocaleFromPref() {
        val myLang = MySharedPreference.getPreference(
            this@LocalizationActivity,
            "prefLang",
            "lang",
            "en"
        ) as String
        setLocale(myLang)
    }

    override fun onResume() {
        super.onResume()
        d("lifeCycle", "onResume: ")
        setLocaleFromPref()
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