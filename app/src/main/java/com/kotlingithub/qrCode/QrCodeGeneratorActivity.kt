package com.kotlingithub.qrCode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlingithub.R
import kotlinx.android.synthetic.main.activity_qr_generator.*

class QrCodeGeneratorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_generator)
        initBasicTask()
    }

    private fun initBasicTask() {
        var content = "Hello Qr Generator" // Whatever you need to encode in the QR code
        btnGenerate.setOnClickListener {
            if (etQrContent.text.toString() != "") {
                content = etQrContent.text.toString()
                if (QrGenerator.generateQrCodeBitmapImage(content) != null) {
                    ivQrCode.setImageBitmap(QrGenerator.generateQrCodeBitmapImage(content))
                }
            }
        }
    }
}