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
import com.elintminds.mac.metatopos.beans.getpost.GetPostByIdRecentComment;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.OfferHolder> {
    Context context;
    ArrayList<Integer> data;
    List<GetPostByIdRecentComment> comments;

    public PostCommentAdapter(Context context, List<GetPostByIdRecentComment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public OfferHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.makeoffer_recyclerview_layout, viewGroup, false);
        return new OfferHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferHolder viewHolder, int i) {
        if (comments != null) {
            Log.e("comments", "" + comments.get(i).getComment());

            Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + comments.get(i).getProfileImageUrl());
            Glide.with(context).load(path).into(viewHolder.userPic);

            viewHolder.username.setText(comments.get(i).getUserName());
            viewHolder.comment.setText(comments.get(i).getComment());
            String lastActiveDate = AppConstants.convertLastActiveDate(comments.get(0).getAddedOn());
            viewHolder.comment_time.setText(lastActiveDate);
        }
    }

    @Override
    public int getItemCount() {

        if (comments != null) {
            return comments.size();
        } else
            return 0;

    }

    public class OfferHolder extends RecyclerView.ViewHolder {
        TextView username, comment, comment_time;
        ImageView userPic;

        public OfferHolder(@NonNull View itemView) {

            super(itemView);
            userPic = itemView.findViewById(R.id.user_Profile_pic);
            username = itemView.findViewById(R.id.tv_username);
            comment = itemView.findViewById(R.id.tv_comment);
            comment_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
