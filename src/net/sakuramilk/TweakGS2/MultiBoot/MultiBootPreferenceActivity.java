/*
 * Copyright (C) 2011-2012 sakuramilk <c.sakuramilk@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;

public class MultiBootPreferenceActivity extends PreferenceActivity
    implements OnPreferenceClickListener, OnPreferenceChangeListener {

    MbsConf mMbsConf = new MbsConf();
    ListPreference mRomSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.multi_boot_pref);

        if (!mMbsConf.exists()) {
            return;
        }

        int value = mMbsConf.getBootRom();
        mRomSelect = (ListPreference)findPreference(MultiBootSetting.KEY_ROM_SELECT);
        mRomSelect.setOnPreferenceChangeListener(this);
        mRomSelect.setValue(String.valueOf(value));
        mRomSelect.setEnabled(true);

        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference(MultiBootSetting.KEY_ROOT_PREF);
        int i;
        for (i = 0; i < MbsConf.MAX_ROM_ID; i++) {
            String label = mMbsConf.getLabel(i);
            if (label != null) {
                PreferenceScreen pref = prefManager.createPreferenceScreen(this);
                String key = MultiBootSetting.KEY_ROM_SETTING_BASE + i;
                pref.setKey(key);
                pref.setTitle("ROM" + i + " : " + label);
                String sysPart = mMbsConf.getSystemPartition(i);
                String sysImg = mMbsConf.getSystemImage(i);
                String sysPath = mMbsConf.getSystemPath(i);
                String dataPart = mMbsConf.getDataPartition(i);
                String dataImg = mMbsConf.getDataImage(i);
                String dataPath = mMbsConf.getDataPath(i);
                
                String summary =
                        " SYS_PART:" + sysPart + "\n" +
                        (Misc.isNullOfEmpty(sysImg) ? "  SYS_IMG:" + sysImg + "\n" : "") +
                        (Misc.isNullOfEmpty(sysPath) ? " SYS_PATH:" + sysPath + "\n" : "") +
                        (Misc.isNullOfEmpty(dataPart) ? "DATA_PART:" + dataPart + "\n" : "") +
                        (Misc.isNullOfEmpty(dataImg) ? "  DATA_IMG:" + dataImg + "\n" : "") +
                        (Misc.isNullOfEmpty(dataPath) ? "DATA_PATH:" + dataPath + "\n" : "");
                pref.setSummary(summary);
                pref.setOnPreferenceClickListener(this);
                rootPref.addPreference(pref);
            }
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

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mRomSelect) {
            
            // don't return true
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.multi_boot_menu, menu);
        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_add_rom:
            Intent intent = new Intent(getApplicationContext(), MultiBootWizardActivity.class);
            intent.putExtra("rom_id", mMbsConf.getNextRomId());
            this.startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
