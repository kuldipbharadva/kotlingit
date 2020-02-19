package com.kotlingithub.viewPagerUse

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kotlingithub.R

class MyCustomTabPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    /**
     * If you want to only show icons, return null from this method.
     * @param position
     * @return
     */
    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getItemPosition(@NonNull `object`: Any): Int {
        return POSITION_NONE
    }

    fun addFragment(fragment: Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    @SuppressLint("InflateParams")
    fun getTabView(context: Context, position: Int): View {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        val v = LayoutInflater.from(context).inflate(R.layout.tab_custom_item, null)
        val tv = v.findViewById<TextView>(R.id.tvTabItem)
        tv.text = mFragmentTitleList[position]
        tv.setTextColor(context.resources.getColor(R.color.colorWhite))
        tv.background = context.resources.getDrawable(R.drawable.bg_radius_fill)
        return v
    }
}

// https://stackoverflow.com/questions/40896907/can-a-custom-view-be-used-as-a-tabitem/40897198