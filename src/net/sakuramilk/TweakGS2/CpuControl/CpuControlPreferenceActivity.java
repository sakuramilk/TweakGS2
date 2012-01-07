/*
 * Copyright (C) 2011 sakuramilk <c.sakuramilk@gmail.com>
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

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.BootProperty;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SysFs;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class CpuControlPreferenceActivity extends PreferenceActivity
    implements OnPreferenceChangeListener, OnPreferenceClickListener {

    private static final String TAG = "CpuControlPreferenceActivity";
    
    private ListPreference mGovernorList;
    private PreferenceScreen mGovernorSetting;
    private CheckBoxPreference mGorvenorSetOnBoot;
    private ListPreference mFreqMax;
    private ListPreference mFreqMin;
    private CheckBoxPreference mFreqSetOnBoot;
    
    private static final String CRTL_PATH = "/sys/devices/system/cpu/cpu0/cpufreq"; 
    private SysFs mSysFsAvailableGovernors = new SysFs(CRTL_PATH + "/scaling_available_governors");
    private SysFs mSysFsScalingGovernor = new SysFs(CRTL_PATH + "/scaling_governor");
    private SysFs mSysFsCpuFreqTable = new SysFs("/sys/power/cpufreq_table");
    private SysFs mSysFsScalingMaxFreq = new SysFs(CRTL_PATH + "/scaling_max_freq");
    private SysFs mSysFsScalingMinFreq = new SysFs(CRTL_PATH + "/scaling_min_freq");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cpu_control_pref);
        
        // governor
        mGovernorList = (ListPreference)findPreference("cpu_governor_list");
        String[] availableGovernors = mSysFsAvailableGovernors.read();
        String[] values = availableGovernors[0].split(" ");
        
        mGovernorList.setEntries(values);
        mGovernorList.setEntryValues(values);
        String[] scalingGovernor = mSysFsScalingGovernor.read();
        mGovernorList.setSummary(Misc.getCurrentValueText(this, scalingGovernor[0]));
        mGovernorList.setValue(scalingGovernor[0]);
        mGovernorList.setOnPreferenceChangeListener(this);
        
        mGovernorSetting = (PreferenceScreen)findPreference("cpu_governor_setting");
        mGovernorSetting.setOnPreferenceClickListener(this);
        
        mGorvenorSetOnBoot = (CheckBoxPreference)findPreference("cpu_governor_set_on_boot");
        mGorvenorSetOnBoot.setOnPreferenceChangeListener(this);
        
        // cpu freq
        String[] freqTable = mSysFsCpuFreqTable.read();
        values = freqTable[0].split(" ");
        String[] maxFreqValue = mSysFsScalingMaxFreq.read();
        String[] minFreqValue = mSysFsScalingMinFreq.read();
        
        mFreqMax = (ListPreference)findPreference("cpu_max_freq");
        mFreqMax.setEntries(values);
        mFreqMax.setEntryValues(values);
        mFreqMax.setValue(maxFreqValue[0]);
        mFreqMax.setOnPreferenceChangeListener(this);
        mFreqMax.setSummary(Misc.getCurrentValueText(this, maxFreqValue[0]));
        
        mFreqMin = (ListPreference)findPreference("cpu_min_freq");
        mFreqMin.setEntries(values);
        mFreqMin.setEntryValues(values);
        mFreqMin.setValue(minFreqValue[0]);
        mFreqMin.setOnPreferenceChangeListener(this);
        mFreqMin.setSummary(Misc.getCurrentValueText(this, minFreqValue[0]));
        
        mFreqSetOnBoot = (CheckBoxPreference)findPreference("cpu_freq_set_on_boot");
        mFreqSetOnBoot.setOnPreferenceChangeListener(this);
        
        // voltage
        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference("cpu_control_root");
        PreferenceScreen pref = prefManager.createPreferenceScreen(this);
        pref.setTitle("voltage level1");
        rootPref.addPreference(pref);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mGovernorList) {
            mSysFsScalingGovernor.write(newValue.toString());
            mGovernorList.setSummary(Misc.getCurrentValueText(this, newValue.toString()));
            return true;
            
        } else if (preference == mGorvenorSetOnBoot) {
            return true;
            
        } else if (preference == mFreqMax) {
            mSysFsScalingMaxFreq.write(newValue.toString());
            mFreqMax.setSummary(Misc.getCurrentValueText(this, newValue.toString()));
            return true;
            
        } else if (preference == mFreqMin) {
            mSysFsScalingMinFreq.write(newValue.toString());
            mFreqMin.setSummary(Misc.getCurrentValueText(this, newValue.toString()));
            return true;
            
        } else if (preference == mFreqSetOnBoot) {
            return true;
            
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference arg0) {
        // TODO 自動生成されたメソッド・スタブ
        return false;
    }
}
