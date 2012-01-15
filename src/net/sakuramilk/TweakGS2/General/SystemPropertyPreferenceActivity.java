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
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SystemPropertyPreferenceActivity extends PreferenceActivity
    implements Preference.OnPreferenceChangeListener {

    private final SystemPropertySetting mSetting = new SystemPropertySetting();
    private CheckBoxPreference mBootSound;
    private CheckBoxPreference mCameraSound;
    private CheckBoxPreference mCrtEffect;
    private CheckBoxPreference mLogger;
    private CheckBoxPreference mCifs;
    private CheckBoxPreference mNtfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.general_system_property_pref);

        boolean value;
        value = mSetting.getBootSound();
        mBootSound = (CheckBoxPreference)findPreference("sysprop_boot_sound");
        mBootSound.setChecked(value);
        mBootSound.setOnPreferenceChangeListener(this);

        value = mSetting.getCameraSound();
        mCameraSound = (CheckBoxPreference)findPreference("sysprop_camera_sound");
        mCameraSound.setChecked(value);
        mCameraSound.setOnPreferenceChangeListener(this);

        value = mSetting.getCrtEffect();
        mCrtEffect = (CheckBoxPreference)findPreference("sysprop_crt_effect");
        mCrtEffect.setChecked(value);
        mCrtEffect.setOnPreferenceChangeListener(this);

        value = mSetting.getLogger();
        mLogger = (CheckBoxPreference)findPreference("sysprop_android_logger");
        mLogger.setChecked(value);
        mLogger.setOnPreferenceChangeListener(this);

        value = mSetting.getCifs();
        mCifs = (CheckBoxPreference)findPreference("sysprop_cifs");
        mCifs.setChecked(value);
        mCifs.setOnPreferenceChangeListener(this);

        value = mSetting.getNtfs();
        mNtfs = (CheckBoxPreference)findPreference("sysprop_ntfs");
        mNtfs.setChecked(value);
        mNtfs.setOnPreferenceChangeListener(this);
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        boolean newValue = (Boolean)objValue;
        if (mBootSound == preference) {
            mSetting.setBootSound(newValue);
            mBootSound.setChecked(newValue);
        } else if (mCameraSound == preference) {
            mSetting.setCameraSound(newValue);
            mCameraSound.setChecked(newValue);
        } else if (mCrtEffect == preference) {
            mSetting.setCrtEffect(newValue);
            mCrtEffect.setChecked(newValue);
        } else if (mLogger == preference) {
            mSetting.setLogger(newValue);
            mLogger.setChecked(newValue);
        } else if (mCifs == preference) {
            mSetting.setCifs(newValue);
            mCifs.setChecked(newValue);
        } else if (mNtfs == preference) {
            mSetting.setNtfs(newValue);
            mNtfs.setChecked(newValue);
        }
        return false;
    }
}
