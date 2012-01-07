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

package net.sakuramilk.TweakGS2.SoundAndVib;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SysFs;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class SoundAndVibPreferenceActivity extends PreferenceActivity implements
    OnPreferenceChangeListener, OnSeekBarPreferenceDoneListener {
    
    SeekBarPreference mVibLevel;
    CheckBoxPreference mIncreaseOnIncoming;
    String mCurVibLevel;
    
    SysFs mSysFsVibLevel = new SysFs("/sys/devices/platform/tspdrv/vibrator_level");
    SysFs mSysFsVibMaxLevel = new SysFs("/sys/devices/platform/tspdrv/vibrator_level_max");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.sound_and_vib_pref);

        mVibLevel = (SeekBarPreference)findPreference("vib_level");
        if (mSysFsVibLevel.exists()) {
            mVibLevel.setEnabled(true);
            String[] maxLevel = mSysFsVibMaxLevel.read();
            String[] curLevel = mSysFsVibLevel.read();
            mCurVibLevel = curLevel[0];
            mVibLevel.setValue(Integer.parseInt(maxLevel[0]), 0, 1, Integer.parseInt(curLevel[0]));
            mVibLevel.setOnPreferenceChangeListener(this);
            mVibLevel.setOnPreferenceDoneListener(this);
            mVibLevel.setSummary(Misc.getCurrentValueText(this, curLevel[0]));
        }
        
        mIncreaseOnIncoming = (CheckBoxPreference)findPreference("vib_increase_on_incoming");
        if (mSysFsVibLevel.exists()) {
            mIncreaseOnIncoming.setEnabled(true);
        }
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        if (mVibLevel == preference) {
            mSysFsVibLevel.write(newValue.toString());
            mVibLevel.setSummary(Misc.getCurrentValueText(this, newValue.toString()));
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (mVibLevel == preference) {
            mSysFsVibLevel.write((String)objValue);
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(60);
            Misc.sleep(60);
            mSysFsVibLevel.write(mCurVibLevel);
        }
        return false;
    }
    
    
}