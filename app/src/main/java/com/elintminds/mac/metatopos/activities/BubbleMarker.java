package com.elintminds.mac.metatopos.activities;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import com.google.maps.android.R.drawable;

class BubbleMarker extends Drawable {
    private final Drawable mShadow;
    private final Drawable mMask;
    private int mColor = -1;

    public BubbleMarker(Resources res) {
        this.mMask = res.getDrawable(drawable.amu_bubble_mask);
        this.mShadow = res.getDrawable(drawable.amu_bubble_shadow);
    }

    public void setColor(int color) {
        this.mColor = color;
    }

    public void draw(Canvas canvas) {
        this.mMask.draw(canvas);
        canvas.drawColor(this.mColor, Mode.SRC_IN);
        this.mShadow.draw(canvas);
    }

    public void setAlpha(int alpha) {
        throw new UnsupportedOperationException();
    }

    public void setColorFilter(ColorFilter cf) {
        throw new UnsupportedOperationException();
    }

    public int getOpacity() {
        return -3;
    }

    public void setBounds(int left, int top, int right, int bottom) {
        this.mMask.setBounds(left, top, right, bottom);
        this.mShadow.setBounds(left, top, right, bottom);
    }

    public void setBounds(Rect bounds) {
        this.mMask.setBounds(bounds);
        this.mShadow.setBounds(bounds);
    }

    public boolean getPadding(Rect padding) {
        return this.mMask.getPadding(padding);
    }
}

