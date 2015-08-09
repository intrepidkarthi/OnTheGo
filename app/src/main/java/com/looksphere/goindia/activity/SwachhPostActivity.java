package com.looksphere.goindia.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cloudinary.Cloudinary;
import com.looksphere.goindia.R;
import com.looksphere.goindia.controller.SharedPreferencesController;
import com.looksphere.goindia.network.ConnectionDetector;
import com.looksphere.goindia.utils.AppConstants;
import com.looksphere.goindia.utils.AppController;
import com.looksphere.goindia.utils.CapturePhoto;
import com.looksphere.goindia.utils.GPSTracker;
import com.looksphere.goindia.utils.UtilityMethods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SwachhPostActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final int ACTION_REQUEST_GALLERY = 0;
    private final int ACTION_REQUEST_CAMERA = 1;
    private ImageView swachhPostImage;
    private FrameLayout imageLayout;
    private boolean imageSet = false;
    private String mCurrentPhotoPath;
    private TextView description;
    private String selectedSeverity = "MEDIUM";
    private TextView landmark;
    private Bitmap selectedBitmap;
    CapturePhoto capture;
    SharedPreferencesController sharedPreferencesController;
    private GPSTracker gps;
    private double latitude;
    private double longitude;
    private String location = "";
    private TextView locationData;
    ProgressDialog progressDialog;
    private Uri fileUri;
    private Bitmap bitmap;
    private Cloudinary cloudinary;
    private String cloudinaryImageId;
    private List<Address> listOfAddress = new ArrayList<Address>();
    // Capture image implementation
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_IMAGE_REQUEST_CODE = 50;
    private static final int CAMERA_CROP = 2;
    private static final int GALLERY_CROP = 3;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "onthego";
    private String imageURL = "";
    private boolean isImageUploading = false;
    private boolean isSignUpButtonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
       getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.activity_swachh_post);


//        final ActionBar ab = getActionBar();
//        if (ab != null) {
//            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
//            ab.setDisplayHomeAsUpEnabled(true);
//        }

        getLocation();
        initUI();
    }


    private void getLocation() {

        sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(this);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();

        if (isInternetPresent) {

            gps = new GPSTracker(this);

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

        } else {
            Toast.makeText(getApplicationContext(), "No Internet connection", Toast.LENGTH_LONG).show();
        }


        new GetLocation().execute(latitude, longitude);

    }




    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        switch (pos) {
            case 0:
                selectedSeverity = "HIGH";
                break;
            case 1:
                selectedSeverity = "MEDIUM";
                break;
            case 2:
                selectedSeverity = "LOW";
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    private void initUI() {

        //Cloudinary
        Map config = new HashMap();
        config.put("cloud_name", "yourcloudname");
        config.put("api_key", "yourkey");
        config.put("api_secret", "yoursecret");
        cloudinary = new Cloudinary(config);
        capture = new CapturePhoto(this);
        sharedPreferencesController = SharedPreferencesController.getSharedPreferencesController(this);
        swachhPostImage = (ImageView) findViewById(R.id.swachh_photo);
        imageLayout = (FrameLayout) findViewById(R.id.swachh_photo_layout);
        locationData = (TextView) findViewById(R.id.location_data);
        locationData.setSelected(true);
        imageLayout.setOnClickListener(this);

        selectImage(R.id.swachh_photo);
        description = (TextView) findViewById(R.id.description_details);
        landmark = (TextView) findViewById(R.id.landmark_details);

        Spinner spinner = (Spinner) findViewById(R.id.severity_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.severity_array, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.swachh_post, menu);
        return true;
    }


    private void postData() {
        new UploadImageTask().execute();
    }


    /**
     * {
     * "description":"Garbage Sample 2",
     * "imageUrl":"http://cdn.business2community.com/wp-content/uploads/2012/11/GarbagePile.jpg",
     * "severity":"HIGH",
     * "location":[12, 21],
     * "status":"OPEN",
     * "userId":"prasanna.venkataraman1",
     * "fbAccessToken":"TEST"
     * }
     */


    public Request post(String description, String imageUrl, String severity,
                        String location, String status, String userId, String fbAccessToken) {
        JSONObject params = null;
        try {
            params = new JSONObject();
            params.put("description", description);
            params.put("imageUrl", imageUrl);
            params.put("severity", severity);
            params.put("location", location);
            params.put("status", status);
            params.put("userId", userId);
            params.put("fbAccessToken", fbAccessToken);
        } catch (JSONException e) {
            Log.e("SwachhPostActivity", e.getMessage());
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.POST,
                AppConstants.ADD_NEW_POST,
                params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", "Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Error: " + error.getMessage());
            }
        });

        return req;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

