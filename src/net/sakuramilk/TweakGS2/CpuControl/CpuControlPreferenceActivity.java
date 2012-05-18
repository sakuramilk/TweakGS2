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
    private ListPreference mMaxFreq;
    private ListPreference mMinFreq;
    private ListPreference mMaxSuspendFreq;
    private ListPreference mMinSuspendFreq;

    private String getFreqText(String value) {
        if (value == null) { 
            return getText(R.string.none).toString();
        } else {
            return (Integer.valueOf(value) / 1000) + "MHz";
        }
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
        String curMaxFreqValue = mSetting.getScalingMaxFreq();
        String curMinFreqValue = mSetting.getScalingMinFreq();
        String savedMaxFreqValue = mSetting.loadScalingMaxFreq();
        String savedMinFreqValue = mSetting.loadScalingMinFreq();
        String[] availableFreqEntries = Misc.getFreqencyEntries(availableFrequencies);

        mMaxFreq = (ListPreference)findPreference(CpuControlSetting.KEY_CPU_MAX_FREQ);
        mMaxFreq.setEntries(availableFreqEntries);
        mMaxFreq.setEntryValues(availableFrequencies);
        mMaxFreq.setValue(curMaxFreqValue);
        mMaxFreq.setOnPreferenceChangeListener(this);
        mMaxFreq.setSummary(Misc.getCurrentAndSavedValueText(
                this, getFreqText(curMaxFreqValue), getFreqText(savedMaxFreqValue)));

        mMinFreq = (ListPreference)findPreference(CpuControlSetting.KEY_CPU_MIN_FREQ);
        mMinFreq.setEntries(availableFreqEntries);
        mMinFreq.setEntryValues(availableFrequencies);
        mMinFreq.setValue(curMinFreqValue);
        mMinFreq.setOnPreferenceChangeListener(this);
        mMinFreq.setSummary(Misc.getCurrentAndSavedValueText(
                this, getFreqText(curMinFreqValue), getFreqText(savedMinFreqValue)));

        if (mSetting.isEnableSuspendFreq()) {
            String curMaxSuspendFreqValue = mSetting.getScalingMaxSuspendFreq();
            String curMinSuspendFreqValue = mSetting.getScalingMinSuspendFreq();
            String savedMaxSuspendFreqValue = mSetting.loadScalingMaxSuspendFreq();
            String savedMinSuspendFreqValue = mSetting.loadScalingMinSuspendFreq();

            mMaxSuspendFreq = (ListPreference)findPreference(CpuControlSetting.KEY_CPU_MAX_SUSPEND_FREQ);
            mMaxSuspendFreq.setEnabled(true);
            mMaxSuspendFreq.setEntries(availableFreqEntries);
            mMaxSuspendFreq.setEntryValues(availableFrequencies);
            mMaxSuspendFreq.setValue(curMaxSuspendFreqValue);
            mMaxSuspendFreq.setOnPreferenceChangeListener(this);
            mMaxSuspendFreq.setSummary(Misc.getCurrentAndSavedValueText(
                    this, getFreqText(curMaxSuspendFreqValue), getFreqText(savedMaxSuspendFreqValue)));

            mMinSuspendFreq = (ListPreference)findPreference(CpuControlSetting.KEY_CPU_MIN_SUSPEND_FREQ);
            mMinSuspendFreq.setEnabled(true);
            mMinSuspendFreq.setEntries(availableFreqEntries);
            mMinSuspendFreq.setEntryValues(availableFrequencies);
            mMinSuspendFreq.setValue(curMinSuspendFreqValue);
            mMinSuspendFreq.setOnPreferenceChangeListener(this);
            mMinSuspendFreq.setSummary(Misc.getCurrentAndSavedValueText(
                    this, getFreqText(curMinSuspendFreqValue), getFreqText(savedMinSuspendFreqValue)));
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mGovernorList) {
            String value = newValue.toString();
            mSetting.setScalingGovernor(value);
            mSetting.saveScalingGovernor(value);
            mGovernorList.setSummary(Misc.getCurrentValueText(this, value));
            return false;

        } else if (preference == mMaxFreq) {
            String value = newValue.toString();
            mSetting.setScalingMaxFreq(value);
            mSetting.saveScalingMaxFreq(value);
            mMaxFreq.setSummary(Misc.getCurrentAndSavedValueText(
                    this, getFreqText(value), getFreqText(value)));
            return false;

        } else if (preference == mMinFreq) {
            String value = newValue.toString();
            mSetting.setScalingMinFreq(value);
            mSetting.saveScalingMinFreq(value);
            mMinFreq.setSummary(Misc.getCurrentAndSavedValueText(
                    this, getFreqText(value), getFreqText(value)));
            return false;

        } else if (preference == mMaxSuspendFreq) {
            String value = newValue.toString();
            mSetting.setScalingMaxSuspendFreq(value);
            mSetting.saveScalingMaxSuspendFreq(value);
            mMaxSuspendFreq.setSummary(Misc.getCurrentAndSavedValueText(
                    this, getFreqText(value), getFreqText(value)));
            return false;

        } else if (preference == mMinSuspendFreq) {
            String value = newValue.toString();
            mSetting.setScalingMinSuspendFreq(value);
            mSetting.saveScalingMinSuspendFreq(value);
            mMinSuspendFreq.setSummary(Misc.getCurrentAndSavedValueText(
                    this, getFreqText(value), getFreqText(value)));
            return false;

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
