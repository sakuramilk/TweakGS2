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

package net.sakuramilk.TweakGS2.GpuControl;

import java.util.ArrayList;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.ApplyButtonPreferenceActivity;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class GpuControlPreferenceActivity extends ApplyButtonPreferenceActivity
    implements OnSeekBarPreferenceDoneListener, OnClickListener, OnPreferenceChangeListener {

    private GpuControlSetting mSetting;

    private Integer mFreqs[] = null;
    private Integer mVolts[] = null;
    private int mStep;
    private ArrayList<SeekBarPreference> mFreqPrefList;
    private ArrayList<SeekBarPreference> mVoltPrefList;
    private CheckBoxPreference mSetOnBoot;
    private boolean mSetOnBootChecked;

    private void setMaxMinValue() {
        if (mFreqPrefList != null) {
            for (int i = 0; i < mFreqPrefList.size(); i++) {
                SeekBarPreference pref = mFreqPrefList.get(i);
                pref.setSummary(Misc.getCurrentValueText(this, String.valueOf(mFreqs[i]) + "MHz"));
                if (i == 0) {
                    pref.setValue(mFreqs[i+1], GpuControlSetting.FREQ_MIN, mFreqs[i]);
                } else if (i == (mFreqPrefList.size() - 1)) {
                    pref.setValue(GpuControlSetting.FREQ_MAX, mFreqs[i-1], mFreqs[i]);
                } else {
                    pref.setValue(mFreqs[i+1], mFreqs[i-1], mFreqs[i]);
                }
            }
        }
        if (mVoltPrefList != null) {
            for (int i = 0; i < mVoltPrefList.size(); i++) {
                SeekBarPreference pref = mVoltPrefList.get(i);
                pref.setSummary(Misc.getCurrentValueText(this, String.valueOf(mVolts[i]) + "mV"));
                if (i == 0) {
                    pref.setValue(mVolts[i+1], GpuControlSetting.VOLT_MIN, mVolts[i]);
                } else if (i == (mVoltPrefList.size() - 1)) {
                    pref.setValue(GpuControlSetting.VOLT_MAX, mVolts[i-1], mVolts[i]);
                } else {
                    pref.setValue(mVolts[i+1], mVolts[i-1], mVolts[i]);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.gpu_control_pref);

        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference(GpuControlSetting.KEY_ROOT_PREF);

        mSetting = new GpuControlSetting(this);
        if (mSetting.isEnableFreqCtrl()) {
            mFreqs = mSetting.getFreqs();
            mFreqPrefList = new ArrayList<SeekBarPreference>();
            for (int i = 0; i < mFreqs.length; i++) {
                SeekBarPreference pref = new SeekBarPreference(this, null);
                pref.setKey(GpuControlSetting.KEY_GPU_FREQ_BASE + i);
                pref.setTitle(R.string.frequency);
                pref.setDialogTitle(R.string.frequency);
                pref.setOnPreferenceDoneListener(this);
                mFreqPrefList.add(pref);
            }
            mStep = mFreqs.length;
        }
        if (mSetting.isEnableVoltageCtrl()) {
            mVolts = mSetting.getVolts();
            mVoltPrefList = new ArrayList<SeekBarPreference>();
            for (int i = 0; i < mVolts.length; i++) {
                SeekBarPreference pref = new SeekBarPreference(this, null);
                pref.setKey(GpuControlSetting.KEY_GPU_VOLT_BASE + i);
                pref.setTitle(R.string.voltage);
                pref.setDialogTitle(R.string.voltage);
                pref.setOnPreferenceDoneListener(this);
                mVoltPrefList.add(pref);
            }
            mStep = mVolts.length;
        }

        for (int i = 0; i < mStep; i++) {
            PreferenceCategory categoryPref = new PreferenceCategory(this);
            categoryPref.setTitle("Step" + i);
            rootPref.addPreference(categoryPref);
            if (mFreqPrefList != null) {
                rootPref.addPreference(mFreqPrefList.get(i));
            }
            if (mVoltPrefList != null) {
                rootPref.addPreference(mVoltPrefList.get(i));
            }
        }
        if (mStep > 0) {
            PreferenceCategory categoryPref = new PreferenceCategory(this);
            categoryPref.setTitle(R.string.option);
            mSetOnBoot = new CheckBoxPreference(this);
            mSetOnBoot.setTitle(R.string.set_on_boot);
            rootPref.addPreference(mSetOnBoot);
            mSetOnBootChecked = mSetting.loadSetOnBoot();
            mSetOnBoot.setOnPreferenceChangeListener(this);
            mSetOnBoot.setChecked(mSetOnBootChecked);
        }
        mApplyButton.setOnClickListener(this);
        
        setMaxMinValue();
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {

        int value = Integer.parseInt((String)newValue);
        if (mFreqPrefList != null) {
            int index = mFreqPrefList.indexOf(preference);
            if (index != -1) {
                if (mFreqs[index] != value) {
                    mFreqs[index] = value;
                    setMaxMinValue();
                    mApplyButton.setEnabled(true);
                }
                return false; // don't return true
            }
        }
        if (mVoltPrefList != null) {
            int index = mVoltPrefList.indexOf(preference);
            if (index != -1) {
                if (mVolts[index] != value) {
                    mVolts[index] = value;
                    setMaxMinValue();
                    mApplyButton.setEnabled(true);
                }
                return false; // don't return true
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        mApplyButton.setEnabled(false);
        mSetOnBootChecked = mSetOnBoot.isChecked();
        mSetting.saveSetOnBoot(mSetOnBootChecked);

        // NOTICE: set first volt, next freq
        if (mVolts != null) {
            mSetting.saveVolts(mVolts);
            mSetting.setVolts(mVolts);
        }
        if (mFreqs != null) {
            mSetting.saveFreqs(mFreqs);
            mSetting.setFreqs(mFreqs);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        mApplyButton.setEnabled(true);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mApplyButton.isEnabled()) {
            mSetting.saveSetOnBoot(mSetOnBootChecked);
        }
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
