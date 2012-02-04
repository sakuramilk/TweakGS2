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

import java.util.ArrayList;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class MultiBootPreferenceActivity extends PreferenceActivity
    implements OnPreferenceClickListener, OnPreferenceChangeListener {

    private static final int REQUEST_ROM_EDIT = 1001;
    private static final int REQUEST_ROM_CREATE = 1002;

    private MbsConf mMbsConf = new MbsConf();
    private ListPreference mRomSelect;
    private ArrayList<ListPreference> mRomSettingList;
    private ArrayList<String> mRomIdEntries;
    private ArrayList<String> mRomIdEntryValues;

    private String getRomIdEntry(String endtryValue) {
        for (int i = 0; i < mRomIdEntryValues.size(); i++) {
            if (mRomIdEntryValues.get(i).equals(endtryValue)) {
                return mRomIdEntries.get(i);
            }            
        }
        return mRomIdEntries.get(0); // if not find, return safe value
    }

    private void createPreference() {
        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference(MultiBootSetting.KEY_ROOT_PREF);
        
        rootPref.removeAll();

        int nextRomId = mMbsConf.getNextRomId();
        if (nextRomId == 0) {
            // nothing rom setting, create rom0 and rom1 setting
            mMbsConf.setLabel(0, "Primary");
            mMbsConf.setSystemPartition(0, MbsConf.Partition.mmcblk0p9);
            mMbsConf.setDataPartition(0, MbsConf.Partition.mmcblk0p10);
            mMbsConf.setLabel(1, "Secondary");
            mMbsConf.setSystemPartition(1, MbsConf.Partition.mmcblk0p11);
            mMbsConf.setDataPartition(1, MbsConf.Partition.mmcblk0p10);
            mMbsConf.setBootRomId(0);
            nextRomId = mMbsConf.getNextRomId();
        }

        int bootRomId = mMbsConf.getBootRomId();
        mRomIdEntries = new ArrayList<String>();
        mRomIdEntryValues = new ArrayList<String>();
        for (int i = 0; i < nextRomId; i++) {
            mRomIdEntries.add("ROM" + i + ":" + mMbsConf.getLabel(i));
            mRomIdEntryValues.add(String.valueOf(i));
        }

        PreferenceCategory categoryPref = new PreferenceCategory(this);
        categoryPref.setTitle(R.string.multi_boot_rom_select_category);
        rootPref.addPreference(categoryPref);

        mRomSelect = new ListPreference(this);
        mRomSelect.setTitle(R.string.multi_boot_rom_select_title);
        mRomSelect.setEnabled(false);
        if (nextRomId > 0) {
            mRomSelect.setOnPreferenceChangeListener(this);
            mRomSelect.setEntries(mRomIdEntries.toArray(new String[0]));
            mRomSelect.setEntryValues(mRomIdEntryValues.toArray(new String[0]));
            mRomSelect.setValue(String.valueOf(bootRomId));
            mRomSelect.setSummary(getRomIdEntry(String.valueOf(bootRomId)));
            mRomSelect.setEnabled(true);
        } else {
            mRomSelect.setValue("ROM設定がありません");
            mRomSelect.setEnabled(false);
        }
        rootPref.addPreference(mRomSelect);

        categoryPref = new PreferenceCategory(this);
        categoryPref.setTitle(R.string.multi_boot_rom_setting);
        rootPref.addPreference(categoryPref);

        mRomSettingList = new ArrayList<ListPreference>();
        int i;
        for (i = 0; i <= MbsConf.MAX_ROM_ID; i++) {
            String sysPart = mMbsConf.getSystemPartition(i);
            if (!Misc.isNullOfEmpty(sysPart)) {
                ListPreference pref = new ListPreference(this);
                String key = MultiBootSetting.KEY_ROM_SETTING_BASE + i;
                String label = mMbsConf.getLabel(i);
                pref.setKey(key);
                pref.setTitle("ROM" + i + " : " + label);
                pref.setEntries(R.array.rom_setting_menu_entries);
                pref.setEntryValues(R.array.rom_setting_menu_values);
                String sysImg = mMbsConf.getSystemImage(i);
                String dataPart = mMbsConf.getDataPartition(i);
                String dataImg = mMbsConf.getDataImage(i);
                String summary =
                        " SYS_PART:" + sysPart + "\n" +
                        (!Misc.isNullOfEmpty(sysImg) ? "SYS_IMG:" + sysImg + "\n" : "") +
                        (!Misc.isNullOfEmpty(dataPart) ? "DATA_PART:" + dataPart + "\n" : "") +
                        (!Misc.isNullOfEmpty(dataImg) ? " DATA_IMG:" + dataImg : "");
                pref.setSummary(summary);
                pref.setOnPreferenceChangeListener(this);
                rootPref.addPreference(pref);
                mRomSettingList.add(pref);
            }
        }

        if (mMbsConf.getNextRomId() != -1) {
            PreferenceScreen pref = prefManager.createPreferenceScreen(this);
            pref.setKey(MultiBootSetting.KEY_ROM_CREATE);
            pref.setTitle("新規ROMの作成");
            pref.setOnPreferenceClickListener(this);
            rootPref.addPreference(pref);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.multi_boot_pref);

        createPreference();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(MultiBootSetting.KEY_ROM_CREATE)) {
            Intent intent = new Intent(getApplicationContext(), RomSettingPreferenceActivity.class);
            intent.putExtra("rom_id", mMbsConf.getNextRomId());
            startActivityForResult(intent, REQUEST_ROM_CREATE);
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mRomSelect) {
            mMbsConf.setBootRomId(Integer.valueOf((String)objValue));
            mRomSelect.setSummary(getRomIdEntry((String)objValue));
            mRomSelect.setValue((String)objValue);
            // don't return true
        } else {
            int index = mRomSettingList.indexOf(preference);
            if (index >= 0) {
                ListPreference pref = mRomSettingList.get(index);
                int romId = Integer.valueOf(pref.getKey().substring(MultiBootSetting.KEY_ROM_SETTING_BASE.length()));
                if ("modify".equals((String)objValue)) {
                    Intent intent = new Intent(getApplicationContext(), RomSettingPreferenceActivity.class);
                    intent.putExtra("rom_id", romId);
                    startActivityForResult(intent, REQUEST_ROM_EDIT);
                } else if ("delete".equals((String)objValue)) {
                    PreferenceManager prefManager = getPreferenceManager();
                    PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference(MultiBootSetting.KEY_ROOT_PREF);
                    rootPref.removePreference(pref);
                    mMbsConf.deleteRomId(romId);
                }
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_ROM_EDIT || requestCode == REQUEST_ROM_CREATE) {
            createPreference();
        }
    }
}
