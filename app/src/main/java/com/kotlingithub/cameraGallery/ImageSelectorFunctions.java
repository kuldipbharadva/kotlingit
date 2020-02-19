package com.kotlingithub.cameraGallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.andylib.dialog.CustomDialog;
import com.kotlingithub.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class ImageSelectorFunctions {

    private static ImageSelectorFunctions imageSelectorFunctions;
    public int REQ_GALLERY_CODE = 5;
    public int REQ_CAMERA_CODE = 6;
    public static Uri photoURI;

    public static ImageSelectorFunctions getImageUploadInstance() {
        if (imageSelectorFunctions == null) {
            imageSelectorFunctions = new ImageSelectorFunctions();
        }
        return imageSelectorFunctions;
    }

    /* this method is used for open camera gallery dialog*/
    public void openSelectionDialog(Context context) {
        Dialog dialog = new Dialog(context);
        CustomDialog.customDialog(context, dialog, R.layout.dialog_open_pic_option, R.id.tvCamera, R.id.tvGallery, true, CustomDialog.CustomDialogInterface.wrap_content, new CustomDialog.CustomDialogInterface() {
            @Override
            public void onOkClicked(Dialog dialog, View view) {
                dialog.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile(context);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        ex.getLocalizedMessage();
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        photoURI = FileProvider.getUriForFile(context,
                                context.getPackageName() + ".provider_path",
                                photoFile);
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        ((Activity) context).startActivityForResult(cameraIntent, REQ_CAMERA_CODE);
                    }
                }
            }

            @Override
            public void onCancelClicked(Dialog dialog) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select..."), REQ_GALLERY_CODE);
            }

            @Override
            public void onNeutralClicked(Dialog dialog) {
                /* this method used when three button dialog needed */
            }
        });
    }

    public File createImageFile(Context context) throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    /* this method handel all onActivity result */
    public Uri onResultOfPicSelection(Context context, int requestCode, int resultCode, Intent data) {
        Uri uri = null;
        if (requestCode == ImageSelectorFunctions.getImageUploadInstance().REQ_GALLERY_CODE) {
            if (data != null) {
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .setBorderLineColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setBorderLineThickness(1f)
                        .setBorderCornerLength(0f)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .start(((Activity) context));
            }
        } else if (requestCode == ImageSelectorFunctions.getImageUploadInstance().REQ_CAMERA_CODE) {
            if (photoURI != null && resultCode == RESULT_OK) {
                CropImage.activity(photoURI).start(((Activity) context));
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    uri = result.getUri();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.d("cropError", "onActivityResult");
                }
            }
        }
        return uri;
    }
}