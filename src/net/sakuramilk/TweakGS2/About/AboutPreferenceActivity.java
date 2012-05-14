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

package net.sakuramilk.TweakGS2.About;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.RootProcess;
import net.sakuramilk.TweakGS2.Display.DisplaySetting;
import net.sakuramilk.TweakGS2.Dock.DockSetting;
import net.sakuramilk.TweakGS2.General.GeneralSetting;
import net.sakuramilk.TweakGS2.General.LowMemKillerSetting;
import net.sakuramilk.TweakGS2.General.SystemPropertySetting;
import net.sakuramilk.TweakGS2.General.VirtualMemorySetting;
import net.sakuramilk.TweakGS2.Notification.NotificationSetting;
import net.sakuramilk.TweakGS2.Parts.ConfirmDialog;
import net.sakuramilk.TweakGS2.SoundAndVib.HwVolumeSetting;
import net.sakuramilk.TweakGS2.SoundAndVib.SoundAndVibSetting;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class AboutPreferenceActivity extends PreferenceActivity
    implements Preference.OnPreferenceClickListener {

    private PreferenceScreen mVersion;
    private PreferenceScreen mCheckUpdate;
    private PreferenceScreen mRecommend;
    private PreferenceScreen mReset;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        addPreferencesFromResource(R.xml.about_pref);

        mVersion = (PreferenceScreen)findPreference("about_version");
        mVersion.setSummary(Misc.getVersionName(this));

        mCheckUpdate = (PreferenceScreen)findPreference("about_check_update");
        mCheckUpdate.setOnPreferenceClickListener(this);

        mRecommend = (PreferenceScreen)findPreference("about_recommend");
        mRecommend.setOnPreferenceClickListener(this);

        mReset = (PreferenceScreen)findPreference("about_reset");
        mReset.setOnPreferenceClickListener(this);
    }

    public boolean onPreferenceClick(Preference preference)
    {
        if (preference == mReset) {
            final ConfirmDialog confirmDialog = new ConfirmDialog(this);
            confirmDialog.setResultListener(new ConfirmDialog.ResultListener() {
                @Override
                public void onYes() {
                    // clear shared preference
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
                    Editor ed = settings.edit();
                    ed.clear();
                    ed.commit();
                    SystemPropertySetting sysPropSetting = new SystemPropertySetting(mContext);
                    sysPropSetting.reset();
                    Misc.confirmReboot(mContext, R.string.reboot_reflect_comfirm);
                }
            });
            confirmDialog.show(this, R.string.all_reset_title, R.string.all_reset_summary);

        } else if (preference == mRecommend) {
            final ConfirmDialog confirmDialog = new ConfirmDialog(this);
            confirmDialog.setResultListener(new ConfirmDialog.ResultListener() {
                @Override
                public void onYes() {
                    RootProcess process = new RootProcess();
                    process.init();

                    // General
                    GeneralSetting generalSetting = new GeneralSetting(mContext, process);
                    generalSetting.setRecommend();
                    LowMemKillerSetting lowMemKillerSetting = new LowMemKillerSetting(mContext, process);
                    lowMemKillerSetting.setRecommend();
                    VirtualMemorySetting vmSetting = new VirtualMemorySetting(mContext, process);
                    vmSetting.setRecommend();

                    // Sound and vib
                    HwVolumeSetting hwVolumeSetting = new HwVolumeSetting(mContext, process);
                    hwVolumeSetting.setRecommend();
                    SoundAndVibSetting vibSetting = new SoundAndVibSetting(mContext, process);
                    vibSetting.setRecommend();

                    // Display
                    DisplaySetting displaySetting = new DisplaySetting(mContext, process);
                    displaySetting.setRecommend();

                    // Notification
                    NotificationSetting notifySetting = new NotificationSetting(mContext, process);
                    notifySetting.setRecommend();

                    // Dock
                    DockSetting dockSetting = new DockSetting(mContext, process);
                    dockSetting.setRecommend();
                    
                    process.term();
                    process = null;

                    Misc.confirmReboot(mContext, R.string.reboot_reflect_comfirm);
                }
            });
            confirmDialog.show(this, R.string.all_recommend_title, R.string.all_recommend_summary);

        } else if (preference == mCheckUpdate) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=net.sakuramilk.TweakGS2");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }
        return false;
    }
}
