package morgane.fr.painauchocolat.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import morgane.fr.painauchocolat.R;
import morgane.fr.painauchocolat.adapters.ContributorAdapter;
import morgane.fr.painauchocolat.model.Contributor;


public class ManageContributorsActivity extends Activity implements View.OnClickListener {

    private ContributorAdapter mContributorAdapter;

    private LinearLayout mAddContributorLayout;

    private EditText mNewContributorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contributors);

        findViewById(R.id.manage_contributors_add_button).setOnClickListener(this);
        findViewById(R.id.manage_contributors_cancel).setOnClickListener(this);
        findViewById(R.id.manage_contributors_validate).setOnClickListener(this);

        mAddContributorLayout = (LinearLayout) findViewById(R.id.manage_contributors_add_layout);
        mNewContributorName = (EditText) findViewById(R.id.manage_contributors_name);

        final ListView contributorList = (ListView) findViewById(R.id.manage_contributors_list);
        contributorList.setItemsCanFocus(true);

        List<Contributor> contributors = Contributor.getAll();

        mContributorAdapter = new ContributorAdapter(this, R.layout.item_contributor, contributors);

        contributorList.setAdapter(mContributorAdapter);
    }

    public void addContributor(View view) {
        mAddContributorLayout.setVisibility(View.VISIBLE);
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
                    mContributorAdapter.add(contributor);
                    mContributorAdapter.notifyDataSetChanged();

                    mAddContributorLayout.setVisibility(View.GONE);
                    mNewContributorName.setText(null);
                }
                break;

            case R.id.manage_contributors_cancel:
                mAddContributorLayout.setVisibility(View.GONE);
                mNewContributorName.setText(null);
                break;

        }
    }
}
