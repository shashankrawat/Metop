package com.elintminds.mac.metatopos.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.FollowingUserInterface;
import com.elintminds.mac.metatopos.beans.getfollowerslist.FollowersListData;
import com.elintminds.mac.metatopos.beans.getfollowerslist.FollowersUserInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.OfferHolder> {
    Context context;
    ArrayList<Integer> data;
    List<FollowersListData> followingdata;

    FollowingUserInterface mlistener;

    public FollowingAdapter(Context context, List<FollowersListData> followingdata, FollowingUserInterface mlistener) {
        this.context = context;
        this.followingdata = followingdata;
        this.mlistener = mlistener;

    }


    @NonNull
    @Override
    public OfferHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.following_recyclerview_layout, viewGroup, false);
        return new OfferHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final OfferHolder viewHolder, final int i) {
        FollowersUserInfo followdata = followingdata.get(i).getUserInfo();
        if (followdata != null) {
            String imagepath = followdata.getProfileImageUrl();
            Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + imagepath);
            Glide.with(context).load(path).into(viewHolder.userPic);
            viewHolder.username.setText(followdata.getFullName());
        }
        if (followdata.getIsAlreadyFollowed().equalsIgnoreCase("1")) {
            viewHolder.followingbtn.setBackgroundResource(R.drawable.chatbtn_back);
            viewHolder.followingbtn.setText("Following");
            viewHolder.followingbtn.setTextColor(R.color.colorlightblue);
        } else {
            viewHolder.followingbtn.setBackgroundResource(R.drawable.followbtn_back);
            viewHolder.followingbtn.setText("Follow");
            viewHolder.followingbtn.setTextColor(Color.WHITE);
        }
        viewHolder.followingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlistener.onFollowFollowingClick(viewHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return followingdata.size();
    }

    public class OfferHolder extends RecyclerView.ViewHolder {
        TextView username;
        CircleImageView userPic;
        Button followingbtn;

        public OfferHolder(@NonNull View itemView) {
            super(itemView);
            userPic = itemView.findViewById(R.id.followers_userDp);
            username = itemView.findViewById(R.id.followers_username);
            followingbtn = itemView.findViewById(R.id.follow_Btn);
        }
    }
}
