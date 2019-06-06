package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.FollowUnfollowInterface;
import com.elintminds.mac.metatopos.beans.getfollowerslist.FollowersListData;
import com.elintminds.mac.metatopos.beans.getfollowerslist.FollowersUserInfo;
import com.elintminds.mac.metatopos.common.APIService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.OfferHolder> {
    Context context;
    ArrayList<Integer> data;
    List<FollowersListData> followersListData;
    HashMap<String, String> map;
    APIService mapiService;
    FollowUnfollowInterface mlistner;

    public FollowersAdapter(Context context, List<FollowersListData> followersListData, FollowUnfollowInterface listner) {
        this.context = context;
        this.followersListData = followersListData;
        mlistner = listner;
    }

    @NonNull
    @Override
    public OfferHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.followers_recyclerview_layout, viewGroup, false);
        return new OfferHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OfferHolder viewHolder, int i) {

        FollowersUserInfo followdata = followersListData.get(i).getUserInfo();
        Log.e("Followers Data", "" + followdata);
        if (followdata != null) {
            String imagepath = followdata.getProfileImageUrl();
            Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + imagepath);
            Glide.with(context).load(path).into(viewHolder.userPic);
            viewHolder.username.setText(followdata.getFullName());
            if (followdata.getIsAlreadyFollowed().equalsIgnoreCase("1")) {
//                viewHolder.follow_unfollow_btn.setBackgroundResource(R.drawable.followbtn_back);
                viewHolder.follow_unfollow_btn.setText("Unfollow");
            } else {
//                viewHolder.follow_unfollow_btn.setBackgroundResource(R.drawable.chatbtn_back);
                viewHolder.follow_unfollow_btn.setText("Follow");
            }
            viewHolder.follow_unfollow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistner.onFollowClick(viewHolder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return followersListData.size();

    }

    public class OfferHolder extends RecyclerView.ViewHolder {
        TextView username;
        CircleImageView userPic;
        Button follow_unfollow_btn;

        public OfferHolder(@NonNull View itemView) {
            super(itemView);
            userPic = itemView.findViewById(R.id.followers_userDp);
            username = itemView.findViewById(R.id.followers_username);
            follow_unfollow_btn = itemView.findViewById(R.id.follow_Btn);

        }
    }


}
