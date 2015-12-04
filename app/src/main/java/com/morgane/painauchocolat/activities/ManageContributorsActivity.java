package com.morgane.painauchocolat.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.adapters.ContributorAdapter;
import com.morgane.painauchocolat.fragments.EditContributorFragment;
import com.morgane.painauchocolat.model.Contributor;

/**
 * This class allows the user to manage the contributors of the Breakfast Day. He can create
 * new contributors, and modify and remove existing ones.
 * The user has also the visibility on the contributors who have already brought the breakfast
 * in this session, and the ones who haven't yet.
 */
public class ManageContributorsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

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

    /**
     * The list view which displays the list of the contributors who have already
     * brought the breakfast.
     */
    private ListView mContributorsAlreadyListView;

    /**
     * The list view which displays the list of the contributors who haven't
     * brought the breakfast yet.
     */
    private ListView mContributorsNotYetListView;

    /**
     * The text view which indicates how to add a contributor if there is none.
     */
    private TextView mNoContributorAlert;

    /**
     * The button used to add a new contributor.
     */
    private FloatingActionButton mAddButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contributors);

        mAddButton = (FloatingActionButton) findViewById(R.id.manage_contributors_add_button);
        mAddButton.setOnClickListener(this);
        findViewById(R.id.manage_contributors_validate).setOnClickListener(this);

        mAddContributorLayout = (LinearLayout) findViewById(R.id.manage_contributors_add_layout);
        mNewContributorName = (EditText) findViewById(R.id.manage_contributors_name);

        // Get the brought number
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int sessionNumber = preferences.getInt(HomeActivity.PREFERENCES_MIN_SESSION_NUMBER, 0);

        // Create the list of the contributors who haven't brought yet
        mContributorsNotYetListView = (ListView) findViewById(R.id.manage_contributors_not_yet_list);
        mContributorsNotYetListView.setItemsCanFocus(true);

        List<Contributor> contributorsNotYet = Contributor.getNotYetContributors(sessionNumber);
        mContributorsNotYetAdapter = new ContributorAdapter(this, R.layout.item_contributor, contributorsNotYet);

        View headerNotYet = getLayoutInflater().inflate(R.layout.item_list_header, null, false);
        TextView headerNotYetTitle = (TextView) headerNotYet.findViewById(R.id.list_header_title);
        headerNotYetTitle.setText(getString(R.string.contributors_not_yet));
        mContributorsNotYetListView.addHeaderView(headerNotYet);

        mContributorsNotYetListView.setAdapter(mContributorsNotYetAdapter);

        // Create the list of the contributors who have already brought
        mContributorsAlreadyListView = (ListView) findViewById(R.id.manage_contributors_already_list);
        mContributorsAlreadyListView.setItemsCanFocus(true);

        List<Contributor> contributorsAlready = Contributor.getAlreadyContributors(sessionNumber);
        ContributorAdapter contributorsAlreadyAdapter = new ContributorAdapter(this, R.layout.item_contributor, contributorsAlready);

        View headerAlready = getLayoutInflater().inflate(R.layout.item_list_header, null, false);
        TextView headerAlreadyTitle = (TextView) headerAlready.findViewById(R.id.list_header_title);
        headerAlreadyTitle.setText(getString(R.string.contributors_already));
        mContributorsAlreadyListView.addHeaderView(headerAlready);

        mContributorsAlreadyListView.setAdapter(contributorsAlreadyAdapter);

        mNoContributorAlert = (TextView) findViewById(R.id.manage_contributors_none_alert);

        mContributorsNotYetListView.setOnItemClickListener(this);

        // If there is no contributor registered, display the help message
        if (contributorsNotYet.size() == 0 && contributorsAlready.size() == 0) {
            mContributorsNotYetListView.setVisibility(View.GONE);
            mContributorsAlreadyListView.setVisibility(View.GONE);
            mNoContributorAlert.setVisibility(View.VISIBLE);
        }
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

                    if (mContributorsNotYetAdapter.getCount() == 0) {
                        mContributorsNotYetListView.setVisibility(View.VISIBLE);
                        mContributorsAlreadyListView.setVisibility(View.VISIBLE);
                        mNoContributorAlert.setVisibility(View.GONE);
                    }

                    mContributorsNotYetAdapter.add(contributor);
                    mContributorsNotYetAdapter.notifyDataSetChanged();

                    mAddContributorLayout.setVisibility(View.GONE);
                    mNewContributorName.setText(null);
                }
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // We don't want to include the title in the item click
        if (id != -1) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.manage_contributors_fragment,
                            EditContributorFragment.newInstance(view.getTop(), mContributorsNotYetAdapter.getItem(position - 1)),
                            "edit_contributor")
                    .commit();
            mAddButton.setVisibility(View.GONE);
        }
    }

    public void closeFragment(Fragment fragment) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        mAddButton.setVisibility(View.VISIBLE);
        mContributorsNotYetAdapter.notifyDataSetChanged();
    }
}
