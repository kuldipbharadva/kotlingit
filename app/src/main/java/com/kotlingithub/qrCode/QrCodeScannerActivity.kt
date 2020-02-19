package com.kotlingithub.qrCode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlingithub.R
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import android.content.Intent
import android.annotation.SuppressLint
import kotlinx.android.synthetic.main.activity_qr_scanner.*

class QrCodeScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)
        initBasicTask()
    }

    private fun initBasicTask() {
        btnScan.setOnClickListener {
            val integrator = IntentIntegrator(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            //            integrator.setPrompt("Scan");
            integrator.setCameraId(0)  // Use a specific camera of the device
            integrator.setBeepEnabled(false)
            integrator.setBarcodeImageEnabled(true)
            integrator.initiateScan()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                tvQrScanContent.text = result.contents + "\n" + result.formatName
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}