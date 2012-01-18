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

package net.sakuramilk.TweakGS2.About;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.SystemCommand;
import net.sakuramilk.TweakGS2.Display.DisplaySetting;
import net.sakuramilk.TweakGS2.General.GeneralSetting;
import net.sakuramilk.TweakGS2.SoundAndVib.HwVolumeSetting;
import net.sakuramilk.TweakGS2.SoundAndVib.SoundAndVibSetting;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class AboutPreferenceActivity extends PreferenceActivity
    implements Preference.OnPreferenceClickListener {

    private PreferenceScreen mVersion;
    private PreferenceScreen mRecommended;
    private PreferenceScreen mReset;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        addPreferencesFromResource(R.xml.about_pref);
        
        String version;
        try {
            version = this.getPackageManager().getPackageInfo(this.getPackageName(), 1).versionName;
        } catch (NameNotFoundException e) {
            version = "";
        }
        mVersion = (PreferenceScreen)findPreference("about_version");
        mVersion.setSummary(version);

        mRecommended = (PreferenceScreen)findPreference("about_recommended");
        mRecommended.setOnPreferenceClickListener(this);

        mReset = (PreferenceScreen)findPreference("about_reset");
        mReset.setOnPreferenceClickListener(this);
    }

    public boolean onPreferenceClick(Preference preference)
    {
        if (preference == mReset) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.all_reset_title);
            alertDialogBuilder.setMessage(R.string.all_reset_summary);
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
                    Editor ed = settings.edit();
                    ed.clear();
                    ed.commit();
                    //GeneralSetting generalSetting new = (mContext);
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setTitle(R.string.all_reset_title);
                    alertDialogBuilder.setMessage(R.string.reboot_reflect_comfirm);
                    alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SystemCommand.reboot(null);
                        }
                    });
                    alertDialogBuilder.setNegativeButton(android.R.string.no, null);
                    alertDialogBuilder.create().show();
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, null);
            alertDialogBuilder.create().show();
        } else if (preference == mRecommended) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.all_recommend_title);
            alertDialogBuilder.setMessage(R.string.all_recommend_summary);
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    GeneralSetting generalSetting = new GeneralSetting(mContext);
                    generalSetting.setRecommend();
                    HwVolumeSetting hwVolumeSetting = new HwVolumeSetting(mContext);
                    hwVolumeSetting.setRecommend();
                    SoundAndVibSetting vibSetting = new SoundAndVibSetting(mContext);
                    vibSetting.setRecommend();
                    DisplaySetting displaySetting = new DisplaySetting(mContext);
                    displaySetting.setRecommend();

                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setTitle(R.string.all_recommend_title);
                    alertDialogBuilder.setMessage(R.string.reboot_reflect_comfirm);
                    alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SystemCommand.reboot(null);
                        }
                    });
                    alertDialogBuilder.setNegativeButton(android.R.string.no, null);
                    alertDialogBuilder.create().show();
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, null);
            alertDialogBuilder.create().show();
        }
        return false;
    }
}
