package com.morgane.painauchocolat.activities;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
public class ManageContributorsActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    /**
     * The adapter of the list of the contributors who haven't bring
     * the breakfast yet.
     */
    private ContributorAdapter mContributorsAdapter;

    /**
     * The list view which displays the list of the contributors who haven't
     * brought the breakfast yet.
     */
    private ListView mContributorsListView;

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

        // Create the list of the contributors
        mContributorsListView = (ListView) findViewById(R.id.manage_contributors_list);
        mContributorsListView.setItemsCanFocus(true);

        List<Contributor> contributors = Contributor.getContributors();
        mContributorsAdapter = new ContributorAdapter(this, R.layout.item_contributor, contributors);

        mContributorsListView.setAdapter(mContributorsAdapter);

        mNoContributorAlert = (TextView) findViewById(R.id.manage_contributors_none_alert);

        // If there is no contributor registered, display the help message
        if (contributors.size() == 0) {
            mNoContributorAlert.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        switch (view.getId()) {
            case R.id.manage_contributors_add_button:
                final View headerView = getLayoutInflater().inflate(R.layout.header_add_contributor, null);
                mContributorsListView.addHeaderView(headerView);

                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, bitmapOptions);
                LinearLayout headerLayout = (LinearLayout) headerView.findViewById(R.id.add_contributor_layout);
                headerLayout.setPadding(bitmapOptions.outWidth,
                        headerLayout.getPaddingTop(),
                        headerLayout.getPaddingRight(),
                        headerLayout.getPaddingBottom());

                EditText headerEdit = (EditText) headerView.findViewById(R.id.add_contributor_name);
                headerEdit.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                headerEdit.setOnEditorActionListener(this);

                headerView.findViewById(R.id.add_contributor_validate).setOnClickListener(this);

                mAddButton.setVisibility(View.GONE);
                break;

            case R.id.add_contributor_validate:
                EditText newContributorName = (EditText) findViewById(R.id.add_contributor_name);
                if (newContributorName.getText() != null && newContributorName.getText().length() > 0) {
                    Contributor contributor = new Contributor(newContributorName.getText().toString());
                    contributor.save();

                    if (mContributorsAdapter.getCount() == 0) {
                        mNoContributorAlert.setVisibility(View.GONE);
                    }

                    mContributorsAdapter.replaceContributorList(Contributor.getContributors());
                }

                mContributorsListView.removeHeaderView(findViewById(R.id.add_contributor_layout));
                mAddButton.setVisibility(View.VISIBLE);

                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                break;
        }
    }

    /**
     * Launch the edition of the selected contributor
     * @param position The position of the contributor in the list.
     */
    public void editContributor(int position) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.manage_contributors_fragment,
                        EditContributorFragment.newInstance(mContributorsListView.getChildAt(position).getTop(),
                                mContributorsAdapter.getItem(position)),
                        "edit_contributor")
                .commit();
        mAddButton.setVisibility(View.GONE);
    }

    public void closeFragment(Fragment fragment) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        mAddButton.setVisibility(View.VISIBLE);
        mContributorsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            findViewById(R.id.add_contributor_validate).performClick();
        }
        return false;
    }
}
