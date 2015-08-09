package com.looksphere.goindia.activity;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.looksphere.goindia.R;

public class SwachhMapActivity extends Activity {


    static double latitude;
    static double longitude;
    public static  TextView addressTextview;
    public static  TextView descriptionTextview;
    public static TextView landmarkTextview;
    static String data = "India";
    public  static String addressReceived = "";
    public  static String descriptionReceived = "";
    public  static String landmarkReceived = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        setContentView(R.layout.activity_swachh_map);



        if(getIntent()!=null)
        {
            latitude = getIntent().getDoubleExtra("latitude", 12.9432644 );
            longitude = getIntent().getDoubleExtra("longitude", 77.6294053);
            addressReceived = getIntent().getExtras().getString("address");
            descriptionReceived = getIntent().getExtras().getString("description");
            landmarkReceived = getIntent().getExtras().getString("landmark");


        }


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    void initUI()
    {
       }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.swachh_map, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private GoogleMap googleMap;
        double currLatitude;
        double currLongitude;

        public PlaceholderFragment() {
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_swachh_map, container, false);
            initilizeMap();

            addressTextview = (TextView) rootView.findViewById(R.id.location_data);
            descriptionTextview = (TextView) rootView.findViewById(R.id.issue_description);
            landmarkTextview = (TextView) rootView.findViewById(R.id.landmark_details);

            addressTextview.setText(addressReceived);
            descriptionTextview.setText(descriptionReceived);
            landmarkTextview.setText(landmarkReceived);


            return rootView;
        }

        private void initilizeMap() {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.map)).getMap();

                // check if map is created successfully or not
                if (googleMap == null) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                            .show();
                }

                addMarkerToMap(latitude, longitude, "Swachh this location!");

                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(latitude, longitude)).zoom(11).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            }
        }

        private void addMarkerToMap(double latitude, double longitude, String text){
            MarkerOptions currLocation = new MarkerOptions().position(new LatLng(latitude, longitude)).title(text);
            googleMap.addMarker(currLocation);
        }



    }
}
