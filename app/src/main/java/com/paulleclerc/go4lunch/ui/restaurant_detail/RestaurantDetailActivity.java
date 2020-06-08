/*
 * RestaurantDetailActivity.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/8/20 4:54 PM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.ui.restaurant_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Restaurant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantDetailActivity extends AppCompatActivity {
    private static final String TAG = RestaurantDetailActivity.class.getSimpleName();
    public static final String KEY_RESTAURANT_EXTRA_SERIALIZABLE = "RESTAURANT_EXTRA_SERIALIZABLE";

    private RestaurantDetailViewModel viewModel;
    private Restaurant restaurant;
    @BindView(R.id.restaurant_detail_workmates_list)
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @BindView(R.id.restaurant_detail_title)
    TextView titleTextView;
    @BindView(R.id.restaurant_detail_address)
    TextView addressTextView;
    @BindView(R.id.restaurant_detail_background)
    ImageView backgroundImageView;

    @BindView(R.id.restaurant_detail_call_layout)
    LinearLayout callLayout;
    @BindView(R.id.restaurant_detail_call_image)
    ImageView callImageView;
    @BindView(R.id.restaurant_detail_call_text)
    TextView callTextView;
    @BindView(R.id.restaurant_detail_website_layout)
    LinearLayout websiteLayout;
    @BindView(R.id.restaurant_detail_website_image)
    ImageView websiteImageView;
    @BindView(R.id.restaurant_detail_website_text)
    TextView websiteTextView;
    @BindView(R.id.restaurant_detail_like_layout)
    LinearLayout likeLayout;
    @BindView(R.id.restaurant_detail_like_image)
    ImageView likeImageView;
    @BindView(R.id.restaurant_detail_like_text)
    TextView likeTextView;
    private RestaurantDetailWorkmatesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        this.restaurant = (Restaurant) getIntent().getSerializableExtra(KEY_RESTAURANT_EXTRA_SERIALIZABLE);
        if (restaurant == null) finish();

        viewModel = new ViewModelProvider(this).get(RestaurantDetailViewModel.class);
        viewModel.getPlaceDetail(restaurant).observe(this, this::setDetails);
        viewModel.getIsLiked(restaurant).observe(this, isLiked -> {
            likeTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            likeImageView.setImageDrawable(isLiked ? getResources().getDrawable(R.drawable.star) : getResources().getDrawable(R.drawable.star_border));
            likeImageView.setColorFilter(getResources().getColor(R.color.colorPrimary));
        });

        ButterKnife.bind(this);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new RestaurantDetailWorkmatesListAdapter();
        adapter.setWorkmates(restaurant.getInterestedWorkmates());
        recyclerView.setAdapter(adapter);

        titleTextView.setText(restaurant.name);
        addressTextView.setText(restaurant.address);


        Glide.with(this)
                .load(restaurant.getPhotoUrl())
                .placeholder(R.drawable.marker_restaurant_orange)
                .into(backgroundImageView);

        callLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + restaurant.getDetails().getPhone()));
            startActivity(intent);
        });

        websiteLayout.setOnClickListener(v -> {
            if (restaurant.getDetails().getWebsite() == null) return;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(restaurant.getDetails().getWebsite()));
            startActivity(browserIntent);
        });

        likeLayout.setOnClickListener(v -> viewModel.switchLike(restaurant));
    }

    private void setDetails(Restaurant.RestaurantDetails details) {
        restaurant.setDetails(details);

        if (details.getPhone() != null) {
            callImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
            callTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        if (details.getWebsite() != null) {
            websiteImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));
            websiteTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }
}
