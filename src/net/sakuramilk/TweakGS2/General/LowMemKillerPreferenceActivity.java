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

import java.util.ArrayList;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class LowMemKillerPreferenceActivity extends PreferenceActivity
    implements OnSeekBarPreferenceDoneListener {

    private LowMemKillerSetting mSetting;
    private ArrayList<SeekBarPreference> mLowMemList;
    private String[] mValues;

    private void setMaxMinValue() {
        for (int i = 0; i < mLowMemList.size(); i++) {
            SeekBarPreference pref = mLowMemList.get(i);
            pref.setSummary(Misc.getCurrentValueText(this, mValues[i]));
            if (i == 0) {
                pref.setValue(Integer.parseInt(mValues[i+1]), LowMemKillerSetting.MEM_FREE_MIN, Integer.parseInt(mValues[i]));
            } else if (i == (mLowMemList.size() - 1)) {
                pref.setValue(LowMemKillerSetting.MEM_FREE_MAX, Integer.parseInt(mValues[i-1]), Integer.parseInt(mValues[i]));
            } else {
                pref.setValue(Integer.parseInt(mValues[i+1]), Integer.parseInt(mValues[i-1]), Integer.parseInt(mValues[i]));
            }
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.general_low_mem_killer_pref);

        mSetting = new LowMemKillerSetting(this);
        mValues = mSetting.getLowMemKillerMinFree();
        mLowMemList = new ArrayList<SeekBarPreference>();
        mLowMemList.add((SeekBarPreference)findPreference(LowMemKillerSetting.KEY_LOWMEM_FORGROUND_APP));
        mLowMemList.add((SeekBarPreference)findPreference(LowMemKillerSetting.KEY_LOWMEM_VISIBLE_APP));
        mLowMemList.add((SeekBarPreference)findPreference(LowMemKillerSetting.KEY_LOWMEM_SECONDARY_SERVER));
        mLowMemList.add((SeekBarPreference)findPreference(LowMemKillerSetting.KEY_LOWMEM_HIDDEN_APP));
        mLowMemList.add((SeekBarPreference)findPreference(LowMemKillerSetting.KEY_LOWMEM_CONTENT_PROVIDER));
        mLowMemList.add((SeekBarPreference)findPreference(LowMemKillerSetting.KEY_LOWMEM_EMPTY_APP));
        for (SeekBarPreference pref : mLowMemList) {
            pref.setOnPreferenceDoneListener(this);
        }
        setMaxMinValue();
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        int index = mLowMemList.indexOf(preference);
        if (index < 6) {
            mValues[index] = newValue;
            setMaxMinValue();
            mSetting.setLowMemKillerMinFree(mValues);
            mSetting.saveLowMemKillerMinFree(mValues);
            // don't return true
        }
        return false;
    }
}
