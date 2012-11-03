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

package net.sakuramilk.TweakGS2.CpuControl;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.util.Misc;
import net.sakuramilk.TweakGS2.CpuControl.CpuGovernorSetting;
import net.sakuramilk.widget.SeekBarPreference;
import net.sakuramilk.widget.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class CpuGovernorPreferenceActivity extends PreferenceActivity
    implements OnSeekBarPreferenceDoneListener, OnPreferenceChangeListener {

    private CpuGovernorSetting mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cpu_control_governor_pref);

        Intent intent = this.getIntent();
        String govName = intent.getStringExtra("governor");
        mSetting = new CpuGovernorSetting(this, govName);

        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference("root_pref");
        PreferenceCategory ctegoryPref = new PreferenceCategory(this);
        ctegoryPref.setTitle(govName);
        rootPref.addPreference(ctegoryPref);

        CpuGovernorSetting.Parameter[] params = mSetting.getParameters();
        for (CpuGovernorSetting.Parameter param : params) {
            String value;
            switch (param.type) {
                case CpuGovernorSetting.Parameter.TYPE_SEEK_BAR:
                {
                    SeekBarPreference seekBarPref = new SeekBarPreference(this, null);
                    seekBarPref.setKey(mSetting.makeKey(param.name));
                    seekBarPref.setTitle(param.name);
                    seekBarPref.setDialogTitle(param.name);
                    value = mSetting.getValue(param.name);
                    seekBarPref.setValue(param.max, param.min, Integer.valueOf(value));
                    seekBarPref.setSummary(Misc.getCurrentValueText(this, value) + param.unit);
                    seekBarPref.setUnit(param.unit);
                    seekBarPref.setOnPreferenceDoneListener(this);
                    rootPref.addPreference(seekBarPref);
                    break;
                }

                case CpuGovernorSetting.Parameter.TYPE_LIST:
                {
                    ListPreference listPref = new ListPreference(this);
                    listPref.setKey(mSetting.makeKey(param.name));
                    listPref.setTitle(param.name);
                    listPref.setEntries(param.listEntries);
                    listPref.setEntryValues(param.listValues);
                    value = mSetting.getValue(param.name);
                    listPref.setValue(value);
                    listPref.setIntent(intent);
                    listPref.setSummary(Misc.getCurrentValueText(this,
                            Misc.getEntryFromEntryValue(param.listEntries, param.listValues, value) + (param.unit == null ? "" : param.unit)));
                    listPref.setOnPreferenceChangeListener(this);
                    rootPref.addPreference(listPref);
                    break;
                }
            }
        }
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        SeekBarPreference seekBarPref = (SeekBarPreference)preference;
        String paramName = seekBarPref.getTitle().toString();
        mSetting.setValue(paramName, newValue);
        seekBarPref.setSummary(Misc.getCurrentValueText(this, newValue) + seekBarPref.getUnit());
        seekBarPref.updateValue(Integer.valueOf(newValue));
        return true;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        ListPreference listPref = (ListPreference)preference;
        String paramName = listPref.getTitle().toString();
        mSetting.setValue(paramName, objValue.toString());
        listPref.setValue(objValue.toString());
        listPref.setSummary(Misc.getCurrentValueText(this,
                Misc.getEntryFromEntryValue(listPref.getEntries(), listPref.getEntryValues(), objValue.toString())));
        return false;
    }
}
