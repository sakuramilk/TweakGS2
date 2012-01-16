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

    private SoundAndVibSetting mSetting;
    private CheckBoxPreference mSoundPlayFreqLock;
    private SeekBarPreference mVibLevel;
    private CheckBoxPreference mIncreaseOnIncoming;
    private String mCurVibLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.sound_and_vib_pref);

        mSetting = new SoundAndVibSetting(this);
        mSoundPlayFreqLock = (CheckBoxPreference)findPreference(SoundAndVibSetting.KEY_SND_PLAY_CPU_LOCK);
        if (mSetting.isEnableSoundPlayFreqLock()) {
            mSoundPlayFreqLock.setEnabled(true);
        }

        if (Misc.isAospRom()) {
            mVibLevel = (SeekBarPreference)findPreference(SoundAndVibSetting.KEY_VIB_LEVEL);
            if (mSetting.isEnableVibLevel()) {
                mVibLevel.setEnabled(true);
                String maxLevel = mSetting.getVibMaxLevel();
                String curLevel = mSetting.getVibLevel();
                mCurVibLevel = curLevel;
                mVibLevel.setValue(Integer.parseInt(maxLevel), 0, Integer.parseInt(curLevel));
                mVibLevel.setOnPreferenceChangeListener(this);
                mVibLevel.setOnPreferenceDoneListener(this);
                mVibLevel.setSummary(Misc.getCurrentValueText(this, curLevel));
            }
            
            mIncreaseOnIncoming = (CheckBoxPreference)findPreference(SoundAndVibSetting.KEY_VIB_INCREASE_ON_INCOMING);
            if (mSetting.isEnableVibLevel()) {
                mIncreaseOnIncoming.setEnabled(true);
            }
        }
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        if (mVibLevel == preference) {
            mSetting.setVibLevel(newValue);
            mVibLevel.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (mVibLevel == preference) {
            mSetting.setVibLevel(objValue.toString());
            Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vib.vibrate(60);
            Misc.sleep(60);
            mSetting.setVibLevel(mCurVibLevel);
        }
        return false;
    }
}
