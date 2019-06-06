package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.getmessges.Messages;
import com.elintminds.mac.metatopos.common.AppConstants;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.Chat_holder> {
    private List<Messages> msgList = null;
    private Context context;
    private String loggedUserId;

    public ChatAdapter(Context context, List<Messages> msgList, String loggedUserId) {
        this.msgList = msgList;
        this.context = context;
        this.loggedUserId = loggedUserId;
        Log.e("user ID", "" + loggedUserId);
    }

    @NonNull
    @Override
    public Chat_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.chat_recyclerview_layout, viewGroup, false);
        return new Chat_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Chat_holder chat_holder, int i) {
        Messages msgDto = this.msgList.get(i);
        // If the message is a received message.
        Log.e("USER ID", "" + msgDto.getAddedBy());
        if (msgDto.getAddedBy().equalsIgnoreCase(loggedUserId)) {
            // Show sent message in right linearlayout.
            chat_holder.rightMsgLayout.setVisibility(LinearLayout.VISIBLE);
            chat_holder.rightMsgTextView.setVisibility(View.VISIBLE);
            chat_holder.rightMsgTextView.setText(msgDto.getMessage());
            chat_holder.rightmsgTime.setText(AppConstants.convertLastActiveDate(msgDto.getAddedOn()));
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            chat_holder.leftMsgLayout.setVisibility(LinearLayout.GONE);
//                chat_holder.imageviewRight.setVisibility(View.GONE);
        }
        // If the message is a sent message.
        else {

            chat_holder.leftMsgLayout.setVisibility(LinearLayout.VISIBLE);
            chat_holder.leftMsgTextView.setVisibility(View.VISIBLE);
            chat_holder.leftMsgTextView.setText(msgDto.getMessage());
            chat_holder.leftmsgtime.setText(AppConstants.convertLastActiveDate(msgDto.getAddedOn()));
//                chat_holder.imageviewLeft.setVisibility(View.GONE);
//                chat_holder.linImage.setVisibility(View.GONE);
//                chat_holder.linMessage.setVisibility(View.VISIBLE);
            // Remove left linearlayout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.
            chat_holder.rightMsgLayout.setVisibility(LinearLayout.GONE);


            // Show received message in left linearlayout.chat_right_msg_text_view
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();

    }

    public class Chat_holder extends RecyclerView.ViewHolder {
        LinearLayout leftMsgLayout;
        ImageView imageviewRight;
        ImageView imageviewLeft;

        LinearLayout rightMsgLayout;
        LinearLayout linImage;
        LinearLayout linMessage;
        TextView leftMsgTextView, leftmsgtime;
        TextView rightMsgTextView, rightmsgTime;

        public Chat_holder(@NonNull View itemView) {
            super(itemView);
            if (itemView != null) {
                leftMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_left_msg_layout);
//                linImage = (LinearLayout) itemView.findViewById(R.id.linImage);
//                linMessage = (LinearLayout) itemView.findViewById(R.id.linMessage);
//                imageviewRight = (ImageView) itemView.findViewById(R.id.imageviewRight);
//                imageviewLeft = (ImageView) itemView.findViewById(R.id.imageviewLeft);
                rightMsgLayout = (LinearLayout) itemView.findViewById(R.id.chat_right_msg_layout);
                leftMsgTextView = (TextView) itemView.findViewById(R.id.chat_left_msg_text_view);
                rightMsgTextView = (TextView) itemView.findViewById(R.id.chat_right_msg_text_view);
                leftmsgtime = (TextView) itemView.findViewById(R.id.left_msg_time_textView);
                rightmsgTime = (TextView) itemView.findViewById(R.id.right_msg_time_textView);

            }
        }
    }

}
