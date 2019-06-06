package com.elintminds.mac.metatopos.activities.addnewpost.Adapers;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.fileupload.FileUploadData;
import com.elintminds.mac.metatopos.beans.removeUploadedFile.RemovefileResponse;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewJobsImagesAdapter extends RecyclerView.Adapter<AddNewJobsImagesAdapter.PostImagesViewHolder> {
    Context context;
    ArrayList<String> data;
    FileUploadData fileUploadData;
    ProgressDialog m_Dialog;
    APIService mapiService;
    HashMap<String, String> map = new HashMap<String, String>();

    public AddNewJobsImagesAdapter(Context context, ArrayList<String> fileUploadData) {
        this.context = context;
        this.data = fileUploadData;
    }

    @NonNull
    @Override
    public PostImagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.new_post_images_container_recyclerview_layout, viewGroup, false);
        return new PostImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostImagesViewHolder viewHolder, final int i) {

        final String fileUploadData = data.get(i);
        final String filedata = fileUploadData;

        final Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + filedata);
        Glide.with(context).load(path).into(viewHolder.buy_or_sell_post_images);
        viewHolder.imagepath.setText(fileUploadData);

        viewHolder.remove_image.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_remove));

        viewHolder.remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String previouspath = fileUploadData;
                map.put("previouspath", previouspath);
                removeUploadedFile(map);
                data.remove(i);
                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        if (data != null) {
            return data.size();
        } else {
            return 0;
        }

    }

    public class PostImagesViewHolder extends RecyclerView.ViewHolder {

        ImageView buy_or_sell_post_images, remove_image;
        TextView imagepath;
        CardView cardView;

        public PostImagesViewHolder(@NonNull View itemView) {

            super(itemView);
            cardView = itemView.findViewById(R.id.container_top);
            buy_or_sell_post_images = itemView.findViewById(R.id.buy_or_sell_post_images);
            remove_image = itemView.findViewById(R.id.remove_post_image);
            imagepath = itemView.findViewById(R.id.image_path);


        }
    }

    private void removeUploadedFile(HashMap<String, String> map) {
        Log.e("Follow", "" + map);
        mapiService = RetrofitClient.getClient().create(APIService.class);
        Call<RemovefileResponse> call = mapiService.removeFile(map);

        call.enqueue(new Callback<RemovefileResponse>() {
            @Override
            public void onResponse(Call<RemovefileResponse> call, Response<RemovefileResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == true) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<RemovefileResponse> call, Throwable t) {
                Toast.makeText(context, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
