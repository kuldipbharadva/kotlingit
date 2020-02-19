package com.kotlingithub.showCase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.toptas.fancyshowcase.FancyShowCaseView
import android.view.View
import com.kotlingithub.R
import kotlinx.android.synthetic.main.activity_show_case.*

class ShowCaseViewActivity : AppCompatActivity() {

    private var mFancyShowCaseView: FancyShowCaseView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_case)

        openShowCase(tvShowCase)
    }

    /* this is show case view function which generally used for tutorial purpose. */
    private fun openShowCase(view: View) {
        mFancyShowCaseView = FancyShowCaseView.Builder(this)
            .focusOn(view)
            .title("This is Showcase View.")
            .build()
        mFancyShowCaseView!!.show()
    }

    override fun onBackPressed() {
        if (mFancyShowCaseView != null && mFancyShowCaseView!!.isShown) {
            mFancyShowCaseView!!.hide()
        } else {
            super.onBackPressed()
        }
    }
}

/*
 *  TODO
 *    Reference :~  https://github.com/faruktoptas/FancyShowCaseView/blob/master/app/src/main/java/me/toptas/fancyshowcasesample/MainActivity.kt
 */