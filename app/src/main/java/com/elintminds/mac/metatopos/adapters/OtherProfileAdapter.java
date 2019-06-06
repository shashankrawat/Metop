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
import com.elintminds.mac.metatopos.beans.userevents.EventDetails;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class OtherProfileAdapter extends RecyclerView.Adapter<OtherProfileAdapter.ViewHolder> {
    Context context;
    ArrayList<Integer> mojiImages;
    List<EventDetails> eventlist;

    public OtherProfileAdapter(Context context, List<EventDetails> eventlist) {
        this.context = context;
        this.eventlist = eventlist;
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
        EventDetails eventdata = eventlist.get(i);
        Log.e("Event Data", "" + eventdata.getAttachments());
        if (eventdata.getAttachments() != null && eventdata.getAttachments().size() > 0) {
            final String postattactment = eventdata.getAttachments().get(0).getImageUrl();
            Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + postattactment);
            Glide.with(context).load(path).into(viewHolder.iv_post_image);
        }
        viewHolder.tv_totalviews.setText(eventdata.getViewCount());
        viewHolder.tv_total_comment.setText(eventdata.getTotalComment());
        viewHolder.tv_evnet_title.setText(eventdata.getTitle());
        viewHolder.tv_intrested_people.setText(" " + eventdata.getTotalInterestedPeople());
        String lastActiveDate = AppConstants.convertLastActiveDate(eventlist.get(0).getAddedOn());
        viewHolder.event_post_time.setText(lastActiveDate);
//        viewHolder.event_post_time.setText(eventdata.getPostinfo().getPosts().get(0).getStartDateTime());
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
        if (eventlist == null) {
            return 0;
        } else {
            return eventlist.size();

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_post_image;
        TextView tv_totalviews, tv_total_comment, tv_intrested_people, tv_evnet_title, event_post_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_post_image = itemView.findViewById(R.id.event_images);
            tv_totalviews = itemView.findViewById(R.id.tv_totalviews);
            tv_total_comment = itemView.findViewById(R.id.tv_comments);
            tv_intrested_people = itemView.findViewById(R.id.total_intrested_count);
            tv_evnet_title = itemView.findViewById(R.id.tv_event_title);
            event_post_time = itemView.findViewById(R.id.event_post_time);
        }
    }

}
