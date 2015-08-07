package com.looksphere.goindia.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.looksphere.goindia.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContainerActivityFragment extends Fragment {

    public ContainerActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_container, container, false);
    }
}
