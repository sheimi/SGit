# JiGit

A Git client for Android. Forked from [SGit](https://play.google.com/store/apps/details?id=me.sheimi.sgit). The only reason for this fork is because original seems to be abandoned.

## Notes

 * All repositories are stored in `[sdcard dir]/Android/data/ch.phcoder.jigit/files/[repo name]`. If you want to delete this app, you can manually backup repositories from this location.
 * The GitHub repo of this project is: [phcoder/SGit](https://github.com/phcoder/SGit).
 * If you encounter any issues (bugs, crashes, etc.) and want to help improve this project, please open an issue on [GitHub](https://github.com/phcoder/SGit/issues/new) describing: what the issues are; and how they were caused, to allow for re-creation and fixing of bugs.
 * This app is for Android v4.x and later. 4.4 or later or recommended, earlier versions work with reduced functionality and possibly more bugs.

## Supported Features

New in 1.3

* Multiline commit message amend
* Support for setting commit author
* Force pull and ability to choose remote for pull
* Force push and ability to choose remote for push (by Benjamin Tan)
* Rebase on non-local branch
* Show staged and unstaged diff.
* Added three different version of deleting file or directory,
  delete from working directory , delete from index or from both (by liscju)
* Renaming of repositories (by liscju)

Added in JiGit:

* SSH key generation and management
* SSH keys are stored in memory only this app can use.
* Branch management
* Several bug fixes

Inherited from SGit:

* Create local repositories
* Clone remote repositories
* Pull from origin
* Delete local repositories
* Browse files
* Browse commit messages (short)
* Checkout branches and tags
* HTTP/HTTPS/SSH are supported (without private key passphrase)
* Username/Password authentication is supported
* Search local repositories
* Private keys management
* Manually choose code language
* `git diff` between commits (to be enhanced)
* Import copied repositories (that is, you can copy a repository from computer and import it)
* Checkout remote branches
* Merge branches
* Push merged content
* Edit file (you must have some app that can edit file)
* Commit and push changed files (commit all changes)
* Committer information
* Prompt for password
* Choose not to save password and username (will not be saved in disk but may be temporarily saved in memory)
* `git status`
* Cancel when cloning
* Add modified file to stage
* `git rebase`
* `git cherrypick`
* `git checkout <file>` (reset changes of a file)

<a href="https://play.google.com/store/apps/details?id=ch.phcoder.jigit"><img alt="Android app on Google Play" src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" /></a>

## Quick start

### Clone a remote repository

1. Click on the *+* icon to add a new repository.
2. Enter remote URL (see URL format below).
3. Enter local repository name - note that this is not a path since JiGit stores all repositories in the same directory on the mobile device.
4. Username - username to use to clone the remote repo.
5. Password - password to use to clone the remote repo.
6. Click the *Clone* button.
7. If all the credentials are correct, JiGit will download the repository (all branches) to your device.

### Create a local repository
1. Click on the *+* icon to add a new repository.
2. Click on *Init Local* to create a local repository.
3. Enter the name for this repository when prompted.
4. A local repo will be created.

### URL format

#### SSH URLs

 * SSH running on standard port (22): `ssh://username@server_name/path/to/repo`
* SSH running on non-standard port: `ssh://username@server_name:port/path/to/repo`
* `username` is needed - by default, JiGit tries to connect as root.

#### HTTP(S) URLs

* HTTP(S) URL: `https://server_name/path/to/repo`

## To Do List

 * Private key passphrase
 * Commits related to a file
 * Commit graph (low priority)

## License

GPLv3

