package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.genEmoji.AdapterClickListerner;
import com.elintminds.mac.metatopos.beans.genEmoji.GenEmojiData;

import java.util.ArrayList;
import java.util.List;

public class GenMojiAdapter extends RecyclerView.Adapter<GenMojiAdapter.ViewHolder> {
    Context context;
    //    ArrayList<GenEmojiData> mojiImages=new ArrayList<GenEmojiData>();
    List<GenEmojiData> mojiImages = new ArrayList();

    AdapterClickListerner mListener;
    int selectedposition = -1;

    public GenMojiAdapter(Context context, List<GenEmojiData> mojiImages, AdapterClickListerner listerner) {
        this.context = context;
        this.mojiImages = mojiImages;
        mListener = listerner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.select_genmoji_recyclerview_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
//        viewHolder.iv_image.setImageResource((Integer) mojiImages.get(i));
        final GenEmojiData genEmoji = mojiImages.get(i);
        Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + mojiImages.get(i).getMojiImageUrl());
        Glide.with(context).load(path).into(viewHolder.iv_genmoji);
        if (selectedposition == i)
            viewHolder.genmoji_lay.setBackgroundResource(R.drawable.select_box);
        else
            viewHolder.genmoji_lay.setBackgroundResource(0);

        viewHolder.iv_genmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.onEmojiClick(viewHolder.getAdapterPosition());
                selectedposition = i;
                notifyDataSetChanged();



            }
        });

    }

    @Override
    public int getItemCount() {
        return mojiImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_genmoji;
        RelativeLayout genmoji_lay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_genmoji = itemView.findViewById(R.id.iv_image);
            genmoji_lay = itemView.findViewById(R.id.genmoji_lay);
        }
    }


}
