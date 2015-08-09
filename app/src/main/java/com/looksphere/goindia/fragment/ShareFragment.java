package com.looksphere.goindia.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.looksphere.goindia.R;
import com.looksphere.goindia.activity.MainActivity;
import com.looksphere.goindia.adapter.FriendsAdapter;
import com.looksphere.goindia.controller.SharedPreferencesController;
import com.looksphere.goindia.model.TaggableFriends;
import com.looksphere.goindia.model.TaggableFriendsWrapper;

import java.util.ArrayList;
import java.util.List;


public class ShareFragment extends Fragment implements View.OnClickListener{

    MainActivity activityShareFragment;
    private SharedPreferencesController sharedPreferencesController;
    private String accessToken = "";
    //Session activeSession;
    private ProgressBar progressBar;
    private ListView listView;
    private FriendsAdapter friendsAdapter;
    String taggable_friends = "";
    private View rootView;
    //private UiLifecycleHelper uiHelper;
    TaggableFriendsWrapper taggableFriendsWrapper;
    int textlength = 0;
    private ArrayList<TaggableFriends> filteredFriends = new ArrayList<TaggableFriends>();
    private ArrayList<String> wholeData = new ArrayList<String>();
    private boolean canPresentShareDialog;
//    private GraphUser user;
//    private GraphPlace place;
//    private List<GraphUser> tags;
    private static Location CURRENT_LOCATION = null;
    private PendingAction pendingAction = PendingAction.NONE;
    private String sharingText = "Swachh calls you for action to show you care and clean your share. It is an initiative and a community platform to motivate people towards developing a cleaner India. It helps you to identify and fix the issues buy collaborating with your friends and also socially interested people nearby your place. Get help from people socially through this app https://play.google.com/store/apps/details?id=com.looksphere.goindia";
    private String sharingTwitterText = "Swachh calls you to clean your share on cleaner India! Connect here socially https://play.google.com/store/apps/details?id=com.looksphere.goindia";


