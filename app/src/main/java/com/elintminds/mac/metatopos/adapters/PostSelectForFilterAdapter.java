package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.activities.SelectedFilterCategoryListener;
import com.elintminds.mac.metatopos.beans.getfiltersCategoryList.PostFilterData;

import java.util.List;

public class PostSelectForFilterAdapter extends RecyclerView.Adapter<PostSelectForFilterAdapter.OfferHolder> {
    Context context;
    List<PostFilterData> postFilterData;
    int selectedposition = -1;
    SelectedFilterCategoryListener mListener;

    public PostSelectForFilterAdapter(Context context, List<PostFilterData> postFilterData, SelectedFilterCategoryListener mListener) {
        this.context = context;
        this.postFilterData = postFilterData;
        this.mListener = mListener;

    }


    @NonNull
    @Override
    public OfferHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.post_selected_for_filter_layout, viewGroup, false);
        return new OfferHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OfferHolder viewHolder, final int i) {

        PostFilterData filterData = postFilterData.get(i);
        if (filterData != null) {
            viewHolder.filter_category_title.setText(filterData.getLabel());
        }
        if (selectedposition == i)
            viewHolder.filter_category_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_post_selected, 0);
        else
            viewHolder.filter_category_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_post_unselected, 0);


        viewHolder.filter_category_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPostSubCategoryClick(viewHolder.getAdapterPosition());
                selectedposition = i;
                notifyDataSetChanged();


            }
        });


    }

    @Override
    public int getItemCount() {
        return postFilterData.size();
    }

    public class OfferHolder extends RecyclerView.ViewHolder {
        TextView filter_category_title;

        public OfferHolder(@NonNull View itemView) {
            super(itemView);
            filter_category_title = itemView.findViewById(R.id.filter_category_title);

        }
    }
}
