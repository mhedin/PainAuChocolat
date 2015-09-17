package morgane.fr.painauchocolat.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.prefs.PreferenceChangeEvent;

import morgane.fr.painauchocolat.R;
import morgane.fr.painauchocolat.utils.Constant;

/**
 * This activity allows the user to choose the Breakfast Day. If a day is selected,
 * a reminder will be sent the day before to remind the user to choose a bringer.
 */
public class SettingsActivity extends Activity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Spinner daySpinner = (Spinner) findViewById(R.id.settings_day_chooser);

        // Create the list of the days
        ArrayAdapter<String> spinnerDayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.settings_days));
        spinnerDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(spinnerDayAdapter);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        daySpinner.setSelection(preferences.getInt(Constant.PREFERENCES_BREAKFAST_DAY, 0));

        daySpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Register the day selected in the preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putInt(Constant.PREFERENCES_BREAKFAST_DAY, position).commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
