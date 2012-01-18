/*
 * Copyright (C) 2011 sakuramilk <c.sakuramilk@gmail.com>
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

package net.sakuramilk.TweakGS2.RomManager;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Constant;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SystemCommand;
import net.sakuramilk.TweakGS2.Parts.TextInputDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class RomManagerPreferenceActivity extends PreferenceActivity
    implements OnPreferenceClickListener, OnPreferenceChangeListener {

    private PreferenceScreen mRebootNormal;
    private PreferenceScreen mRebootRecovery;
    private PreferenceScreen mRebootDownload;
    private ListPreference mNandroidBackup;
    private ListPreference mNandroidRestore;
    private ListPreference mNandroidManage;
    private PreferenceScreen mFlashInstallZip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.rom_manager_pref);

        mRebootNormal = (PreferenceScreen)findPreference("reboot_normal");
        mRebootNormal.setOnPreferenceClickListener(this);
        mRebootRecovery = (PreferenceScreen)findPreference("reboot_recovery");
        mRebootRecovery.setOnPreferenceClickListener(this);
        mRebootDownload = (PreferenceScreen)findPreference("reboot_download");
        mRebootDownload.setOnPreferenceClickListener(this);

        mNandroidBackup = (ListPreference)findPreference("nandroid_backup");
        mNandroidBackup.setOnPreferenceChangeListener(this);
        mNandroidRestore = (ListPreference)findPreference("nandroid_restore");
        mNandroidRestore.setOnPreferenceChangeListener(this);
        mNandroidManage = (ListPreference)findPreference("nandroid_manage");
        mNandroidManage.setOnPreferenceChangeListener(this);

        mFlashInstallZip = (PreferenceScreen)findPreference("flash_install_zip");
        mFlashInstallZip.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mRebootNormal) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.reboot);
            alertDialogBuilder.setMessage(R.string.reboot_summary);
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SystemCommand.reboot("");
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, null);
            alertDialogBuilder.create().show();

        } else if (preference == mRebootRecovery) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.reboot);
            alertDialogBuilder.setMessage(R.string.recovery_summary);
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SystemCommand.reboot("recovery");
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, null);
            alertDialogBuilder.create().show();

        } else if (preference == mRebootDownload) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.reboot);
            alertDialogBuilder.setMessage(R.string.download_summary);
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SystemCommand.reboot("download");
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, null);
            alertDialogBuilder.create().show();

        } else if (preference == mFlashInstallZip) {
            Intent intent = new Intent(getApplicationContext(), ZipFilePickerActivity.class);
            intent.putExtra("title", getText(R.string.select_zip_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".zip");
            this.startActivity(intent);
        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNandroidBackup) {
            final String sdcardPath = Misc.getSdcardPath("internal".equals(newValue));
            TextInputDialog dlg = new TextInputDialog(this, android.R.string.ok, android.R.string.cancel);
            dlg.setFinishTextInputListener(new TextInputDialog.FinishTextInputListener() {
                @Override
                public void onFinishTextInput(CharSequence input) {
                    String inputName = input.toString();
                    inputName = inputName.replace("\n", "").trim();
                    SystemCommand.backup_rom(sdcardPath + inputName);
                    SystemCommand.reboot("recovery");
                }
            });
            dlg.show(R.string.backup, R.string.backup_name, Misc.getDateString());

        } else if (preference == mNandroidRestore) {
            final String sdcardPath = Misc.getSdcardPath("internal".equals(newValue));
            Intent intent = new Intent(getApplicationContext(), RestoreDirPickerActivity.class);
            intent.putExtra("title", getText(R.string.select_backup_title));
            intent.putExtra("select", "dir");
            intent.putExtra("mode", "restore");
            intent.putExtra("path", sdcardPath + Constant.CWM_BACKUP_DIR);
            this.startActivity(intent);

        } else if (preference == mNandroidManage) {
            final String sdcardPath = Misc.getSdcardPath("internal".equals(newValue));
            Intent intent = new Intent(getApplicationContext(), RestoreDirPickerActivity.class);
            intent.putExtra("title", getText(R.string.select_backup_title));
            intent.putExtra("select", "dir");
            intent.putExtra("mode", "manage");
            intent.putExtra("path", sdcardPath + Constant.CWM_BACKUP_DIR);
            this.startActivity(intent);
        }
        return false;
    }
}
