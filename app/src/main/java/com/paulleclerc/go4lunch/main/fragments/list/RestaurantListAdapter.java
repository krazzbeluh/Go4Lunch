package com.paulleclerc.go4lunch.main.fragments.list;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.maps.model.PlacesSearchResult;
import com.paulleclerc.go4lunch.R;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantListViewHolder> {
    private PlacesSearchResult[] places = {};

    public void setPlaces(PlacesSearchResult[] places) {
        this.places = places;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_recyclerview_row, parent, false);
        return new RestaurantListViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListViewHolder holder, int position) {
        holder.bindRestaurant(places[position]);
    }

    @Override
    public int getItemCount() {
        return places.length;
    }

    static class RestaurantListViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = RestaurantListViewHolder.class.getSimpleName();
        private PlacesSearchResult restaurant;

        @BindView(R.id.restaurant_title)
        TextView title;
        @BindView(R.id.restaurant_distance)
        TextView distance;
        @BindView(R.id.restaurant_adress)
        TextView address;
        @BindView(R.id.restaurant_opening_time)
        TextView openingTime;
        @BindView(R.id.restaurant_workmates_number)
        TextView workmatesNumber;
        @BindView(R.id.restaurant_star_1)
        ImageView star1;
        @BindView(R.id.restaurant_star_2)
        ImageView star2;
        @BindView(R.id.restaurant_star_3)
        ImageView star3;

        public RestaurantListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> Log.d(TAG, "RestaurantListViewHolder: CLICK!"));
        }

        void bindRestaurant(PlacesSearchResult restaurant) {
            this.restaurant = restaurant;
            title.setText(restaurant.name);
            address.setText(restaurant.formattedAddress); // TODO: Null?
            //openingTime.setText(restaurant.openingHours.weekdayText[0]); // TODO: Null? // TODO: set day index
            workmatesNumber.setText("0"); // TODO: set workmates count
        }
    }
}
