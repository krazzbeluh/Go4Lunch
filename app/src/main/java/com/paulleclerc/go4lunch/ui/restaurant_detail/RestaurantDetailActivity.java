package com.paulleclerc.go4lunch.ui.restaurant_detail;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Restaurant;

public class RestaurantDetailActivity extends AppCompatActivity {
    private static final String TAG = RestaurantDetailActivity.class.getSimpleName();

    private RestaurantDetailViewModel viewModel;
    private Restaurant restaurant;

    @BindView(R.id.restaurant_detail_title)
    TextView titleTextView;
    @BindView(R.id.restaurant_detail_address)
    TextView addressTextView;
    @BindView(R.id.restaurant_detail_background)
    ImageView backgroundImageView;

    @BindView(R.id.call_layout)
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        this.restaurant = (Restaurant) getIntent().getSerializableExtra(getString(R.string.restaurant_extra_identifier));
        if (restaurant == null) finish();

        viewModel = new ViewModelProvider(this).get(RestaurantDetailViewModel.class);
        viewModel.getPlaceDetail(restaurant).observe(this, this::setDetails);

        ButterKnife.bind(this);

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


    }

    private void setDetails(Restaurant.RestaurantDetails details) {
        restaurant.setDetails(details);

        if (details.getPhone() != null) {
            callImageView.setColorFilter(getColor(R.color.colorPrimary));
            callTextView.setTextColor(getColor(R.color.colorPrimary));
        }

        if (details.getWebsite() != null) {
            websiteImageView.setColorFilter(getColor(R.color.colorPrimary));
            websiteTextView.setTextColor(getColor(R.color.colorPrimary));
        }
    }
}
