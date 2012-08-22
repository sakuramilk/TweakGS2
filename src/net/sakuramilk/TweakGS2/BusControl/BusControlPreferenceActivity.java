/*
 * Copyright (C) 2012 sakuramilk <c.sakuramilk@gmail.com>
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

package net.sakuramilk.TweakGS2.BusControl;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Convert;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.ApplyButtonPreferenceActivity;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class BusControlPreferenceActivity extends ApplyButtonPreferenceActivity
    implements OnSeekBarPreferenceDoneListener, OnClickListener, OnPreferenceChangeListener {

    private BusControlSetting mSetting;

    private ListPreference mAsvGroup;
    private SeekBarPreference mUpThreshold;
    private SeekBarPreference mDownThreshold;
    private CheckBoxPreference mSetOnBoot;
    private boolean mSetOnBootChecked;
    
    private String mSavedAsvGroup;
    private String mSavedUpThreshold;
    private String mSavedDownThreshold;

    private void setMaxMinValue() {
    	String curValue = mSetting.getAsvGroup();
        mAsvGroup.setSummary(Misc.getCurrentAndSavedValueText(this, curValue, mSavedAsvGroup));
        mAsvGroup.setValue(mSavedAsvGroup);
        
        if (mUpThreshold != null && mDownThreshold != null) {
          	curValue = mSetting.getUpThreshold();
        	mUpThreshold.setSummary(Misc.getCurrentAndSavedValueText(
                    this, curValue + "%",
                    mSavedUpThreshold == null ? getText(R.string.none).toString() : mSavedUpThreshold + "%"));
        	int upThread = Convert.toInt(mSavedUpThreshold == null ? curValue : mSavedUpThreshold);
        	
        	curValue = mSetting.getDownThreshold();
        	mDownThreshold.setSummary(Misc.getCurrentAndSavedValueText(
                    this, curValue + "%",
                    mSavedDownThreshold == null ? getText(R.string.none).toString() : mSavedDownThreshold + "%"));
        	int downThread = Convert.toInt(mSavedDownThreshold == null ? curValue : mSavedDownThreshold);
            
            mUpThreshold.setValue(BusControlSetting.THRESHOLD_MAX, BusControlSetting.THRESHOLD_MIN, upThread);
            mDownThreshold.setValue(BusControlSetting.THRESHOLD_MAX, BusControlSetting.THRESHOLD_MIN, downThread);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.bus_control_pref);
        mSetting = new BusControlSetting(this);
        
        if (mSetting.isEnableAvsGroup()) {
        	mAsvGroup = (ListPreference)findPreference(BusControlSetting.KEY_BUS_ASV_GROUP);
        	mAsvGroup.setEnabled(true);
        	mAsvGroup.setOnPreferenceChangeListener(this);
        	mSavedAsvGroup = mSetting.loadAsvGroup();
        }

        if (mSetting.isEnableUpThreshold() && mSetting.isEnableDownThreshold()) {
        	mUpThreshold = (SeekBarPreference)findPreference(BusControlSetting.KEY_BUS_UP_THRESHOLD);
        	mUpThreshold.setEnabled(true);
        	mUpThreshold.setOnPreferenceDoneListener(this);
        	mSavedUpThreshold = mSetting.loadUpThreshold();

        	mDownThreshold = (SeekBarPreference)findPreference(BusControlSetting.KEY_BUS_DOWN_THRESHOLD);
        	mDownThreshold.setEnabled(true);
        	mDownThreshold.setOnPreferenceDoneListener(this);
        	mSavedDownThreshold = mSetting.loadDownThreshold();
        }

        mSetOnBoot = (CheckBoxPreference)findPreference(BusControlSetting.KEY_BUS_SET_ON_BOOT);
        mSetOnBoot.setTitle(R.string.set_on_boot);
        mSetOnBootChecked = mSetting.loadSetOnBoot();
        mSetOnBoot.setOnPreferenceChangeListener(this);
        mSetOnBoot.setChecked(mSetOnBootChecked);
        mApplyButton.setOnClickListener(this);

        setMaxMinValue();
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        if (preference == mUpThreshold) {
        	mSavedUpThreshold = (String)newValue;
        	mApplyButton.setEnabled(true);
        	setMaxMinValue();
        } else if (preference == mDownThreshold) {
        	mSavedDownThreshold = (String)newValue;
        	mApplyButton.setEnabled(true);
        	setMaxMinValue();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        mApplyButton.setEnabled(false);
        mSetOnBootChecked = mSetOnBoot.isChecked();
        mSetting.saveSetOnBoot(mSetOnBootChecked);

        if (!Misc.isNullOfEmpty(mSavedAsvGroup)) {
        	mSetting.saveAsvGroup(mSavedAsvGroup);
        	mSetting.setAsvGroup(mSavedAsvGroup);
        }
        if (!Misc.isNullOfEmpty(mSavedUpThreshold)) {
        	mSetting.saveUpThreshold(mSavedUpThreshold);
        	mSetting.setUpThreshold(mSavedUpThreshold);
        }
        if (!Misc.isNullOfEmpty(mSavedDownThreshold)) {
        	mSetting.saveDownThreshold(mSavedDownThreshold);
        	mSetting.setDownThreshold(mSavedDownThreshold);
        }
        setMaxMinValue();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
    	if (preference == mAsvGroup) {
    		mSavedAsvGroup = (String)newValue;
    		setMaxMinValue();
    		mApplyButton.setEnabled(true);
    		return false;
    	}
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
