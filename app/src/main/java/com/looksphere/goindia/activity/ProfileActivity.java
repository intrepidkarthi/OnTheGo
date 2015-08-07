package com.looksphere.goindia.activity;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.looksphere.goindia.R;
import com.looksphere.goindia.application.SwachhApplication;
import com.looksphere.goindia.controller.SharedPreferencesController;
import com.looksphere.goindia.customview.RoundedImageView;
import com.looksphere.goindia.model.UserProfileCounts;
import com.looksphere.goindia.utils.AppConstants;
import com.looksphere.goindia.utils.AppController;

public class ProfileActivity extends Activity {

    private ImageLoader imageLoader;
    RoundedImageView iconImage;
    TextView userName;
    TextView userLocation;
    TextView postedItemCount, completedItemCount, assignedItemCount, likesCount, commentsCount;
    UserProfileCounts count;
    SharedPreferencesController sharedPreferencesController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.activity_profile);

        initUI();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    private void initUI()
    {
        imageLoader = ImageLoader.getInstance();
        sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(this);
        iconImage = (RoundedImageView) findViewById(R.id.userIcon);
        imageLoader.displayImage(AppConstants.GRAPH_API_URL + sharedPreferencesController.getString("FB_USER_ID") + AppConstants.GRAPH_API_IMAGE_SUFFIX, iconImage);
        userName = (TextView) findViewById(R.id.userName);
        userName.setTypeface(SwachhApplication.roboticThin);
        userName.setText(sharedPreferencesController.getString("FB_FIRST_NAME") + " " + sharedPreferencesController.getString("FB_LAST_NAME"));

        //postedItemCount, completedItemCount, assignedItemCount, likesCount, commentsCount;
        postedItemCount = (TextView) findViewById(R.id.total_count);
        completedItemCount = (TextView) findViewById(R.id.total_completed);
        assignedItemCount = (TextView) findViewById(R.id.total_assigned);
        likesCount = (TextView) findViewById(R.id.total_likes);
        commentsCount = (TextView) findViewById(R.id.total_comments);

        userLocation = (TextView) findViewById(R.id.userLocation);
        userLocation.setTypeface(SwachhApplication.abelRegular);
        userLocation.setText(""+sharedPreferencesController.getString("locationAddress"));



        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, AppConstants.SERVER_HOST_ADDRESS + AppConstants.FETCH_USER_PROFILE, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        Log.d("clicked", response.toString());
                        count = new UserProfileCounts();
                        count.setAssignedDirtCnt(response.getString("assignedDirtCnt"));
                        count.setCommentsCnt(response.getString("commentsCnt"));
                        count.setCompletedDirtCnt(response.getString("completedDirtCnt"));
                        count.setLikesCnt(response.getString("likesCnt"));
                        count.setPostedDirtCnt(response.getString("postedDirtCnt"));

                        postedItemCount.setText(count.getPostedDirtCnt());
                        completedItemCount.setText(count.getCompletedDirtCnt());
                        assignedItemCount.setText(count.getAssignedDirtCnt());
                        likesCount.setText(count.getLikesCnt());
                        commentsCount.setText(count.getCommentsCnt());


                    }
                    catch(Exception e)
                    {

                    }


                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("", "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> actualHeader = new HashMap<String, String>();
                String apiKey = sharedPreferencesController.getString("APIKey");
                actualHeader.put("APIKey", apiKey);
                return actualHeader;
            }
        };

        // Adding request to volley request queue
        AppController.getInstance(getApplicationContext()).addToRequestQueue(req);





    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
