package com.looksphere.goindia.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.looksphere.goindia.R;
import com.looksphere.goindia.activity.MainActivity;
import com.looksphere.goindia.application.SwachhApplication;

/**
 * Created by karthikeyan on 10/11/14.
 */
public class AboutUsFragment extends Fragment {

    private TextView about_one, about_two, about_three, about_four, about_five;
    View rootView;
    MainActivity activityAboutUsFragment;

    public AboutUsFragment() {
    }

    /**
     * Returns a new instance of this fragment
     */
    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_aboutus, container, false);
        initUI();
        return rootView;
    }


    private void initUI() {
        about_one = (TextView) rootView.findViewById(R.id.about1);
        about_two = (TextView) rootView.findViewById(R.id.about2);
        about_three = (TextView) rootView.findViewById(R.id.about3);
        about_four = (TextView) rootView.findViewById(R.id.about4);
        about_five = (TextView) rootView.findViewById(R.id.about5);
        about_one.setTypeface(SwachhApplication.abelRegular);
        about_two.setTypeface(SwachhApplication.abelRegular);
        about_three.setTypeface(SwachhApplication.abelRegular);
        about_four.setTypeface(SwachhApplication.abelRegular);
        about_five.setTypeface(SwachhApplication.roboticThin);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            activityAboutUsFragment = ((MainActivity) activity);

        } catch (Exception e) {
            e.printStackTrace();
            getActivity().finish();
            Intent newIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(newIntent);


        }


    }

}
