package com.looksphere.goindia.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cloudinary.Cloudinary;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.looksphere.goindia.R;
import com.looksphere.goindia.controller.SharedPreferencesController;
import com.looksphere.goindia.network.ConnectionDetector;
import com.looksphere.goindia.utils.AppConstants;
import com.looksphere.goindia.utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class LoginSignUpActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final List<String> PERMISSIONS = Arrays.asList("public_profile", "email", "user_friends");
    private LoginButton fbLoginButton;
    private Button signInButton;
    private SignInButton gplusButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    private SharedPreferencesController sharedPreferencesController;
    private AccessToken accessToken;
    ProfileTracker profileTracker;
    private Profile profile;
    private String imageURL = "";
    private boolean isImageUploading = false;
    private boolean isSignUpButtonClicked = false;
    private Uri fileUri;
    private Bitmap bitmap;
    private Cloudinary cloudinary;
    private String cloudinaryImageId;


    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "LoginButtonActivity";
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_signup);
        initUI();
    }

    private void initUI() {

        //preference initialization
        sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(this);

        //Google plus login
        gplusButton = (SignInButton) findViewById(R.id.gplus_login);
        // Initializing google plus api client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
        //  gplusButton.setStyle(200, R.style.SignInButton);
        gplusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGplus();
            }
        });


        callbackManager = CallbackManager.Factory.create();
        //Facebook login
        fbLoginButton = (LoginButton) findViewById(R.id.fblogin);

        fbLoginButton.setReadPermissions(PERMISSIONS);

        // Callback registration
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.v(TAG, "permissions " + loginResult.getRecentlyGrantedPermissions().toString());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.

                // If the access token is available already assign it.
                accessToken = AccessToken.getCurrentAccessToken();
                sharedPreferencesController.putString("FB_ACCESS_TOKEN", accessToken.getToken());

            }
        };


       // if (accessToken != null)
         //   Log.v(TAG, "accessToken " + accessToken.getUserId() + "   " + accessToken.getToken() + "  " + accessToken.getApplicationId());

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                Log.v(TAG, "profile details " + currentProfile.getFirstName() + "  " + currentProfile.getLastName() + "  " + currentProfile.getProfilePictureUri(200, 200));

                sharedPreferencesController.putString("FB_FIRST_NAME", currentProfile.getFirstName());
                sharedPreferencesController.putString("FB_LAST_NAME", currentProfile.getLastName());
                sharedPreferencesController.putString("FB_EMAIL", "intrepidkarthi@gmail.com");
                sharedPreferencesController.putString("FB_HANDLE", currentProfile.getName());
                sharedPreferencesController.putString("FB_USER_ID", currentProfile.getId());

                persistUserInfoInServer(accessToken.getToken(), currentProfile.getName());
                //Call Launcher activity
                launchMainActivity();



            }
        };


        //Own login
        signInButton = (Button) findViewById(R.id.login_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private void persistUserInfoInServer(String accessToken, String handle) {

        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("fbAccessToken",accessToken);
            requestObj.put("fbUserHandle",handle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.POST, AppConstants.SERVER_HOST_ADDRESS+ AppConstants.SIGNUP,
                requestObj, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", "Response: " + response.toString());
                try {
                    Object obj = response.getString("APIKey");
                    String apiKey = "";
                    if (obj != null){
                        apiKey = obj.toString();
                        SharedPreferencesController sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(LoginSignUpActivity.this);
                        sharedPreferencesController.getSharedPreferencesEditor();
                        sharedPreferencesController.putString("APIKey",apiKey);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Error", "Error: " + error.getMessage());
            }
        });


        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
        //Volley does retry for you if you have specified the policy.
        req.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        // Adding request to volley request queue
        AppController.getInstance(getApplicationContext()).addToRequestQueue(req);

    }





    private void callSignUpAPI() {

        /**
         { "firstName":"HelloPrasanna", "lastName":"Test", "emailId":"test@gmail.com",
         "phoneNumber":"+919742381630", "profileUrl":"http://google.com", "deviceId":"123456",
         "pushId":"1234", "network":"VODAFONE", "accessToken":"1234", "loginType":"FACEBOOK" }
         */
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        if (cd.isConnectingToInternet()) {
//            YourDealsService.getService().userSignUp(new SignUpRequest("HelloPrasanna","Test", "test@gmail.com",
//                    "+919742381630","http://google.com", "123456",
//                    "1234", "VODAFONE", "1234", "FACEBOOK"), new Callback<SignUpResponse>() {
//                @Override
//                public void success(SignUpResponse signUpResponse, Response response) {
//                    Log.d(TAG, response.getStatus() + " " + signUpResponse.getUserId());
//
//                    if (response.getStatus() == 200) {
//
//                        sharedPreferencesController.putString("userId", signUpResponse.getUserId());
//
//                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                        intent.putExtra("userId", signUpResponse.getUserId());
//                        startActivity(intent);
//                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                        finish();
//                    }
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//                    Log.d(TAG, "ERROR : " + error.toString());
//                }
//            });
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            Toast.makeText(getApplicationContext(), "yo", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == RC_SIGN_IN) {
            if (resultCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //AppEventsLogger.activateApp(this);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        // Get user's information
        getProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    /**
     * Sign-in into google
     */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            // resolveSignInError();
        }
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }


    /**
     * Fetching user's information name, email, profile pic
     */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.v(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

//                txtName.setText(personName);
//                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + 300;

                //new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

























































    private final String PENDING_ACTION_BUNDLE_KEY = "com.facebook.samples.hellofacebook:PendingAction";
    private LoginButton loginButton;
    private PendingAction pendingAction = PendingAction.NONE;
    private ViewGroup controlsContainer;


    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }






//    private void updateUI() {
//        Session session = Session.getActiveSession();
//        boolean enableButtons = (session != null && session.isOpened());
//
//        if (user != null) {
//            System.out.println("comes inside access get user data " +session.getAccessToken()+"  "+ user.getId() + " " + user.getFirstName());
//            SharedPreferencesController sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(LoginSignUpActivity.this);
//            sharedPreferencesController.getSharedPreferencesEditor();
//            sharedPreferencesController.putString("FB_ACCESS_TOKEN", session.getAccessToken());
//            sharedPreferencesController.putString("FB_FIRST_NAME", user.getFirstName());
//            sharedPreferencesController.putString("FB_LAST_NAME", user.getLastName());
//            sharedPreferencesController.putString("FB_EMAIL", (String) user.getProperty("email"));
//            sharedPreferencesController.putString("FB_HANDLE", user.getName());
//            sharedPreferencesController.putString("FB_USER_ID", user.getId());
//
//            persistUserInfoInServer(session.getAccessToken(), user.getName());
//            //Call Launcher activity
//            launchMainActivity();
//
//
//        } else {
//            System.out.println("no fuck");
//        }
//    }



    private void launchMainActivity() {
        //Call main activ ity
        Intent i = new Intent(LoginSignUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;


    }



}
