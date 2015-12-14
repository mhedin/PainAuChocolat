package com.morgane.painauchocolat.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * This activity displays to the user the credits of the application.
 */
public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        TextView creditsTextView = (TextView) findViewById(R.id.credits_text);
        creditsTextView.setText(R.string.credits_links);
        creditsTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
