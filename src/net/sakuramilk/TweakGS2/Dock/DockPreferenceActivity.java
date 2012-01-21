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

package net.sakuramilk.TweakGS2.Dock;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SystemCommand;
import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class DockPreferenceActivity extends PreferenceActivity implements
    Preference.OnPreferenceChangeListener, OnPreferenceClickListener {

    private DockSetting mSetting;

    private ListPreference mDockEmulate;
    private PreferenceScreen mDockManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.dock_pref);
        mSetting = new DockSetting(this);

        mDockEmulate = (ListPreference)findPreference(DockSetting.KEY_DOCK_EMULATE);
        mDockManual = (PreferenceScreen)findPreference(DockSetting.KEY_DOCK_MANUAL);

        String savedDockEmulate = mSetting.loadDockEmulate();
        if (mSetting.isEnableDockEmulate()) {
            mDockEmulate.setEntries(R.array.dock_emulate_hw_entries);
            mDockEmulate.setEntryValues(R.array.dock_emulate_hw_values);
        } else {
            mDockEmulate.setEntries(R.array.dock_emulate_entries);
            mDockEmulate.setEntryValues(R.array.dock_emulate_values);
            if ("2".equals(savedDockEmulate)) {
                mSetting.saveDockEmulate("0");
                savedDockEmulate = "0";
            }
        }
        mDockEmulate.setValue(mSetting.getEmuIndexFromEmuValue(savedDockEmulate));
        mDockEmulate.setOnPreferenceChangeListener(this);
        updateDockEmulate(savedDockEmulate);

        mDockManual.setOnPreferenceClickListener(this);
        UiModeManager uiModeManager = (UiModeManager)getSystemService(Context.UI_MODE_SERVICE);
        int mUiMode = uiModeManager.getCurrentModeType();
        if (mUiMode == Configuration.UI_MODE_TYPE_DESK) {
            mDockManual.setSummary(R.string.dock_manual_stop);
        } else {
            mDockManual.setSummary(R.string.dock_manual_start);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (mDockEmulate == preference) {
            updateDockEmulate(objValue.toString());
            return true;
        }
        return false;
    }

    public boolean onPreferenceClick(Preference preference) {
        if (preference == mDockManual) {
            UiModeManager uiModeManager = (UiModeManager)getSystemService(Context.UI_MODE_SERVICE);
            int mUiMode = uiModeManager.getCurrentModeType();
            if (mUiMode == Configuration.UI_MODE_TYPE_DESK) {
                SystemCommand.stop_dock();
                mDockManual.setSummary(R.string.dock_manual_start);
            } else {
                SystemCommand.start_dock();
                mDockManual.setSummary(R.string.dock_manual_stop);
            }
        }
        return false;
    }

    private void updateDockEmulate(String value) {
        if (DockSetting.DOCK_EMU_HW_INDEX.equals(value)) {
            mSetting.setDockEmulate(DockSetting.DOCK_EMU_HW_VALUE);
            mDockEmulate.setSummary(Misc.getCurrentValueText(this, R.string.dock_emu_hw));
        } else if (DockSetting.DOCK_EMU_SW_INDEX.equals(value)) {
            mSetting.setDockEmulate(DockSetting.DOCK_EMU_SW_VALUE);
            mDockEmulate.setSummary(Misc.getCurrentValueText(this, R.string.dock_emu_sw));
        } else {
            mSetting.setDockEmulate(DockSetting.DOCK_EMU_DISABLE_VALUE);
            mDockEmulate.setSummary(Misc.getCurrentValueText(this, R.string.dock_emu_disable));
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.reset_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_reset:
                mSetting.reset();
                mDockEmulate.setSummary(Misc.getCurrentValueText(this, R.string.dock_emu_disable));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
