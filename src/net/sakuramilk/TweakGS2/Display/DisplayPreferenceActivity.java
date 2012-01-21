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
import android.view.Menu;
import android.view.MenuItem;

public class DisplayPreferenceActivity extends PreferenceActivity implements
    Preference.OnPreferenceChangeListener, OnSeekBarPreferenceDoneListener {

    private DisplaySetting mSetting;

    private ListPreference mLcdType;
    private SeekBarPreference mLcdGamma;
    private CheckBoxPreference mMdnieForceDisable;
    private CheckBoxPreference mMdnieMode;
    private SeekBarPreference mMdnieColorCb;
    private SeekBarPreference mMdnieColorCr;
    
    private boolean mIsEnableMdnieForceDisable = false;
    private boolean mIsEnableMdnieMode = false;
    private boolean mIsEnableMdnieMcmCb = false;
    private boolean mIsEnableMdnieMcmCr = false;

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
            mLcdType.setValue(value);
            mLcdType.setSummary(Misc.getCurrentValueText(this, mSetting.getLcdTypeText(value)));
        }

        if (mSetting.isEnableLcdGamma()) {
            mLcdGamma.setEnabled(true);
            String value = mSetting.getLcdGamma();
            mLcdGamma.setSummary(Misc.getCurrentValueText(this, value));
            mLcdGamma.setValue(50, -50, Integer.parseInt(value));
            mLcdGamma.setOnPreferenceDoneListener(this);
        }

        mIsEnableMdnieForceDisable = mSetting.isEnableMdnieForceDisable();
        boolean isMdnieForceDisable = false;
        if (mIsEnableMdnieForceDisable) {
            mMdnieForceDisable.setEnabled(true);
            mMdnieForceDisable.setOnPreferenceChangeListener(this);
            String value = mSetting.getMdnieForceDisable();
            isMdnieForceDisable = "0".equals(value) ? false : true;
        }

        mIsEnableMdnieMode = mSetting.isEnableMdnieMode();
        boolean isMdnieMcmEnable = false;
        if (mIsEnableMdnieMode) {
            mMdnieMode.setEnabled(!isMdnieForceDisable);
            mMdnieMode.setOnPreferenceChangeListener(this);
            String value = mSetting.getMdnieMode();
            isMdnieMcmEnable = DisplaySetting.MDNIE_MCM_ENABLE.equals(value) ? true : false;
        }

        mIsEnableMdnieMcmCb = mSetting.isEnableMdnieMcmCb();
        if (mIsEnableMdnieMcmCb) {
            mMdnieColorCb.setEnabled(mMdnieForceDisable.isEnabled() &
                    mMdnieMode.isEnabled() & isMdnieMcmEnable);
            mMdnieColorCb.setOnPreferenceDoneListener(this);
            String value = mSetting.getMdnieMcmCb();
            mMdnieColorCb.setSummary(Misc.getCurrentValueText(this, value));
            mMdnieColorCb.setValue(148, 108, Integer.parseInt(value));
        }

        mIsEnableMdnieMcmCr = mSetting.isEnableMdnieMcmCr();
        if (mIsEnableMdnieMcmCr) {
            mMdnieColorCr.setEnabled(mMdnieForceDisable.isEnabled() &
                    mMdnieMode.isEnabled() & isMdnieMcmEnable);
            mMdnieColorCr.setOnPreferenceDoneListener(this);
            String value = mSetting.getMdnieMcmCr();
            mMdnieColorCr.setSummary(Misc.getCurrentValueText(this, value));
            mMdnieColorCr.setValue(148, 108, Integer.parseInt(value));
        }
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (mLcdType == preference) {
            mSetting.setLcdType(objValue.toString());
            mLcdType.setSummary(Misc.getCurrentValueText(this, mSetting.getLcdTypeText(objValue.toString())));
            return true;

        } else if (mMdnieForceDisable == preference) {
            mSetting.setMdnieForceDisable(((Boolean)objValue));
            boolean mode = mSetting.loadMdnieMode();
            mMdnieMode.setEnabled((Boolean)objValue);
            mMdnieColorCb.setEnabled((Boolean)objValue & mode);
            mMdnieColorCr.setEnabled((Boolean)objValue & mode);
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
