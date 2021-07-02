package com.example.fud.Controller;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * This class is responsible for managing Android Volley requests
 */
public class DisplayController extends Application{

    public static final String NIGHT_MODE = "NIGHT_MODE";
    public static final String CHANGED = "CHANGED";
    private boolean nightModeEnabled = false;
    private boolean changedMode = false;
    SharedPreferences mSharedPreferences;
    private static Context ctx;

    private static DisplayController singleton = null;

    private DisplayController(Context context) {
        ctx = context;
        singleton = null;
    }

    public static synchronized DisplayController getInstance(Context context) {

        if(singleton == null)
        {
            singleton = new DisplayController(context);
        }
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.nightModeEnabled = mPrefs.getBoolean(NIGHT_MODE, false);
    }

    public boolean nightModeEnabled() {
        return nightModeEnabled;
    }
    public boolean changedMode() {
        return changedMode;
    }


    public void setIsNightModeEnabled(boolean isNightModeEnabled) {
        this.nightModeEnabled = isNightModeEnabled;

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(NIGHT_MODE, isNightModeEnabled);
        editor.apply();
    }

    public void setChangedMode(boolean changedMode) {
        this.changedMode = changedMode;

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(CHANGED, changedMode);
        editor.apply();
    }
}