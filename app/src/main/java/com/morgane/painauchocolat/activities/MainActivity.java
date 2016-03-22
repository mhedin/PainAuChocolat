package com.morgane.painauchocolat.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
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

    /**
     * Id of the home activity in the menu
     */
    private static final int ID_HOME = 1;

    /**
     * Id of the contributors activity in the menu
     */
    private static final int ID_CONTRIBUTORS = 2;

    /**
     * Id of the settings activity in the menu
     */
    private static final int ID_SETTINGS = 3;

    /**
     * Id of the credits activity in the menu
     */
    private static final int ID_CREDITS = 4;

    private Drawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withName(R.string.activity_bringer)
                                .withIcon(R.drawable.home_state)
                                .withIdentifier(ID_HOME),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem()
                                .withName(R.string.action_contributors)
                                .withIcon(R.drawable.contributor_state)
                                .withIdentifier(ID_CONTRIBUTORS),
                        new PrimaryDrawerItem()
                                .withName(R.string.action_settings)
                                .withIcon(R.drawable.calendar_state)
                                .withIdentifier(ID_SETTINGS),
                        new PrimaryDrawerItem()
                                .withName(R.string.action_credits)
                                .withIcon(R.drawable.copyright_state)
                                .withIdentifier(ID_CREDITS)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Fragment fragment = null;

                        switch (view.getId()) {
                            case ID_HOME:
                                fragment = new HomeFragment();
                                break;
                            case ID_CONTRIBUTORS:
                                fragment = new ManageContributorsFragment();
                                break;
                            case ID_SETTINGS:
                                fragment = new SettingsFragment();
                                break;
                            case ID_CREDITS:
                                fragment = new CreditsFragment();
                                break;
                        }

                        if (fragment != null) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.main_fragment_layout, fragment)
                                    .commit();
                        }
                        return false;
                    }
                })
                .build();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mDrawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_layout, new HomeFragment())
                .commit();
    }

    /**
     * Launch the edition of the selected contributor
     * @param position The position of the contributor in the list.
     */
    public void editContributor(int position, int parentTop) {
        ManageContributorsFragment contributorsFragment =
                (ManageContributorsFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment_layout);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.manage_contributors_fragment,
                        EditContributorFragment.newInstance(parentTop,
                                contributorsFragment.getContributorAt(position)),
                        "edit_contributor")
                .commit();
        contributorsFragment.setAddButtonVisibility(false);
    }

    public void closeContributorFragment(Fragment fragment) {
        ManageContributorsFragment contributorsFragment =
                (ManageContributorsFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment_layout);
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        contributorsFragment.setAddButtonVisibility(true);
        contributorsFragment.notifyDataSetChanged();
    }
}
