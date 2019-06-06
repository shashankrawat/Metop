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
import com.elintminds.mac.metatopos.beans.userposts.Postdescription;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class OtherProfilePostAdapter extends RecyclerView.Adapter<OtherProfilePostAdapter.ViewHolder> {
    Context context;
    ArrayList<Integer> mojiImages;
    List<Postdescription> postlist;

    public OtherProfilePostAdapter(Context context, List<Postdescription> postlist) {
        this.context = context;
        this.postlist = postlist;
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
        Postdescription postdata = postlist.get(i);
        Log.e("Post Data", "" + postdata.getAttachments());
        if (postdata.getAttachments() != null && postdata.getAttachments().size() > 0) {
            final String postattactment = postdata.getAttachments().get(0).getImageUrl();
            Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + postattactment);
            Glide.with(context).load(path).into(viewHolder.iv_post_image);
        }
        viewHolder.post_price.setText("$" + postdata.getPrice());
        viewHolder.post_description.setText(postdata.getTitle());
        viewHolder.post_views.setText(" " + postdata.getViewCount());
        String lastActiveDate = AppConstants.convertLastActiveDate(postlist.get(0).getAddedOn());
        viewHolder.post_time.setText(lastActiveDate);
        viewHolder.post_comments.setText(postdata.getTotalComment());
        viewHolder.iv_post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                Intent removeeditpost = new Intent(context, RemoveAndEditPostActivtiy.class);
//                context.startActivity(removeeditpost);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (postlist == null) {
            return 0;
        } else {
            return postlist.size();
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
