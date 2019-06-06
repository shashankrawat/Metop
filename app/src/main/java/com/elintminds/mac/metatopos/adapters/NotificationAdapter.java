package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.NotificationInterface;
import com.elintminds.mac.metatopos.beans.getNotifications.NotificationsData;
import com.elintminds.mac.metatopos.beans.getnotificationtype.NotificationTypeData;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    Context context;
    List<NotificationsData> notificationsData;
    List<NotificationTypeData> notificationTypeDataList;
    NotificationInterface mListner;
    //    NotificationItemClickListerner mListener;
    APIService mapiService;
    HashMap<String, String> map;

    public NotificationAdapter(Context context, List<NotificationsData> notificationsData, List<NotificationTypeData> notificationTypeDataList, NotificationInterface listner) {
        this.context = context;
        this.notificationsData = notificationsData;
        this.notificationTypeDataList = notificationTypeDataList;
        mListner = listner;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.notifications_recyclerview_layout, viewGroup, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationHolder notificationHolder, final int i) {

        final NotificationsData data = notificationsData.get(i);
//        final NotificationTypeData notificationTypeData = notificationTypeDataList.get(i);
//        Log.e("notificationTypeData", "" + notificationTypeData);
        if (data != null) {
            notificationHolder.userName.setText(data.getFullName());
            notificationHolder.notificationDescription.setText(data.getDescription());
            notificationHolder.userName.setText(data.getFullName());
            String time = AppConstants.convertLastActiveDate(data.getNotificationTime());
            notificationHolder.notificationTime.setText(time);
            Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + data.getProfileImageUrl());
            Glide.with(context).load(path).into(notificationHolder.userDp);
            if (data.getIsRead().equals("0")) {
                notificationHolder.notificationStatus.setVisibility(View.VISIBLE);
                notificationHolder.notification_lay.setBackgroundColor(Color.WHITE);
            } else {
                notificationHolder.notificationStatus.setVisibility(View.GONE);
            }
        }

        notificationHolder.notification_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.onNotificationClick(notificationHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationsData.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        TextView userName, notificationTime, notificationDescription;
        CircleImageView userDp;
        ImageView notificationStatus;
        RelativeLayout notification_lay;
        View divider;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.tv_username);
            userDp = itemView.findViewById(R.id.user_Profile_pic);
            notificationTime = itemView.findViewById(R.id.tv_time);
            notificationDescription = itemView.findViewById(R.id.description);
            notificationStatus = itemView.findViewById(R.id.iv_notification_status);
            notification_lay = itemView.findViewById(R.id.notification_lay);
            divider = itemView.findViewById(R.id.divider);
        }

    }


    public interface NotificationItemClickListerner {
        void onItemClick(int position);
    }


    public List<NotificationsData> getData() {
        return notificationsData;
    }

    public void removeItem(int position) {
        notificationsData.remove(position);
        notifyItemRemoved(position);
    }

}
