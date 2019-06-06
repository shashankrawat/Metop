package com.elintminds.mac.metatopos.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elintminds.mac.metatopos.R;
import com.elintminds.mac.metatopos.beans.advertisementplan.PlansData;

import java.util.List;

public class AdvertisementPlanAdapter extends RecyclerView.Adapter<AdvertisementPlanAdapter.PlanHolder> {
    Context context;
    //    public ArrayList<AdvertisementPlanBean> data;
    List<PlansData> planDatalist;
    int[] colorCodeList;

    public AdvertisementPlanAdapter(Context context, List<PlansData> planDatalist) {
        this.context = context;
        this.planDatalist = planDatalist;
    }

    @NonNull
    @Override
    public PlanHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View plan_view = layoutInflater.inflate(R.layout.advertisement_plan_recyclerview_layout, viewGroup, false);
        return new PlanHolder(plan_view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanHolder planHolder, int i) {
        PlansData plandata = planDatalist.get(i);
        planHolder.tv_plan_name.setText(plandata.getTitle());
        planHolder.tv_plan_description.setText(plandata.getDescription());
        if (i == 0) {
            planHolder.viewBgLayout.setBackgroundResource(R.drawable.plan_gradient_green);

        } else if (i == 1) {
            planHolder.viewBgLayout.setBackgroundResource(R.drawable.plan_gradient_blue);

        } else if (i == 2) {
            planHolder.viewBgLayout.setBackgroundResource(R.drawable.plan_gradient_purple);

        } else if (i == 3) {
            planHolder.viewBgLayout.setBackgroundResource(R.drawable.plan_gradient_red);

        }
    }

    @Override
    public int getItemCount() {
        return planDatalist.size();
    }

    public class PlanHolder extends RecyclerView.ViewHolder {
        TextView tv_plan_name, tv_plan_description;
        CardView cardView;
        LinearLayout viewBgLayout;

        public PlanHolder(@NonNull View itemView) {
            super(itemView);
            tv_plan_name = itemView.findViewById(R.id.tv_plan_name);
            cardView = itemView.findViewById(R.id.cardview);
            viewBgLayout = itemView.findViewById(R.id.plan_lay);
            tv_plan_description = itemView.findViewById(R.id.tv_plan_description);
        }
    }
}
