/*
* Copyright 2011 sakuramilk <c.sakuramilk@gmail.com>
* 
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License as
* published by the Free Software Foundation; either version 2 of 
* the License, or (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.sakuramilk.TweakGS2.GpuControl;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.ApplyButtonPreferenceActivity;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

public class GpuControlPreferenceActivity extends ApplyButtonPreferenceActivity
    implements OnSeekBarPreferenceDoneListener, OnClickListener, OnPreferenceChangeListener {

    private GpuControlSetting mSetting;

    private SeekBarPreference mHighFreq;
    private SeekBarPreference mHighVolt;
    private SeekBarPreference mLowFreq;
    private SeekBarPreference mLowVolt;
    private CheckBoxPreference mSetOnBoot;

    private static final int MIN_CLOCK_GPU = 10;
    private static final int MAX_CLOCK_GPU = 450;
    private static final int MIN_VOLTAGE_GPU = 800000 / 1000;
    private static final int MAX_VOLTAGE_GPU = 1200000 / 1000;
    private static final int CLOCK_STEP = 1;
    private static final int VOLTATE_STEP = 1;

    private int mLowFreqValue;
    private int mHighFreqValue;
    private int mLowVoltValue;
    private int mHighVoltValue;
    private boolean mSetOnBootChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.gpu_control_pref);

        mSetting = new GpuControlSetting(this);
        /*
        mHighFreq = (SeekBarPreference)findPreference(GpuControlSetting.KEY_GPU_HIGH_FREQ);
        mHighVolt = (SeekBarPreference)findPreference(GpuControlSetting.KEY_GPU_HIGH_VOLT);
        mLowFreq = (SeekBarPreference)findPreference(GpuControlSetting.KEY_GPU_LOW_FREQ);
        mLowVolt = (SeekBarPreference)findPreference(GpuControlSetting.KEY_GPU_LOW_VOLT);
        */
        mSetOnBoot = (CheckBoxPreference)findPreference(GpuControlSetting.KEY_GPU_SET_ON_BOOT);

        if (mSetting.isEnableFreqCtrl()) {
            mHighFreq.setEnabled(true);
            mLowFreq.setEnabled(true);
            Integer[] values = mSetting.getFreq();
            mHighFreqValue = values[0];
            mLowFreqValue = values[1];
            mLowFreq.setSummary(Misc.getCurrentValueText(this, mLowFreqValue + "MHz"));
            mHighFreq.setSummary(Misc.getCurrentValueText(this, mHighFreqValue + "MHz"));
            
            mLowFreq.setValue(mHighFreqValue, MIN_CLOCK_GPU, CLOCK_STEP, mLowFreqValue);
            mLowFreq.setOnPreferenceDoneListener(this);
            mHighFreq.setValue(MAX_CLOCK_GPU, mLowFreqValue, CLOCK_STEP, mHighFreqValue);
            mHighFreq.setOnPreferenceDoneListener(this);
        }
        if (mSetting.isEnableVoltageCtrl()) {
            mHighVolt.setEnabled(true);
            mLowVolt.setEnabled(true);
            Integer[] values = mSetting.getVolt();
            mHighVoltValue = values[0];
            mLowVoltValue = values[1];
            mLowVolt.setSummary(Misc.getCurrentValueText(this, mLowVoltValue + "mV"));
            mHighVolt.setSummary(Misc.getCurrentValueText(this, mHighVoltValue + "mV"));

            mLowVolt.setValue(mHighVoltValue, MIN_VOLTAGE_GPU, VOLTATE_STEP, mLowVoltValue);
            mLowVolt.setOnPreferenceDoneListener(this);
            mHighVolt.setValue(MAX_VOLTAGE_GPU, mLowVoltValue, VOLTATE_STEP, mHighVoltValue);
            mHighVolt.setOnPreferenceDoneListener(this);
        }

        mSetOnBootChecked = mSetting.loadSetOnBoot();
        mSetOnBoot.setOnPreferenceChangeListener(this);
        mSetOnBoot.setChecked(mSetOnBootChecked);

        mApplyButton.setOnClickListener(this);
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {

        int value = Integer.parseInt((String)newValue);

        if (preference == mLowFreq) {
            if (value != mLowFreqValue) {
                mApplyButton.setEnabled(true);
                mLowFreqValue = value;

                mLowFreq.setSummary(Misc.getCurrentValueText(this, mLowFreqValue + "MHz"));
                mLowFreq.setValue(mHighFreqValue, MIN_CLOCK_GPU,  mLowFreqValue);
                mHighFreq.setValue(MAX_CLOCK_GPU, mLowFreqValue,  mHighFreqValue);
            }
        } else if (preference == mHighFreq) {
            if (value != mHighFreqValue) {
                mApplyButton.setEnabled(true);
                mHighFreqValue = value;

                mHighFreq.setSummary(Misc.getCurrentValueText(this, mHighFreqValue + "MHz"));
                mLowFreq.setValue(mHighFreqValue, MIN_CLOCK_GPU,  mLowFreqValue);
                mHighFreq.setValue(MAX_CLOCK_GPU, mLowFreqValue,  mHighFreqValue);
            }
        } else if (preference == mLowVolt) {
            if (value != mLowVoltValue) {
                mApplyButton.setEnabled(true);
                mLowVoltValue = value;

                mLowVolt.setSummary(Misc.getCurrentValueText(this, mLowVoltValue + "mV"));
                mLowVolt.setValue(mHighVoltValue, MIN_VOLTAGE_GPU, mLowVoltValue);
                mHighVolt.setValue(MAX_VOLTAGE_GPU, mLowVoltValue, mHighVoltValue);
            }
        } else if (preference == mHighVolt) {
            if (value != mHighVoltValue) {
                mApplyButton.setEnabled(true);
                mHighVoltValue = value;

                mHighVolt.setSummary(Misc.getCurrentValueText(this, mHighVoltValue + "mV"));
                mLowVolt.setValue(mHighVoltValue, MIN_VOLTAGE_GPU, mLowVoltValue);
                mHighVolt.setValue(MAX_VOLTAGE_GPU, mLowVoltValue, mHighVoltValue);
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        mApplyButton.setEnabled(false);
        mSetOnBootChecked = mSetOnBoot.isChecked();
        mSetting.saveSetOnBoot(mSetOnBootChecked);
        /*
        mSetting.saveFreq(mHighFreqValue, mLowFreqValue);
        mSetting.saveVolt(mHighVoltValue, mLowVoltValue);
        mSetting.setFreq(mHighFreqValue, mLowFreqValue);
        mSetting.setVolt(mHighVoltValue, mLowVoltValue);
        */
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
}
