package com.morgane.painauchocolat.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.model.Contributor;
import com.morgane.painauchocolat.utils.Constant;

/**
 * This class is the adapter displaying the name of the contributors in a list.
 *
 * Created by morgane.hedin on 25/08/2015.
 */
public class ContributorAdapter extends ArrayAdapter<Contributor> {

    /**
     * The id of the resource used to display the name of the contributor.
     */
    private int mResource;

    /**
     * The current session number.
     */
    private int mMinSessionNumber;

    /**
     * The holder containing the view used to display the information about the contributors.
     */
    private static class ViewHolderItem {
        ViewPager viewPager;
    }

    public ContributorAdapter(Context context, int resource, List<Contributor> objects) {
        super(context, resource, objects);

        mResource = resource;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mMinSessionNumber = preferences.getInt(Constant.PREFERENCES_MIN_SESSION_NUMBER, 0);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolderItem viewHolder;
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(mResource, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.viewPager = (ViewPager) convertView.findViewById(R.id.contributor_view_pager);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        final Contributor contributor = getItem(position);

        ContributorPagerAdapter mPagerAdapter =
                new ContributorPagerAdapter((AppCompatActivity)getContext(), contributor, mMinSessionNumber, position);

        viewHolder.viewPager.setAdapter(mPagerAdapter);

        return convertView;
    }
}
