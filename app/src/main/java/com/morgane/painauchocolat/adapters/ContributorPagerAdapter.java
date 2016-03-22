package com.morgane.painauchocolat.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.activities.MainActivity;
import com.morgane.painauchocolat.model.Contributor;

/**
 * Adapter of the view pager of the contributor view.
 *
 * Created by morgane.hedin on 07/12/15.
 */
public class ContributorPagerAdapter extends PagerAdapter {

    /**
     * The activity which uses the adapter.
     */
    private Activity mActivity;

    /**
     * The current contributor displayed.
     */
    private Contributor mContributor;

    /**
     * The min session number of the application.
     */
    private int mMinSessionNumber;

    /**
     * The position of the current view pager in the parent list view.
     */
    private int mParentPosition;

    /**
     * Constructor of the adapter.
     * @param activity The calling activity.
     * @param contributor The current contributor.
     * @param minSessionNumber The min session number.
     * @param parentPosition The position of the view pager in the parent list view.
     */
    public ContributorPagerAdapter(Activity activity, Contributor contributor, int minSessionNumber, int parentPosition) {
        mActivity = activity;
        mContributor = contributor;
        mMinSessionNumber = minSessionNumber;
        mParentPosition = parentPosition;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = mActivity.getLayoutInflater().inflate(R.layout.item_contributor_slide, null);
        TextView textView = (TextView) view.findViewById(R.id.contributor_name);
        textView.setGravity(Gravity.CENTER_VERTICAL);

        int additionalPadding = 0;

        if (position == 0) {
            textView.setText(mContributor.getName());
            textView.setBackgroundColor(ContextCompat.getColor(mActivity, android.R.color.transparent));

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)mActivity).editContributor(mParentPosition, ((ViewGroup)v.getParent().getParent()).getTop());
                }
            });

            if (mContributor.getSessionNumber() <= mMinSessionNumber) {
                textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher, 0, 0, 0);
            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_check_mark, 0, 0, 0);
            }

        } else {
            textView.setText(R.string.contributor_delete);
            textView.setTextColor(ContextCompat.getColor(mActivity, android.R.color.white));
            textView.setBackgroundColor(ContextCompat.getColor(mActivity, android.R.color.holo_red_light));
            additionalPadding = textView.getPaddingRight();
        }

        textView.setPadding(textView.getPaddingLeft() + additionalPadding,
                textView.getPaddingTop(),
                textView.getPaddingRight(),
                textView.getPaddingBottom());

        container.addView(textView);

        return textView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((TextView)object);
    }
}
