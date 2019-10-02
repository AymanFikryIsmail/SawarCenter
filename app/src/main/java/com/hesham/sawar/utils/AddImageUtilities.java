package com.hesham.sawar.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;


import com.hesham.sawar.R;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddImageUtilities {
    private static final int GALLERY = 10;
    public static final int READ_EXTERNAL_STORAGE_REQUEST_PERMISSION = 5;
    public static final int CAMERA_REQUEST_PERMISSION = 6;

    public static void openGalleryOrCameraIntent(int galleryOrCamera, Activity activity, OnRequestImageIntentListener listener){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            listener.onRequestGallery(galleryIntent);


    }
//    public static boolean validateImage(String imagePath,  Activity context) {
//        boolean validate = true;
//        if (imagePath != null) {
//            String extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
//            if (extension != null) {
//                if (extension.toLowerCase().equals("jpg") || extension.toLowerCase().equals("png") || extension.toLowerCase().equals("jpeg")) {
//                    File file = new File(imagePath);
//                    if (file.length() / ((double) (1024 * 1024)) > 20) {
//                        validate = false;
//                        Toast.makeText(context, R.string.image_size_error, Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    validate = false;
//                    Toast.makeText(context, R.string.image_type_error, Toast.LENGTH_LONG).show();
//                }
//            } else {
//                validate = false;
//                Toast.makeText(context, R.string.image_type_error, Toast.LENGTH_LONG).show();
//            }
//        } else {
//            validate = false;
//        }
//        return validate;
//    }

    public static String getImagePath(Uri imageUri, Context context){
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(imageUri,filePath,null,null,null);
        String imagePath = "";
        if (cursor != null){
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePath[0]);
            imagePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return imagePath;
    }

    private static String randomString(int len) {
        final String AB = "abcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    private static File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }



}
