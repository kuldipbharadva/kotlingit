package com.kotlingithub.parallaxScrolling

import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.kotlingithub.R
import kotlinx.android.synthetic.main.activity_parallax_scrolling.*


class ParallaxScrollingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parallax_scrolling)
        initBasicTask()
    }

    private fun initBasicTask() {
        val barLayout = findViewById<AppBarLayout>(R.id.appBarLayout)
        barLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                    ivBack.visibility = View.VISIBLE
                    d("tag", "onOffsetChanged: true")
                } else if (isShow) {
                    isShow = false
                    ivBack.visibility = View.GONE
                    d("tag", "onOffsetChanged: false")
                }
            }
        })
    }
}