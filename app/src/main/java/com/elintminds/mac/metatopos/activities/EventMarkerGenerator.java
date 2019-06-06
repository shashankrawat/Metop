package com.elintminds.mac.metatopos.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.MarkerInfo;
import com.elintminds.mac.metatopos.R;
import com.google.maps.android.ui.RotationLayout;

import de.hdodenhof.circleimageview.CircleImageView;


public class EventMarkerGenerator {
    private final Context mContext;
    private ViewGroup mContainer;
    private RotationLayout mRotationLayout;
    private TextView mTextView;
    private View mContentView;
    private int mRotation;
    private float mAnchorU = 0.5F;
    private float mAnchorV = 1.0F;
    private BubbleMarker mBackground;
    public static final int STYLE_DEFAULT = 1;
    public static final int STYLE_WHITE = 2;
    public static final int STYLE_RED = 3;
    public static final int STYLE_BLUE = 4;
    public static final int STYLE_GREEN = 5;
    public static final int STYLE_PURPLE = 6;
    public static final int STYLE_ORANGE = 7;
    public CircleImageView profileimage;
    TextView title, viewcount;

    public EventMarkerGenerator(Context context) {
        this.mContext = context;
        this.mBackground = new BubbleMarker(this.mContext.getResources());
        this.mContainer = (ViewGroup) LayoutInflater.from(this.mContext).inflate(R.layout.event_marker_layout, (ViewGroup) null);
        this.mContentView = this.title = (TextView) this.mContainer.findViewById(R.id.event_description);
        this.mContentView = this.viewcount = (TextView) this.mContainer.findViewById(R.id.event_view_count);
        this.mContentView = this.profileimage = this.mContainer.findViewById(R.id.event_user_pic);
        this.setStyle(1);
    }

    public Bitmap makeIcon(CharSequence text) {
        if (this.mTextView != null) {
            this.mTextView.setText(text);
        }

        return this.makeIcon();
    }

    public View setEventMarkerDetails(MarkerInfo markerInfo)
    {
        this.title.setText(markerInfo.name);
        this.viewcount.setText(markerInfo.postviews);
        return profileimage;
    }

    public Bitmap makeIcon() {
        @SuppressLint("WrongConstant") int measureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        this.mContainer.measure(measureSpec, measureSpec);
        int measuredWidth = this.mContainer.getMeasuredWidth();
        int measuredHeight = this.mContainer.getMeasuredHeight();
        this.mContainer.layout(0, 0, measuredWidth, measuredHeight);
        if (this.mRotation == 1 || this.mRotation == 3) {
            measuredHeight = this.mContainer.getMeasuredWidth();
            measuredWidth = this.mContainer.getMeasuredHeight();
        }
        Bitmap r = Bitmap.createBitmap(measuredWidth, measuredHeight, Config.ARGB_8888);
        r.eraseColor(0);
        Canvas canvas = new Canvas(r);
        if (this.mRotation != 0) {
            if (this.mRotation == 1) {
                canvas.translate((float) measuredWidth, 0.0F);
                canvas.rotate(90.0F);
            } else if (this.mRotation == 2) {
                canvas.rotate(180.0F, (float) (measuredWidth / 2), (float) (measuredHeight / 2));
            } else {
                canvas.translate(0.0F, (float) measuredHeight);
                canvas.rotate(270.0F);
            }
        }

        this.mContainer.draw(canvas);
        return r;
    }

    public void setContentView(View contentView) {
//        this.mRotationLayout.removeAllViews();
//        this.mRotationLayout.addView(contentView);
        this.mContentView = contentView;
//        View view = this.mRotationLayout.findViewById(com.google.maps.android.R.id.amu_text);
//        this.mTextView = view instanceof TextView ? (TextView) view : null;
    }

    public void setContentRotation(int degrees) {
        this.mRotationLayout.setViewRotation(degrees);
    }

    public void setRotation(int degrees) {
        this.mRotation = (degrees + 360) % 360 / 90;
    }

    public float getAnchorU() {
        return this.rotateAnchor(this.mAnchorU, this.mAnchorV);
    }

    public float getAnchorV() {
        return this.rotateAnchor(this.mAnchorV, this.mAnchorU);
    }

    private float rotateAnchor(float u, float v) {
        switch (this.mRotation) {
            case 0:
                return u;
            case 1:
                return 1.0F - v;
            case 2:
                return 1.0F - u;
            case 3:
                return v;
            default:
                throw new IllegalStateException();
        }
    }

    public void setTextAppearance(Context context, int resid) {
        if (this.mTextView != null) {
            this.mTextView.setTextAppearance(context, resid);
        }

    }

    public void setTextAppearance(int resid) {
        this.setTextAppearance(this.mContext, resid);
    }

    public void setStyle(int style) {
        this.setColor(getStyleColor(style));
//        this.setTextAppearance(this.mContext, getTextStyle(style));
    }

    public void setColor(int color) {
//        this.mBackground.setColor(color);
//        this.setBackground(this.mBackground);
    }

    public void setBackground(Drawable background) {
        this.mContainer.setBackgroundDrawable(background);
        if (background != null) {
            Rect rect = new Rect();
            background.getPadding(rect);
            this.mContainer.setPadding(rect.left, rect.top, rect.right, rect.bottom);
        } else {
            this.mContainer.setPadding(0, 0, 0, 0);
        }

    }

    public void setContentPadding(int left, int top, int right, int bottom) {
        this.mContentView.setPadding(left, top, right, bottom);
    }

    private static int getStyleColor(int style) {
        switch (style) {
            case 1:
            case 2:
            default:
                return -1;
            case 3:
                return -3407872;
            case 4:
                return -16737844;
            case 5:
                return -10053376;
            case 6:
                return -6736948;
            case 7:
                return -30720;
        }
    }

//    private static int getTextStyle(int style) {
//        switch(style) {
//            case 1:
//            case 2:
//            default:
//                return style.amu_Bubble_TextAppearance_Dark;
//            case 3:
//            case 4:
//            case 5:
//            case 6:
//            case 7:
//                return style.amu_Bubble_TextAppearance_Light;
//        }
//    }
}

