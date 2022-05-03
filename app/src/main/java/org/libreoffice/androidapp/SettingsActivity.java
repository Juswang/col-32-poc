package org.libreoffice.androidapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import org.libreoffice.androidapp.debug.R;
import org.libreoffice.androidapp.ui.LibreOfficeUIActivity;

/* loaded from: classes.dex */
public class SettingsActivity extends AppCompatActivity {
    private static SharedPreferences prefs;

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.LibreOfficeTheme_Base);
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(16908290, new SettingsFragment()).commit();
        prefs = getSharedPreferences("EXPLORER_PREFS", 0);
    }

    /* loaded from: classes.dex */
    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        int dayNightMode;
        int selectedThemeBtnId;

        @Override // androidx.preference.PreferenceFragmentCompat
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.libreoffice_preferences);
            if (!BuildConfig.DEBUG) {
                findPreference("ENABLE_SHOW_DEBUG_INFO").setVisible(false);
            }
            Preference themePreference = findPreference("THEME_SELECTION");
            this.dayNightMode = AppCompatDelegate.getDefaultNightMode();
            this.selectedThemeBtnId = R.id.radioBtn_default;
            int i = this.dayNightMode;
            if (i == -1) {
                themePreference.setSummary(R.string.theme_system_default);
                this.selectedThemeBtnId = R.id.radioBtn_default;
            } else if (i == 1) {
                themePreference.setSummary(R.string.theme_light);
                this.selectedThemeBtnId = R.id.radioBtn_light;
            } else if (i == 2) {
                themePreference.setSummary(R.string.theme_dark);
                this.selectedThemeBtnId = R.id.radioBtn_dark;
            }
            themePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: org.libreoffice.androidapp.SettingsActivity.SettingsFragment.1
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public boolean onPreferenceClick(Preference preference) {
                    View view = SettingsFragment.this.getLayoutInflater().inflate(R.layout.theme_selection, (ViewGroup) null);
                    RadioGroup group = (RadioGroup) view.findViewById(R.id.radioGrp_theme);
                    group.check(SettingsFragment.this.selectedThemeBtnId);
                    group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: org.libreoffice.androidapp.SettingsActivity.SettingsFragment.1.1
                        @Override // android.widget.RadioGroup.OnCheckedChangeListener
                        public void onCheckedChanged(RadioGroup radioGroup, int i2) {
                            switch (i2) {
                                case R.id.radioBtn_dark /* 2131296472 */:
                                    SettingsFragment.this.dayNightMode = 2;
                                    break;
                                case R.id.radioBtn_default /* 2131296473 */:
                                    SettingsFragment.this.dayNightMode = -1;
                                    break;
                                case R.id.radioBtn_light /* 2131296474 */:
                                    SettingsFragment.this.dayNightMode = 1;
                                    break;
                            }
                            AppCompatDelegate.setDefaultNightMode(SettingsFragment.this.dayNightMode);
                            SettingsActivity.prefs.edit().putInt(LibreOfficeUIActivity.NIGHT_MODE_KEY, SettingsFragment.this.dayNightMode).commit();
                            SettingsFragment.this.getActivity().recreate();
                        }
                    });
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsFragment.this.getActivity());
                    dialog.setView(view);
                    dialog.setTitle(SettingsFragment.this.getString(R.string.choose_theme));
                    dialog.setCancelable(true);
                    dialog.setNegativeButton(SettingsFragment.this.getString(R.string.cancel_label), (DialogInterface.OnClickListener) null);
                    dialog.show();
                    return false;
                }
            });
        }

        @Override // androidx.fragment.app.Fragment
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override // androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            SettingsListenerModel.getInstance().changePreferenceState(sharedPreferences, key);
        }
    }
}
