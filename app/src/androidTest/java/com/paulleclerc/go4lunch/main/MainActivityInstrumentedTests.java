/*
 * MainActivityInstrumentedTests.java
 *   Go4Lunch
 *
 *   Updated by paulleclerc on 6/24/20 10:16 AM.
 *   Copyright © 2020 Paul Leclerc. All rights reserved.
 */

package com.paulleclerc.go4lunch.main;

/*public class MainActivityInstrumentedTests {
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public GrantPermissionRule grantPermissionRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    private MockWebServer mockWebServer = new MockWebServer();

    @Before
    public void setUp() throws IOException {
        HttpUrl baseUrl = mockWebServer.url("https://maps.googleapis.com/");
        mockWebServer.start();
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testOpenedFragmentIsMap() {
        onView(withId(R.id.map)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testOnClickOnSecondTabOpenedFragmentIsRestaurantList() {
        onView(withId(R.id.main_bottom_navigation_list)).perform(click());
        onView(withId(R.id.restaurant_recyclerview)).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void testOnClickOnThirdTabOpenFragmentIsWorkmatesList() {
        onView(withId(R.id.main_bottom_navigation_workmates)).perform(click());
        onView(withId(R.id.workmates_list)).check(matches(isCompletelyDisplayed()));
    }

   @Test
    public void testMapShouldContain20MarkersIfResponseWith20Results() {
        MockResponse mockResponse = null;
        //try {
            mockResponse = new MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody("{}");
            // FileUtils.readTestResourceFile("placesNearbySearchOK.json")
        //} catch (IOException e) {
            //e.printStackTrace();
            //fail();
        //}
        mockWebServer.enqueue(mockResponse);

        onView(withContentDescription("Jules Verne")).check(matches(isCompletelyDisplayed()));
    }
}*/
