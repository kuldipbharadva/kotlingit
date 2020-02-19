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
import android.view.View
import java.util.*


class ViewPagerCustomTabActivity : AppCompatActivity() {

    private val tabNames = arrayOf("Tab One", "Tab Two")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager_usage)
        initBasicTask()
    }

    private fun initBasicTask() {
        setUpViewPager()
        setupTabView(0)
    }

    private fun setUpViewPager() {
        val myViewPagerAdapter = MyCustomTabPagerAdapter(supportFragmentManager)
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

            override fun onPageSelected(position: Int) {
                setupTabView(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    /* this function set custom layout of selected current tab */
    private fun setupTabView(position: Int) {
        for (i in 0 until tabLayout.tabCount) {
            Objects.requireNonNull<TabLayout.Tab>(tabLayout.getTabAt(i)).customView = null
            Objects.requireNonNull<TabLayout.Tab>(tabLayout.getTabAt(i))
                .setCustomView(R.layout.tab_custom_item)
            val tvTabItem =
                Objects.requireNonNull<View>(Objects.requireNonNull<TabLayout.Tab>(tabLayout.getTabAt(i)).customView)
                    .findViewById<TextView>(R.id.tvTabItem)
            tvTabItem.text = tabNames[i]
            if (position == i) {
                tvTabItem.setTextColor(resources.getColor(R.color.colorWhite))
                tvTabItem.background = resources.getDrawable(R.drawable.bg_radius_fill)
            }/* else {
                tvTabItem.setTextColor(getResources().getColor(R.color.colorBlack));
                tvTabItem.setBackground(getResources().getDrawable(R.drawable.bg_reg_simple));
            }*/
        }
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