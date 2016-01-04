package com.morgane.painauchocolat.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.morgane.painauchocolat.R;

/**
 * This activity displays to the user the credits of the application.
 */
public class CreditsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_credits, container, false);

        TextView creditsTextView = (TextView) view.findViewById(R.id.credits_text);
        creditsTextView.setText(R.string.credits_links);
        creditsTextView.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.action_credits);
    }
}
