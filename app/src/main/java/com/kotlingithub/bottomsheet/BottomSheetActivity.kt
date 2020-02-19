package com.kotlingithub.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kotlingithub.R
import kotlinx.android.synthetic.main.activity_bottom_sheet.*

class BottomSheetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet)
        initBasicTask()
    }

    private fun initBasicTask() {
        tvShowCase.setOnClickListener { openBottomSheet() }
    }

    /* this function open bottom sheet layout and handle click event */
    private fun openBottomSheet() {
        val view = LayoutInflater.from(this).inflate(
            R.layout.dialog_open_pic_option,
            bottomSheet,
            false
        )

        bottomSheet.showWithSheetView(view)

        val tvCamera = view.findViewById<TextView>(R.id.tvCamera)
        tvCamera.setOnClickListener {
            Toast.makeText(this@BottomSheetActivity, "Camera", Toast.LENGTH_SHORT).show()
            if (bottomSheet.isSheetShowing) bottomSheet.dismissSheet()
        }
    }
}

/*
 *  For BottomSheet use need to add dependency.
 *  TODO :
 *    implementation 'com.flipboard:bottomsheet-core:1.5.3'
 *
 *  Then just use above code to open bottom sheet and give on click event of your option.
 *  Keep in mind you can give onClick event after bottom sheet open, if you write onClick event before .showWithSheetView
 *  method there might be chance to crash your app.
 *
 *  Todo :
 *    Ref : https://github.com/Flipboard/bottomsheet
 */