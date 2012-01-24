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

package net.sakuramilk.TweakGS2.General;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class VirtualMemoryPreferenceActivity extends PreferenceActivity implements OnSeekBarPreferenceDoneListener {

    private VirtualMemorySetting mSetting;

    private SeekBarPreference mSwappiness;
    private SeekBarPreference mVfsCachePressure;
    private SeekBarPreference mDirtyExpireCentisecs;
    private SeekBarPreference mDirtyWritebackCentisecs;
    private SeekBarPreference mDirtyRatio;
    private SeekBarPreference mDirtyBackgroundRatio;

    private void updateValues() {
        String curValue = mSetting.getVmSwappiness();
        String savedValue = mSetting.loadVmSwappiness();
        mSwappiness.setSummary(Misc.getCurrentAndSavedValueText(this, curValue, savedValue));
        mSwappiness.setValue(100, 0, Integer.parseInt(curValue));

        curValue = mSetting.getVmVfsCachePressure();
        savedValue = mSetting.loadVmVfsCachePressure();
        mVfsCachePressure.setSummary(Misc.getCurrentAndSavedValueText(this, curValue, savedValue));
        mVfsCachePressure.setValue(100, 1, Integer.parseInt(curValue));

        curValue = mSetting.getVmDirtyExpireCentisecs();
        savedValue = mSetting.loadVmDirtyExpireCentisecs();
        mDirtyExpireCentisecs.setSummary(Misc.getCurrentAndSavedValueText(this, curValue, savedValue));
        mDirtyExpireCentisecs.setValue(6000, 100, 100, Integer.parseInt(curValue));

        curValue = mSetting.getVmDirtyWritebackCentisecs();
        savedValue = mSetting.loadVmDirtyWritebackCentisecs();
        mDirtyWritebackCentisecs.setSummary(Misc.getCurrentAndSavedValueText(this, curValue, savedValue));
        mDirtyWritebackCentisecs.setValue(6000, 100, 100, Integer.parseInt(curValue));

        curValue = mSetting.getVmDirtyRatio();
        savedValue = mSetting.loadVmDirtyRatio();
        mDirtyRatio.setSummary(Misc.getCurrentAndSavedValueText(this, curValue, savedValue));
        mDirtyRatio.setValue(100, 0, Integer.parseInt(curValue));

        curValue = mSetting.getVmDirtyBackgroundRatio();
        savedValue = mSetting.loadVmDirtyBackgroundRatio();
        mDirtyBackgroundRatio.setSummary(Misc.getCurrentAndSavedValueText(this, curValue, savedValue));
        mDirtyBackgroundRatio.setValue(100, 0, Integer.parseInt(curValue));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.general_vm_pref);

        mSetting = new VirtualMemorySetting(this);
        mSwappiness = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_SWAPPINESS);
        mSwappiness.setOnPreferenceDoneListener(this);
        mVfsCachePressure = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_VFS_CACHE_PRESSURE);
        mVfsCachePressure.setOnPreferenceDoneListener(this);
        mDirtyExpireCentisecs = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_DIRTY_EXPIRE_CENTISECS);
        mDirtyExpireCentisecs.setOnPreferenceDoneListener(this);
        mDirtyWritebackCentisecs = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_DIRTY_WRITEBACK_CENTISECS);
        mDirtyWritebackCentisecs.setOnPreferenceDoneListener(this);
        mDirtyRatio = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_DIRTY_RATIO);
        mDirtyRatio.setOnPreferenceDoneListener(this);
        mDirtyBackgroundRatio = (SeekBarPreference)findPreference(VirtualMemorySetting.KEY_VM_DIRTY_BACKGROUND_RATIO);
        mDirtyBackgroundRatio.setOnPreferenceDoneListener(this);

        updateValues();
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        if (mSwappiness == preference) {
            mSetting.setVmSwappiness(newValue);
            mSwappiness.setSummary(Misc.getCurrentAndSavedValueText(this, newValue, newValue));
            return true;
        } else if (mVfsCachePressure == preference) {
            mSetting.setVmVfsCachePressure(newValue);
            mVfsCachePressure.setSummary(Misc.getCurrentAndSavedValueText(this, newValue, newValue));
            return true;
        } else if (mDirtyExpireCentisecs == preference) {
            mSetting.setVmDirtyExpireCentisecs(newValue);
            mDirtyExpireCentisecs.setSummary(Misc.getCurrentAndSavedValueText(this, newValue, newValue));
            return true;
        } else if (mDirtyWritebackCentisecs == preference) {
            mSetting.setVmDirtyWritebackCentisecs(newValue);
            mDirtyWritebackCentisecs.setSummary(Misc.getCurrentAndSavedValueText(this, newValue, newValue));
            return true;
        } else if (mDirtyRatio == preference) {
            mSetting.setVmDirtyRatio(newValue);
            mDirtyRatio.setSummary(Misc.getCurrentAndSavedValueText(this, newValue, newValue));
            return true;
        } else if (mDirtyBackgroundRatio == preference) {
            mSetting.setVmDirtyBackgroundRatio(newValue);
            mDirtyBackgroundRatio.setSummary(Misc.getCurrentAndSavedValueText(this, newValue, newValue));
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_reset:
            mSetting.reset();
            Misc.confirmReboot(this, R.string.reboot_reflect_comfirm);
            return true;
        case R.id.menu_recommend:
            mSetting.setRecommend();
            updateValues();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
