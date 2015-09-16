package morgane.fr.painauchocolat.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import morgane.fr.painauchocolat.R;
import morgane.fr.painauchocolat.adapters.ContributorAdapter;
import morgane.fr.painauchocolat.model.Contributor;

/**
 * This class allows the user to manage the contributors of the Breakfast Day. He can create
 * new contributors, and modify and remove existing ones.
 * The user has also the visibility on the contributors who have already brought the breakfast
 * in this session, and the ones who haven't yet.
 */
public class ManageContributorsActivity extends Activity implements View.OnClickListener {

    /**
     * The adapter of the list of the contributors who haven't bring
     * the breakfast yet.
     */
    private ContributorAdapter mContributorsNotYetAdapter;

    /**
     * The layout containing the button to add a new contributor.
     */
    private LinearLayout mAddContributorLayout;

    /**
     * The editable field in which the user can add the name of a contributor.
     */
    private EditText mNewContributorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contributors);

        findViewById(R.id.manage_contributors_add_button).setOnClickListener(this);
        findViewById(R.id.manage_contributors_validate).setOnClickListener(this);

        mAddContributorLayout = (LinearLayout) findViewById(R.id.manage_contributors_add_layout);
        mNewContributorName = (EditText) findViewById(R.id.manage_contributors_name);

        // Get the brought number
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int sessionNumber = preferences.getInt(HomeActivity.PREFERENCES_MIN_SESSION_NUMBER, 0);

        // Create the list of the contributors who haven't brought yet
        final ListView contributorNotYetList = (ListView) findViewById(R.id.manage_contributors_not_yet_list);
        contributorNotYetList.setItemsCanFocus(true);

        List<Contributor> contributorsNotYet = Contributor.getNotYetContributors(sessionNumber);
        mContributorsNotYetAdapter = new ContributorAdapter(this, R.layout.item_contributor, contributorsNotYet);
        contributorNotYetList.setAdapter(mContributorsNotYetAdapter);

        View headerNotYet = getLayoutInflater().inflate(R.layout.item_list_header, (ViewGroup) findViewById(android.R.id.content), false);
        TextView headerNotYetTitle = (TextView) headerNotYet.findViewById(R.id.list_header_title);
        headerNotYetTitle.setText(getString(R.string.contributors_not_yet));
        contributorNotYetList.addHeaderView(headerNotYet);

        // Create the list of the contributors who have already brought
        final ListView contributorAlreadyList = (ListView) findViewById(R.id.manage_contributors_already_list);
        contributorAlreadyList.setItemsCanFocus(true);

        List<Contributor> contributorsAlready = Contributor.getAlreadyContributors(sessionNumber);
        ContributorAdapter contributorsAlreadyAdapter = new ContributorAdapter(this, R.layout.item_contributor, contributorsAlready);
        contributorAlreadyList.setAdapter(contributorsAlreadyAdapter);

        View headerAlready = getLayoutInflater().inflate(R.layout.item_list_header, (ViewGroup) findViewById(android.R.id.content), false);
        TextView headerAlreadyTitle = (TextView) headerAlready.findViewById(R.id.list_header_title);
        headerAlreadyTitle.setText(getString(R.string.contributors_already));
        contributorAlreadyList.addHeaderView(headerAlready);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.manage_contributors_add_button:
                mAddContributorLayout.setVisibility(View.VISIBLE);
                mNewContributorName.requestFocus();
                break;

            case R.id.manage_contributors_validate:
                if (mNewContributorName.getText() != null && mNewContributorName.getText().length() > 0) {
                    Contributor contributor = new Contributor(mNewContributorName.getText().toString());
                    contributor.save();
                    mContributorsNotYetAdapter.add(contributor);
                    mContributorsNotYetAdapter.notifyDataSetChanged();

                    mAddContributorLayout.setVisibility(View.GONE);
                    mNewContributorName.setText(null);
                }
                break;

        }
    }
}
