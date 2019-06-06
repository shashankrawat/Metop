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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.ChatActivity;
import com.elintminds.mac.metatopos.beans.getchatthreads.ChatThreadData;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    Context context;
    ArrayList<String> msglist;
    MessageItemClickListerner listerner;
    List<ChatThreadData> chatThreadData;

    public MessageAdapter(Context context, List<ChatThreadData> chatThreadData, MessageItemClickListerner listerner) {
        this.context = context;
        this.chatThreadData = chatThreadData;
        this.listerner = listerner;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View msgview = layoutInflater.inflate(R.layout.all_msg_list_recyclerview_layout, viewGroup, false);
        return new MessageHolder(msgview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageHolder messageHolder, final int i) {
        final ChatThreadData chatdata = chatThreadData.get(i);
        if (chatdata != null) {
            Log.e("Chat Thread", "" + chatThreadData.get(i));
            messageHolder.tv_username.setText(chatThreadData.get(i).getUserdata().getFullName());
            messageHolder.tv_message.setText(chatThreadData.get(i).getMessage());
            Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + chatThreadData.get(i).getUserdata().getProfileImageUrl());
            Glide.with(context).load(path).into(messageHolder.profile_pic);
            String lastActiveDate = AppConstants.convertLastActiveDate(chatThreadData.get(i).getMessageAddedOn());
            messageHolder.tv_msg_time.setText(lastActiveDate);
            int unreadCount = Integer.parseInt(chatThreadData.get(i).getUnreadMessageCount());
            if (unreadCount > 0) {
                messageHolder.msg_count.setVisibility(View.VISIBLE);
                messageHolder.msg_count.setText(chatThreadData.get(i).getUnreadMessageCount());
            } else {
                messageHolder.msg_count.setVisibility(View.INVISIBLE);
            }

//            if (chatdata.isSwipedLeft()) {
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageHolder.msg_lay.getLayoutParams();
//                params.setMargins(-200, 0, 200, 0);
//                messageHolder.msg_lay.setLayoutParams(params);
//            } else {
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) messageHolder.msg_lay.getLayoutParams();
//                params.setMargins(0, 0, 0, 0);
//                messageHolder.msg_lay.setLayoutParams(params);
//            }

            messageHolder.msg_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String postID = chatThreadData.get(i).getPostId();
                    String receiversID = chatThreadData.get(i).getUserdata().getUserID();
                    Intent chatIntent = new Intent(context, ChatActivity.class);
                    chatIntent.putExtra("PostID", postID);
                    chatIntent.putExtra("ReceiverID", receiversID);
                    chatIntent.putExtra("ImageURL", chatThreadData.get(i).getUserdata().getProfileImageUrl());
                    chatIntent.putExtra("UserName", chatThreadData.get(i).getUserdata().getFullName());
                    chatIntent.putExtra("isFrom", "FromPostActivity");
                    context.startActivity(chatIntent);
//                getActivity().startActivityForResult(chatIntent, 101);
//                listerner.onItemClick(messageHolder.getAdapterPosition());

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (chatThreadData.size() > 0) {
            return chatThreadData.size();
        } else return 0;
    }


    public class MessageHolder extends RecyclerView.ViewHolder {
        RelativeLayout msg_lay;
        TextView tv_username, tv_message, tv_msg_time, msg_count;
        CircleImageView profile_pic;


        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            msg_lay = itemView.findViewById(R.id.msg_lay);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_message = itemView.findViewById(R.id.tv_msg);
            tv_msg_time = itemView.findViewById(R.id.tv_time);
            msg_count = itemView.findViewById(R.id.tv_new_message_count);
            profile_pic = itemView.findViewById(R.id.user_Profile_pic);
        }
    }

    public interface MessageItemClickListerner {
        void onItemClick(int position);
    }


    public List<ChatThreadData> getData() {
        return chatThreadData;
    }

    public void removeItem(int position) {
        chatThreadData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(ChatThreadData item, int position) {
        chatThreadData.add(position, item);
        notifyItemInserted(position);
    }

}
