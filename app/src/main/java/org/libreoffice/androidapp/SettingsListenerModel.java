package org.libreoffice.androidapp;

import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class SettingsListenerModel {
    private static SettingsListenerModel mInstance;
    private String key;
    private OnSettingsPreferenceChangedListener mListener;
    private SharedPreferences sharedPreferences;

    /* loaded from: classes.dex */
    public interface OnSettingsPreferenceChangedListener {
        void settingsPreferenceChanged(SharedPreferences sharedPreferences, String str);
    }

    private SettingsListenerModel() {
    }

    public static SettingsListenerModel getInstance() {
        if (mInstance == null) {
            mInstance = new SettingsListenerModel();
        }
        return mInstance;
    }

    public void setListener(OnSettingsPreferenceChangedListener listener) {
        this.mListener = listener;
    }

    public void changePreferenceState(SharedPreferences sharedPreferences, String key) {
        if (this.mListener != null) {
            this.sharedPreferences = sharedPreferences;
            this.key = key;
            notifyPreferenceChange(sharedPreferences, key);
        }
    }

    public SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

    public String getKey() {
        return this.key;
    }

    private void notifyPreferenceChange(SharedPreferences preferences, String key) {
        this.mListener.settingsPreferenceChanged(preferences, key);
    }
}
