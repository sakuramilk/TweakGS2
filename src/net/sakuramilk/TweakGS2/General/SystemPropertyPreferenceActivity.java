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
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SystemPropertyPreferenceActivity extends PreferenceActivity
    implements Preference.OnPreferenceChangeListener, OnSeekBarPreferenceDoneListener {

    private SystemPropertySetting mSetting;
    private CheckBoxPreference mBootSound;
    private CheckBoxPreference mCameraSound;
    private SeekBarPreference mLcdDensity;
    private CheckBoxPreference mCrtEffect;
    private CheckBoxPreference mLogger;
    private CheckBoxPreference mCifs;
    private CheckBoxPreference mNtfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.general_system_property_pref);

        mSetting = new SystemPropertySetting(this);

        boolean value;
        value = mSetting.getBootSound();
        mBootSound = (CheckBoxPreference)findPreference(SystemPropertySetting.KEY_BOOT_SOUND);
        mBootSound.setChecked(value);
        mBootSound.setOnPreferenceChangeListener(this);

        value = mSetting.getCameraSound();
        mCameraSound = (CheckBoxPreference)findPreference(SystemPropertySetting.KEY_CAMERA_SOUND);
        mCameraSound.setChecked(value);
        mCameraSound.setOnPreferenceChangeListener(this);
        
        String lcdDensity = mSetting.getLcdDensity();
        mLcdDensity = (SeekBarPreference)findPreference(SystemPropertySetting.KEY_LCD_DENSITY);
        mLcdDensity.setValue(300, 120, Integer.valueOf(lcdDensity));
        mLcdDensity.setSummary(Misc.getCurrentValueText(this, lcdDensity));
        mLcdDensity.setOnPreferenceDoneListener(this);

        value = mSetting.getCrtEffect();
        mCrtEffect = (CheckBoxPreference)findPreference(SystemPropertySetting.KEY_CRT_EFFECT);
        mCrtEffect.setChecked(value);
        mCrtEffect.setOnPreferenceChangeListener(this);

        value = mSetting.getLogger();
        mLogger = (CheckBoxPreference)findPreference(SystemPropertySetting.KEY_LOGGER);
        mLogger.setChecked(value);
        mLogger.setOnPreferenceChangeListener(this);

        value = mSetting.getCifs();
        mCifs = (CheckBoxPreference)findPreference(SystemPropertySetting.KEY_CIFS);
        mCifs.setChecked(value);
        mCifs.setOnPreferenceChangeListener(this);

        value = mSetting.getNtfs();
        mNtfs = (CheckBoxPreference)findPreference(SystemPropertySetting.KEY_NTFS);
        mNtfs.setChecked(value);
        mNtfs.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        boolean newValue = (Boolean)objValue;
        if (mBootSound == preference) {
            mSetting.setBootSound(newValue);
            mBootSound.setChecked(newValue);
            // not return true
        } else if (mCameraSound == preference) {
            mSetting.setCameraSound(newValue);
            mCameraSound.setChecked(newValue);
            // not return true
        } else if (mCrtEffect == preference) {
            mSetting.setCrtEffect(newValue);
            mCrtEffect.setChecked(newValue);
            // not return true
        } else if (mLogger == preference) {
            mSetting.setLogger(newValue);
            mLogger.setChecked(newValue);
            // not return true
        } else if (mCifs == preference) {
            mSetting.setCifs(newValue);
            mCifs.setChecked(newValue);
            // not return true
        } else if (mNtfs == preference) {
            mSetting.setNtfs(newValue);
            mNtfs.setChecked(newValue);
            // not return true
        }
        return false;
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        if (mLcdDensity == preference) {
            mSetting.setLcdDensity(newValue);
            mLcdDensity.setSummary(Misc.getCurrentValueText(this, newValue));
            // not return true
        }
        return false;
    }
}
