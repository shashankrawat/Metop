package com.elintminds.mac.metatopos.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elintminds.mac.metatopos.MarkerInfo;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.common.AppUtils;


public class PostMarkerGenerator
{
    private final Context mContext;
    private View mContainer;
    private ImageView post_Image;
    private TextView post_title, post_views;
    private ViewGroup parentView;

    public PostMarkerGenerator(Context context, View viewGroup)
    {
        parentView = (ViewGroup) viewGroup;
        mContext = context;
        mContainer = LayoutInflater.from(mContext).inflate(R.layout.common_post_marker_layout, parentView, false);

        post_Image = mContainer.findViewById(R.id.post_emoji);
        post_title = mContainer.findViewById(R.id.post_content);
        post_views = mContainer.findViewById(R.id.post_views);
    }


    public View setMarkerDetails(MarkerInfo markerInfo, int top, int bottom)
    {
        post_title.setText(markerInfo.name);
        post_views.setText(markerInfo.postviews);

        /*LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, top, 0, bottom);
        post_Image.setLayoutParams(lp);*/

        return post_Image;
    }


    public Bitmap makeIcon() {
        @SuppressLint("WrongConstant") int measureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        mContainer.measure(measureSpec, measureSpec);
        int measuredWidth = mContainer.getMeasuredWidth();
        int measuredHeight = mContainer.getMeasuredHeight();
        mContainer.layout(0, 0, measuredWidth, measuredHeight);

        Bitmap r = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
//        r.eraseColor(0);
        Canvas canvas = new Canvas(r);

        mContainer.draw(canvas);
        return r;
    }


    // Convert a view to bitmap
    public Bitmap createDrawableFromView(View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
}

