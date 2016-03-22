package com.morgane.painauchocolat.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.activities.BringerActivity;
import com.morgane.painauchocolat.model.Contributor;
import com.morgane.painauchocolat.utils.Constant;

/**
 * This class is the home page of the application. It allows to access the main features
 * of the application.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    /**
     * The view containing the name of the current bringer.
     */
    private TextView mCurrentBringerTextView;

    /**
     * The button to find a new bringer.
     */
    private Button mFindBringerButton;

    /**
     * The button to select the bringer.
     */
    private Button mValidateBringerButton;

    /**
     * The button to ask for another bringer.
     */
    private Button mAnotherBringerButton;

    /**
     * The view displaying the information message when there is no contributor registered.
     */
    private TextView mNoContributorTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        mCurrentBringerTextView = (TextView) view.findViewById(R.id.bringer_name);
//        mFindBringerButton = (Button) view.findViewById(R.id.bringer_new);
//        mValidateBringerButton = (Button) view.findViewById(R.id.bringer_validate);
//        mAnotherBringerButton = (Button) view.findViewById(R.id.bringer_another);
//        mNoContributorTextView = (TextView) view.findViewById(R.id.home_no_contributor);

//        mFindBringerButton.setOnClickListener(this);
//        mValidateBringerButton.setOnClickListener(this);
//        mAnotherBringerButton.setOnClickListener(this);

        refreshView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.app_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.home_find_bringer:
//                Intent intent = new Intent(getActivity(), BringerActivity.class);
//                startActivityForResult(intent, Constant.REQUEST_CODE_FIND_BRINGER);
//                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK
                && (requestCode == Constant.REQUEST_CODE_CREATE_CONTRIBUTOR
                    || requestCode == Constant.REQUEST_CODE_FIND_BRINGER)) {
            refreshView();
        }
    }

    /**
     * Refresh the view with the "find a contributor" button if there is contributor registered
     * or a help message if there is not.
     */
    private void refreshView() {
        // Find the min session number and register it
        int minSessionNumber = Contributor.getMinimumSessionNumber();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.edit().putInt(Constant.PREFERENCES_MIN_SESSION_NUMBER, minSessionNumber).commit();

        // If there is no contributor registered, display the help message
        if (!Contributor.isThereContributors()) {
//            mFindBringerButton.setVisibility(View.GONE);
//            mNoContributorTextView.setVisibility(View.VISIBLE);
//            mCurrentBringerTextView.setVisibility(View.INVISIBLE);

        } else {
//            mFindBringerButton.setVisibility(View.VISIBLE);
//            mNoContributorTextView.setVisibility(View.GONE);

            // If there is a current bringer, display it
            long currentBringerId = preferences.getLong(Constant.PREFERENCES_CURRENT_BRINGER, -1);
            if (currentBringerId != -1) {
                Contributor currentBringer = Contributor.getContributorById(currentBringerId);

                if (currentBringer != null) {
                    mCurrentBringerTextView.setVisibility(View.VISIBLE);

                    int textId;
                    if (preferences.getInt(Constant.PREFERENCES_BREAKFAST_DAY, 0) == 0) {
                        textId = R.string.home_current_bringer_anyway;
                    } else {
                        textId = R.string.home_current_bringer_week;
                    }

                    mCurrentBringerTextView.setText(getString(textId, currentBringer.getName()));

                } else {
                    mCurrentBringerTextView.setVisibility(View.INVISIBLE);
                }

            } else {
                mCurrentBringerTextView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
