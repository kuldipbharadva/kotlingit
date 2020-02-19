package com.kotlingithub.cameraGallery

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View

import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider

import com.andylib.dialog.CustomDialog
import com.kotlingithub.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

import android.app.Activity.RESULT_OK

class ImageSelectorKt {

    var REQ_GALLERY_CODE = 10
    var REQ_CAMERA_CODE = 11

    /* this method is used for open camera gallery dialog*/
    fun openSelectionDialog(context: Context) {
        val dialog = Dialog(context)
        CustomDialog.customDialog(
            context,
            dialog,
            R.layout.dialog_open_pic_option,
            R.id.tvCamera,
            R.id.tvGallery,
            true,
            CustomDialog.CustomDialogInterface.wrap_content,
            object : CustomDialog.CustomDialogInterface {
                override fun onOkClicked(dialog: Dialog, view: View) {
                    dialog.dismiss()
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if (cameraIntent.resolveActivity(context.packageManager) != null) {
                        var photoFile: File? = null
                        try {
                            photoFile = createImageFile(context)
                        } catch (ex: IOException) {
                            // Error occurred while creating the File
                            ex.localizedMessage
                        }

                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            photoURI = FileProvider.getUriForFile(
                                context,
                                context.packageName + ".provider_path",
                                photoFile
                            )
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            (context as Activity).startActivityForResult(
                                cameraIntent,
                                REQ_CAMERA_CODE
                            )
                        }
                    }
                }

                override fun onCancelClicked(dialog: Dialog) {
                    dialog.dismiss()
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    (context as Activity).startActivityForResult(
                        Intent.createChooser(
                            intent,
                            "Select..."
                        ), REQ_GALLERY_CODE
                    )
                }

                override fun onNeutralClicked(dialog: Dialog) {
                    /* this method used when three button dialog needed */
                }
            })
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        val mCurrentPhotoPath = image.absolutePath
        return image
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    /* this method handel all onActivity result */
    fun onResultOfPicSelection(
        context: Context,
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ): Uri? {
        var uri: Uri? = null
        if (requestCode == imageUploadInstance.REQ_GALLERY_CODE) {
            if (data != null) {
                CropImage.activity(data.data)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setFixAspectRatio(true)
                    .setBorderLineColor(ContextCompat.getColor(context, R.color.colorPrimary))
                    .setBorderLineThickness(1f)
                    .setBorderCornerLength(0f)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .start(context as Activity)
            }
        } else if (requestCode == imageUploadInstance.REQ_CAMERA_CODE) {
            if (photoURI != null && resultCode == RESULT_OK) {
                CropImage.activity(photoURI).start(context as Activity)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == RESULT_OK) {
                    uri = result.uri
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.d("cropError", "onActivityResult")
                }
            }
        }
        return uri
    }

    companion object {

        private var imageSelectorFunctions: ImageSelectorKt? = null
        var photoURI: Uri? = null

        val imageUploadInstance: ImageSelectorKt
            get() {
                if (imageSelectorFunctions == null) {
                    imageSelectorFunctions = ImageSelectorKt()
                }
                return imageSelectorFunctions!!
            }
    }
}