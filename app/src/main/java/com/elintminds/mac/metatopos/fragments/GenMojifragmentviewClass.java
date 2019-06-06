package com.elintminds.mac.metatopos.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.GenMojiActivity;
import com.elintminds.mac.metatopos.adapters.GenMojiAdapter;
import com.elintminds.mac.metatopos.beans.genEmoji.AdapterClickListerner;
import com.elintminds.mac.metatopos.beans.genEmoji.GemEmojiResponse;
import com.elintminds.mac.metatopos.beans.genEmoji.GenEmojiData;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenMojifragmentviewClass extends Fragment implements AdapterClickListerner {

    APIService mapiService;
    GenMojiAdapter adapter;
    RecyclerView genMoji_Rview;
    GridLayoutManager gridLayoutManager;
    List<GenEmojiData> emojidata;
    GenMojiActivity parentActivity;

    public GenMojifragmentviewClass() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.viewpager_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parentActivity = (GenMojiActivity) getActivity();
        genMoji_Rview = view.findViewById(R.id.genMoji_Rview);

        getGenEmoji();
    }

    void getGenEmoji() {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<GemEmojiResponse> call = mapiService.GenmojiRespose();
            final ProgressDialog m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<GemEmojiResponse>() {
                @Override
                public void onResponse(Call<GemEmojiResponse> call, Response<GemEmojiResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            emojidata = response.body().getData();
                            gridLayoutManager = new GridLayoutManager(getContext(), 4, LinearLayoutManager.VERTICAL, false);
                            genMoji_Rview.setLayoutManager(gridLayoutManager);
                            adapter = new GenMojiAdapter(getContext(), emojidata, GenMojifragmentviewClass.this);
                            genMoji_Rview.setAdapter(adapter);
                            m_Dialog.dismiss();


                        } else {
                            Toast.makeText(getContext(), "" + response.body().getError(), Toast.LENGTH_SHORT).show();
                            m_Dialog.dismiss();
                        }
                    }


                }

                @Override
                public void onFailure(Call<GemEmojiResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();

                }
            });
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onEmojiClick(int position) {
        GenEmojiData data = emojidata.get(position);
        parentActivity.onDataSelect(data);
    }
}
