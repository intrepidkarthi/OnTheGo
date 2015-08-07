package com.looksphere.goindia.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.looksphere.goindia.R;
import com.looksphere.goindia.adapter.CommentsAdapter;
import com.looksphere.goindia.controller.SharedPreferencesController;
import com.looksphere.goindia.model.CommentItem;
import com.looksphere.goindia.utils.AppConstants;
import com.looksphere.goindia.utils.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentsActivity extends Activity {

    private ArrayList<CommentItem> commentList;
    private ListView listView;
    ImageView sendButton;
    EditText myCommentText;
    String strComment = "";
    String feedId;
    CommentsAdapter commentsAdapter;
    SharedPreferencesController sharedPreferencesController;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getActionBar().setDisplayHomeAsUpEnabled(true);
  //      getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.activity_comments);


        if (getIntent() != null) {

            if (getIntent().getBooleanExtra("data", false)) {
                feedId = getIntent().getStringExtra("feedId");
                commentList = new ArrayList<CommentItem>();
                commentList.addAll((ArrayList<CommentItem>) getIntent().getSerializableExtra("comments"));
                Log.d("comments ", commentList.size() + "");
            } else {
                feedId = getIntent().getStringExtra("feedId");
            }


        }

        initUI();

    }


    private void initUI() {
        listView = (ListView) findViewById(R.id.listview);
        sendButton = (ImageView) findViewById(R.id.send);
        myCommentText = (EditText) findViewById(R.id.mycomment);

        sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(this);
        if (commentList != null && commentList.size() > 0) {
            commentsAdapter = new CommentsAdapter(getApplicationContext(), commentList);
            listView.setAdapter(commentsAdapter);
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strComment = myCommentText.getText().toString();
                progressDialog = new ProgressDialog(CommentsActivity.this);
                progressDialog.setMessage("Adding comment...");
                progressDialog.show();

                Log.d("comments ", "feed " + feedId + "   " + strComment);
                JSONObject requestObj = new JSONObject();
                try {
                    requestObj.put("dirtId", feedId);
                    requestObj.put("comment", strComment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVER_HOST_ADDRESS + AppConstants.COMMENT_ON_POST, requestObj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            Log.d("clicked", response.toString());


                            callCommentsAPI();

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("", "Error: " + error.getMessage());
                        finish();
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
        });


    }


    void callCommentsAPI() {
        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("dirtId", feedId);
           // requestObj.put("comment", strComment);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonArrayRequest request = new JsonArrayRequest(AppConstants.SERVER_HOST_ADDRESS+AppConstants.FETCH_COMMENTS_BYID + feedId, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                if(commentList!= null)
                    commentList.clear();

                try {
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObj = new JSONObject();
                            CommentItem commentItem = new CommentItem();
                            jObj =  jsonArray.getJSONObject(i);
                            commentItem.setComment(jObj.getString("comment"));
                            commentItem.setDirtId(jObj.getString("dirtId"));
                            commentItem.setTimestamp(jObj.getString("timestamp"));
                            commentItem.setUserId(jObj.getString("userId"));
                            //commentItem.setUserHandle(jObj.getString("userHandle"));
                            commentList.add(commentItem);
                        }
                        Log.d("comments size from all fetched", commentList.size() + "");
                    }

                }
                catch(Exception e)
                {

                }

                if (commentList != null && commentList.size() > 0) {
                    commentsAdapter = new CommentsAdapter(getApplicationContext(), commentList);
                    listView.setAdapter(commentsAdapter);
                }


                try {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Comment added successfully", Toast.LENGTH_SHORT).show();
                    //finish();
                } catch (Exception e) {

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });


        // Adding request to volley request queue
        AppController.getInstance(getApplicationContext()).addToRequestQueue(request);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.comments, menu);
        return true;
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
