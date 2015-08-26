package morgane.fr.painauchocolat.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import morgane.fr.painauchocolat.R;
import morgane.fr.painauchocolat.model.Contributor;


public class BringerActivity extends Activity {

    private Contributor mBringer;

    private TextView mBringerTextView;

    private int mMinBroughtNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bringer);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mMinBroughtNumber = preferences.getInt(HomeActivity.PREFERENCES_MIN_BROUGHT_NUMBER, 0);

        mBringer = Contributor.getRandomBringer(mMinBroughtNumber);

        mBringerTextView = (TextView) findViewById(R.id.bringer_name);
        if (mBringer != null) {
            mBringerTextView.setText(mBringer.name);
        } else {
            mBringerTextView.setText(getString(R.string.no_bringer_found));
            findViewById(R.id.bringer_validate).setVisibility(View.GONE);
            findViewById(R.id.bringer_another).setVisibility(View.GONE);
        }
    }

    public void validateBringer(View view) {
        mBringer.broughtNumber = mMinBroughtNumber + 1;
        mBringer.save();
        finish();
    }

    public void findAnotherBringer(View view) {
        mBringer = Contributor.getRandomBringer(mMinBroughtNumber);
        mBringerTextView.setText(mBringer.name);
    }
}
