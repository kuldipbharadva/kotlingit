package com.kotlingithub.pipMode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlingithub.R
import android.app.PictureInPictureParams
import android.os.Build
import android.util.Rational
import androidx.annotation.RequiresApi
import android.content.res.Configuration
import android.graphics.Point
import android.view.View
import kotlinx.android.synthetic.main.activity_pip_mode.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class PiPModeActivity : AppCompatActivity() {

    private var pictureInPictureParamsBuilder: PictureInPictureParams.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pip_mode)
        initBasicTask()
    }

    private fun initBasicTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            pictureInPictureParamsBuilder = PictureInPictureParams.Builder()
        }

        btnPipMode.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pictureInPictureMode()
            }
        }
    }

    /* this function open pip mode */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun pictureInPictureMode() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = size.x
        val height = size.y
        val aspectRatio = Rational(width, height)
        pictureInPictureParamsBuilder?.setAspectRatio(aspectRatio)?.build()
        enterPictureInPictureMode(pictureInPictureParamsBuilder?.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public override fun onUserLeaveHint() {
        if (!isInPictureInPictureMode) {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            val width = size.x
            val height = size.y
            val aspectRatio = Rational(width, height)
            pictureInPictureParamsBuilder?.setAspectRatio(aspectRatio)?.build()
            enterPictureInPictureMode(pictureInPictureParamsBuilder?.build())
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        if (isInPictureInPictureMode) {
            btnPipMode.visibility = View.GONE
        } else {
            btnPipMode.visibility = View.VISIBLE
        }
    }
}