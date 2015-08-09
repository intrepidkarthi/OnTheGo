package com.looksphere.goindia.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.looksphere.goindia.R;
import com.looksphere.goindia.activity.MainActivity;
import com.looksphere.goindia.activity.SplashActivity;
import com.looksphere.goindia.controller.SharedPreferencesController;
import com.looksphere.goindia.utils.AppConstants;
import com.looksphere.goindia.utils.AppController;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends Fragment {


    private MainActivity activitySettingsFragment;
    private TextView logoutButton;
    private View rootView;
    private PackageManager packageManager;
    private PackageInfo pInfo;
    private String version;
    private SharedPreferencesController sharedPreferencesController;
    private TextView versionText;
    private TextView helpText;

    public SettingsFragment() {
    }

    /**
     * Returns a new instance of this fragment
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        initUI();
        sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(getActivity());
        return rootView;
    }


    private void initUI() {
        logoutButton = (TextView) rootView.findViewById(R.id.logout);
        versionText = (TextView) rootView.findViewById(R.id.version);
        helpText = (TextView) rootView.findViewById(R.id.help);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //callFacebookLogout(getActivity());
                clearPreferences() ;
                launchSplashActivity();
            }
        });

        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = pInfo.versionName;
            versionText.setText("App version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        helpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.looksphere.com"));
                startActivity(browserIntent);
            }
        });


    }


//    /**
//     * Logout From Facebook
//     */
//    public void callFacebookLogout(Context context) {
//
//        clearSwachhSession();
//        Session session = Session.getActiveSession();
//        if (session != null) {
//
//            if (!session.isClosed()) {
//                session.closeAndClearTokenInformation();
//                //clear your preferences if saved
//                clearPreferences();
//                launchSplashActivity();
//
//
//            }
//        } else {
//
//            session = new Session(context);
//            Session.setActiveSession(session);
//
//            session.closeAndClearTokenInformation();
//            //clear your preferences if saved
//            clearPreferences();
//            launchSplashActivity();
//        }
//
//    }

    private void clearSwachhSession() {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVER_HOST_ADDRESS + "/protected/signout",
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getActivity().getApplicationContext(), "Logged out successfully !!!", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("Error", "Error: " + error.getMessage());

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
        AppController.getInstance(getActivity().getApplicationContext()).addToRequestQueue(req);
    }


    private void launchSplashActivity() {
        //Call main activ ity
        Intent i = new Intent(getActivity(), SplashActivity.class);
        startActivity(i);
        getActivity().finish();
        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    private void clearPreferences() {

        sharedPreferencesController.getSharedPreferencesEditor();
        sharedPreferencesController.putString("FB_ACCESS_TOKEN", "");
        sharedPreferencesController.putString("FB_FIRST_NAME", "");
        sharedPreferencesController.putString("FB_LAST_NAME", "");
        sharedPreferencesController.putString("FB_EMAIL", "");
        sharedPreferencesController.putString("FB_HANDLE", "");
        sharedPreferencesController.putString("FB_USER_ID", "");
        sharedPreferencesController.putString("APIKey", "");
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            activitySettingsFragment = ((MainActivity) activity);

        } catch (Exception e) {
            e.printStackTrace();
            getActivity().finish();
            Intent newIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(newIntent);

        }
    }
}
