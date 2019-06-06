package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.RemoveAndEditAdvertisementActivtiy;
import com.elintminds.mac.metatopos.beans.useradvertisement.Advertisementdetail;
import com.elintminds.mac.metatopos.beans.useradvertisement.UserAdvertisementdata;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.List;

public class MyProfileAdvertisementAdapter extends RecyclerView.Adapter<MyProfileAdvertisementAdapter.ViewHolder> {
    Context context;
    UserAdvertisementdata advertisementdata;

    List<Advertisementdetail> advertisementdetails;

    public MyProfileAdvertisementAdapter(Context context, List<Advertisementdetail> advertisementdetails) {
        this.context = context;
        this.advertisementdetails = advertisementdetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.myprofile_advertisement_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Advertisementdetail advertisementlist = advertisementdetails.get(i);
        if (advertisementlist != null)
        {
            Log.e("Advertisement Data", "" + advertisementlist.getAttachments());
            if (advertisementlist.getAttachments() != null && advertisementlist.getAttachments().size() > 0) {
                final String postattactment = advertisementlist.getAttachments().get(0).getImageUrl();
                Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + postattactment);
                Glide.with(context).load(path).into(viewHolder.advertisement_image);
            }
            viewHolder.tv_totalViews.setText(advertisementlist.getViewCount());
            viewHolder.tv_totalCommemnt.setText(advertisementlist.getTotalComment());
            viewHolder.tv_adsTitle.setText(advertisementlist.getTitle());
            String lastActiveDate = AppConstants.convertLastActiveDate(advertisementlist.getAddedOn());
            viewHolder.tv_postTime.setText(lastActiveDate);
            viewHolder.advertisement_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String postid = advertisementlist.getPostId();
                    Intent removeeditpost = new Intent(context, RemoveAndEditAdvertisementActivtiy.class);
                    removeeditpost.putExtra("POST_ID", postid);
                    context.startActivity(removeeditpost);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (advertisementdetails != null) {
            return advertisementdetails.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView advertisement_image;
        TextView tv_totalViews, tv_totalCommemnt, tv_adsTitle, tv_postTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            advertisement_image = itemView.findViewById(R.id.advertisement_imageView);
            tv_totalViews = itemView.findViewById(R.id.tv_totalviews);
            tv_totalCommemnt = itemView.findViewById(R.id.tv_comments);
            tv_adsTitle = itemView.findViewById(R.id.advertisement_title);
            tv_postTime = itemView.findViewById(R.id.advertisement_post_time);
        }
    }

}
