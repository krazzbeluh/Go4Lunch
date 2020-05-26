package com.paulleclerc.go4lunch.ui.main.fragments.list;

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
import com.bumptech.glide.Glide;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Restaurant;
import com.paulleclerc.go4lunch.ui.main.ShowDetailListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantListViewHolder> {
    private final ShowDetailListener showDetailListener;
    private List<Restaurant> places = new ArrayList<>();

    public RestaurantListAdapter(ShowDetailListener showDetailListener) {
        this.showDetailListener = showDetailListener;
    }

    public void setPlaces(List<Restaurant> places) {
        this.places = places;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RestaurantListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_recyclerview_row, parent, false);
        return new RestaurantListViewHolder(inflatedView, showDetailListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListViewHolder holder, int position) {
        holder.bindRestaurant(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class RestaurantListViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = RestaurantListViewHolder.class.getSimpleName();
        private Restaurant restaurant;

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
        @BindView(R.id.restaurant_image)
        ImageView restaurantThumbnail;

        public RestaurantListViewHolder(@NonNull View itemView, ShowDetailListener showDetailListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> showDetailListener.showDetail(restaurant));
        }

        void bindRestaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            title.setText(restaurant.name);
            address.setText(restaurant.address);
            if (restaurant.isOpened != null) {
                if (restaurant.isOpened) {
                    openingTime.setText(itemView.getContext().getString(R.string.restaurant_opened));
                    openingTime.setTextColor(itemView.getContext().getColor(R.color.restaurant_opened));
                } else {
                    openingTime.setText(itemView.getContext().getString(R.string.restaurant_closed));
                    openingTime.setTextColor(itemView.getContext().getColor(R.color.restaurant_closed));
                }
            }
            workmatesNumber.setText(String.valueOf(restaurant.getInterestedWorkmates().size()));
            distance.setText((restaurant.distance != null) ? restaurant.distance + "m" : "");

            if (restaurant.rate == Restaurant.Rate.UNKNOWN) {
                star1.setAlpha(0f);
                star2.setAlpha(0f);
                star3.setAlpha(0f);
            }
            if (restaurant.rate == Restaurant.Rate.UNKNOWN || restaurant.rate == Restaurant.Rate.BAD)
                star2.setImageDrawable(itemView.getResources().getDrawable(R.drawable.star_gray, itemView.getContext().getTheme()));
            if (restaurant.rate != Restaurant.Rate.GOOD)
                star3.setImageDrawable(itemView.getResources().getDrawable(R.drawable.star_gray, itemView.getContext().getTheme()));

            Glide.with(itemView.getContext())
                    .load(restaurant.getPhotoUrl())
                    .centerCrop()
                    .placeholder(R.drawable.marker_restaurant_orange)
                    .into(restaurantThumbnail);
        }
    }
}
