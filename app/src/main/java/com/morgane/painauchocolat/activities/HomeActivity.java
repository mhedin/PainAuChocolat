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

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.model.Contributor;
import com.morgane.painauchocolat.utils.Constant;

/**
 * This class is the home page of the application. It allows to access the main features
 * of the application.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * A constant corresponding to the minimum session number registered in the preferences.
     */
    public static final String PREFERENCES_MIN_SESSION_NUMBER = "minSessionNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.home_find_bringer).setOnClickListener(this);

        refreshView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Find the min session number and register it
        int minSessionNumber = Contributor.getMinimumSessionNumber();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt(PREFERENCES_MIN_SESSION_NUMBER, minSessionNumber).commit();
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

        switch (item.getItemId()) {
            case R.id.action_bakery:
                //TODO
                break;

            case R.id.action_contributors:
                intent = new Intent(this, ManageContributorsActivity.class);
                break;

            case R.id.action_settings :
                intent = new Intent(this, SettingsActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_find_bringer:
                Intent intent = new Intent(this, BringerActivity.class);
                startActivityForResult(intent, Constant.REQUEST_CODE_CREATE_CONTRIBUTOR);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQUEST_CODE_CREATE_CONTRIBUTOR && resultCode == Activity.RESULT_OK) {
            refreshView();
        }
    }

    /**
     * Refresh the view with the "find a contributor" button if there is contributor registered
     * or a help message if there is not.
     */
    private void refreshView() {
        // If there is no contributor registered, display the help message
        if (!Contributor.isThereContributors()) {
            findViewById(R.id.home_find_bringer).setVisibility(View.GONE);
            findViewById(R.id.home_no_contributor).setVisibility(View.VISIBLE);
        }
    }
}
