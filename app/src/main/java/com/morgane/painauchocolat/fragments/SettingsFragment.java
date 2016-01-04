package com.morgane.painauchocolat.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.utils.Constant;

/**
 * This activity allows the user to choose the Breakfast Day. If a day is selected,
 * a reminder will be sent the day before to remind the user to choose a bringer.
 */
public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Spinner daySpinner = (Spinner) view.findViewById(R.id.settings_day_chooser);

        // Create the list of the days
        ArrayAdapter<String> spinnerDayAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.settings_days));
        spinnerDayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(spinnerDayAdapter);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        daySpinner.setSelection(preferences.getInt(Constant.PREFERENCES_BREAKFAST_DAY, 0));

        daySpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.action_settings);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Register the day selected in the preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.edit().putInt(Constant.PREFERENCES_BREAKFAST_DAY, position).commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}
