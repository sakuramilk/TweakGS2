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

package net.sakuramilk.TweakGS2.Display;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class DisplayPreferenceActivity extends PreferenceActivity implements
    Preference.OnPreferenceChangeListener, OnSeekBarPreferenceDoneListener {

    private DisplaySetting mSetting;

    private ListPreference mLcdType;
    private SeekBarPreference mLcdGamma;
    private CheckBoxPreference mMdnieForceDisable;
    private CheckBoxPreference mMdnieMode;
    private SeekBarPreference mMdnieColorCb;
    private SeekBarPreference mMdnieColorCr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.display_pref);
        mSetting = new DisplaySetting(this);

        mLcdType = (ListPreference)findPreference(DisplaySetting.KEY_LCD_TYPE);
        mLcdGamma = (SeekBarPreference)findPreference(DisplaySetting.KEY_LCD_GAMMA);
        mMdnieForceDisable = (CheckBoxPreference)findPreference(DisplaySetting.KEY_MDNIE_FORCE_DISABLE);
        mMdnieMode = (CheckBoxPreference)findPreference(DisplaySetting.KEY_MDNIE_MODE);
        mMdnieColorCb = (SeekBarPreference)findPreference(DisplaySetting.KEY_MDNIE_MCM_CB);
        mMdnieColorCr = (SeekBarPreference)findPreference(DisplaySetting.KEY_MDNIE_MCM_CR);

        if (mSetting.isEnableLcdType()) {
            mLcdType.setEnabled(true);
            mLcdType.setOnPreferenceChangeListener(this);
            String value = mSetting.getLcdType();
            mLcdType.setSummary(Misc.getCurrentValueText(this, value));
        }

        if (mSetting.isEnableLcdGamma()) {
            mLcdGamma.setEnabled(true);
            String value = mSetting.getLcdGamma();
            mLcdGamma.setSummary(Misc.getCurrentValueText(this, value));
            mLcdGamma.setValue(50, -50, Integer.parseInt(value));
            mLcdGamma.setOnPreferenceDoneListener(this);
        }
        
        boolean isMdnieForceDisable = false;
        if (mSetting.isEnableMdnieForceDisable()) {
            mMdnieForceDisable.setEnabled(true);
            mMdnieForceDisable.setOnPreferenceChangeListener(this);
            String value = mSetting.getMdnieForceDisable();
            isMdnieForceDisable = "0".equals(value) ? false : true;
        }

        boolean isMdnieMcmEnable = false;
        if (mSetting.isEnableMdnieMode()) {
            mMdnieMode.setEnabled(!isMdnieForceDisable);
            mMdnieMode.setOnPreferenceChangeListener(this);
            String value = mSetting.getMdnieMode();
            isMdnieMcmEnable = DisplaySetting.MDNIE_MCM_ENABLE.equals(value) ? true : false;
        }

        if (mSetting.isEnableMdnieMcmCb()) {
            mMdnieColorCb.setEnabled(isMdnieForceDisable & isMdnieMcmEnable);
            mMdnieColorCb.setOnPreferenceDoneListener(this);
            String value = mSetting.getMdnieMcmCb();
            mMdnieColorCb.setSummary(Misc.getCurrentValueText(this, value));
            mMdnieColorCb.setValue(148, 108, Integer.parseInt(value));
        }

        if (mSetting.isEnableMdnieMcmCr()) {
            mMdnieColorCr.setEnabled(isMdnieForceDisable & isMdnieMcmEnable);
            mMdnieColorCr.setOnPreferenceDoneListener(this);
            String value = mSetting.getMdnieMcmCr();
            mMdnieColorCr.setSummary(Misc.getCurrentValueText(this, value));
            mMdnieColorCr.setValue(148, 108, Integer.parseInt(value));
        }
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (mLcdType == preference) {
            mSetting.setLcdType(objValue.toString());
            mLcdType.setSummary(Misc.getCurrentValueText(this, objValue.toString()));
            return true;
        } else if (mMdnieForceDisable == preference) {
            mSetting.setMdnieForceDisable(((Boolean)objValue));
            mMdnieMode.setEnabled((Boolean)objValue);
            mMdnieColorCb.setEnabled((Boolean)objValue);
            mMdnieColorCr.setEnabled((Boolean)objValue);
            return true;
        } else if (mMdnieMode == preference) {
            mSetting.setMdnieMode(((Boolean)objValue) ?
                    DisplaySetting.MDNIE_MCM_ENABLE : DisplaySetting.MDNIE_MCM_DISABLE);
            mMdnieColorCb.setEnabled((Boolean)objValue);
            mMdnieColorCr.setEnabled((Boolean)objValue);
            return true;
        }
        return false;
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        if (mLcdGamma == preference) {
            mSetting.setLcdGamma(newValue);
            mLcdGamma.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;
        } else if (mMdnieColorCb == preference) {
            mSetting.setMdnieMcmCb(newValue);
            mMdnieColorCb.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;
        } else if (mMdnieColorCr == preference) {
            mSetting.setMdnieMcmCr(newValue);
            mMdnieColorCr.setSummary(Misc.getCurrentValueText(this, newValue));
            return true;
        }
        return false;
    }
}
