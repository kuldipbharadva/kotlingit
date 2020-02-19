package com.kotlingithub.viewPagerUse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.kotlingithub.R
import kotlinx.android.synthetic.main.activity_view_pager_usage.*
import android.widget.TextView
import android.graphics.Typeface
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import android.content.Context


class ViewPagerUsageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager_usage)
        initBasicTask()
    }

    private fun initBasicTask() {
        setUpViewPager()
    }

    private fun setUpViewPager() {
        val myViewPagerAdapter = MyViewPagerAdapter(supportFragmentManager)
        myViewPagerAdapter.addFragment(TabOneFragment(), "TAB ONE")
        myViewPagerAdapter.addFragment(TabTwoFragment(), "TAB TWO")
        viewPager.adapter = myViewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        viewPager.currentItem = 0
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    /* this function set custom font to tab */
    private fun setCustomFontToTab(tabLayout: TabLayout, context: Context) {
        val vg = tabLayout.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildCount = vgTab.childCount
            for (i in 0 until tabChildCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    //Put your font in assets folder
                    //assign name of the font here (Must be case sensitive)
                    tabViewChild.typeface = Typeface.createFromAsset(
                        context.assets,
                        "fonts1/dejavusanscondensed.otf"
                    )
                    tabViewChild.textSize = 22f
                }
            }
        }
    }
}