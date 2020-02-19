package com.kotlingithub.viewPagerUse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kotlingithub.R

internal class TabTwoFragment : Fragment() {

    private var isStarted: Boolean? = false
    private var isVisible: Boolean? = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tabs, container, false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isVisible = isVisibleToUser
        if (isStarted!! && isVisible!!) {
            viewDidAppear()
        }
    }

    override fun onStart() {
        super.onStart()
        isStarted = true
        if (isVisible!!) {
            viewDidAppear()
        }
    }

    override fun onStop() {
        super.onStop()
        isStarted = false
        isVisible = false
    }

    private fun viewDidAppear() {}
}