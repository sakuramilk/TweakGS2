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
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;

public class CpuVoltagePreferenceActivity extends ApplyButtonPreferenceActivity
    implements OnSeekBarPreferenceDoneListener, OnClickListener {

    private CpuVoltageSetting mSetting;
    private ArrayList<SeekBarPreference> mFreqPrefList;
    String[] mCurVoltTable;
    String[] mSavedVoltTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cpu_control_voltage_pref);
        mSetting = new CpuVoltageSetting(this);

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
            int i = 0;
            for (String curVolt : mCurVoltTable) {
                SeekBarPreference pref = new SeekBarPreference(this, null);
                String freq = String.valueOf(Integer.parseInt(availableFrequencies[i]) / 1000);
                String key = CpuVoltageSetting.KEY_CPU_VOLT_CTRL_BASE + freq;
                mSavedVoltTable[i] = mSetting.loadVoltage(key);
                pref.setKey(key);
                pref.setValue(1500, 700, mSavedVoltTable[i] == null ?
                        Integer.valueOf(curVolt) : Integer.valueOf(mSavedVoltTable[i]));
                pref.setTitle(availableFreqEntries[i]);
                pref.setSummary(Misc.getCurrentAndSavedValueText(this,
                        curVolt + "mV",
                        (mSavedVoltTable[i] == null ? getText(R.string.none).toString() : mSavedVoltTable[i] + "mV")));
                pref.setOnPreferenceDoneListener(this);
                rootPref.addPreference(pref);
                mFreqPrefList.add(pref);
                i++;
            }
        } else {
            PreferenceScreen pref = prefManager.createPreferenceScreen(this);
            pref.setTitle(R.string.not_support);
            rootPref.addPreference(pref);
        }
    }

    @Override
    public void onClick(View v) {
        mApplyButton.setEnabled(false);
        for (int i = 0; i < mSavedVoltTable.length; i++) {
            if (mSavedVoltTable[i] != null) {
                mCurVoltTable[i] = mSavedVoltTable[i];
            }
        }
        mSetting.setVoltageTable(mCurVoltTable);
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
}