//    /**
//     * A simple task that uploads an image from the local Image View
//     */
//    public class UploadImageTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(SwachhPostActivity.this);
//            progressDialog.setMessage("Posting new Swachh item...");
//            progressDialog.show();
//        }
//
//        JSONObject result;
//
//        protected String doInBackground(String... urls) {
//
//
//            Bitmap bitmap = ((BitmapDrawable) swachhPostImage.getDrawable()).getBitmap();
//            File f = null;
//
//            try {
//                //create a file to write bitmap data
//                f = new File(Environment.getExternalStorageDirectory() + File.separator + "swachh.png");
//                f.createNewFile();
//
//                //Convert bitmap to byte array
//                //Bitmap bitmap = your bitmap;
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
//                byte[] bitmapdata = bos.toByteArray();
//
//                //write the bytes in file
//                FileOutputStream fos = new FileOutputStream(f);
//                fos.write(bitmapdata);
//                fos.flush();
//                fos.close();
//            } catch (Exception e) {
//
//            }
//
//
//            try {
//                result = cloudinary.uploader().upload(f, Cloudinary.emptyMap());
//                System.out.println("response res " + result.toString());
//
//                cloudinaryImageId = result.getString("url");
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            } catch (JSONException e) {
//                Log.e("Cloudinary Image Upload", e.getMessage());
//                return null;
//            }
//
//
//            return cloudinaryImageId;
//        }

//        protected void onPostExecute(String image) {
//
//            // making fresh volley request and getting json
//
//            JSONObject requestObj = new JSONObject();
//            try {
//                requestObj.put("description", description.getText().toString());
//                requestObj.put("imageUrl", cloudinaryImageId);
//                requestObj.put("severity", selectedSeverity);
//                requestObj.put("landmark", landmark.getText().toString());
//                String latLong = sharedPreferencesController.getString("latLong");
//                requestObj.put("location", latLong);
//                requestObj.put("locationAddress", location);
//                // No Needed as we will get username from API Key itself
//                //requestObj.put("username","prasanna.venkataraman1");
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVER_HOST_ADDRESS + AppConstants.ADD_NEW_POST,
//                    //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVER_BASE_URL,
//                    requestObj, new Response.Listener<JSONObject>() {
//
//                @Override
//                public void onResponse(JSONObject response) {
//                    try
//                    {
//                        progressDialog.dismiss();
//                    }
//                    catch (Exception e)
//                    {
//
//                    }
//                    Log.d("Response", "swachh Response: " + response.toString());
//                    onBackPressed();
//                }
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//
//                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//                    Log.d("Error", "Error: " + error.getMessage());
//
//                }
//            }) {
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> actualHeader = new HashMap<String, String>();
//                    String apiKey = sharedPreferencesController.getString("APIKey");
//                    actualHeader.put("APIKey", apiKey);
//                    return actualHeader;
//                }
//            };
//
//
//            // Adding request to volley request queue
//            AppController.getInstance(getApplicationContext()).addToRequestQueue(req);
//
//
//            /**
//             *  {
//             "description":"Garbage Sample 2",
//             "imageUrl":"http://cdn.business2community.com/wp-content/uploads/2012/11/GarbagePile.jpg",
//             "severity":"HIGH",
//             "location":[12, 21],
//             "status":"OPEN",
//             "userId":"prasanna.venkataraman1",
//             "fbAccessToken":"TEST"
//             }
//             */
//
//        }
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.save:
                postData();
                //handleYourEvent();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.swachh_photo_layout:
                selectImage(id);
                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK) {
//            //ImageView imageView = (ImageView)findViewById(requestCode);
//            if (capture.getActionCode() == CapturePhoto.PICK_IMAGE) {
//                Uri targetUri = data.getData();
//                if (targetUri != null)
//                    capture.handleMediaPhoto(targetUri, swachhPostImage);
//            } else {
//                capture.handleCameraPhoto(swachhPostImage);
//            }
//        }
//    }

    private void selectImage(final int id) {
//        final CharSequence[] items = {"Click from Camera", "Choose from Gallery",
//                "Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(SwachhPostActivity.this);
//        builder.setTitle("");
//        builder.setCancelable(true);
//        builder.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (items[item].equals("Click from Camera")) {
//                    // take photo from camera
//                    capture.dispatchTakePictureIntent(CapturePhoto.SHOT_IMAGE, id);
//                } else if (items[item].equals("Choose from Gallery")) {
//                    // take photo from gallery
//                    capture.dispatchTakePictureIntent(CapturePhoto.PICK_IMAGE, id);
//                } else if (items[item].equals("Cancel")) {
//                    // close the dialog
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light));
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    try {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        // start the image capture Intent
                        if (CAMERA_CAPTURE_IMAGE_REQUEST_CODE == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                            intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Unable to take picture", Toast.LENGTH_SHORT).show();
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    try {
//                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(intent, GALLERY_IMAGE_REQUEST_CODE);

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        startActivityForResult(Intent.createChooser(intent,
                                "Complete action using"), GALLERY_IMAGE_REQUEST_CODE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Unable to choose picture", Toast.LENGTH_SHORT).show();
                    }


                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    cropCapturedImage(fileUri);
                }
            } else if (requestCode == GALLERY_IMAGE_REQUEST_CODE && resultCode == RESULT_OK &&
                    null != data) {


                InputStream stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                stream.close();

                String path = MediaStore.Images.Media.insertImage(SwachhPostActivity.this.getContentResolver(), bitmap, "Title", null);
                cropCapturedImage(Uri.parse(path));

            } else if (requestCode == CAMERA_CROP && resultCode == RESULT_OK &&
                    null != data) {
                //addImageTextFrame.setVisibility(View.GONE);
                Bundle extras = data.getExtras();
                selectedBitmap = extras.getParcelable("data");
                swachhPostImage.setImageBitmap(selectedBitmap);
                swachhPostImage.setTag(selectedBitmap);
                //Uploading image to cloudinary server
                //new UploadImageTask().execute(selectedBitmap);
            } else if (requestCode == GALLERY_CROP && resultCode == RESULT_OK &&
                    null != data) {
                //addImageTextFrame.setVisibility(View.GONE);
                Bundle extras = data.getExtras();
                selectedBitmap = extras.getParcelable("data");
                // Set The Bitmap Data To ImageView
                swachhPostImage.setImageBitmap(selectedBitmap);
                swachhPostImage.setTag(selectedBitmap);
                //Uploading image to cloudinary server
                //new UploadImageTask().execute(selectedBitmap);

            } else {
                // failed to capture image
//                ToastUtil.showToastCenter(SignUpActivity.this,
//                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            ToastUtil.showToastCenter(SignUpActivity.this,
//                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT);
        }



    }

    public void cropCapturedImage(Uri picUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(picUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, CAMERA_CROP);
    }


    /**
     * A simple task that uploads an image to cloudinary from the local Image View
     */
    public class UploadImageTask extends AsyncTask<Bitmap, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             progressDialog = new ProgressDialog(SwachhPostActivity.this);
             progressDialog.setMessage("Signing up...");
             progressDialog.show();
        }

        JSONObject result;
        File f = null;

        protected String doInBackground(Bitmap... bitmaps) {


            try {
                //isImageUploading = true;

                bitmap = selectedBitmap;
                //create a file to write bitmap data
                f = new File(Environment.getExternalStorageDirectory() + File.separator + "whistlr.png");
                f.createNewFile();

                //Convert bitmap to byte array
                //Bitmap bitmap = your bitmap;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();


                result = cloudinary.uploader().upload(f, Cloudinary.emptyMap());
                System.out.println("response res " + result.toString());

                cloudinaryImageId = result.getString("url");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            return cloudinaryImageId;
        }

        protected void onPostExecute(String image) {

            try{
                if( progressDialog!=null)
                    progressDialog.dismiss();
            }
            catch(Exception e)
            {

            }

            handleYourEvent();
        }
    }


    private void handleYourEvent()
    {
        // making fresh volley request and getting json

        JSONObject requestObj = new JSONObject();
        try {
            requestObj.put("description", description.getText().toString());
            requestObj.put("imageUrl", cloudinaryImageId);
            requestObj.put("severity", selectedSeverity);
            requestObj.put("landmark", landmark.getText().toString());
            String latLong = sharedPreferencesController.getString("latLong");
            requestObj.put("location", latLong);
            requestObj.put("locationAddress", location);
            // No Needed as we will get username from API Key itself
            //requestObj.put("username","prasanna.venkataraman1");


            Log.v("json call", "description " + description.getText().toString()+ "imageUrl "+ cloudinaryImageId + "severity "+ selectedSeverity+"landmark"+ landmark.getText().toString()+"location"+ latLong+"locationAddress"+ location);


        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVER_HOST_ADDRESS + AppConstants.ADD_NEW_POST,
                //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, AppConstants.SERVER_BASE_URL,
                requestObj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    progressDialog.dismiss();
                }
                catch (Exception e)
                {

                }
                Log.d("Response", "swachh Response: " + response.toString());
                onBackPressed();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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
        AppController.getInstance(getApplicationContext()).addToRequestQueue(req);


        /**
         *  {
         "description":"Garbage Sample 2",
         "imageUrl":"http://cdn.business2community.com/wp-content/uploads/2012/11/GarbagePile.jpg",
         "severity":"HIGH",
         "location":[12, 21],
         "status":"OPEN",
         "userId":"prasanna.venkataraman1",
         "fbAccessToken":"TEST"
         }
         */
    }


    private class GetLocation extends AsyncTask<Double, Void, List<Address>> {
        @Override
        protected List<Address> doInBackground(Double... params) {
            try {
                return UtilityMethods.getStringFromLocation(params[0], params[1]);
            } catch (Exception e) {
                e.printStackTrace();
                ;
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Address> addressList) {

            if (addressList != null && addressList.size() > 0) {
                location = addressList.get(0).getAddressLine(0).toString();
                sharedPreferencesController.putString("location", location );
                locationData.setText(location);
            }
        }
    }


}
