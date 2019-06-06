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
import com.elintminds.mac.metatopos.activities.RemoveEditEventsActivity;
import com.elintminds.mac.metatopos.beans.userevents.EventDetails;
import com.elintminds.mac.metatopos.beans.userevents.UserEventdata;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.List;

public class MyProfileEventAdapter extends RecyclerView.Adapter<MyProfileEventAdapter.ViewHolder> {
    Context context;
    UserEventdata eventdata;
    List<EventDetails> eventDetails;

    public MyProfileEventAdapter(Context context, List<EventDetails> eventDetails) {
        this.context = context;
        this.eventDetails = eventDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.other_profile_post_recyclerview_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final EventDetails eventlist = eventDetails.get(i);
        if (eventlist != null) {
            Log.e("Post Data", "" + eventlist.getAttachments());
            if (eventlist.getAttachments() != null && eventlist.getAttachments().size() > 0) {
                final String postattactment = eventlist.getAttachments().get(0).getImageUrl();
                Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + postattactment);
                Glide.with(context).load(path).into(viewHolder.eventImage);
            }
            viewHolder.tv_totalviews.setText(eventlist.getViewCount());
            viewHolder.tv_total_comment.setText(eventlist.getTotalComment());
            viewHolder.tv_evnet_title.setText(eventlist.getTitle());
            viewHolder.tv_intrested_people.setText(" " + eventlist.getTotalInterestedPeople());
            String lastActiveDate = AppConstants.convertLastActiveDate(eventlist.getAddedOn());
            viewHolder.event_post_time.setText(lastActiveDate);
            viewHolder.eventImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String postId = eventlist.getPostId();
                    Intent removeeditpost = new Intent(context, RemoveEditEventsActivity.class);
                    removeeditpost.putExtra("POST_ID", postId);
                    context.startActivity(removeeditpost);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (eventDetails != null) {
            return eventDetails.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView tv_totalviews, tv_total_comment, tv_intrested_people, tv_evnet_title, event_post_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_images);
            tv_totalviews = itemView.findViewById(R.id.tv_totalviews);
            tv_total_comment = itemView.findViewById(R.id.tv_comments);
            tv_intrested_people = itemView.findViewById(R.id.total_intrested_count);
            tv_evnet_title = itemView.findViewById(R.id.tv_event_title);
            event_post_time = itemView.findViewById(R.id.event_post_time);
        }
    }

}
