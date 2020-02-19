package com.kotlingithub.cameraGallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_image_set.*


class CameraGalleryActivity : AppCompatActivity() {

    private var profilePicUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.kotlingithub.R.layout.activity_image_set)
        initBasicTask()
    }

    private fun initBasicTask() {
        ImageSelectorKt.imageUploadInstance.openSelectionDialog(this)
//        ImageSelectorFunctions.getImageUploadInstance().openSelectionDialog(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImageSelectorKt.imageUploadInstance.REQ_GALLERY_CODE) {
            ImageSelectorKt.imageUploadInstance.onResultOfPicSelection(this, ImageSelectorKt.imageUploadInstance.REQ_GALLERY_CODE, resultCode, data)
//            ImageSelectorFunctions.getImageUploadInstance().onResultOfPicSelection(this, ImageSelectorFunctions.getImageUploadInstance().REQ_GALLERY_CODE, resultCode, data)
        } else if (requestCode == ImageSelectorKt.imageUploadInstance.REQ_CAMERA_CODE) {
            ImageSelectorKt.imageUploadInstance.onResultOfPicSelection(this, ImageSelectorKt.imageUploadInstance.REQ_CAMERA_CODE, resultCode, data)
//            ImageSelectorFunctions.getImageUploadInstance().onResultOfPicSelection(this, ImageSelectorFunctions.getImageUploadInstance().REQ_CAMERA_CODE, resultCode, data)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            profilePicUri = ImageSelectorFunctions.getImageUploadInstance().onResultOfPicSelection(this, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE, resultCode, data)
            profilePicUri = ImageSelectorKt.imageUploadInstance.onResultOfPicSelection(this, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE, resultCode, data)
            if (profilePicUri != null) ivImage.setImageURI(profilePicUri)
        }
    }
}