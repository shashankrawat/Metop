package com.elintminds.mac.metatopos.activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.adapters.ChatAdapter;
import com.elintminds.mac.metatopos.adapters.HintMessageAdapter;
import com.elintminds.mac.metatopos.beans.getmessges.GetMessgesResponse;
import com.elintminds.mac.metatopos.beans.getmessges.HintMessages;
import com.elintminds.mac.metatopos.beans.getmessges.Messages;
import com.elintminds.mac.metatopos.beans.getmessges.MessagesData;
import com.elintminds.mac.metatopos.beans.sendmessage.SendMessageData;
import com.elintminds.mac.metatopos.beans.sendmessage.SendMessageResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppConstants;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, HintMessageAdapter.HintMessageItemClickListerner {
    ImageView chat_backbtn, msg_send_btn, metatopos_icon;
    RecyclerView chat_recyclerview, message_hint_recyclerview;
    EditText ed_msg_content;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    TextView tv_username;
    LinearLayout track_lay;
    CircleImageView iv_user_dp;
    ChatAdapter chatAdapter;
    HintMessageAdapter hintMessageAdapter;
    Messages chatMessageList;
    List<Messages> msglist = new ArrayList<Messages>();
    HashMap<String, String> map;
    APIService mapiService;
    ProgressDialog m_Dialog;
    SharedPreferences preferences;
    String token, receverId, postID, userImagePath, username, messagerecevedcontent, msgreceivedtime;
    String logged_UserID;
    String postLatitude, postLongitude;
    String isFrom;
    public static boolean isUserOnChat = false;
    private Handler handler = new Handler();
    private List<HintMessages> msgdata = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        initviews();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager2 = new LinearLayoutManager(this);
        chat_recyclerview.setLayoutManager(linearLayoutManager);
        message_hint_recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        hintMessageAdapter = new HintMessageAdapter(this, msgdata, ChatActivity.this);
        message_hint_recyclerview.setAdapter(hintMessageAdapter);
        message_hint_recyclerview.scrollToPosition(1);
        hintMessageData();
        chatAdapter = new ChatAdapter(this, msglist, logged_UserID);
        chat_recyclerview.setAdapter(chatAdapter);
        Intent intent = getIntent();
        postID = intent.getStringExtra("PostID");
        receverId = intent.getStringExtra("ReceiverID");
        isFrom = intent.getStringExtra("isFrom");
        Log.e("PostID", "" + postID + "RecieverId" + receverId);
        username = intent.getStringExtra("UserName");
        userImagePath = intent.getStringExtra("ImageURL");
        tv_username.setText(username);
        Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + userImagePath);
        Glide.with(getApplicationContext()).load(path).into(iv_user_dp);
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        map = new HashMap<>();
        map.put("receiverid", receverId);
        map.put("postid", postID);
        getMessages(token, map);
    }

    private void initviews() {
        isUserOnChat = true;
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("New Message"));
        preferences = getApplicationContext().getSharedPreferences("Prefrences", 0);
        logged_UserID = preferences.getString("LOGGED_UDERID", null);
        chat_backbtn = findViewById(R.id.chat_backbtn);
        tv_username = findViewById(R.id.userName);
        iv_user_dp = findViewById(R.id.user_dp);
        msg_send_btn = findViewById(R.id.msg_send_btn);
        metatopos_icon = findViewById(R.id.metatopos_icon);
        ed_msg_content = findViewById(R.id.ed_msg_content);
        track_lay = findViewById(R.id.track_lay);
        chat_recyclerview = findViewById(R.id.chat_recyclerview);
        message_hint_recyclerview = findViewById(R.id.default_text_msg);
        chat_backbtn.setOnClickListener(this);
        msg_send_btn.setOnClickListener(this);
