package com.elintminds.mac.metatopos.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.AdvertisementActivity;
import com.elintminds.mac.metatopos.activities.ChatActivity;
import com.elintminds.mac.metatopos.activities.EventsActivity;
import com.elintminds.mac.metatopos.activities.FollowersActivity;
import com.elintminds.mac.metatopos.activities.NotificationInterface;
import com.elintminds.mac.metatopos.activities.PostActivity;
import com.elintminds.mac.metatopos.activities.RatingFeedbackActivity;
import com.elintminds.mac.metatopos.activities.SwipeToDeleteCallback;
import com.elintminds.mac.metatopos.adapters.NotificationAdapter;
import com.elintminds.mac.metatopos.beans.getNotifications.NotificationsData;
import com.elintminds.mac.metatopos.beans.getNotifications.NotificationsResponse;
import com.elintminds.mac.metatopos.beans.getNotifications.ReadUnreadNotificationResponse;
import com.elintminds.mac.metatopos.beans.getnotificationtype.GetNotificationTypeResponse;
import com.elintminds.mac.metatopos.beans.getnotificationtype.NotificationTypeData;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragmentclass extends Fragment implements NotificationInterface
{
    public static final String TAG = "NotificationsFragmentclass";
    APIService mapiService;
    ProgressDialog m_Dialog;
    SharedPreferences preferences;
    String token;
    Context mContext;
    List<NotificationsData> notificationsData;
    List<NotificationTypeData> notificationTypeDataList;
    RecyclerView notifications_recycclerview, online_user_recyclerview;
    NotificationAdapter notificationAdapter;
    LinearLayoutManager linearLayoutManager1, linearLayoutManager2;
    String notificationtypeID, notificationtypeName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notifications_layout, container, false);
        preferences = getActivity().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        Log.e("Divice Token", token);
        getNotifications(token);
        notifications_recycclerview = view.findViewById(R.id.notifications_recycclerview);
        online_user_recyclerview = view.findViewById(R.id.online_user_recyclerview);
        linearLayoutManager1 = new LinearLayoutManager(getContext());
        linearLayoutManager2 = new LinearLayoutManager(getContext());
        online_user_recyclerview.setLayoutManager(linearLayoutManager1);
        notifications_recycclerview.setLayoutManager(linearLayoutManager2);
        enableSwipeToDeleteAndUndo();
//        getNotificationType(token);
        return view;
    }
    private void getNotifications(final String token) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<NotificationsResponse> call = mapiService.getNotifications(token);
            m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<NotificationsResponse>() {
                @Override
                public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            notificationsData = response.body().getData();
                            Log.e("Notifications Data", "" + notificationsData.size());
                            notificationAdapter = new NotificationAdapter(getContext(), notificationsData, notificationTypeDataList, NotificationsFragmentclass.this);
                            notifications_recycclerview.setAdapter(notificationAdapter);
//                            getNotificationType(token);

                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    private void getNotificationType(final String token) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<GetNotificationTypeResponse> call = mapiService.getNotificationTypes(token);
            call.enqueue(new Callback<GetNotificationTypeResponse>() {
                @Override
                public void onResponse(Call<GetNotificationTypeResponse> call, Response<GetNotificationTypeResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            Log.e("Notification Types", "" + response.body().getData().size());
                            notificationTypeDataList = response.body().getData();


                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<GetNotificationTypeResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
//                final NotificationsData item = notificationAdapter.get(position);
//
//                chatThreadData.get(position).setSwipedLeft(true);
//                messageAdapter.notifyDataSetChanged();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("notificationid", notificationsData.get(position).getNotificationId());
                removeNotification(token, map);
                notificationAdapter.removeItem(position);

//                Snackbar snackbar = Snackbar.make(layout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
//                snackbar.setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        messageAdapter.restoreItem(item, position);
//                        msgRecyclerView.scrollToPosition(position);
//                    }
//                });
//                snackbar.setActionTextColor(Color.YELLOW);
//                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(notifications_recycclerview);
    }


    private void notificationStatus(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<ReadUnreadNotificationResponse> call = mapiService.readUnreadNotification(token, map);
            call.enqueue(new Callback<ReadUnreadNotificationResponse>() {
                @Override
                public void onResponse(Call<ReadUnreadNotificationResponse> call, Response<ReadUnreadNotificationResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {

                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<ReadUnreadNotificationResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeNotification(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<ReadUnreadNotificationResponse> call = mapiService.removeNotification(token, map);
            m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<ReadUnreadNotificationResponse>() {
                @Override
                public void onResponse(Call<ReadUnreadNotificationResponse> call, Response<ReadUnreadNotificationResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            m_Dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReadUnreadNotificationResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onNotificationClick(int position) {
        NotificationsData data = notificationsData.get(position);
//        Log.e("typedata", "" + notificationTypeDataList.get(0).getTypeId());
//        notificationtypeID = notificationTypeDataList.get(0).getTypeId();
//        notificationtypeName = notificationTypeDataList.get(0).getTypeId();


        String posttype = data.getPostType();
        String postID = String.valueOf(data.getTargetId());
        HashMap<String, String> map = new HashMap<>();
        map.put("notificationid", data.getNotificationId());
        map.put("isRead", "1");
        notificationStatus(token, map);
        if (data.getTargetTypeId().equals("7")) {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            startActivity(intent);
        } else if (data.getTargetTypeId().equals("1")) {
            if (posttype.equalsIgnoreCase("post")) {
                Intent intent = new Intent(getContext(), PostActivity.class);
                intent.putExtra("PostID", postID);
                startActivity(intent);
            }
        } else if (data.getTargetTypeId().equals("1")) {
            if (posttype.equalsIgnoreCase("event")) {
                Intent intent = new Intent(getContext(), EventsActivity.class);
//                intent.putExtra("")
                startActivity(intent);
            }
        } else if (data.getTargetTypeId().equals("1")) {
            if (posttype.equalsIgnoreCase("advertisemnt")) {
                Intent intent = new Intent(getContext(), AdvertisementActivity.class);
                startActivity(intent);
            }
        } else if (data.getTargetTypeId().equals("5")) {
            Intent intent = new Intent(getContext(), RatingFeedbackActivity.class);
            intent.putExtra("IS_FROM", "Notification_Fragment");
            startActivity(intent);

        } else if (data.getTargetTypeId().equals("3")) {
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("PostID", postID);
            startActivity(intent);

        } else if (data.getTargetTypeId().equals("4")) {
            Intent intent = new Intent(getContext(), FollowersActivity.class);
            intent.putExtra("userid", postID);
            startActivity(intent);

        }
        notificationAdapter.notifyDataSetChanged();

    }

}