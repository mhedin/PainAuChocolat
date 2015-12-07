package com.morgane.painauchocolat.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.model.Contributor;
import com.morgane.painauchocolat.utils.Constant;

/**
 * This class displays the person who will bring the breakfast the day after. The user can accept
 * the random choice of person or ask for another one who hasn't bring the breakfast in this session
 * yet.
 */
public class BringerActivity extends AppCompatActivity {

    /**
     * This contributor selected to be the bringer.
     */
    private Contributor mBringer;

    /**
     * The field in which the name of the contributor selected is displayed.
     */
    private TextView mBringerTextView;

    /**
     * The minimum number of the session, which corresponds to the current session so the onein which contributor must be searched.
     */
    private int mMinSessionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bringer);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mMinSessionNumber = preferences.getInt(Constant.PREFERENCES_MIN_SESSION_NUMBER, 0);

        mBringer = Contributor.getRandomBringer(mMinSessionNumber);

        mBringerTextView = (TextView) findViewById(R.id.bringer_name);
        if (mBringer != null) {
            mBringerTextView.setText(mBringer.name);
        }
    }

    public void validateBringer(View view) {
        mBringer.sessionNumber = mMinSessionNumber + 1;
        mBringer.save();
        finish();
    }

    public void findAnotherBringer(View view) {
        mBringer = Contributor.getRandomBringer(mMinSessionNumber);
        mBringerTextView.setText(mBringer.name);
    }
}
