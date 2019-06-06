package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.favouritepost.FavouritePosts;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class FavouriteItemAdapter extends RecyclerView.Adapter<FavouriteItemAdapter.ItemHolder> {
    Context context;
    ArrayList<Integer> itemimages;
    List<FavouritePosts> favouritepostData;
    String current_time, previous_time;

    public FavouriteItemAdapter(Context context, List<FavouritePosts> favouritepostData) {
        this.context = context;
        this.favouritepostData = favouritepostData;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.offers_recyclerview_layout, viewGroup, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder listHolder, int i) {
        Log.e("FAV ITEMS", "" + i);
        FavouritePosts postdata = favouritepostData.get(i);
        if (postdata != null) {
            final String getpostType = postdata.getPostType();
            switch (getpostType) {
                case "advertisement":
                    listHolder.posttype.setText(postdata.getPostType());
                    listHolder.posttype.setBackgroundResource(R.drawable.advertisementbtnback);
                    listHolder.posttype.setTextColor(Color.WHITE);
                    Typeface typeface3 = ResourcesCompat.getFont(context, R.font.lato_regular);
                    listHolder.posttype.setTypeface(typeface3);
                    listHolder.posttype.setTextSize(10);
                    listHolder.eventIntrestedPeople.setVisibility(View.GONE);
                    break;
                case "event":
                    listHolder.posttype.setText(postdata.getPostType());
                    listHolder.posttype.setBackgroundResource(R.drawable.eventbtngradient);
                    listHolder.posttype.setTextColor(Color.WHITE);
                    Typeface typeface2 = ResourcesCompat.getFont(context, R.font.lato_regular);
                    listHolder.posttype.setTypeface(typeface2);
                    listHolder.posttype.setTextSize(10);
                    listHolder.eventIntrestedPeople.setText(postdata.getTotalInterestedPeople());
                    break;
                case "post":
                    listHolder.posttype.setText("$" + postdata.getPrice());
                    listHolder.posttype.setTextColor(Color.BLACK);
                    listHolder.posttype.setBackgroundResource(R.drawable.simplepostbtnback);
                    listHolder.posttype.setTextSize(14);
                    Typeface typeface = ResourcesCompat.getFont(context, R.font.lato_bold);
                    listHolder.posttype.setTypeface(typeface);
                    listHolder.eventIntrestedPeople.setVisibility(View.GONE);
                    break;
            }
            listHolder.postTitle.setText(postdata.getTitle());
            listHolder.postView.setText(postdata.getViewCount());
            listHolder.postcomment.setText(postdata.getTotalComment());
            String lastActiveDate = AppConstants.convertLastActiveDate(postdata.getAddedOn());
            listHolder.postTime.setText(lastActiveDate);

            Log.e("Post Data", "" + postdata.getAttachments());
            if (postdata.getAttachments() != null && postdata.getAttachments().size() > 0) {
                final String postattactment = postdata.getAttachments().get(0).getImageUrl();
                Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + postattactment);
                Glide.with(context).load(path).into(listHolder.postImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (favouritepostData != null) {
            return favouritepostData.size();
        } else {
            return 0;
        }

    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView posttype, postTitle, postView, postcomment, eventIntrestedPeople, postTime;


        public ItemHolder(@NonNull View itemView) {

            super(itemView);
            postImage = itemView.findViewById(R.id.iv_postImageview);
            posttype = itemView.findViewById(R.id.tv_post_type);
            postTitle = itemView.findViewById(R.id.tv_post_title);
            postView = itemView.findViewById(R.id.tv_totalviews);
            postcomment = itemView.findViewById(R.id.tv_comments);
            eventIntrestedPeople = itemView.findViewById(R.id.total_intrested_count);
            postTime = itemView.findViewById(R.id.tv_post_time);
        }

    }

}
