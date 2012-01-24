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
import net.sakuramilk.TweakGS2.Display.DisplaySetting;
import net.sakuramilk.TweakGS2.Dock.DockSetting;
import net.sakuramilk.TweakGS2.General.GeneralSetting;
import net.sakuramilk.TweakGS2.General.LowMemKillerSetting;
import net.sakuramilk.TweakGS2.General.SystemPropertySetting;
import net.sakuramilk.TweakGS2.General.VirtualMemorySetting;
import net.sakuramilk.TweakGS2.Notification.NotificationSetting;
import net.sakuramilk.TweakGS2.Parts.ConfirmAlertDialog;
import net.sakuramilk.TweakGS2.SoundAndVib.HwVolumeSetting;
import net.sakuramilk.TweakGS2.SoundAndVib.SoundAndVibSetting;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class AboutPreferenceActivity extends PreferenceActivity
    implements Preference.OnPreferenceClickListener {

    private PreferenceScreen mVersion;
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

        mRecommend = (PreferenceScreen)findPreference("about_recommend");
        mRecommend.setOnPreferenceClickListener(this);

        mReset = (PreferenceScreen)findPreference("about_reset");
        mReset.setOnPreferenceClickListener(this);
    }

    public boolean onPreferenceClick(Preference preference)
    {
        if (preference == mReset) {
            final ConfirmAlertDialog confirmDialog = new ConfirmAlertDialog(this);
            confirmDialog.setResultListener(new ConfirmAlertDialog.ResultListener() {
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
            final ConfirmAlertDialog confirmDialog = new ConfirmAlertDialog(this);
            confirmDialog.setResultListener(new ConfirmAlertDialog.ResultListener() {
                @Override
                public void onYes() {
                    // General
                    GeneralSetting generalSetting = new GeneralSetting(mContext);
                    generalSetting.setRecommend();
                    LowMemKillerSetting lowMemKillerSetting = new LowMemKillerSetting(mContext);
                    lowMemKillerSetting.setRecommend();
                    VirtualMemorySetting vmSetting = new VirtualMemorySetting(mContext);
                    vmSetting.setRecommend();

                    // Sound and vib
                    HwVolumeSetting hwVolumeSetting = new HwVolumeSetting(mContext);
                    hwVolumeSetting.setRecommend();
                    SoundAndVibSetting vibSetting = new SoundAndVibSetting(mContext);
                    vibSetting.setRecommend();

                    // Display
                    DisplaySetting displaySetting = new DisplaySetting(mContext);
                    displaySetting.setRecommend();

                    // Notification
                    NotificationSetting notifySetting = new NotificationSetting(mContext);
                    notifySetting.setRecommend();

                    // Dock
                    DockSetting dockSetting = new DockSetting(mContext);
                    dockSetting.setRecommend();

                    Misc.confirmReboot(mContext, R.string.reboot_reflect_comfirm);
                }
            });
            confirmDialog.show(this, R.string.all_recommend_title, R.string.all_recommend_summary);
        }
        return false;
    }
}
