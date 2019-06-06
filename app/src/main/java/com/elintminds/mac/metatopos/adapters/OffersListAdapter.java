package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
import com.elintminds.mac.metatopos.activities.AdvertisementActivity;
import com.elintminds.mac.metatopos.activities.EventsActivity;
import com.elintminds.mac.metatopos.activities.PostActivity;
import com.elintminds.mac.metatopos.beans.getallpostsbylatlong.PostList;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class OffersListAdapter extends RecyclerView.Adapter<OffersListAdapter.ListHolder> {
    Context context;
    ArrayList<Integer> itemimages;
    List<PostList> postsLists;

    public OffersListAdapter(Context context, List<PostList> postsLists) {
        this.context = context;
        this.postsLists = postsLists;
    }


    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.offers_recyclerview_layout, viewGroup, false);
        return new ListHolder(view);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final ListHolder listHolder, int i) {
        final int position = listHolder.getAdapterPosition();

        final PostList postdata = postsLists.get(i);
        Log.e("PostData=", "" + postsLists.get(i));

        final String getpostType = postdata.getPostType();
        switch (getpostType) {
            case "advertisement":
                listHolder.posttype.setText("Advertisement");
                listHolder.posttype.setBackgroundResource(R.drawable.advertisementbtnback);
                listHolder.posttype.setTextColor(Color.WHITE);
                Typeface typeface3 = ResourcesCompat.getFont(context, R.font.lato_regular);
                listHolder.posttype.setTypeface(typeface3);
                listHolder.posttype.setTextSize(10);
                listHolder.eventIntrestedPeople.setVisibility(View.GONE);
                break;
            case "event":
                listHolder.posttype.setText("Event");
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
            if (postdata.getPostExpireStatus().equalsIgnoreCase("2")) {
                listHolder.expire_postview.setVisibility(View.VISIBLE);
                listHolder.expire_postview.setVisibility(View.GONE);
            } else {
                final String postattactment = postdata.getAttachments().get(0).getImageUrl();
                Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + postattactment);
                Glide.with(context).load(path).into(listHolder.postImage);
                listHolder.expire_postview.setVisibility(View.GONE);
            }
        }
        if (postdata.getPostExpireStatus().equals("2")) {
            ///
            listHolder.postImage.setOnClickListener(null);
        } else {

            listHolder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent();
                    final String getPostID = postdata.getPostId();

                    switch (getpostType) {
                        case "post":
                            intent = new Intent(context, PostActivity.class);
                            intent.putExtra("PostID", getPostID);
                            context.startActivity(intent);
                            break;
                        case "event":
                            intent = new Intent(context, EventsActivity.class);
                            intent.putExtra("PostID", getPostID);
                            context.startActivity(intent);
                            break;
                        case "advertisement":
                            intent = new Intent(context, AdvertisementActivity.class);
                            intent.putExtra("PostID", getPostID);
                            context.startActivity(intent);
                            break;


                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        if (postsLists != null) {
            return postsLists.size();
        } else {
            return 0;
        }

    }


    public class ListHolder extends RecyclerView.ViewHolder {

        ImageView postImage, expire_postview;
        TextView posttype, postTitle, postView, postcomment, eventIntrestedPeople, postTime;


        public ListHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.iv_postImageview);
            expire_postview = itemView.findViewById(R.id.iv_expirepost);
            posttype = itemView.findViewById(R.id.tv_post_type);
            postTitle = itemView.findViewById(R.id.tv_post_title);
            postView = itemView.findViewById(R.id.tv_totalviews);
            postcomment = itemView.findViewById(R.id.tv_comments);
            eventIntrestedPeople = itemView.findViewById(R.id.total_intrested_count);
            postTime = itemView.findViewById(R.id.tv_post_time);

        }
    }
}
