package com.looksphere.goindia.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.looksphere.goindia.R;
import com.looksphere.goindia.activity.MainActivity;

import java.util.List;

public class EmailFragment extends Fragment {

    MainActivity activityEmailFragment;

    public EmailFragment() {
    }

    /**
     * Returns a new instance of this fragment
     */
    public static EmailFragment newInstance() {
        EmailFragment fragment = new EmailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_email, container, false);
        startActivity(createEmailIntent("karthik@bigo.in", "Suggestion for Swachh Bharat", "Hi,"));
        return rootView;
    }

    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, "karthik@bigo.in");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Suggestion for Swachh Bharat");
        intent.putExtra(Intent.EXTRA_TEXT, "Hi,");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public Intent createEmailIntent(final String toEmail,
                                           final String subject,
                                           final String message) {
        Intent sendTo = new Intent(Intent.ACTION_SENDTO);
        String uriText = "mailto:" + Uri.encode(toEmail) +
                "?subject=" + Uri.encode(subject) +
                "&body=" + Uri.encode(message);
        Uri uri = Uri.parse(uriText);
        sendTo.setData(uri);

        List<ResolveInfo> resolveInfos =
                getActivity().getPackageManager().queryIntentActivities(sendTo, 0);

        // Emulators may not like this check...
        if (!resolveInfos.isEmpty()) {
            return sendTo;
        }

        // Nothing resolves send to, so fallback to send...
        Intent send = new Intent(Intent.ACTION_SEND);

        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_EMAIL,
                new String[]{toEmail});
        send.putExtra(Intent.EXTRA_SUBJECT, subject);
        send.putExtra(Intent.EXTRA_TEXT, message);

        return Intent.createChooser(send, "Your Title Here");
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            activityEmailFragment = ((MainActivity) activity);

        } catch (Exception e) {
            e.printStackTrace();
            getActivity().finish();
            Intent newIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(newIntent);

        }
    }
}
