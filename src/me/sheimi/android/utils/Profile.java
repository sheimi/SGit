package me.sheimi.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;

import me.sheimi.sgit.R;
import me.sheimi.sgit.database.models.Repo;

/**
 * Created by lee on 2015-02-01.
 */
public class Profile {

    private static final String GIT_USER_NAME = "user.name";
    private static final String GIT_USER_EMAIL = "user.email";
    private static final String THEME = "theme";
    private static SharedPreferences sSharedPreference;

    private static boolean sHasLastCloneFail = false;
    private static Repo sLastFailRepo;
    private static int sTheme = -1;

    private static SharedPreferences getProfileSharedPreference() {
        if (sSharedPreference == null) {
            sSharedPreference = BasicFunctions.getActiveActivity().getSharedPreferences(
                                    BasicFunctions.getActiveActivity().getString(R.string.preference_file_key),
                                    Context.MODE_PRIVATE);
        }
        return sSharedPreference;
    }

    public static String getUsername() {
        return getProfileSharedPreference().getString(GIT_USER_NAME, "");
    }

    public static String getEmail() {
        return getProfileSharedPreference().getString(GIT_USER_EMAIL, "");
    }

    public static void setProfileInformation(String username, String email, int theme) {
        SharedPreferences.Editor editor = getProfileSharedPreference().edit();
        editor.putString(Profile.GIT_USER_NAME, username);
        editor.putString(Profile.GIT_USER_EMAIL, email);
        editor.putInt(Profile.THEME, theme);
        editor.apply();
        sTheme = theme;
    }

    public static boolean hasLastCloneFailed() {
        return sHasLastCloneFail;
    }

    public static Repo getLastCloneTryRepo() {
        return sLastFailRepo;
    }

    public static void setLastCloneFailed(Repo repo) {
        sHasLastCloneFail = true;
        sLastFailRepo = repo;
    }

    public static void setLastCloneSuccess() {
        sHasLastCloneFail = false;
    }

    public static synchronized int getTheme() {
        if (sTheme == -1) {
            sTheme = getProfileSharedPreference().getInt(THEME, 0);
            if (sTheme < 0 || sTheme > 1)
                sTheme = 0;
        }
        return sTheme;
    }

    public static int getThemeResource() {
        final int[] themes = { R.style.AppTheme, R.style.DarkAppTheme };
        return themes[getTheme()];
    }

    public static String getCodeMirrorTheme() {
        final String[] themes = { "default", "midnight" };
        return themes[getTheme()];
    }

    public static int getStyledResource(Context ctxt, int unstyled) {
        TypedArray a = ctxt.getTheme().obtainStyledAttributes(getThemeResource(), new int[] {unstyled});
        int styled = a.getResourceId(0, 0);
        a.recycle();
        return styled;
    }
}












