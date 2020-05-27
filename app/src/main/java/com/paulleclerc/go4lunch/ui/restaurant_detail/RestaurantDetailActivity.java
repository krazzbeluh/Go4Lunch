package com.paulleclerc.go4lunch.ui.restaurant_detail;

import android.media.Image;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.model.Restaurant;

public class RestaurantDetailActivity extends AppCompatActivity {
    private static final String TAG = RestaurantDetailActivity.class.getSimpleName();

    private Restaurant restaurant;

    @BindView(R.id.restaurant_detail_title)
    TextView titleTextView;
    @BindView(R.id.restaurant_detail_address)
    TextView addressTextView;
    @BindView(R.id.call_layout)
    LinearLayout callLayout;
    @BindView(R.id.restaurant_detail_background)
    ImageView backgroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        this.restaurant = (Restaurant) getIntent().getSerializableExtra(getString(R.string.restaurant_extra_identifier));
        if (restaurant == null) finish();

        ButterKnife.bind(this);

        titleTextView.setText(restaurant.name);
        addressTextView.setText(restaurant.address);


        Glide.with(this)
                .load(restaurant.getPhotoUrl())
                .placeholder(R.drawable.marker_restaurant_orange)
                .into(backgroundImageView);

        callLayout.setOnClickListener(v -> Log.d(TAG, "onCreate: Call"));
    }
}
