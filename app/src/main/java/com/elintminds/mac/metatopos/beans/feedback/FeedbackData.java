package com.elintminds.mac.metatopos.beans.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedbackData {


    @SerializedName("user_data")
    @Expose
    private OtherUserFeedbackData userData;
    @SerializedName("feedbacks_data")
    @Expose
    private List<FeedbacksList> feedbacksData = null;

    public OtherUserFeedbackData getUserData() {
        return userData;
    }

    public void setUserData(OtherUserFeedbackData userData) {
        this.userData = userData;
    }

    public List<FeedbacksList> getFeedbacksData() {
        return feedbacksData;
    }

    public void setFeedbacksData(List<FeedbacksList> feedbacksData) {
        this.feedbacksData = feedbacksData;
    }


}
