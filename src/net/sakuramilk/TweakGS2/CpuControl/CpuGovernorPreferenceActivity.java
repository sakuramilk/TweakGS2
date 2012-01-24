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
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.CpuControl.CpuGovernorSetting;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
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
    
    private String getEntryText(String[] entries, String[] values, String value) {
        int i = 0;
        for (i = 0; i < entries.length; i++) {
            if (values[i].equals(value)) {
                return entries[i];
            }
        }
        return value; // if not found value, return safe value.
    }
    
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
                    SeekBarPreference seekBarPref = new SeekBarPreference(this, null);
                    seekBarPref.setTitle(param.name);
                    seekBarPref.setDialogTitle(param.name);
                    value = mSetting.getValue(param.name);
                    seekBarPref.setValue(param.max, param.min, Integer.valueOf(value));
                    seekBarPref.setSummary(Misc.getCurrentValueText(this, value));
                    seekBarPref.setOnPreferenceDoneListener(this);
                    rootPref.addPreference(seekBarPref);
                    break;

                case CpuGovernorSetting.Parameter.TYPE_LIST:
                    ListPreference listPref = new ListPreference(this);
                    listPref.setTitle(param.name);
                    listPref.setEntries(param.listEntries);
                    listPref.setEntryValues(param.listValues);
                    value = mSetting.getValue(param.name);
                    listPref.setValue(value);
                    listPref.setSummary(Misc.getCurrentValueText(this,
                            getEntryText(param.listEntries, param.listValues, value)));
                    listPref.setOnPreferenceChangeListener(this);
                    rootPref.addPreference(listPref);
                    break;
            }
        }
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference arg0, Object arg1) {
        return false;
    }
}