//        metatopos_icon.setOnClickListener(this);
        track_lay.setOnClickListener(this);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equalsIgnoreCase("New Message")) {
                getMessages(token, map);
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view == chat_backbtn) {
            finish();
        } else if (view == msg_send_btn) {
            String msgContent = ed_msg_content.getText().toString().trim();
            if (!TextUtils.isEmpty(msgContent)) {
                // Add a new sent message to the list.
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.rotate_down);
                msg_send_btn.startAnimation(shake);
                map = new HashMap<>();
                map.put("receiverid", receverId);
                map.put("postid", postID);
                map.put("message", msgContent);
                SendMessages(token, map);

            }
        } else if (view == track_lay) {
            Intent path_intent = new Intent(this, ShareLocationActivity.class);
            path_intent.putExtra("POST_LATITUDE", postLatitude);
            path_intent.putExtra("POST_LONGITUDE", postLongitude);
            startActivity(path_intent);
//            Intent intent = new Intent();
//            intent.putExtra("FROM","CHAT");
//            setResult(RESULT_OK, intent);
//            finish();
        }

    }

    private void getMessages(String token, HashMap<String, String> map) {
        Log.e("Messages Data", "" + map);
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);

            Call<GetMessgesResponse> call = mapiService.getMessages(token, map);
            call.enqueue(new Callback<GetMessgesResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetMessgesResponse> call, @NonNull Response<GetMessgesResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Log.e("On Messages Recieved", "" + response.body().getMessage());
                            MessagesData messagesData = response.body().getData();
                            postLatitude = messagesData.getUserdata().getPostLatitude();
                            postLongitude = messagesData.getUserdata().getPostLongitude();
                            Log.e("Post_Latitude", "" + postLatitude + "Post_Longitude" + postLongitude);
                            msglist.addAll(messagesData.getMessages());
                            Log.e("Messages", "" + msglist.size());
                            chatAdapter.notifyDataSetChanged();
                            chat_recyclerview.scrollToPosition(msglist.size() - 1);

                        } else {
                            Log.e("OnError", "" + response.body().getError());
                            Toast.makeText(ChatActivity.this, "" + response.body().getError(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<GetMessgesResponse> call, Throwable t) {
                    Log.e("OnFailure", "" + t.getMessage());
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    private void SendMessages(String token, HashMap<String, String> map) {
        if (AppUtils.isInternetIsAvailable(getApplicationContext())) {
            Call<SendMessageResponse> call = mapiService.sendMessage(token, map);
            call.enqueue(new Callback<SendMessageResponse>() {
                @Override
                public void onResponse(Call<SendMessageResponse> call, Response<SendMessageResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            SendMessageData sendMessageData = response.body().getData();
                            String sent_message_content = sendMessageData.getMessagesData().getMessageInfo().get(0).getMessage();
                            String sent_msg_time = AppConstants.convertLastActiveDate(sendMessageData.getMessagesData().getMessageInfo().get(0).getAddedOn());
                            chatMessageList = new Messages();
                            chatMessageList.setMessage(sent_message_content);
                            chatMessageList.setAddedOn(sent_msg_time);
                            chatMessageList.setAddedBy(logged_UserID);
                            msglist.add(chatMessageList);
                            int newMsgPosition = msglist.size() - 1;
                            chatAdapter.notifyItemInserted(newMsgPosition);
                            chat_recyclerview.scrollToPosition(newMsgPosition);
                            ed_msg_content.setText("");
                        } else {
                            Toast.makeText(getApplicationContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SendMessageResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(int position) {

        map = new HashMap<>();
        map.put("receiverid", receverId);
        map.put("postid", postID);
        map.put("message", msgdata.get(position).getHintmsg());
        SendMessages(token, map);

    }

    private void hintMessageData() {
        HintMessages data = new HintMessages(getString(R.string.lowest_price));
        msgdata.add(data);
        data = new HintMessages(getString(R.string.is_the_price_negotiable));
        msgdata.add(data);
        data = new HintMessages(getString(R.string.make_an_offer));
        msgdata.add(data);
        data = new HintMessages(getString(R.string.im_intrested_in_buying_this));
        msgdata.add(data);
        data = new HintMessages(getString(R.string.is_it_available));
        msgdata.add(data);
        data = new HintMessages(getString(R.string.hello));
        msgdata.add(data);

    }


    @Override
    public void onBackPressed() {
        handler.removeCallbacksAndMessages(null);
        super.onBackPressed();

    }

    @Override
    protected void onPause() {
        isUserOnChat = false;
        handler.removeCallbacksAndMessages(null);
        super.onPause();
    }

    @Override
    protected void onResume() {
        isUserOnChat = true;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
