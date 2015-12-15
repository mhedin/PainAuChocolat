package com.morgane.painauchocolat.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.activities.ManageContributorsActivity;
import com.morgane.painauchocolat.model.Contributor;

/**
 * The fragment used to edit the name of a contributor.
 */
public class EditContributorFragment extends Fragment implements View.OnClickListener, EditText.OnEditorActionListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_POSITION_FROM_TOP = "positionFromTop";
    private static final String ARG_CONTRIBUTOR = "contributor";

    /**
     * The contributor currently edited.
     */
    private Contributor mContributor;

    /**
     * The view in which the name of the contributor is edited.
     */
    private EditText mNameEditText;

    /**
     * Factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param positionFromTop The position where place the text view in the screen :
     *                        it will be superimpose to the one in the list before edition.
     * @param contributor The contributor who will be edited.
     * @return A new instance of fragment EditContributorFragment.
     */
    public static EditContributorFragment newInstance(int positionFromTop, Contributor contributor) {
        EditContributorFragment fragment = new EditContributorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION_FROM_TOP, positionFromTop);
        args.putParcelable(ARG_CONTRIBUTOR, contributor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_contributor, container, false);

        mContributor = getArguments().getParcelable(ARG_CONTRIBUTOR);

        mNameEditText = (EditText) view.findViewById(R.id.edit_contributor_name);
        mNameEditText.setText(mContributor.getName());
        mNameEditText.setOnEditorActionListener(this);

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, bitmapOptions);
        mNameEditText.setPadding(bitmapOptions.outWidth,
                mNameEditText.getPaddingTop(),
                mNameEditText.getPaddingRight(),
                mNameEditText.getPaddingBottom());

        FrameLayout.LayoutParams nameLayoutParams =
                (FrameLayout.LayoutParams) mNameEditText.getLayoutParams();
        nameLayoutParams.setMargins(0, getArguments().getInt(ARG_POSITION_FROM_TOP), 0, 0);

        view.findViewById(R.id.edit_contributor_layout).setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        mNameEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    @Override
    public void onClick(View v) {
        ((ManageContributorsActivity)getActivity()).closeFragment(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (v.getText() != null
                    && v.getText().length() > 0
                    && !v.getText().toString().equals(mContributor.getName())) {
                mContributor.setName(v.getText().toString());
                mContributor.save();
            }

            ((ManageContributorsActivity)getActivity()).closeFragment(this);
        }
        return false;
    }
}
