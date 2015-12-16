package com.morgane.painauchocolat.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.model.Contributor;
import com.morgane.painauchocolat.utils.Constant;

/**
 * This class is the home page of the application. It allows to access the main features
 * of the application.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.home_find_bringer).setOnClickListener(this);

        refreshView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        int requestCode = -1;

        switch (item.getItemId()) {
            case R.id.action_bakery:
                //TODO
                break;

            case R.id.action_contributors:
                intent = new Intent(this, ManageContributorsActivity.class);
                requestCode = Constant.REQUEST_CODE_CREATE_CONTRIBUTOR;
                break;

            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                break;

            case R.id.action_credits:
                intent = new Intent(this, CreditsActivity.class);
                break;
        }

        if (intent != null) {
            if (requestCode != -1) {
                startActivityForResult(intent, requestCode);
            } else {
                startActivity(intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_find_bringer:
                Intent intent = new Intent(this, BringerActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE_FIND_BRINGER);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt(Constant.PREFERENCES_MIN_SESSION_NUMBER, minSessionNumber).commit();

        TextView currentBringerTextView = (TextView) findViewById(R.id.home_current_bringer);
        Button findBringerButton = (Button) findViewById(R.id.home_find_bringer);
        TextView noContributorTextView = (TextView) findViewById(R.id.home_no_contributor);

        // If there is no contributor registered, display the help message
        if (!Contributor.isThereContributors()) {
            findBringerButton.setVisibility(View.GONE);
            noContributorTextView.setVisibility(View.VISIBLE);
            currentBringerTextView.setVisibility(View.INVISIBLE);

        } else {
            findBringerButton.setVisibility(View.VISIBLE);
            noContributorTextView.setVisibility(View.GONE);

            // If there is a current bringer, display it
            long currentBringerId = preferences.getLong(Constant.PREFERENCES_CURRENT_BRINGER, -1);
            if (currentBringerId != -1) {
                Contributor currentBringer = Contributor.getContributorById(currentBringerId);

                if (currentBringer != null) {
                    currentBringerTextView.setVisibility(View.VISIBLE);

                    int textId;
                    if (preferences.getInt(Constant.PREFERENCES_BREAKFAST_DAY, 0) == 0) {
                        textId = R.string.home_current_bringer_anyway;
                    } else {
                        textId = R.string.home_current_bringer_week;
                    }

                    currentBringerTextView.setText(getString(textId, currentBringer.getName()));

                } else {
                    currentBringerTextView.setVisibility(View.INVISIBLE);
                }

            } else {
                currentBringerTextView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
