package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.getpost.GetPostByIdAttachment;

import java.util.List;

public class PostImagesSliderAdapter extends PagerAdapter {
    private Context context;
    //    private ArrayList<Integer> IMAGES;
    List<GetPostByIdAttachment> attachments;

    public PostImagesSliderAdapter(Context context, List<GetPostByIdAttachment> attachments) {
        this.context = context;
        this.attachments = attachments;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return attachments.size();

    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View imageLayout = layoutInflater.inflate(R.layout.image_slider_layout, view, false);
        String images = attachments.get(position).getImageUrl();
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
        Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + attachments.get(position).getImageUrl());
        Glide.with(context).load(path).into(imageView
        );


//        imageView.setImageResource(attachments.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }


    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