    RelativeLayout facebookLayout;
    RelativeLayout twitterLayout;
    RelativeLayout whatsappLayout;
    RelativeLayout hikeLayout;
    RelativeLayout othersLayout;


    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }


    public ShareFragment() {
    }

    EditText searchBox;
    List<String> listFriends = new ArrayList<String>();

    /**
     * Returns a new instance of this fragment
     */
    public static ShareFragment newInstance() {
        ShareFragment fragment = new ShareFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_share, container, false);
        initUI();

        setHasOptionsMenu(true);
        return rootView;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Auto-generated method stub
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.launcher, menu);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle item selection
//        switch (item.getItemId()) {
//            case R.id.save:
//                //  Toast.makeText(getActivity(), "yo", Toast.LENGTH_SHORT).show();
//
////                FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(getActivity())
////                        .setLink("http://www.swachh.org")
////                        .setFriends(listFriends)
////                        .build();
////                uiHelper.trackPendingDialogCall(shareDialog.present());
//
//                //publishFeedDialog();
//
//                sendRequestDialog();
//
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }



    void initUI()
    {
        facebookLayout = (RelativeLayout) rootView.findViewById(R.id.share_layout_facebook);
        twitterLayout = (RelativeLayout) rootView.findViewById(R.id.share_layout_twitter);
        whatsappLayout = (RelativeLayout) rootView.findViewById(R.id.share_layout_whatsapp);
        hikeLayout = (RelativeLayout) rootView.findViewById(R.id.share_layout_hike);
        othersLayout = (RelativeLayout) rootView.findViewById(R.id.share_layout_others);

        facebookLayout.setOnClickListener(this);
        twitterLayout.setOnClickListener(this);
        whatsappLayout.setOnClickListener(this);
        hikeLayout.setOnClickListener(this);
        othersLayout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id)
        {
            case R.id.share_layout_facebook:
                publishFeedDialog();
                break;
            case R.id.share_layout_twitter:
                ShareTwitterIntent();
                break;
            case R.id.share_layout_whatsapp:
                ShareWhatsappIntent();
                break;
            case R.id.share_layout_hike:
                ShareHikeIntent();
                break;
            case R.id.share_layout_others:
                shareText();
                break;
        }

    }

    private void sendRequestDialog() {
        Bundle params = new Bundle();
        params.putString("message", "Lets make India a cleaner and stronger country! Join Swachh bharat mission today!");
        params.putString("data", "");

        params.putString("to",
                taggable_friends.substring(1, taggable_friends.length() - 1));



    }



    void ShareHikeIntent()
    {
        /**
         * Swachh calls you for action to show you care and clean your share. It is an initiative and a community platform to motivate people towards developing a cleaner India. It helps you to identify and fix the issues buy collaborating with your friends and also socially interested people nearby your place. Get help from people socially through this app https://play.google.com/store/apps/details?id=com.looksphere.goindia
         */
        PackageManager pm= getActivity().getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            PackageInfo info= pm.getPackageInfo("com.bsb.hike", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.bsb.hike");
            waIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
            startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Hike not Installed", Toast.LENGTH_SHORT)
                    .show();
            //download for example after dialog
            Uri uri = Uri.parse("market://details?id=com.bsb.hike");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        }
    }




    void ShareTwitterIntent()
    {
        /**
         * Swachh calls you for action to show you care and clean your share. It is an initiative and a community platform to motivate people towards developing a cleaner India. It helps you to identify and fix the issues buy collaborating with your friends and also socially interested people nearby your place. Get help from people socially through this app https://play.google.com/store/apps/details?id=com.looksphere.goindia
         */
        PackageManager pm= getActivity().getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            PackageInfo info= pm.getPackageInfo("com.twitter.android", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.twitter.android");
            waIntent.putExtra(Intent.EXTRA_TEXT, sharingTwitterText);
            startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Twitter not Installed", Toast.LENGTH_SHORT)
                    .show();
            //download for example after dialog
            Uri uri = Uri.parse("market://details?id=com.twitter.android");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        }
    }


    void ShareWhatsappIntent()
    {
        /**
         * Swachh calls you for action to show you care and clean your share. It is an initiative and a community platform to motivate people towards developing a cleaner India. It helps you to identify and fix the issues buy collaborating with your friends and also socially interested people nearby your place. Get help from people socially through this app https://play.google.com/store/apps/details?id=com.looksphere.goindia
         */
        PackageManager pm= getActivity().getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            PackageInfo info= pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, sharingText);
            startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
            //download for example after dialog
            Uri uri = Uri.parse("market://details?id=com.whatsapp");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        }
    }

    void shareText()
    {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Swachh calls you to take action towards clean India!");
        intent.putExtra(Intent.EXTRA_TEXT, sharingText);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    private void publishFeedDialog() {
        Bundle params = new Bundle();
        params.putString("name", "On The Go");
        params.putString("tags", taggable_friends);
        params.putString("caption", "Show you care, clean your share");
        params.putString("description", "On The Go is an initiative and platform to motivate people towards developing a cleaner India. It helps you to identify and fix the issues buy collaborating with your friends and also socially interested people nearby your place.");
        params.putString("link", "https://www.looksphere.com");
        params.putString("picture", "https://s3-ap-southeast-1.amazonaws.com/cleanindia/ic_launcher.png");


    }






    @Override
    public void onResume() {
        super.onResume();

    }

//    private Drawable getProgressDrawable() {
//        Drawable progressDrawable = new FoldingCirclesDrawable.Builder(getActivity())
//                .colors(getProgressDrawableColors())
//                .build();
//
//        return progressDrawable;
//    }

    public void AddToList(ArrayList<TaggableFriends> friendsWrapper) {
        TaggableFriendsWrapper newWrapper = new TaggableFriendsWrapper();
        newWrapper.setData(friendsWrapper);
        friendsAdapter = new FriendsAdapter(getActivity(), newWrapper);
        listView.setAdapter(friendsAdapter);
    }




    private int[] getProgressDrawableColors() {
        int[] colors = new int[4];
        colors[0] = getResources().getColor(R.color.flat_orange);
        colors[1] = getResources().getColor(R.color.flat_white);
        colors[2] = getResources().getColor(R.color.flat_green);
        colors[3] = getResources().getColor(R.color.blue);
        return colors;
    }




    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            activityShareFragment = ((MainActivity) activity);

        } catch (Exception e) {
            e.printStackTrace();
            getActivity().finish();
            Intent newIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(newIntent);

        }
    }

}
