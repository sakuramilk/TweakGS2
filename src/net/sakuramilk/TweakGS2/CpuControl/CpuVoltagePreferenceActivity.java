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

package net.sakuramilk.TweakGS2.CpuControl;

import java.util.ArrayList;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.ApplyButtonPreferenceActivity;
import net.sakuramilk.TweakGS2.Parts.ConfirmDialog;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class CpuVoltagePreferenceActivity extends ApplyButtonPreferenceActivity
    implements OnSeekBarPreferenceDoneListener, OnClickListener, OnPreferenceChangeListener {

    private Context mContext;
    private ListPreference mAsvGroup;
    private CpuVoltageSetting mSetting;
    private ArrayList<SeekBarPreference> mFreqPrefList;
    private String[] mCurVoltTable;
    private String[] mSavedVoltTable;
    private String mCurAsvGroup;
    private String mSavedAsvGroup;

    private void setValues() {
        mAsvGroup.setValue(mSavedAsvGroup);
        mAsvGroup.setSummary(Misc.getCurrentAndSavedValueText(mContext,
                Misc.getEntryFromEntryValue(mAsvGroup.getEntries(),mAsvGroup.getEntryValues(), mCurAsvGroup),
                Misc.getEntryFromEntryValue(mAsvGroup.getEntries(),mAsvGroup.getEntryValues(), mSavedAsvGroup)));

        for (int i = 0; i < mCurVoltTable.length; i++) {
            SeekBarPreference pref = mFreqPrefList.get(i);
            pref.setValue(1500, 700, mSavedVoltTable[i] == null ?
                    Integer.valueOf(mCurVoltTable[i]) : Integer.valueOf(mSavedVoltTable[i]));
            pref.setSummary(Misc.getCurrentAndSavedValueText(this,
                    mCurVoltTable[i] + "mV",
                    (mSavedVoltTable[i] == null ? getText(R.string.none).toString() : mSavedVoltTable[i] + "mV")));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        addPreferencesFromResource(R.xml.cpu_control_voltage_pref);
        mSetting = new CpuVoltageSetting(this);

        mApplyButton.setOnClickListener(this);

        mCurAsvGroup = mSetting.getAsvGroup();
        mAsvGroup = (ListPreference)findPreference(CpuVoltageSetting.KEY_ASV_GROUP);
        if (Misc.isNullOfEmpty(mCurAsvGroup)) {
            mAsvGroup.setEnabled(false);
        } else {
            mSavedAsvGroup = mSetting.loadAsvGroup();
            mAsvGroup.setValue(mSavedAsvGroup);
            mAsvGroup.setOnPreferenceChangeListener(this);
            mAsvGroup.setSummary(Misc.getCurrentAndSavedValueText(this,
                    Misc.getEntryFromEntryValue(mAsvGroup.getEntries(),mAsvGroup.getEntryValues(), mCurAsvGroup),
                    Misc.getEntryFromEntryValue(mAsvGroup.getEntries(),mAsvGroup.getEntryValues(), mSavedAsvGroup)));
        }
        CpuControlSetting cpuSetting = new CpuControlSetting(this);
        String[] availableFrequencies = cpuSetting.getAvailableFrequencies();
        ArrayList<String> list = new ArrayList<String>();
        for (String freq : availableFrequencies) {
            list.add(String.valueOf(Integer.parseInt(freq) / 1000) + "MHz");
        }
        String[] availableFreqEntries = list.toArray(new String[0]);

        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference(CpuVoltageSetting.KEY_CPU_VOLT_ROOT_PREF);
        if (mSetting.isEnableVoltageControl()) {
            mFreqPrefList = new ArrayList<SeekBarPreference>();
            mCurVoltTable = mSetting.getVoltageTable();
            mSavedVoltTable = new String[mCurVoltTable.length];
            for (int i = 0; i < availableFrequencies.length; i++) {
                SeekBarPreference pref = new SeekBarPreference(this, null);
                String freq = String.valueOf(Integer.parseInt(availableFrequencies[i]) / 1000);
                String key = CpuVoltageSetting.KEY_CPU_VOLT_CTRL_BASE + freq;
                mSavedVoltTable[i] = mSetting.loadVoltage(key);
                pref.setKey(key);
                pref.setTitle(availableFreqEntries[i]);
                pref.setOnPreferenceDoneListener(this);
                rootPref.addPreference(pref);
                mFreqPrefList.add(pref);
            }
            setValues();
        } else {
            PreferenceScreen pref = prefManager.createPreferenceScreen(this);
            pref.setTitle(R.string.not_support);
            rootPref.addPreference(pref);
        }
    }

    @Override
    public void onClick(View v) {
        mApplyButton.setEnabled(false);
        mSetting.setAsvGroup(mSavedAsvGroup);
        mCurAsvGroup = mSavedAsvGroup;
        mCurVoltTable = mSetting.getVoltageTable();
        for (int i = 0; i < mSavedVoltTable.length; i++) {
            if (mSavedVoltTable[i] != null) {
                mCurVoltTable[i] = mSavedVoltTable[i];
            }
        }
        mSetting.setVoltageTable(mCurVoltTable);
        setValues();
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        mApplyButton.setEnabled(true);
        int index = mFreqPrefList.indexOf(preference);
        mSavedVoltTable[index] = newValue;
        preference.setSummary(Misc.getCurrentAndSavedValueText(this,
                mCurVoltTable[index] + "mV", newValue + "mV"));
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.reset_menu, menu);
        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_reset:
            mSetting.reset();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mAsvGroup) {
            if (mCurAsvGroup.equals(newValue.toString())) {
                return false;
            }
            final ConfirmDialog dlg = new ConfirmDialog(this);
            final String newAsvValue = newValue.toString();
            dlg.setResultListener(new ConfirmDialog.ResultListener() {
                @Override
                public void onYes() {
                    mSetting.reset();
                    for (int i = 0; i < mSavedVoltTable.length; i++) {
                        mSavedVoltTable[i] = null;
                    }
                    mSavedAsvGroup = newAsvValue;
                    mSetting.saveAsvGroup(mSavedAsvGroup);
                    mAsvGroup.setValue(mSavedAsvGroup);
                    mAsvGroup.setSummary(Misc.getCurrentAndSavedValueText(mContext,
                            Misc.getEntryFromEntryValue(mAsvGroup.getEntries(),mAsvGroup.getEntryValues(), mCurAsvGroup),
                            Misc.getEntryFromEntryValue(mAsvGroup.getEntries(),mAsvGroup.getEntryValues(), mSavedAsvGroup)));
                    mApplyButton.setEnabled(true);
                }
            });
            dlg.show(this, android.R.string.dialog_alert_title, R.string.cpu_asv_group_change_message);
        }
        return false;
    }
}
