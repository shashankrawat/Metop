package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.feedback.CurrentUserFeedbackInfo;
import com.elintminds.mac.metatopos.beans.feedback.FeedbackData;
import com.elintminds.mac.metatopos.beans.feedback.FeedbacksList;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackHolder> {
    Context context;
    ArrayList<Integer> images;
    List<FeedbackData> feedbackData;
    List<FeedbacksList> otherUserFeedbackData;

    public FeedbackAdapter(Context context, List<FeedbacksList> otherUserFeedbackData) {
        this.context = context;
        this.otherUserFeedbackData = otherUserFeedbackData;
    }


    @NonNull
    @Override
    public FeedbackHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rating_and_feedback_layout, viewGroup, false);
        return new FeedbackHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackHolder feedbackHolder, int i) {
        CurrentUserFeedbackInfo data = otherUserFeedbackData.get(i).getUserInfo();
        Log.e("Feedback", "" + otherUserFeedbackData);
        if (data != null) {
            String url = data.getProfileImageUrl();
            Uri imagepath = Uri.parse("https://www.metatopos.elintminds.work/" + url);
            Glide.with(context).load(imagepath).into(feedbackHolder.user_Profile);
            feedbackHolder.userName.setText(data.getUserName());
            feedbackHolder.feedback_msg.setText(otherUserFeedbackData.get(i).getFeedbackMessage());
            feedbackHolder.ratingBar.setRating(otherUserFeedbackData.get(i).getRatingStars());
            String lastActiveDate = AppConstants.convertLastActiveDate(data.getAddedOn());
            feedbackHolder.review_post_time.setText(lastActiveDate);
        }
    }

    @Override
    public int getItemCount() {
        return otherUserFeedbackData.size();
    }

    public class FeedbackHolder extends RecyclerView.ViewHolder {
        CircleImageView user_Profile;
        TextView userName, feedback_msg, review_post_time;
        RatingBar ratingBar;

        public FeedbackHolder(@NonNull View itemView) {
            super(itemView);
            user_Profile = itemView.findViewById(R.id.user_Profile_pic);
            userName = itemView.findViewById(R.id.tv_username);
            feedback_msg = itemView.findViewById(R.id.review_content);
            review_post_time = itemView.findViewById(R.id.tv_time);
            ratingBar = itemView.findViewById(R.id.ratings);

        }
    }
}
