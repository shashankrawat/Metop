package com.elintminds.mac.metatopos.common;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.elintminds.mac.metatopos.common.AppUtils.checkDigit;

public class AppConstants {
    public static boolean IS_APP_OPEN = false;
    public static boolean anyNotification = false;
    public static boolean isInternetIsAvailable(Context mContext)
    {
        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()){
                return true;
            }
        }
        return false;
    }

    public static Bitmap decodeBitmapFromSDCard(String filePath, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / reqWidth, photoH / reqHeight);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(filePath, bmOptions);
    }


    public static Uri getOutputMediaFileUri(Context context) {
        Uri imageUri = null;
        File mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp;
        File mediaFile = null;
        try {
            mediaFile = File.createTempFile(imageFileName, ".jpg", mediaStorageDir);
        } catch (IOException e) {
            Log.e("IMG FILE ERROR", "" + e.getMessage());
        }
        if (mediaFile != null) {
            imageUri = Uri.fromFile(mediaFile);
        }
        return imageUri;
    }

    public static Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static int rotationAngle(String filePath) {
        int rotation = 0;
        ExifInterface ei = null;
        try {
            ei = new ExifInterface(filePath);

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:

                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:

                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:

                    rotation = 270;
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotation;
    }

    public static String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public static String convertLastActiveDate(String date) {
        DateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        inputFormatter1.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = null;
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        String lastTime = "";
        try {
            date1 = inputFormatter1.parse(date);
            long diff = currentDate.getTime() - date1.getTime();
            int dayCount = (int) diff / (24 * 60 * 60 * 1000);
            Log.e("DIFF LAST DATE", "" + dayCount);
            if (dayCount > 0) {
                if (dayCount == 1) {
                    lastTime = checkDigit(dayCount) + " day ago";
                } else {
                    lastTime = checkDigit(dayCount) + " days ago";
                }
            } else {
                dayCount = (int) diff / (60 * 60 * 1000);
                if (dayCount > 0) {
                    if (dayCount == 1) {
                        lastTime = checkDigit(dayCount) + " hour ago";
                    } else {
                        lastTime = checkDigit(dayCount) + " hours ago";
                    }
                } else {
                    dayCount = (int) diff / (60 * 1000);
                    if (dayCount > 0) {
                        if (dayCount == 1) {
                            lastTime = checkDigit(dayCount) + " minute ago";
                        } else {
                            lastTime = checkDigit(dayCount) + " minutes ago";
                        }
                    } else {
                        dayCount = (int) diff / 1000;
                        if (dayCount > 5) {
                            lastTime = checkDigit(dayCount) + " seconds ago";
                        } else if (dayCount >= 0 && dayCount < 5) {
                            lastTime = "seconds ago";
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /*DateFormat outputFormatter1 = new SimpleDateFormat("dd MMMM");
        date = outputFormatter1.format(date1);*/
        return lastTime;
    }

}
