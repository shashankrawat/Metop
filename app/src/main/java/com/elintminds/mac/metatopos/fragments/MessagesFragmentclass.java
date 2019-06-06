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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.ChatActivity;
import com.elintminds.mac.metatopos.activities.SwipeToDeleteCallback;
import com.elintminds.mac.metatopos.adapters.MessageAdapter;
import com.elintminds.mac.metatopos.beans.chat.RecyclerItemTouchHelper;
import com.elintminds.mac.metatopos.beans.getchatthreads.ChatThreadData;
import com.elintminds.mac.metatopos.beans.getchatthreads.ChatThreadsResponse;
import com.elintminds.mac.metatopos.beans.removechathread.RemoveChatThread;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragmentclass extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, MessageAdapter.MessageItemClickListerner
{
    public static final String TAG = "MessagesFragmentclass";
    ArrayList<Integer> data = new ArrayList<>(Arrays.asList(R.drawable.ic_genmoji_man, R.drawable.ic_genmoji_woman));
    ImageView msgicon;
    ArrayList<String> userNames = new ArrayList<>(Arrays.asList("Isabel", "Tyler", "Nellie", "Jesus", "Jim", "Hanson", "Susan", "Gordon", "Caleb", "Jim", "Hanson", "Susan", "Gordon", "Caleb"));
    MessageAdapter messageAdapter;
    RecyclerView msgRecyclerView;
    LinearLayoutManager linearLayoutManager;
    HashMap<String, String> map;
    APIService mapiService;
    ProgressDialog m_Dialog;
    SharedPreferences preferences;
    String token;
    List<ChatThreadData> chatThreadData;
    Context mContext;
    LinearLayout layout;
    String ChatID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messagelist_layout, container, false);
        msgRecyclerView = view.findViewById(R.id.msg_list_recyclerview);
        layout = view.findViewById(R.id.layout);
        linearLayoutManager = new LinearLayoutManager(getContext());
        msgRecyclerView.setLayoutManager(linearLayoutManager);

//        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(getContext(), 0, ItemTouchHelper.LEFT, this);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(msgRecyclerView);
//        msgicon = view.findViewById(R.id.msgicon);
//           msgicon.setOnClickListener(this);
        enableSwipeToDeleteAndUndo();
        map = new HashMap<>();
        preferences = getContext().getSharedPreferences("Prefrences", 0);
        token = preferences.getString("User_Token", null);
        getChatThread(token);
        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }

    @Override
    public void onItemClick(int position) {

        Intent chatIntent = new Intent(getContext(), ChatActivity.class);
        chatIntent.putExtra("isFrom", "FromMessagesActivity");
        startActivity(chatIntent);

        getActivity().startActivityForResult(chatIntent, 101);
    }


//    @Override
//    public void onClick(View view) {
//        if (view == msgicon) {
//            Intent i = new Intent(getContext(), ChatActivity.class);
//            startActivity(i);
//        }

//    }

    private void getChatThread(String token) {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            Call<ChatThreadsResponse> call = mapiService.getChatThreads(token);
            call.enqueue(new Callback<ChatThreadsResponse>() {
                @Override
                public void onResponse(@NonNull Call<ChatThreadsResponse> call, @NonNull Response<ChatThreadsResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {

                            Log.e("Chat Threads", "" + response.body().getData().size());
                            m_Dialog.dismiss();
                            chatThreadData = response.body().getData();
                            if (chatThreadData != null && chatThreadData.size() > 0) {
                                messageAdapter = new MessageAdapter(getContext(), chatThreadData, (MessageAdapter.MessageItemClickListerner) mContext);
                                msgRecyclerView.setAdapter(messageAdapter);
                            }
                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChatThreadsResponse> call, Throwable t) {
//                    Toast.makeText(getContext(), "" + t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

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
                final ChatThreadData item = messageAdapter.getData().get(position);

                chatThreadData.get(position).setSwipedLeft(true);
                messageAdapter.notifyDataSetChanged();
                map.put("chatid", chatThreadData.get(position).getId());
                removeChatThread(token, map);
                messageAdapter.removeItem(position);

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
        itemTouchhelper.attachToRecyclerView(msgRecyclerView);
    }

    private void removeChatThread(String token, HashMap<String, String> map) {
        Log.e("RemoveChatThread", "" + map);
        Log.e("Token", "" + token);
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<RemoveChatThread> call = mapiService.removeChatThread(token, map);
            call.enqueue(new Callback<RemoveChatThread>() {
                @Override
                public void onResponse(@NonNull Call<RemoveChatThread> call, @NonNull Response<RemoveChatThread> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus()) {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RemoveChatThread> call, Throwable t) {
                    Toast.makeText(getContext(), "" + t.getMessage().toString(), Toast.LENGTH_SHORT).show();


                }
            });
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
