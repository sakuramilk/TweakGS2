package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.R;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class MultiBootPreferenceActivity extends PreferenceActivity
    implements OnPreferenceClickListener {

    MultiBootSetting mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.multi_boot_pref);
        mSetting = new MultiBootSetting(this);

        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference(MultiBootSetting.KEY_ROOT_PREF);
        int i;
        for (i = 0; i < MbsConf.MAX_ROM_ID; i++) {
            PreferenceScreen pref = prefManager.createPreferenceScreen(this);
            String key = MultiBootSetting.KEY_ROM_SETTING_BASE + i;
            pref.setKey(key);
            pref.setTitle("ROM" + i);
            pref.setSummary("@system/stest");
            pref.setOnPreferenceClickListener(this);
            rootPref.addPreference(pref);
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        
        String key = preference.getKey();
        if (key.indexOf(MultiBootSetting.KEY_ROM_SETTING_BASE) >= 0) {
            Intent intent = new Intent(getApplicationContext(), RomSettingPreferenceActivity.class);
            intent.putExtra("rom_id", Integer.valueOf(key.substring(MultiBootSetting.KEY_ROM_SETTING_BASE.length())));
            this.startActivityForResult(intent, 1001);
        }
        return false;
    }
}
