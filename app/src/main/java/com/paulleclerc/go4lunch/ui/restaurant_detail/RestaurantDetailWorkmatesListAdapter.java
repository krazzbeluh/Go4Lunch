/*
 * RestaurantDetailWorkmatesListAdapter.java
 *   Go4Lunch
 *
 *   Created by paulleclerc on 5/28/20 9:52 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.restaurant_detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Workmate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantDetailWorkmatesListAdapter extends RecyclerView.Adapter<RestaurantDetailWorkmatesListAdapter.ViewHolder> {
    private List<Workmate> workmates = new ArrayList<>();

    public void setWorkmates(List<Workmate> workmates) {
        this.workmates = workmates;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.workmates_recyclerview_row, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindWorkmate(workmates.get(position));
    }

    @Override
    public int getItemCount() {
        return workmates.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.workmate_avatar)
        ImageView avatar;
        @BindView(R.id.workmate_status)
        TextView status;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindWorkmate(Workmate workmate) {
            Glide.with(itemView).load(workmate.avatarUri).placeholder(R.drawable.workmate).into(avatar);
            status.setText(itemView.getContext().getString(R.string.workmate_is_joining, workmate.username));
        }
    }
}
