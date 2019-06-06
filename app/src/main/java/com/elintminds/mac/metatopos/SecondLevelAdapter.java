package com.elintminds.mac.metatopos;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.elintminds.mac.metatopos.activities.addnewpost.BuyOrSellFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.EventDetailFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.FoodGroceryDeatilsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.JobDetailFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.OtherPostdetailsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.ServicesDetailsFormActivity;
import com.elintminds.mac.metatopos.activities.addnewpost.SportsEntertainmentDetailsFormActivity;
import com.elintminds.mac.metatopos.beans.addpost.CategoryList;
import com.elintminds.mac.metatopos.beans.addpost.SubCategoryList;

import java.util.List;

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    private final Context mContext;
    private List<CategoryList> data;
    private String groupId;
    Dialog dialog;
    SubCategoryList subItem;

    public SecondLevelAdapter(Context mContext, List<CategoryList> data, String groupId) {
        this.mContext = mContext;
        this.data = data;
        this.groupId = groupId;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.data.get(groupPosition).getSubCategoryList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.drawer_list_item, parent, false);
        }

        subItem = (SubCategoryList) getChild(groupPosition, childPosition);
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        txtListChild.setText(subItem.getTitle());
        txtListChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubCategoryList categoryList = (SubCategoryList) getChild(groupPosition, childPosition);
                String getchildposition = categoryList.getId();
                String superCategoryID = categoryList.getSuperCategoryId();
                String subCategoryID = categoryList.getPostCategoryId();
                Intent intent;
                Log.e("childID__", "" + getchildposition + "SuperID" + superCategoryID + "SubCatID" + subCategoryID);
                if (groupId.equals("1")) {
                    intent = new Intent(mContext, BuyOrSellFormActivity.class);
                    intent.putExtra("ChildCategoryID", getchildposition);
                    intent.putExtra("SuperCategoryID", superCategoryID);
                    intent.putExtra("SubCategoryID", subCategoryID);
                    intent.putExtra("isFromRemoveEditActivity", "Category_First");
                    mContext.startActivity(intent);
                } else if (groupId.equals("2")) {
                    intent = new Intent(mContext, ServicesDetailsFormActivity.class);
                    intent.putExtra("ChildCategoryID", getchildposition);
                    intent.putExtra("SuperCategoryID", superCategoryID);
                    intent.putExtra("SubCategoryID", subCategoryID);
                    intent.putExtra("isFromRemoveEditActivity", "Category_Services");
                    mContext.startActivity(intent);
                } else if (groupId.equals("3")) {
                    intent = new Intent(mContext, FoodGroceryDeatilsFormActivity.class);
                    intent.putExtra("ChildCategoryID", getchildposition);
                    intent.putExtra("SuperCategoryID", superCategoryID);
                    intent.putExtra("SubCategoryID", subCategoryID);
                    intent.putExtra("isFromRemoveEditActivity", "Category_Food");
                    mContext.startActivity(intent);
                } else if (groupId.equals("4")) {
                    intent = new Intent(mContext, JobDetailFormActivity.class);
                    intent.putExtra("ChildCategoryID", getchildposition);
                    intent.putExtra("SuperCategoryID", superCategoryID);
                    intent.putExtra("SubCategoryID", subCategoryID);
                    intent.putExtra("isFromRemoveEditActivity", "Category_Job");
                    mContext.startActivity(intent);
                } else if (groupId.equals("5")) {
                    intent = new Intent(mContext, SportsEntertainmentDetailsFormActivity.class);
                    intent.putExtra("ChildCategoryID", getchildposition);
                    intent.putExtra("SuperCategoryID", superCategoryID);
                    intent.putExtra("SubCategoryID", subCategoryID);
                    intent.putExtra("isFromRemoveEditActivity", "Category_Sports");
                    mContext.startActivity(intent);
                } else if (groupId.equals("6")) {
                    intent = new Intent(mContext, EventDetailFormActivity.class);
                    intent.putExtra("ChildCategoryID", getchildposition);
                    intent.putExtra("SuperCategoryID", superCategoryID);
                    intent.putExtra("SubCategoryID", subCategoryID);
                    intent.putExtra("isFromRemoveEditActivity", "Category_Event");
                    mContext.startActivity(intent);
                } else if (groupId.equals("8")) {
                    intent = new Intent(mContext, OtherPostdetailsFormActivity.class);
                    intent.putExtra("ChildCategoryID", getchildposition);
                    intent.putExtra("SuperCategoryID", superCategoryID);
                    intent.putExtra("SubCategoryID", subCategoryID);
                    intent.putExtra("isFromRemoveEditActivity", "Category_Other");
                    mContext.startActivity(intent);
                }

            }
        });
        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return this.data.get(groupPosition).getSubCategoryList().size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.data.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.data.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final CategoryList headerTitle = (CategoryList) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.drawer_list_group_second, parent, false);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle.getTitle());
        if (!headerTitle.getTitle().equals("Other")) {
            Drawable img = mContext.getResources().getDrawable(R.drawable.ic_back_drop);
            lblListHeader.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        }


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
