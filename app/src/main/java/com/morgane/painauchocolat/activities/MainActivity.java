package com.morgane.painauchocolat.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.fragments.CreditsFragment;
import com.morgane.painauchocolat.fragments.EditContributorFragment;
import com.morgane.painauchocolat.fragments.HomeFragment;
import com.morgane.painauchocolat.fragments.ManageContributorsFragment;
import com.morgane.painauchocolat.fragments.SettingsFragment;

/**
 * This class is the home page of the application. It allows to access the main features
 * of the application.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTabHost tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        ImageView locationImage = new ImageView(this);
        locationImage.setImageResource(R.drawable.location_state);
        tabHost.addTab(tabHost.newTabSpec("location").setIndicator(locationImage), HomeFragment.class, null);

        ImageView contributorImage = new ImageView(this);
        contributorImage.setImageResource(R.drawable.contributor_state);
        tabHost.addTab(tabHost.newTabSpec("contributors").setIndicator(contributorImage), ManageContributorsFragment.class, null);

        ImageView homeImage = new ImageView(this);
        homeImage.setImageResource(R.drawable.home_state);
        tabHost.addTab(tabHost.newTabSpec("home").setIndicator(homeImage), HomeFragment.class, null);

        ImageView calendarImage = new ImageView(this);
        calendarImage.setImageResource(R.drawable.calendar_state);
        tabHost.addTab(tabHost.newTabSpec("calendar").setIndicator(calendarImage), SettingsFragment.class, null);

        ImageView copyrightImage = new ImageView(this);
        copyrightImage.setImageResource(R.drawable.copyright_state);
        tabHost.addTab(tabHost.newTabSpec("copyright").setIndicator(copyrightImage), CreditsFragment.class, null);

        tabHost.setCurrentTab(2);
    }

    /**
     * Launch the edition of the selected contributor
     * @param position The position of the contributor in the list.
     */
    public void editContributor(int position) {
        ManageContributorsFragment contributorsFragment =
                (ManageContributorsFragment) getSupportFragmentManager().findFragmentById(android.R.id.tabcontent);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.manage_contributors_fragment,
                        EditContributorFragment.newInstance(contributorsFragment.getContributorTop(position),
                                contributorsFragment.getContributorAt(position)),
                        "edit_contributor")
                .commit();
        contributorsFragment.setAddButtonVisibility(false);
    }

    public void closeContributorFragment(Fragment fragment) {
        ManageContributorsFragment contributorsFragment =
                (ManageContributorsFragment) getSupportFragmentManager().findFragmentById(android.R.id.tabcontent);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        contributorsFragment.setAddButtonVisibility(true);
        contributorsFragment.notifyDataSetChanged();
    }
}
