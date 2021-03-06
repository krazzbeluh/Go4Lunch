/*
 * WorkmatesListAdapter.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/18/20 12:47 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.main.fragments.workmates;

import android.graphics.Color;
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

public class WorkmatesListAdapter extends RecyclerView.Adapter<WorkmatesListAdapter.WorkmatesViewHolder> {
    private final OnClickOnItem clickListener;
    private List<Workmate> workmates = new ArrayList<>();

    WorkmatesListAdapter(OnClickOnItem clickListener) {
        this.clickListener = clickListener;
    }

    public void setWorkmates(List<Workmate> workmates) {
        this.workmates = workmates;
    }

    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.workmates_recyclerview_row, parent, false);
        return new WorkmatesViewHolder(inflatedView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
        holder.bindWorkmate(workmates.get(position));
    }

    @Override
    public int getItemCount() {
        return workmates.size();
    }

    interface OnClickOnItem {
        void onClick(Workmate workmate);

        void displayRestaurant(String placeId);
    }

    static class WorkmatesViewHolder extends RecyclerView.ViewHolder {
        private OnClickOnItem onClickListener;
        private Workmate workmate;

        @BindView(R.id.workmate_status)
        TextView workmateStatus;
        @BindView(R.id.workmate_avatar)
        ImageView workmateAvatar;

        WorkmatesViewHolder(@NonNull View itemView, OnClickOnItem onClickListener) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(v -> onClickListener.onClick(workmate));
        }

        void bindWorkmate(Workmate workmate) {
            this.workmate = workmate;

            Glide.with(itemView).load(workmate.avatarUri).placeholder(R.drawable.workmate).into(workmateAvatar);

            if (workmate.getChosenRestaurantName() == null)
                workmateStatus.setText(itemView.getContext().getString(R.string.user_has_not_decided, workmate.username));
            else {
                workmateStatus.setText(itemView.getContext().getString(R.string.user_has_decided, workmate.username, workmate.getChosenRestaurantName()));
                workmateStatus.setTextColor(Color.BLACK);
                workmateStatus.setTypeface(null);
                workmateStatus.setOnClickListener(v -> onClickListener.displayRestaurant(workmate.getChosenRestaurantId()));
            }
        }
    }
}
