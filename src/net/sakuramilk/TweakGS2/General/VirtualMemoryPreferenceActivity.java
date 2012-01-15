/*
 * Copyright (C) 2011 sakuramilk
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

package net.sakuramilk.TweakGS2.General;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class VirtualMemoryPreferenceActivity extends PreferenceActivity implements OnSeekBarPreferenceDoneListener {

    private VirtualMemorySetting mSetting;

    private SeekBarPreference mSwappiness;
    private SeekBarPreference mVfsCachePressure;
    private SeekBarPreference mDirtyExpireCentisecs;
    private SeekBarPreference mDirtyWritebackCentisecs;
    private SeekBarPreference mDirtyRatio;
    private SeekBarPreference mDirtyBackgroundRatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.general_vm_pref);

        mSetting = new VirtualMemorySetting(this);

        String curValue = mSetting.getVmSwappiness();
        mSwappiness = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_SWAPPINESS);
        mSwappiness.setSummary(Misc.getCurrentValueText(this, curValue));
        mSwappiness.setValue(100, 0, Integer.parseInt(curValue));
        mSwappiness.setOnPreferenceDoneListener(this);

        curValue = mSetting.getVmVfsCachePressure();
        mVfsCachePressure = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_VFS_CACHE_PRESSURE);
        mVfsCachePressure.setSummary(Misc.getCurrentValueText(this, curValue));
        mVfsCachePressure.setValue(100, 1, Integer.parseInt(curValue));
        mVfsCachePressure.setOnPreferenceDoneListener(this);

        curValue = mSetting.getVmDirtyExpireCentisecs();
        mDirtyExpireCentisecs = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_DIRTY_EXPIRE_CENTISECS);
        mDirtyExpireCentisecs.setSummary(Misc.getCurrentValueText(this, curValue));
        mDirtyExpireCentisecs.setValue(6000, 100, 100, Integer.parseInt(curValue));
        mDirtyExpireCentisecs.setOnPreferenceDoneListener(this);

        curValue = mSetting.getVmDirtyWritebackCentisecs();
        mDirtyWritebackCentisecs = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_DIRTY_WRITEBACK_CENTISECS);
        mDirtyWritebackCentisecs.setSummary(Misc.getCurrentValueText(this, curValue));
        mDirtyWritebackCentisecs.setValue(6000, 100, 100, Integer.parseInt(curValue));
        mDirtyWritebackCentisecs.setOnPreferenceDoneListener(this);

        curValue = mSetting.getVmDirtyRatio();
        mDirtyRatio = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_DIRTY_RATIO);
        mDirtyRatio.setSummary(Misc.getCurrentValueText(this, curValue));
        mDirtyRatio.setValue(100, 0, Integer.parseInt(curValue));
        mDirtyRatio.setOnPreferenceDoneListener(this);

        curValue = mSetting.getVmDirtyBackgroundRatio();
        mDirtyBackgroundRatio = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_DIRTY_BACKGROUND_RATIO);
        mDirtyBackgroundRatio.setSummary(Misc.getCurrentValueText(this, curValue));
        mDirtyBackgroundRatio.setValue(100, 0, Integer.parseInt(curValue));
        mDirtyBackgroundRatio.setOnPreferenceDoneListener(this);
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        if (mSwappiness == preference) {
            mSetting.setVmSwappiness(newValue);
            mSwappiness.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;
        } else if (mVfsCachePressure == preference) {
            mSetting.setVmVfsCachePressure(newValue);
            mVfsCachePressure.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;
        } else if (mDirtyExpireCentisecs == preference) {
            mSetting.setVmDirtyExpireCentisecs(newValue);
            mDirtyExpireCentisecs.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;
        } else if (mDirtyWritebackCentisecs == preference) {
            mSetting.setVmDirtyWritebackCentisecs(newValue);
            mDirtyWritebackCentisecs.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;
        } else if (mDirtyRatio == preference) {
            mSetting.setVmDirtyRatio(newValue);
            mDirtyRatio.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;
        } else if (mDirtyBackgroundRatio == preference) {
            mSetting.setVmDirtyBackgroundRatio(newValue);
            mDirtyBackgroundRatio.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;
        }
        return false;
    }
}
