package net.sakuramilk.TweakGS2.BootConfig;

import net.sakuramilk.TweakGS2.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class BootConfigPreferenceActivity extends PreferenceActivity {

    BootConfigSetting mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.boot_config_pref);
        mSetting = new BootConfigSetting(this);

        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference(BootConfigSetting.KEY_ROOT_PREF);
        int i;
        for (i = 0; i < BootConfigSetting.MAX_ROM; i++) {
            PreferenceScreen pref = prefManager.createPreferenceScreen(this);
            String key = BootConfigSetting.KEY_ROM_SETTING_BASE + i;
            pref.setKey(key);
            pref.setTitle("ROM" + i);
            pref.setSummary("設定なし");
            rootPref.addPreference(pref);
        }
    }
}
