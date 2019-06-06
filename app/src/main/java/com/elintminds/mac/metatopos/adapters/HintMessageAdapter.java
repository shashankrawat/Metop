package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.getmessges.HintMessages;

import java.util.List;

public class HintMessageAdapter extends RecyclerView.Adapter<HintMessageAdapter.NotificationHolder> {
    Context context;
    List<HintMessages> msgdata;
    HintMessageItemClickListerner mListener;

    public HintMessageAdapter(Context context, List<HintMessages> msgdata, HintMessageItemClickListerner mListener) {

        this.context = context;
        this.msgdata = msgdata;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.hint_message_recyclerview_layout, viewGroup, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationHolder viewholder, final int i) {
        HintMessages data = msgdata.get(i);
        viewholder.tv_hintMsg.setText(data.getHintmsg());
        viewholder.tv_hintMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(viewholder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return msgdata.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder {
        TextView tv_hintMsg;


        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            tv_hintMsg = itemView.findViewById(R.id.hint_messages);


        }
    }


    public interface HintMessageItemClickListerner {
        void onItemClick(int position);
    }
//
//
//    public List<NotificationsData> getData() {
//        return notificationsData;
//    }
//
//    public void removeItem(int position) {
//        notificationsData.remove(position);
//        notifyItemRemoved(position);
//    }


}