package com.looksphere.goindia.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.looksphere.goindia.R;
import com.looksphere.goindia.activity.LauncherActivity;
import com.looksphere.goindia.activity.MainActivity;
import com.looksphere.goindia.activity.SwachhPostActivity;
import com.looksphere.goindia.adapter.FeedListAdapter;
import com.looksphere.goindia.controller.SharedPreferencesController;
import com.looksphere.goindia.customview.EndlessListView;
import com.looksphere.goindia.model.CommentItem;
import com.looksphere.goindia.model.SwachhFeedItem;
import com.looksphere.goindia.utils.AppConstants;
import com.looksphere.goindia.utils.AppController;
import com.looksphere.goindia.utils.GPSTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener, EndlessListView.EndLessListener {

    private ImageView swachhButton;
    MainActivity activityHomeFragment;
    private View rootView;
    private int API_OFFSET = 1;
    private int API_LIMIT = 200;
    private ProgressBar progressBar;
    private EndlessListView listView;
    private FeedListAdapter listAdapter;
    private List<SwachhFeedItem> feedItems;
    private List<SwachhFeedItem> returnedfeedItems;
    private String URL_FEED = AppConstants.SERVER_HOST_ADDRESS + AppConstants.FETCH_NEAREST_DIRT;
    private String URL_SUFFIX1 = "&size=";
    private String URL_SUFFIX2 = "&start=";
    private double latitude, longitude;
    SharedPreferencesController sharedPreferencesController;

    private String TAG = HomeFragment.class.toString();

    public HomeFragment() {
    }

    /**
     * Returns a new instance of this fragment
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        GPSTracker gps = new GPSTracker(this.getActivity());

        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        URL_FEED += latitude + "," + longitude;

        String latLongValue = latitude + "," + longitude;

        sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(this.getActivity());
        sharedPreferencesController.getSharedPreferencesEditor();
        sharedPreferencesController.putString("latLong", latLongValue);

        //Log.d("api key", sharedPreferencesController.getString("APIKey"));

        // Dirt Feed code start


        listView = (EndlessListView) rootView.findViewById(R.id.endlesslist);

        feedItems = new ArrayList<SwachhFeedItem>();
        returnedfeedItems = new ArrayList<SwachhFeedItem>();

        listAdapter = new FeedListAdapter(this.getActivity(), feedItems);


        listView.setLoadingView(R.layout.loading_layout);
        listView.setListener(new EndlessListView.EndLessListener() {
            @Override
            public void loadData() {
                //Log.d("listview", " inside listener count " + URL_FEED + URL_SUFFIX1 + API_LIMIT + URL_SUFFIX2 + API_OFFSET);



                // making fresh volley request and getting json
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_FEED + URL_SUFFIX1 + API_LIMIT + URL_SUFFIX2 + API_OFFSET, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(TAG, "Response feed: " + response.toString());
                        if (response != null) {

                            API_OFFSET += API_LIMIT;
                            returnedfeedItems = new ArrayList<SwachhFeedItem>();
                            returnedfeedItems = parseJsonFeed(response);
                            if (returnedfeedItems != null) {
                              //  for (int i = 0; i < returnedfeedItems.size(); i++)
                                //    feedItems.add(returnedfeedItems.get(i));

                                listView.addNewData(returnedfeedItems);

                            }
                            //progressBar.setVisibility(View.GONE);
                            //listView.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

                // Adding request to volley request queue
                AppController.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
            }
        });

        listView.setAdapter(listAdapter);


        //Log.d("url work", URL_FEED + URL_SUFFIX1 + API_LIMIT + URL_SUFFIX2 + API_OFFSET);

        // making fresh volley request and getting json
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, URL_FEED + URL_SUFFIX1 + API_LIMIT + URL_SUFFIX2 + API_OFFSET, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response feed: " + response.toString());
                if (response != null) {


                    returnedfeedItems = parseJsonFeed(response);


                    if (returnedfeedItems != null) {
                        for (int i = 0; i < returnedfeedItems.size(); i++)
                            feedItems.add(returnedfeedItems.get(i));


                        // notify data changes to list adapater
                        listAdapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);

                        API_OFFSET += API_LIMIT;
                    }


                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to volley request queue
        AppController.getInstance(this.getActivity().getApplicationContext()).addToRequestQueue(req);
        // }

        // Dirt Feed close


        return rootView;
    }

    @Override
    public void loadData() {
       // Log.d("listview", " reached end " + URL_FEED + URL_SUFFIX1 + API_LIMIT + URL_SUFFIX2 + API_OFFSET);


        // making fresh volley request and getting json
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_FEED + URL_SUFFIX1 + API_LIMIT + URL_SUFFIX2 + API_OFFSET, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response feed: " + response.toString());
                if (response != null) {

                    API_OFFSET += API_LIMIT;
                    returnedfeedItems = new ArrayList<SwachhFeedItem>();
                    returnedfeedItems = parseJsonFeed(response);
                    if (returnedfeedItems != null) {
                        for (int i = 0; i < returnedfeedItems.size(); i++)
                            feedItems.add(returnedfeedItems.get(i));

                        listView.addNewData(feedItems);

                     }
                    //progressBar.setVisibility(View.GONE);
                    //listView.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to volley request queue
        AppController.getInstance(this.getActivity().getApplicationContext()).addToRequestQueue(request);

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private List<SwachhFeedItem> parseJsonFeed(JSONObject feedArray) {
        List<SwachhFeedItem> parsedItems = new ArrayList<SwachhFeedItem>();
        try {

            int length = feedArray.length();
            if (length == 0) {
                SwachhFeedItem item = new SwachhFeedItem();
                item.setName("No Content available");
                return null;
            }

            Iterator iterator = feedArray.keys();
            while (iterator.hasNext()) {
                String id = iterator.next().toString();
                JSONObject feedObj = (JSONObject) feedArray.get(id);

                SwachhFeedItem item = new SwachhFeedItem();
                item.setId(id);
                item.setName(feedObj.getString("userId"));

                // Image might be null sometimes
                String image = feedObj.isNull("imageUrl") ? null : feedObj
                        .getString("imageUrl");
                item.setImage(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(AppConstants.GRAPH_API_URL + feedObj.getString("userId") + AppConstants.GRAPH_API_IMAGE_SUFFIX);
                item.setTimeStamp(feedObj.getString("timestamp"));
                item.setSeverity(feedObj.getString("severity"));
                item.setLandmark(feedObj.getString("landmark"));
                item.setDescription(feedObj.getString("description"));
                item.setLatitude((Double) feedObj.getJSONArray("location").get(0));
                item.setLongitude((Double) feedObj.getJSONArray("location").get(1));
                item.setCommentsCount(feedObj.getInt("commentsCount"));
                item.setLikesCount(feedObj.getInt("likesCount"));
                item.setLiked(feedObj.getBoolean("isLiked"));
                item.setUserHandle(feedObj.getString("userHandle"));
//                item.setEmail(feedObj.getString("email"));
                item.setFbUserId(feedObj.getString("userId"));
                item.setLocationAddress(feedObj.getString("locationAddress"));
                JSONArray jsonArray = new JSONArray();

                ArrayList<CommentItem> commentItemArrayList = new ArrayList<CommentItem>();

                jsonArray = feedObj.getJSONArray("comments");


                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = new JSONObject();
                        CommentItem commentItem = new CommentItem();
                        jObj = jsonArray.getJSONObject(i);
                        commentItem.setComment(jObj.getString("comment"));
                        commentItem.setDirtId(jObj.getString("dirtId"));
                        commentItem.setTimestamp(jObj.getString("timestamp"));
                        commentItem.setUserId(jObj.getString("userId"));
                        //commentItem.setUserHandle(jObj.getString("userHandle"));
                        commentItemArrayList.add(commentItem);
                    }
                    item.setCommentItems(commentItemArrayList);
                }


                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                parsedItems.add(item);
            }

            return parsedItems;


        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void initUI() {
        swachhButton = (ImageView) rootView.findViewById(R.id.swachh);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        swachhButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        /**Dynamically*/
        //Rect bounds = progressBar.getIndeterminateDrawable().getBounds();
        //progressBar.setIndeterminateDrawable(getProgressDrawable());
        //progressBar.getIndeterminateDrawable().setBounds(bounds);
    }

//    private Drawable getProgressDrawable() {
//        Drawable progressDrawable = new FoldingCirclesDrawable.Builder(getActivity())
//                .colors(getProgressDrawableColors())
//                .build();
//
//        return progressDrawable;
//    }


    private int[] getProgressDrawableColors() {
        int[] colors = new int[10];
        colors[0] = getResources().getColor(R.color.color_2);
        colors[1] = getResources().getColor(R.color.color_5);
        colors[2] = getResources().getColor(R.color.color_8);
        colors[3] = getResources().getColor(R.color.color_7);
        return colors;
    }


    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.swachh:
                launchSwachhPostActivity();
                break;
        }
    }


    private void launchSwachhPostActivity() {
        //Call main activ ity
        Intent i = new Intent(getActivity(), SwachhPostActivity.class);
        startActivity(i);
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

            try {
                activityHomeFragment = ((MainActivity) activity);
//                activityHomeFragment.onSectionAttached(
//                        0);
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().finish();
                Intent newIntent = new Intent(getActivity(), LauncherActivity.class);
                startActivity(newIntent);

            }
        }


}
