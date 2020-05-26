package com.paulleclerc.go4lunch.ui.restaurant_detail;

import android.util.Log;
import android.widget.LinearLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.paulleclerc.go4lunch.R;

public class RestaurantDetailActivity extends AppCompatActivity {
    private static final String TAG = RestaurantDetailActivity.class.getSimpleName();

    @BindView(R.id.call_layout)
    LinearLayout callLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();

        ButterKnife.bind(this);

        callLayout.setOnClickListener(v -> Log.d(TAG, "onCreate: Call"));
    }
}
