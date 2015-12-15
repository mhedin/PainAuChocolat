package com.morgane.painauchocolat.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.model.Contributor;
import com.morgane.painauchocolat.utils.Constant;

/**
 * This class displays the person who will bring the breakfast the day after. The user can accept
 * the random choice of person or ask for another one who hasn't bring the breakfast in this session
 * yet.
 */
public class BringerActivity extends AppCompatActivity implements View.OnClickListener {

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

    /**
     * The button used to validate the current bringer.
     */
    private Button mValidateButton;

    /**
     * The button used to find another bringer.
     */
    private Button mAnotherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bringer);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mMinSessionNumber = preferences.getInt(Constant.PREFERENCES_MIN_SESSION_NUMBER, 0);

        mValidateButton = (Button) findViewById(R.id.bringer_validate);
        mValidateButton.setOnClickListener(this);

        mAnotherButton = (Button) findViewById(R.id.bringer_another);
        mAnotherButton.setOnClickListener(this);

        mBringerTextView = (TextView) findViewById(R.id.bringer_name);

        findABringer();
    }

    /**
     * Generate a new bringer, different from the previous one if the user clicks on the
     * "another bringer" button, with a little waiting time symbolized by points.
     */
    public void findABringer() {
        mValidateButton.setEnabled(false);
        mAnotherButton.setEnabled(false);

        Contributor newBringer;
        do {
            newBringer = Contributor.getRandomBringer(mMinSessionNumber);

        } while(mBringer != null && Contributor.getContributors().size() > 1 && mBringer.getName().equals(newBringer.getName()));

        mBringer = newBringer;

        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                // Show a waiting time with points, or remove text if it's a new bringer to found
                if (mBringerTextView.getText() != null
                        && mBringerTextView.getText().length() > 0
                        && (mBringerTextView.getText().equals(".") || mBringerTextView.getText().equals(".."))) {
                    mBringerTextView.setText(mBringerTextView.getText() + ".");
                } else {
                    mBringerTextView.setText(".");
                }
            }

            public void onFinish() {
                mBringerTextView.setText(mBringer.getName());
                mAnotherButton.setEnabled(true);
                mValidateButton.setEnabled(true);
            }

        }.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bringer_another:
                findABringer();
                break;

            case R.id.bringer_validate:
                mBringer.setSessionNumber(mMinSessionNumber + 1);
                mBringer.save();

                // Add the current bringer to the preferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                preferences.edit().putLong(Constant.PREFERENCES_CURRENT_BRINGER, mBringer.getId()).commit();

                finish();
                break;
        }
    }
}
