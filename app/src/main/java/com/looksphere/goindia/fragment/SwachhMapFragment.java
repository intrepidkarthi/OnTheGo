package com.looksphere.goindia.fragment;

/**
 * Created by Karthi on 4/13/2014.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.looksphere.goindia.R;
import com.looksphere.goindia.activity.LauncherActivity;
import com.looksphere.goindia.activity.MainActivity;
import com.looksphere.goindia.controller.SharedPreferencesController;
import com.looksphere.goindia.utils.AppConstants;
import com.looksphere.goindia.utils.AppController;
import com.looksphere.goindia.utils.GPSTracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class SwachhMapFragment extends Fragment {

    private GoogleMap googleMap;
    private GPSTracker gps;
MainActivity activitySwachhMapFragment;
    private SharedPreferencesController sharedPreferencesController;
    private double latitude;
    private double longitude;

    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    private String TAG = "SwachhMapFragment :";

    /**
     * Returns a new instance of this fragment
     */
    public static SwachhMapFragment newInstance() {
        SwachhMapFragment fragment = new SwachhMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public SwachhMapFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(getActivity());
        gps = new GPSTracker(this.getActivity());

        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            sharedPreferencesController.putString("latitude", String.valueOf(latitude));
            sharedPreferencesController.putString("longitude", String.valueOf(longitude));


        } else {

            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        initilizeMap();


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();

                // Setting Dialog Title
                alertDialog.setTitle("Confirmation Dialog");

                // Setting Dialog Message
                alertDialog.setMessage("View full details ?");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.tick);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed

                        Toast.makeText(getActivity().getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
//                        Fragment fragment = HomeFragment.newInstance();
//                        FragmentManager fragmentManager = getFragmentManager();
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.container, fragment)
//                                .commit();

                    }
                });

                // Showing Alert Message
                alertDialog.show();


                return false;
            }
        });


         return rootView;
    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((SupportMapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }

            addMarkerToMap(latitude, longitude, "You are HERE !!!");

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitude, longitude)).zoom(10).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            populateMapWithData(latitude, longitude);
        }
    }

    private void addMarkerToMap(double latitude, double longitude, String text){
        MarkerOptions currLocation = new MarkerOptions().position(new LatLng(latitude, longitude)).title(text);
        googleMap.addMarker(currLocation);
    }

    private void populateMapWithData(double latitude, double longitude){

        String url = AppConstants.SERVER_HOST_ADDRESS+"fetchnearestdirt?location="+latitude+","+longitude;
        makeJsonObjReq(url);

    }

    private void makeJsonObjReq(String url) {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        //Volley.newRequestQueue(this.getActivity().getApplicationContext()).add(req);

        AppController.getInstance(this.getActivity().getApplicationContext()).addToRequestQueue(req);

        // Adding request to request queue
//        getInstance().addToRequestQueue(jsonObjReq,
//                tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    private void parseJsonFeed(JSONObject feedArray){
        try {

            Iterator iterator = feedArray.keys();
            while (iterator.hasNext()) {
                String id = iterator.next().toString();
                JSONObject object = (JSONObject) feedArray.get(id);
                JSONArray locationDetails = (JSONArray)object.get("location");
                String description = object.getString("description");
                addMarkerToMap(locationDetails.getDouble(0), locationDetails.getDouble(1), description);
            }

        }catch(Exception e){

            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            activitySwachhMapFragment = ((MainActivity) activity);
//            activitySwachhMapFragment.onSectionAttached(
//                    1);
        } catch (Exception e) {
            e.printStackTrace();
            getActivity().finish();
            Intent newIntent = new Intent(getActivity(), LauncherActivity.class);
            startActivity(newIntent);

        }
    }
}
