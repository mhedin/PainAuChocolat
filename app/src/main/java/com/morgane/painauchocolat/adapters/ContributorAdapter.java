package com.morgane.painauchocolat.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import com.morgane.painauchocolat.R;
import com.morgane.painauchocolat.activities.HomeActivity;
import com.morgane.painauchocolat.model.Contributor;

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
     * The holder containing the view used to display the information about the contributors.
     */
    private static class ViewHolderItem {
        TextView nameTextView;
    }

    public ContributorAdapter(Context context, int resource, List<Contributor> objects) {
        super(context, resource, objects);

        mResource = resource;
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
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.contributor_name);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        final Contributor contributor = getItem(position);

        viewHolder.nameTextView.setText(contributor.name);

        return convertView;
    }
}
