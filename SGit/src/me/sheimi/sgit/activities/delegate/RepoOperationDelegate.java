package me.sheimi.sgit.activities.delegate;

import me.sheimi.android.utils.FsUtils;
import me.sheimi.sgit.activities.RepoDetailActivity;
import me.sheimi.sgit.activities.delegate.actions.*;
import me.sheimi.sgit.database.models.Repo;
import me.sheimi.sgit.repo.tasks.SheimiAsyncTask.AsyncTaskPostCallback;
import me.sheimi.sgit.repo.tasks.repo.*;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.util.ArrayList;

public class RepoOperationDelegate {

    private Repo mRepo;
    private RepoDetailActivity mActivity;
    private ArrayList<RepoAction> mActions = new ArrayList<RepoAction>();

    public RepoOperationDelegate(Repo repo, RepoDetailActivity activity) {
        mRepo = repo;
        mActivity = activity;
        initActions();
    }

    private void initActions() {
        mActions.add(new PullAction(mRepo, mActivity));
        mActions.add(new PushAction(mRepo, mActivity));
        mActions.add(new AddAllAction(mRepo, mActivity));
        mActions.add(new CommitAction(mRepo, mActivity));
        mActions.add(new ResetAction(mRepo, mActivity));
        mActions.add(new MergeAction(mRepo, mActivity));
        mActions.add(new RebaseAction(mRepo, mActivity));
        mActions.add(new CherryPickAction(mRepo, mActivity));
        mActions.add(new DiffAction(mRepo, mActivity));
        mActions.add(new NewFileAction(mRepo, mActivity));
        mActions.add(new NewDirAction(mRepo, mActivity));
        mActions.add(new AddRemoteAction(mRepo, mActivity));
        mActions.add(new RemoveRemoteAction(mRepo, mActivity));
        mActions.add(new DeleteAction(mRepo, mActivity));
    }

    public void executeAction(int key) {
        RepoAction action = mActions.get(key);
        if (action == null)
            return;
        action.execute();
    }

    public void checkoutCommit(final String commitName) {
        CheckoutTask checkoutTask = new CheckoutTask(mRepo, commitName,
                new AsyncTaskPostCallback() {
                    @Override
                    public void onPostExecute(Boolean isSuccess) {
                        mActivity.reset(commitName);
                    }
                });
        checkoutTask.executeTask();
    }

    public void mergeBranch(final Ref commit, final String ffModeStr,
            final boolean autoCommit) {
        MergeTask mergeTask = new MergeTask(mRepo, commit, ffModeStr,
                autoCommit, new AsyncTaskPostCallback() {
                    @Override
                    public void onPostExecute(Boolean isSuccess) {
                        mActivity.reset();
                    }
                });
        mergeTask.executeTask();
    }

    public void addToStage(String filepath) {
        String relative = getRelativePath(filepath);
        AddToStageTask addToStageTask = new AddToStageTask(mRepo, relative);
        addToStageTask.executeTask();
    }

    public void checkoutFile(String filepath) {
        String relative = getRelativePath(filepath);
        CheckoutFileTask task = new CheckoutFileTask(mRepo, relative, null);
        task.executeTask();
    }

    public void deleteFileFromRepo(String filepath) {
        String relative = getRelativePath(filepath);
        DeleteFileFromRepoTask task = new DeleteFileFromRepoTask(mRepo,
                relative, new AsyncTaskPostCallback() {
                    @Override
                    public void onPostExecute(Boolean isSuccess) {
                        // TODO Auto-generated method stub
                        mActivity.getFilesFragment().reset();
                    }
                });
        task.executeTask();
    }

    private String getRelativePath(String filepath) {
        File base = mRepo.getDir();
        String relative = FsUtils.getRelativePath(new File(filepath), base);
        return relative;
    }

}
