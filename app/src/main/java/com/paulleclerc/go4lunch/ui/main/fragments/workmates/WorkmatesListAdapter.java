package com.paulleclerc.go4lunch.ui.main.fragments.workmates;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkmatesListAdapter extends RecyclerView.Adapter<WorkmatesListAdapter.WorkmatesViewHolder> {
    @NonNull
    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class WorkmatesViewHolder extends RecyclerView.ViewHolder {
        public WorkmatesViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
