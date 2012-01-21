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
import net.sakuramilk.TweakGS2.CpuControl.GovernorSetting;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class CpuControlGovernorPreferenceActivity extends PreferenceActivity
    implements OnSeekBarPreferenceDoneListener {
    
    private GovernorSetting mSetting;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cpu_control_governor_pref);
        
        
        Intent intent = this.getIntent();
        mSetting = new GovernorSetting(this, intent.getStringExtra("governor"));
        
        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference("root_pref");
        
        GovernorSetting.Parameter[] params = mSetting.getParameters();
        for (GovernorSetting.Parameter param : params) {
            SeekBarPreference pref = new SeekBarPreference(this, null);
            pref.setTitle(param.name);
            pref.setDialogTitle(param.name);
            String value = mSetting.getValue(param.name);
            pref.setValue(param.max, param.min, Integer.valueOf(value));
            pref.setSummary(Misc.getCurrentValueText(this, value));
            pref.setOnPreferenceDoneListener(this);
            rootPref.addPreference(pref);
        }
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        return false;
    }
}
