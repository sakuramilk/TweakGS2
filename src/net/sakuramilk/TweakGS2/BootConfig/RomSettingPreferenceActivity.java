package net.sakuramilk.TweakGS2.BootConfig;

import net.sakuramilk.TweakGS2.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class RomSettingPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.rom_setting_pref);
    }
}
