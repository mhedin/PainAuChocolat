package com.morgane.painauchocolat.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.adapters.ContributorAdapter;
import com.morgane.painauchocolat.model.Contributor;

/**
 * This class allows the user to manage the contributors of the Breakfast Day. He can create
 * new contributors, and modify and remove existing ones.
 * The user has also the visibility on the contributors who have already brought the breakfast
 * in this session, and the ones who haven't yet.
 */
public class ManageContributorsFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

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

    /**
     * The header of the list of contributors.
     */
    private View mHeaderView;

    /**
     * The edit text contained in the header.
     */
    private EditText mHeaderEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_manage_contributors, container, false);

        mAddButton = (FloatingActionButton) view.findViewById(R.id.manage_contributors_add_button);
        mAddButton.setOnClickListener(this);

        // Create the list of the contributors
        mContributorsListView = (ListView) view.findViewById(R.id.manage_contributors_list);
        mContributorsListView.setItemsCanFocus(true);

        List<Contributor> contributors = Contributor.getContributors();
        mContributorsAdapter = new ContributorAdapter(getActivity(), R.layout.item_contributor, contributors);

        mContributorsListView.setAdapter(mContributorsAdapter);

        mNoContributorAlert = (TextView) view.findViewById(R.id.manage_contributors_none_alert);

        // If there is no contributor registered, display the help message
        if (contributors.size() == 0) {
            mNoContributorAlert.setVisibility(View.VISIBLE);
        }

        // Create header view
        mHeaderView = inflater.inflate(R.layout.header_add_contributor, null);

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, bitmapOptions);
        LinearLayout headerLayout = (LinearLayout) mHeaderView.findViewById(R.id.add_contributor_layout);
        headerLayout.setPadding(bitmapOptions.outWidth,
                headerLayout.getPaddingTop(),
                headerLayout.getPaddingRight(),
                headerLayout.getPaddingBottom());

        mHeaderEditText = (EditText) mHeaderView.findViewById(R.id.add_contributor_name);
        mHeaderEditText.setOnEditorActionListener(this);

        mHeaderView.findViewById(R.id.add_contributor_validate).setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.action_contributors);
    }

    @Override
    public void onClick(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        switch (view.getId()) {
            case R.id.manage_contributors_add_button:
                // Add header view to the list and give focus with keyboard
                mContributorsListView.addHeaderView(mHeaderView);
                mHeaderEditText.requestFocus();
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                mAddButton.setVisibility(View.GONE);
                break;

            case R.id.add_contributor_validate:
                if (mHeaderEditText.getText() != null && mHeaderEditText.getText().length() > 0) {
                    Contributor contributor = new Contributor(mHeaderEditText.getText().toString());
                    contributor.save();

                    if (mContributorsAdapter.getCount() == 0) {
                        mNoContributorAlert.setVisibility(View.GONE);
                    }

                    mContributorsAdapter.replaceContributorList(Contributor.getContributors());
                }

                mHeaderEditText.setText(null);
                mContributorsListView.removeHeaderView(mHeaderView);
                mAddButton.setVisibility(View.VISIBLE);

                if (getActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                break;
        }
    }

    /**
     * Get the top position of the child view at the position passed in parameter.
     *
     * @param position The position of the child.
     * @return The top position of the child in the view.
     */
    public int getContributorTop(int position) {
        return mContributorsListView.getChildAt(position).getTop();
    }

    /**
     * Get the contributor at the given position in the list.
     *
     * @param position The position of the contributor.
     * @return The contributor found.
     */
    public Contributor getContributorAt(int position) {
        return mContributorsAdapter.getItem(position);
    }

    /**
     * Set the visibility of the button to add a new contributor.
     *
     * @param isVisible The new visibility of the button.
     */
    public void setAddButtonVisibility(boolean isVisible) {
        mAddButton.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    /**
     * Notify the list view its data has changed.
     */
    public void notifyDataSetChanged() {
        mContributorsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            mHeaderView.findViewById(R.id.add_contributor_validate).performClick();
        }
        return false;
    }
}
