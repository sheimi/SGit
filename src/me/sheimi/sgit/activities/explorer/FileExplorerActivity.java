package me.sheimi.sgit.activities.explorer;

import java.io.File;
import java.io.FileFilter;

import me.sheimi.android.activities.SheimiFragmentActivity;
import me.sheimi.sgit.R;
import me.sheimi.sgit.adapters.FilesListAdapter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

public abstract class FileExplorerActivity extends SheimiFragmentActivity {

    public static final String RESULT_PATH = "result_path";

    private File mRootFolder;
    private File mCurrentDir;
    private ListView mFileList;
    protected FilesListAdapter mFilesListAdapter;

    protected abstract File getRootFolder();

    protected abstract FileFilter getExplorerFileFilter();

    protected abstract AdapterView.OnItemClickListener getOnListItemClickListener();

    protected abstract AdapterView.OnItemLongClickListener getOnListItemLongClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mRootFolder = getRootFolder();
        mCurrentDir = mRootFolder;
        mFileList = (ListView) findViewById(R.id.fileList);

        mFilesListAdapter = new FilesListAdapter(this, getExplorerFileFilter());
        mFileList.setAdapter(mFilesListAdapter);
        mFilesListAdapter.setDir(mRootFolder);

        mFileList.setOnItemClickListener(getOnListItemClickListener());
        mFileList.setOnItemLongClickListener(getOnListItemLongClickListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!mRootFolder.equals(mCurrentDir)) {
                setCurrentDir(mCurrentDir.getParentFile());
                return true;
            }
            finish();
            return true;
        }
        return false;
    }

    protected void setCurrentDir(File dir) {
        mCurrentDir = dir;
        mFilesListAdapter.setDir(mCurrentDir);
    }
    
    protected File getCurrentDir() {
        return mCurrentDir;
    }

}
