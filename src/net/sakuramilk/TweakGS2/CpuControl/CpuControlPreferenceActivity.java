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

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;

public class CpuControlPreferenceActivity extends PreferenceActivity
    implements OnPreferenceChangeListener, OnPreferenceClickListener {

    private CpuControlSetting mSetting;
    private ListPreference mGovernorList;
    private PreferenceScreen mGovernorSetting;
    private ListPreference mFreqMax;
    private ListPreference mFreqMin;

    private String getFreqText(String value) {
        return (Integer.valueOf(value) / 1000) + "MHz"; 
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cpu_control_pref);
        mSetting = new CpuControlSetting(this);

        // governor
        mGovernorList = (ListPreference)findPreference(CpuControlSetting.KEY_CPU_GOV_LIST);
        String[] availableGovernors = mSetting.getAvailableGovernors();
        mGovernorList.setEntries(availableGovernors);
        mGovernorList.setEntryValues(availableGovernors);
        String scalingGovernor = mSetting.getScalingGovernor();
        mGovernorList.setSummary(Misc.getCurrentValueText(this, scalingGovernor));
        mGovernorList.setValue(scalingGovernor);
        mGovernorList.setOnPreferenceChangeListener(this);

        mGovernorSetting = (PreferenceScreen)findPreference(CpuControlSetting.KEY_CPU_GOV_SETTING);
        mGovernorSetting.setOnPreferenceClickListener(this);

        // cpu freq
        String[] availableFrequencies = mSetting.getAvailableFrequencies();
        String maxFreqValue = mSetting.getScalingMaxFreq();
        String minFreqValue = mSetting.getScalingMinFreq();
        String[] availableFreqEntries = Misc.getFreqencyEntries(availableFrequencies);

        mFreqMax = (ListPreference)findPreference(CpuControlSetting.KEY_CPU_MAX_FREQ);
        mFreqMax.setEntries(availableFreqEntries);
        mFreqMax.setEntryValues(availableFrequencies);
        mFreqMax.setValue(maxFreqValue);
        mFreqMax.setOnPreferenceChangeListener(this);
        mFreqMax.setSummary(Misc.getCurrentValueText(this, getFreqText(maxFreqValue)));

        mFreqMin = (ListPreference)findPreference(CpuControlSetting.KEY_CPU_MIN_FREQ);
        mFreqMin.setEntries(availableFreqEntries);
        mFreqMin.setEntryValues(availableFrequencies);
        mFreqMin.setValue(minFreqValue);
        mFreqMin.setOnPreferenceChangeListener(this);
        mFreqMin.setSummary(Misc.getCurrentValueText(this, getFreqText(minFreqValue)));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mGovernorList) {
            mSetting.setScalingGovernor(newValue.toString());
            mGovernorList.setSummary(Misc.getCurrentValueText(this, newValue.toString()));
            return true;

        } else if (preference == mFreqMax) {
            mSetting.setScalingMaxFreq(newValue.toString());
            mFreqMax.setSummary(Misc.getCurrentValueText(this, getFreqText(newValue.toString())));
            return true;

        } else if (preference == mFreqMin) {
            mSetting.setScalingMinFreq(newValue.toString());
            mFreqMin.setSummary(Misc.getCurrentValueText(this, getFreqText(newValue.toString())));
            return true;

        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mGovernorSetting) {
            Intent intent = new Intent(getApplicationContext(), CpuGovernorPreferenceActivity.class);
            intent.putExtra("governor", mSetting.getScalingGovernor());
            this.startActivity(intent);
        }
        return false;
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
            Misc.confirmReboot(this, R.string.reboot_reflect_comfirm);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
