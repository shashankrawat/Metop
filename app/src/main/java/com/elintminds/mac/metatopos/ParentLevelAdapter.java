package com.elintminds.mac.metatopos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elintminds.mac.metatopos.activities.addnewpost.AdvertisementDetailsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.BuyOrSellFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.EventDetailFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.FoodGroceryDeatilsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.JobDetailFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.OtherPostdetailsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.ServicesDetailsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.SportsEntertainmentDetailsFormActivity;
import com.elintminds.mac.metatopos.beans.addpost.AddPostData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ParentLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private List<AddPostData> postSuperCategories;
    Dialog dialog;

    public ParentLevelAdapter(Context mContext, List<AddPostData> postSuperCategories) {
        this.mContext = mContext;
        this.postSuperCategories = postSuperCategories;

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return postSuperCategories.get(groupPosition).getCategoryList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final AddPostData groupItem = (AddPostData) getGroup(groupPosition);
        final CustomExpListView secondLevelExpListView = new CustomExpListView(this.mContext);
        secondLevelExpListView.setDivider(null);
        final SecondLevelAdapter secondLevelAdapter = new SecondLevelAdapter(this.mContext, groupItem.getCategoryList(), groupItem.getId());
        secondLevelExpListView.setAdapter(secondLevelAdapter);
        secondLevelExpListView.setGroupIndicator(null);
        secondLevelExpListView.setChildIndicator(null);
        secondLevelExpListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {

                int childCount = secondLevelAdapter.getChildrenCount(groupPosition);
                if (childCount > 0) {
                    if (groupPosition != previousGroup)
                        secondLevelExpListView.collapseGroup(previousGroup);

                    previousGroup = groupPosition;
                } else {
                    Log.e("PAR EXPAND", "" + groupPosition + "" + secondLevelAdapter.getChildrenCount(groupPosition));
                    String getchildposition = groupItem.getCategoryList().get(groupPosition).getId();
                    String superCategoryID = groupItem.getCategoryList().get(groupPosition).getPostSuperCategoryId();
                    Intent intent;
                    if (superCategoryID.equals("1")) {
                        intent = new Intent(mContext, BuyOrSellFormActivity.class);
                        intent.putExtra("ChildCategoryID", getchildposition);
                        intent.putExtra("SuperCategoryID", superCategoryID);
                        intent.putExtra("isFromRemoveEditActivity", "Category_First");
                        mContext.startActivity(intent);
                    } else if (superCategoryID.equals("2")) {
                        intent = new Intent(mContext, ServicesDetailsFormActivity.class);
                        intent.putExtra("ChildCategoryID", getchildposition);
                        intent.putExtra("SuperCategoryID", superCategoryID);
                        intent.putExtra("isFromRemoveEditActivity", "Category_Services");
                        mContext.startActivity(intent);
                    } else if (superCategoryID.equals("3")) {
                        intent = new Intent(mContext, FoodGroceryDeatilsFormActivity.class);
                        intent.putExtra("ChildCategoryID", getchildposition);
                        intent.putExtra("SuperCategoryID", superCategoryID);
                        intent.putExtra("isFromRemoveEditActivity", "Category_Food");
                        mContext.startActivity(intent);
                    } else if (superCategoryID.equals("4")) {
                        intent = new Intent(mContext, JobDetailFormActivity.class);
                        intent.putExtra("ChildCategoryID", getchildposition);
                        intent.putExtra("SuperCategoryID", superCategoryID);
                        intent.putExtra("isFromRemoveEditActivity", "Category_Job");
                        mContext.startActivity(intent);
                    } else if (superCategoryID.equals("5")) {
                        intent = new Intent(mContext, SportsEntertainmentDetailsFormActivity.class);
                        intent.putExtra("ChildCategoryID", getchildposition);
                        intent.putExtra("SuperCategoryID", superCategoryID);
                        intent.putExtra("isFromRemoveEditActivity", "Category_Sports");
                        mContext.startActivity(intent);
                    } else if (superCategoryID.equals("6")) {
                        intent = new Intent(mContext, EventDetailFormActivity.class);
                        intent.putExtra("ChildCategoryID", getchildposition);
                        intent.putExtra("SuperCategoryID", superCategoryID);
                        intent.putExtra("isFromRemoveEditActivity", "Category_Event");
                        mContext.startActivity(intent);
                    } else if (superCategoryID.equals("7")) {
                        intent = new Intent(mContext, AdvertisementDetailsFormActivity.class);
                        intent.putExtra("ChildCategoryID", getchildposition);
                        intent.putExtra("SuperCategoryID", superCategoryID);
                        intent.putExtra("isFromRemoveEditActivity", "Category_Sports");
                        mContext.startActivity(intent);
                    } else if (superCategoryID.equals("8")) {
                        intent = new Intent(mContext, OtherPostdetailsFormActivity.class);
                        intent.putExtra("ChildCategoryID", getchildposition);
                        intent.putExtra("SuperCategoryID", superCategoryID);
                        intent.putExtra("isFromRemoveEditActivity", "Category_Other");
                        mContext.startActivity(intent);
                    }
                }
            }
        });
        return secondLevelExpListView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.postSuperCategories.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.postSuperCategories.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        AddPostData item = (AddPostData) getGroup(groupPosition);
        String headerTitle = item.getTitle();
        String headerdescription = item.getDescription();
        String picturePathURL = item.getImageUrl();
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.add_post_recyclerview_layout, parent, false);
        }
        LinearLayout main_lay = convertView.findViewById(R.id.post_lay);
        CircleImageView postImage = convertView.findViewById(R.id.post_image);
        TextView tv_post_title = convertView.findViewById(R.id.tv_post_title);
        TextView tv_post_content = convertView.findViewById(R.id.tv_post_content);
        if (groupPosition % 2 == 0) {
            main_lay.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_addpost_bg_blue));
            tv_post_title.setTextColor(ContextCompat.getColor(mContext, R.color.colorwhite));
            tv_post_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_white_drop, 0);
            tv_post_content.setTextColor(ContextCompat.getColor(mContext, R.color.colorwhite));
        } else {
            main_lay.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.ic_addpost_bg_gray));
            tv_post_title.setTextColor(ContextCompat.getColor(mContext, R.color.colorblack));
            tv_post_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_back_drop, 0);
            tv_post_content.setTextColor(ContextCompat.getColor(mContext, R.color.colorblack));
        }
        Uri path = Uri.parse("https://www.metatopos.elintminds.work/" + picturePathURL);
        Glide.with(mContext).load(path).into(postImage);
        tv_post_title.setText(headerTitle);
        tv_post_content.setText(headerdescription);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
