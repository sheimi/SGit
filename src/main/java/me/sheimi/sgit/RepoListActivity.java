package me.sheimi.sgit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import org.slf4j.helpers.Util;

import java.io.File;

import me.sheimi.android.activities.SheimiFragmentActivity;
import me.sheimi.android.utils.FsUtils;
import me.sheimi.android.utils.Profile;
import me.sheimi.sgit.activities.UserSettingsActivity;
import me.sheimi.sgit.activities.explorer.ExploreFileActivity;
import me.sheimi.sgit.activities.explorer.ImportRepositoryActivity;
import me.sheimi.sgit.adapters.RepoListAdapter;
import me.sheimi.sgit.database.models.Repo;
import me.sheimi.sgit.dialogs.CloneDialog;
import me.sheimi.sgit.dialogs.DummyDialogListener;
import me.sheimi.sgit.dialogs.ImportLocalRepoDialog;
import me.sheimi.sgit.ssh.PrivateKeyUtils;

public class RepoListActivity extends SheimiFragmentActivity {

    private static final String LOGTAG = RepoListActivity.class.getSimpleName();
    private static final int MAX_UPGRADE_SHOW_TIMES = 4;
    private ListView mRepoList;
    private RepoListAdapter mRepoListAdapter;

    private static final int REQUEST_IMPORT_REPO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrivateKeyUtils.migratePrivateKeys();
        setContentView(R.layout.activity_main);
        mRepoList = (ListView) findViewById(R.id.repoList);
        mRepoListAdapter = new RepoListAdapter(this);
        mRepoList.setAdapter(mRepoListAdapter);
        mRepoListAdapter.queryAllRepo();
        mRepoList.setOnItemClickListener(mRepoListAdapter);
        mRepoList.setOnItemLongClickListener(mRepoListAdapter);

        showUpgradeDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        configSearchAction(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_new:
                CloneDialog cloneDialog = new CloneDialog();
                cloneDialog.show(getFragmentManager(), "clone-dialog");
                return true;
            case R.id.action_import_repo:
                intent = new Intent(this, ImportRepositoryActivity.class);
                startActivityForResult(intent, REQUEST_IMPORT_REPO);
                forwardTransition();
                return true;
            case R.id.action_settings:
                intent = new Intent(this, UserSettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void configSearchAction(MenuItem searchItem) {
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (searchView == null)
            return;
        SearchListener searchListener = new SearchListener();
        searchItem.setOnActionExpandListener(searchListener);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(searchListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_IMPORT_REPO:
                final String path = data.getExtras().getString(
                        ExploreFileActivity.RESULT_PATH);
                File file = new File(path);
                File dotGit = new File(file, Repo.DOT_GIT_DIR);
                if (!dotGit.exists()) {
                    showToastMessage(getString(R.string.error_no_repository));
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        this);
                builder.setTitle(R.string.dialog_comfirm_import_repo_title);
                builder.setMessage(R.string.dialog_comfirm_import_repo_msg);
                builder.setNegativeButton(R.string.label_cancel,
                        new DummyDialogListener());
                builder.setPositiveButton(R.string.label_import,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(
                                    DialogInterface dialogInterface, int i) {
                                Bundle args = new Bundle();
                                args.putString(ImportLocalRepoDialog.FROM_PATH, path);
                                ImportLocalRepoDialog rld = new ImportLocalRepoDialog();
                                rld.setArguments(args);
                                rld.show(getFragmentManager(), "import-local-dialog");
                            }
                        });
                builder.show();
                break;
        }
    }

    public class SearchListener implements SearchView.OnQueryTextListener,
            MenuItem.OnActionExpandListener {

        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            mRepoListAdapter.searchRepo(s);
            return false;
        }

        @Override
        public boolean onMenuItemActionExpand(MenuItem menuItem) {
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(MenuItem menuItem) {
            mRepoListAdapter.queryAllRepo();
            return true;
        }

    }

    public void finish() {
        rawfinish();
    }

    // Prompt user to install MGit as SGit is no longer being maintained
    private void showUpgradeDialog() {

        if (Profile.incUpgradeMesssageShownCount(getApplicationContext()) > MAX_UPGRADE_SHOW_TIMES) {
            return;
        }

        File repoLocation = FsUtils.getRepoDir(getApplicationContext(),"");
        String mesg = getString(R.string.dialog_upgrade_message,
                (repoLocation != null) ? repoLocation.toString() : getString(R.string.default_repo_path));

        new AlertDialog.Builder(this)
                .setMessage(mesg)
                .setPositiveButton(getString(R.string.dialog_upgrade_ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW)
                                .setData(Uri.parse("market://details?id=com.manichord.mgit"));
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            dialog.dismiss();
                            showNoGooglePlayDialog();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.dialog_upgrade_cancel_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .create().show();
    }

    private void showNoGooglePlayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RepoListActivity.this);
        builder.setMessage(getString(R.string.sorry_play_missing_message));
        builder.setPositiveButton(getString(R.string.dialog_goto_github_ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent goToGithub = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse("https://github.com/maks/MGit/releases"));
                try {
                    startActivity(goToGithub);
                } catch (ActivityNotFoundException e) {
                    Log.e(LOGTAG, "could not show Github url for MGit", e);
                }
            }
        })
        .setNegativeButton(getString(R.string.dialog_upgrade_cancel_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        }).create().show();
    }
}
