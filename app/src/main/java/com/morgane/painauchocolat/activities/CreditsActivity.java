package com.morgane.painauchocolat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.morgane.painauchocolat.R;

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
