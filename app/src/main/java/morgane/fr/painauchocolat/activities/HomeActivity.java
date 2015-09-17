package morgane.fr.painauchocolat.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import morgane.fr.painauchocolat.R;
import morgane.fr.painauchocolat.model.Contributor;


public class HomeActivity extends Activity {

    public static final String PREFERENCES_MIN_BROUGHT_NUMBER = "minBroughtNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Find the min brought number and register it
        int minBroughtNumber = Contributor.getMinimumBroughtNumber();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt(PREFERENCES_MIN_BROUGHT_NUMBER, minBroughtNumber).commit();
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

    public void findABringer(View view) {
        Intent intent = new Intent(this, BringerActivity.class);
        startActivity(intent);
    }
}
