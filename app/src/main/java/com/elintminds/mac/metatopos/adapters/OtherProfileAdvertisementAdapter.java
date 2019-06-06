package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
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
import com.elintminds.mac.metatopos.beans.useradvertisement.Advertisementdetail;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class OtherProfileAdvertisementAdapter extends RecyclerView.Adapter<OtherProfileAdvertisementAdapter.ViewHolder> {
    Context context;
    ArrayList<Integer> mojiImages;
    List<Advertisementdetail> adslist;

    public OtherProfileAdvertisementAdapter(Context context, List<Advertisementdetail> adslist) {
        this.context = context;
        this.adslist = adslist;
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
//        viewHolder.iv_image.setImageResource((Integer) mojiImages.get(i));
        Advertisementdetail advertisementlist = adslist.get(i);
        Log.e("Advertisement Data", "" + advertisementlist.getAttachments());
        if (advertisementlist.getAttachments() != null && advertisementlist.getAttachments().size() > 0) {
            final String postattactment = advertisementlist.getAttachments().get(0).getImageUrl();
            Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + postattactment);
            Glide.with(context).load(path).into(viewHolder.iv_post_image);
        }
        viewHolder.tv_totalViews.setText(advertisementlist.getViewCount());
        viewHolder.tv_totalCommemnt.setText(advertisementlist.getTotalComment());
        viewHolder.tv_adsTitle.setText(advertisementlist.getTitle());
        String lastActiveDate = AppConstants.convertLastActiveDate(advertisementlist.getAddedOn());
        viewHolder.tv_postTime.setText(lastActiveDate);
        viewHolder.iv_post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent removeeditpost=new Intent(context, RemoveAndEditPostActivtiy.class);
//                context.startActivity(removeeditpost);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (adslist == null) {
            return 0;
        } else {
            return adslist.size();
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_post_image;
        TextView tv_totalViews, tv_totalCommemnt, tv_adsTitle, tv_postTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_post_image = itemView.findViewById(R.id.advertisement_imageView);
            tv_totalViews = itemView.findViewById(R.id.tv_totalviews);
            tv_totalCommemnt = itemView.findViewById(R.id.tv_comments);
            tv_adsTitle = itemView.findViewById(R.id.advertisement_title);
            tv_postTime = itemView.findViewById(R.id.advertisement_post_time);
        }
    }

}
