/*
 * RestaurantListFragmentTests.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/26/20 10:42 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.azimolabs.conditionwatcher.Instruction;
import com.jakewharton.espresso.OkHttp3IdlingResource;
import com.paulleclerc.go4lunch.R;
import com.paulleclerc.go4lunch.network.OkHttpProvider;
import com.paulleclerc.go4lunch.ui.main.MainActivity;
import com.paulleclerc.go4lunch.utils.FileReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.paulleclerc.go4lunch.utils.recyclerview.RecyclerViewItemCountAssertion.withItemCount;

public class RestaurantListFragmentTests {
    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class, true, false);
    @Rule
    public GrantPermissionRule grantPermissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    private MockWebServer mockWebServer = new MockWebServer();

    @Before
    public void setup() throws IOException {
        mockWebServer.start(8080);
        IdlingRegistry.getInstance().register(
                OkHttp3IdlingResource.create(
                        "okhttp",
                        OkHttpProvider.getOkHttpClient()
                ));
    }

    @Test
    public void test() throws Exception {
        String response = FileReader.readStringFromFile("placesNearbySearchOK.json");
        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) {
                return new MockResponse()
                        .setResponseCode(HttpURLConnection.HTTP_OK)
                        .setBody(response);
            }
        });

        activityRule.launchActivity(new Intent());
        onView(withId(R.id.main_bottom_navigation_list)).perform(click());

        ConditionWatcher.waitForCondition(new Instruction() {
            @Override
            public String getDescription() {
                return "Waiting for data to be displayed";
            }

            @Override
            public boolean checkCondition() {
                Activity activity = activityRule.getActivity();
                if (activity == null) return false;

                ProgressBar progressBar = activity.findViewById(R.id.restaurant_list_progressBar);

                if (progressBar == null) return false;
                else return progressBar.getVisibility() == View.GONE;
            }
        });
        onView(withId(R.id.restaurant_recyclerview)).check(withItemCount(1));
        System.out.println();
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }
}
