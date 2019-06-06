package com.elintminds.mac.metatopos.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.elintminds.mac.metatopos.ParentLevelAdapter;
import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.addnewpost.AdvertisementDetailsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.EventDetailFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.OtherPostdetailsFormActivity;
import com.elintminds.mac.metatopos.beans.addpost.AddNewPostResponse;
import com.elintminds.mac.metatopos.beans.addpost.AddPostData;
import com.elintminds.mac.metatopos.common.APIService;
import com.elintminds.mac.metatopos.common.AppUtils;
import com.elintminds.mac.metatopos.common.DialogUtils;
import com.elintminds.mac.metatopos.common.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPostFragmentclass extends Fragment
{
    public static final String TAG = "AddPostFragmentclass";
    APIService mapiService;
    ProgressDialog m_Dialog;
    ExpandableListView mExpandableListView;
    ParentLevelAdapter parentLevelAdapter;
    Dialog dialog;
    int[] postIcons = {R.drawable.ic_add_post_buy_or_sell, R.drawable.ic_post_services, R.drawable.ic_post_food_and_groceries, R.drawable.ic_post_jobs,
            R.drawable.ic_post_sports, R.drawable.ic_post_event, R.drawable.ic_addpost_advertisement, R.drawable.ic_post_others};
    String superCategoryID;
    List<AddPostData> postSuperCategories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.add_post_layout, container, false);
        // Init top level data

        mExpandableListView = view.findViewById(R.id.expandableListView_Parent);
        getPostCategoriesList();
        return view;
    }


    private void getPostCategoriesList() {
        if (AppUtils.isInternetIsAvailable(getContext())) {
            mapiService = RetrofitClient.getClient().create(APIService.class);
            Call<AddNewPostResponse> call = mapiService.getPostCategoryList();
            m_Dialog = DialogUtils.showProgressDialog(getContext());
            m_Dialog.show();
            call.enqueue(new Callback<AddNewPostResponse>() {
                @Override
                public void onResponse(Call<AddNewPostResponse> call, Response<AddNewPostResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == true) {
                            postSuperCategories = response.body().getData();
                            mExpandableListView.setChildIndicator(null);
                            if (mExpandableListView != null) {
                                parentLevelAdapter = new ParentLevelAdapter(getContext(), postSuperCategories);
                                mExpandableListView.setAdapter(parentLevelAdapter);
                                // display only one expand item
                                mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                                    int previousGroup = -1;

                                    @Override
                                    public void onGroupExpand(int groupPosition) {
                                        if (groupPosition == 6) {
                                            superCategoryID = postSuperCategories.get(groupPosition).getId();
                                            Intent intent = new Intent(getContext(), AdvertisementDetailsFormActivity.class);
                                            intent.putExtra("SuperCATEGORYID", superCategoryID);
                                            intent.putExtra("isFromRemoveEditActivity", "Category_Advertisement");
                                            startActivity(intent);
//                                            postAdvertisementPopUp();
                                        } else if (groupPosition == 5) {
                                            superCategoryID = postSuperCategories.get(groupPosition).getId();
                                            Intent intent = new Intent(getContext(), EventDetailFormActivity.class);
                                            intent.putExtra("SuperCATEGORYID", superCategoryID);
                                            intent.putExtra("isFromRemoveEditActivity", "Category_Event");
                                            startActivity(intent);
                                        } else if (groupPosition == 7) {
                                            superCategoryID = postSuperCategories.get(groupPosition).getId();
                                            Intent intent = new Intent(getContext(), OtherPostdetailsFormActivity.class);
                                            intent.putExtra("SuperCATEGORYID", superCategoryID);
                                            intent.putExtra("isFromRemoveEditActivity", "Category_Other");
                                            startActivity(intent);
                                        } else if (groupPosition != previousGroup) {
                                            mExpandableListView.collapseGroup(previousGroup);
                                            previousGroup = groupPosition;

                                        }


                                    }

                                });

                            }
                            m_Dialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddNewPostResponse> call, Throwable t) {
                    Log.e("OnFailed", "" + t.getMessage());
                    Toast.makeText(getContext(), "Failed" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    m_Dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getContext(), "!No Internet Connection", Toast.LENGTH_SHORT).show();

        }
    }

    private void postAdvertisementPopUp() {
        Button btn_Post, btn_Cancel;
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.post_advertisement_popup);
        dialog.show();
        btn_Post = dialog.findViewById(R.id.postAdvertisement_btn);
        btn_Cancel = dialog.findViewById(R.id.cancel_btn);
        btn_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AdvertisementDetailsFormActivity.class);
                intent.putExtra("SuperCATEGORYID", superCategoryID);
                intent.putExtra("isFromRemoveEditActivity", "Category_Advertisement");
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }


}
