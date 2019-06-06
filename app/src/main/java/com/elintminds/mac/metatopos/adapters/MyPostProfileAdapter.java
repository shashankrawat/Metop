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
import com.elintminds.mac.metatopos.activities.RemoveAndEditPostActivtiy;
import com.elintminds.mac.metatopos.beans.userposts.Postdescription;
import com.elintminds.mac.metatopos.beans.userposts.UserPostdata;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.List;

public class MyPostProfileAdapter extends RecyclerView.Adapter<MyPostProfileAdapter.ViewHolder> {
    Context context;
    UserPostdata myPosts_data;
    List<Postdescription> postdescriptions;

    public MyPostProfileAdapter(Context context, List<Postdescription> postdescriptions) {
        this.context = context;
        this.postdescriptions = postdescriptions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_recyclerview_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Postdescription postlist = postdescriptions.get(i);
        if (postlist != null) {
            Log.e("Post Data", "" + postlist.getAttachments());
            if (postlist.getAttachments() != null && postlist.getAttachments().size() > 0) {
                final String postattactment = postlist.getAttachments().get(0).getImageUrl();
                Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + postattactment);
                Glide.with(context).load(path).into(viewHolder.iv_post_image);
            }
            viewHolder.post_price.setText("$" + postlist.getPrice());
            viewHolder.post_description.setText(postlist.getTitle());
            viewHolder.post_views.setText(" " + postlist.getViewCount());
            viewHolder.post_comments.setText(postlist.getTotalComment());
            String lastActiveDate = AppConstants.convertLastActiveDate(postlist.getAddedOn());
            viewHolder.post_time.setText(lastActiveDate);
            viewHolder.iv_post_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String postId = postlist.getPostId();
                    Intent removeeditpost = new Intent(context, RemoveAndEditPostActivtiy.class);
                    removeeditpost.putExtra("POST_ID", postId);
                    context.startActivity(removeeditpost);
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        if (postdescriptions != null) {
            return postdescriptions.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_post_image;
        TextView post_time, post_description, post_views, post_comments, post_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_post_image = itemView.findViewById(R.id.iv_post_image);
            post_time = itemView.findViewById(R.id.tv_post_time);
            post_price = itemView.findViewById(R.id.tv_post_type);
            post_views = itemView.findViewById(R.id.tv_totalviews);
            post_comments = itemView.findViewById(R.id.tv_comments);
            post_description = itemView.findViewById(R.id.tv_post_description);
        }
    }

}
