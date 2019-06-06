package com.elintminds.mac.metatopos.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.elintminds.mac.metatopos.R;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    public static String[] videoResolutionCategoryList;
    public static String[] videoResolutionValue;
    ProgressDialog progressDialog;


    // method to convert single digit to two digit format
    public static String checkDigit(long number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


    public static String getVideoRecordDateTime(String videoDate) {
        DateFormat inputFormatter1 = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        Date date1;
        try {
            date1 = inputFormatter1.parse(videoDate);
            DateFormat outputFormatter1 = new SimpleDateFormat("dd MMM  h:mma", Locale.getDefault());
            videoDate = outputFormatter1.format(date1);
        } catch (ParseException e) {
            Log.e("DATE FORMAT ERROR", "" + e.toString());
        }
        return videoDate;
    }


    // method to convert long time in seconds to String in min:sec format
    public static String getTimeInMinSecFormat(long timeMiliSec) {
        StringBuilder timeSt = new StringBuilder();
        long timeSeconds = TimeUnit.MILLISECONDS.toSeconds(timeMiliSec);
        if (timeSeconds > 60) {
            long time_min = timeSeconds / 60;
            timeSt.append(checkDigit(time_min)).append(":").append(checkDigit(timeSeconds % 60));
        } else {
            timeSt.append(0).append(":").append(checkDigit(timeSeconds));
        }

        return timeSt.toString();
    }


    // method to check if device support sd_card or not
    public static boolean checkSDCardAvailability() {
        Boolean isSDSupportedDevice = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        Boolean isSDPresent = Environment.isExternalStorageRemovable();
        return isSDSupportedDevice && isSDPresent;
    }


    // method to check if any mailing app is installed or not
    public static boolean isMailClientPresent(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);

        return list.size() != 0;
    }


    public static boolean isFrontCameraAvailable() {
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return true;
            }
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static int freeMemory() {
        StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
        long free = (statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong());
        double KB = free / 1024;
        float MB = (float) KB / 1024;
        Log.e("FREE SIZE", "" + MB);
        return Math.round(MB);
    }


    public static String getSavedDirPath(String appDirectoryName) {
        File appDirectory = new File(Environment.getExternalStorageDirectory(), appDirectoryName);
        if (!appDirectory.exists()) {
            appDirectory.mkdirs();
        }

        return appDirectory.getAbsolutePath();
    }

    public static boolean isInternetIsAvailable(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
        }
        return false;
    }


    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static void startAnimation(Context context, View view)
    {
        final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.shake_anim);
//        myAnim.setRepeatCount(Animation.INFINITE);
//        myAnim.setRepeatMode(Animation.INFINITE);
        view.startAnimation(myAnim);

    }


}
