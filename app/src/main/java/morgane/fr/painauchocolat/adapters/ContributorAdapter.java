package morgane.fr.painauchocolat.adapters;

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

import java.util.List;

import morgane.fr.painauchocolat.R;
import morgane.fr.painauchocolat.activities.HomeActivity;
import morgane.fr.painauchocolat.model.Contributor;

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
     * The position of the item currently selected (all the elements of the list are selectable).
     */
    public int mSelectedPosition = -1;

    /**
     * The minimum number of the session, which corresponds to the current session.
     */
    private int mMinSessionNumber;

    /**
     * The holder containing the view used to display the information about the contributors.
     */
    private static class ViewHolderItem {
        EditText nameTextView;
        FrameLayout frameLayout;
    }

    public ContributorAdapter(Context context, int resource, List<Contributor> objects) {
        super(context, resource, objects);

        mResource = resource;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        mMinSessionNumber = preferences.getInt(HomeActivity.PREFERENCES_MIN_SESSION_NUMBER, 0);
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
            viewHolder.nameTextView = (EditText) convertView.findViewById(R.id.contributor_name);
            viewHolder.frameLayout = (FrameLayout) convertView.findViewById(R.id.contributor_frame_layout);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        final Contributor contributor = getItem(position);

        viewHolder.nameTextView.setText(contributor.name);

        viewHolder.nameTextView.setTextColor(getContext().getResources().getColor(
                contributor.sessionNumber > mMinSessionNumber ? android.R.color.black : android.R.color.holo_green_light));

        if (mSelectedPosition != -1 && position != mSelectedPosition) {
            viewHolder.frameLayout.setForeground(getContext().getDrawable(R.color.transparent_white));
        } else {
            viewHolder.frameLayout.setForeground(getContext().getDrawable(android.R.color.transparent));
        }

        viewHolder.nameTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && mSelectedPosition != position) {
                    mSelectedPosition = position;
                    notifyDataSetChanged();
                }
                else if (!hasFocus
                        && viewHolder.nameTextView.getText() != null
                        && viewHolder.nameTextView.getText().length() > 0
                        && !viewHolder.nameTextView.getText().toString().equals(contributor.name)) {
                    contributor.name = viewHolder.nameTextView.getText().toString();
                    contributor.save();
                }
            }
        });

        //we need to update adapter once we finish with editing
//        viewHolder.nameTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
//                if (actionId == EditorInfo.IME_ACTION_DONE
//                        && viewHolder.nameTextView.getText() != null
//                        && viewHolder.nameTextView.getText().length() > 0) {
//                    contributor.name = viewHolder.nameTextView.getText().toString();
//                    contributor.save();
//                    return true;
//                }
//                textView.clearFocus();
//                return false;
//            }
//        });

        return convertView;
    }
}
