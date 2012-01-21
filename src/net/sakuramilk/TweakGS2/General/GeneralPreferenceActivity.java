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
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuItem;

public class GeneralPreferenceActivity extends PreferenceActivity implements OnPreferenceChangeListener {

    private GeneralSetting mSetting;
    private ListPreference mIoSched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.general_pref);
        mSetting = new GeneralSetting(this);

        mIoSched = (ListPreference)findPreference(GeneralSetting.KEY_IO_SCHED);
        String curValue = mSetting.getCurrentIoScheduler();
        String entries[] = mSetting.getIoSchedulerList();
        mIoSched.setEntries(entries);
        mIoSched.setEntryValues(entries);
        mIoSched.setValue(curValue);
        mIoSched.setOnPreferenceChangeListener(this);
        mIoSched.setSummary(Misc.getCurrentValueText(this, curValue));
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (mIoSched == preference) {
            mSetting.setIoScheduler(newValue.toString());
            mIoSched.setSummary(Misc.getCurrentValueText(this, newValue.toString()));
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean ret = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_reset:
            mSetting.reset();
            Misc.confirmReboot(this, R.string.reboot_reflect_comfirm);
            return true;
        case R.id.menu_recommend:
            mSetting.setRecommend();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
