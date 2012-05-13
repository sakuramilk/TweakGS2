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

package net.sakuramilk.TweakGS2.Notification;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class NotificationPreferenceActivity extends PreferenceActivity
    implements OnPreferenceChangeListener {

    private NotificationSetting mSetting;
    private ListPreference mLedTimeout;
    private CheckBoxPreference mBlnEnabled;
    private ListPreference mBlnTimeout;
    private CheckBoxPreference mBlnBlinking;
    private CheckBoxPreference mBlnBreathing;
    private CheckBoxPreference mBlnOnIncoming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.notification_pref);

        mSetting = new NotificationSetting(this);
        String curValue;

        mLedTimeout = (ListPreference)findPreference(NotificationSetting.KEY_NOTIFY_LED_TIMEOUT);
        if (mSetting.isEnableLedTimeout()) {
            curValue = mSetting.getLedTimeout();
            mLedTimeout.setValue(curValue);
            mLedTimeout.setSummary(Misc.getCurrentValueText(this,
                    Misc.getEntryFromEntryValue(mLedTimeout.getEntries(), mLedTimeout.getEntryValues(), curValue)));
            mLedTimeout.setOnPreferenceChangeListener(this);
            mLedTimeout.setEnabled(true);
        }

        mBlnEnabled = (CheckBoxPreference)findPreference(NotificationSetting.KEY_NOTIFY_BLN_ENABLED);
        if (mSetting.isEnableBlnEnabled()) {
            boolean value = mSetting.getBlnEnabled();
            mBlnEnabled.setChecked(value);
            mBlnEnabled.setOnPreferenceChangeListener(this);
            mBlnEnabled.setEnabled(true);
        }

        mBlnTimeout = (ListPreference)findPreference(NotificationSetting.KEY_NOTIFY_BLN_TIMEOUT);
        if (mSetting.isEnableBlnTimeout()) {
            curValue = mSetting.getBlnTimeout();
            mBlnTimeout.setValue(curValue);
            mBlnTimeout.setSummary(Misc.getCurrentValueText(this,
                    Misc.getEntryFromEntryValue(mBlnTimeout.getEntries(), mBlnTimeout.getEntryValues(), curValue)));
            mBlnTimeout.setOnPreferenceChangeListener(this);
            mBlnTimeout.setEnabled(true);
        }

        mBlnBlinking = (CheckBoxPreference)findPreference(NotificationSetting.KEY_NOTIFY_BLN_BLINKING);
        if (mSetting.isEnableBlnBlinking()) {
            boolean value = mSetting.getBlnBlinking();
            mBlnBlinking.setChecked(value);
            mBlnBlinking.setOnPreferenceChangeListener(this);
            mBlnBlinking.setEnabled(true);
        }

        mBlnBreathing = (CheckBoxPreference)findPreference(NotificationSetting.KEY_NOTIFY_BLN_BREATHING);
        if (mSetting.isEnableBlnBlinking()) {
            boolean value = mSetting.getBlnBreathing();
            mBlnBreathing.setChecked(value);
            mBlnBreathing.setOnPreferenceChangeListener(this);
            mBlnBreathing.setEnabled(true);
        }
        

        mBlnOnIncoming = (CheckBoxPreference)findPreference(NotificationSetting.KEY_NOTIFY_BLN_ON_INCOMING);
        mBlnOnIncoming.setEnabled(true);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == mLedTimeout) {
            String value = newValue.toString();
            mSetting.setLedTimeout(value);
            mSetting.saveLedTimeout(value);
            mLedTimeout.setValue(value);
            mLedTimeout.setSummary(Misc.getCurrentValueText(this,
                    Misc.getEntryFromEntryValue(mLedTimeout.getEntries(), mLedTimeout.getEntryValues(), value)));

        } else if (preference == mBlnEnabled) {
            Boolean value = (Boolean)newValue;
            mSetting.setBlnEnabled(value);
            mSetting.saveBlnEnabled(value);
            mBlnEnabled.setChecked(value);

        } else if (preference == mBlnTimeout) {
            String value = newValue.toString();
            mSetting.setBlnTimeout(value);
            mSetting.saveBlnTimeout(value);
            mBlnTimeout.setValue(value);
            mBlnTimeout.setSummary(Misc.getCurrentValueText(this,
                    Misc.getEntryFromEntryValue(mBlnTimeout.getEntries(), mBlnTimeout.getEntryValues(), value)));

        } else if (preference == mBlnBlinking) {
            Boolean value = (Boolean)newValue;
            mSetting.setBlnBlinking(value);
            mSetting.saveBlnBlinking(value);
            mBlnBlinking.setChecked(value);

        } else if (preference == mBlnBreathing) {
            Boolean value = (Boolean)newValue;
            mSetting.setBlnBreathing(value);
            mSetting.saveBlnBreathing(value);
            mBlnBreathing.setChecked(value);
        }

        return false;
    }
}
