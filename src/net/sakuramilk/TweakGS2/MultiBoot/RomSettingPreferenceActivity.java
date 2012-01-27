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

package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.TextInputDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

public class RomSettingPreferenceActivity extends PreferenceActivity
    implements OnPreferenceChangeListener, OnPreferenceClickListener {

    private Context mContext;
    private int mRomId;
    private final MbsConf mMbsConf = new MbsConf();

    private static final String KEY_LABEL_CATEGORY = "rom_label_category";
    private static final String KEY_LABEL_TEXT = "rom_label_text";
    private static final String KEY_SYSTEM_PART = "rom_system_part_list";
    private static final String KEY_SYSTEM_IMG = "rom_system_img_list";
    private static final String KEY_SYSTEM_PATH = "rom_system_path_list";
    private static final String KEY_DATA_PART = "rom_data_part_list";
    private static final String KEY_DATA_IMG = "rom_data_img_list";
    private static final String KEY_DATA_PATH = "rom_data_path_list";

    private static final int REQUEST_SYS_IMG_PATH = 1000;
    private static final int REQUEST_DATA_IMG_PATH = 1001;

    private static final String[] PART_ENTRIES = {
        "mmcblk0p9(factoryfs)",
        "mmcblk0p10(data)",
        "mmcblk0p11(hidden)",
        "mmcblk1p1(emmc)",
        "mmcblk1p2(emmc)",
        "mmcblk1p3(emmc)",
    };
    private static final String[] PART_ENTRY_VALUES = {
        MbsConf.Partition.mmcblk0p9,
        MbsConf.Partition.mmcblk0p10,
        MbsConf.Partition.mmcblk0p11,
        MbsConf.Partition.mmcblk1p1,
        MbsConf.Partition.mmcblk1p2,
        MbsConf.Partition.mmcblk1p3,
    };

    private PreferenceScreen mLabelText;
    private ListPreference mSystemPart;
    private PreferenceScreen mSystemImg;
    private PreferenceScreen mSystemPath;
    private ListPreference mDataPart;
    private PreferenceScreen mDataImg;
    private PreferenceScreen mDataPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.rom_setting_pref);

        mContext = this;
        Intent intent = getIntent();
        mRomId = intent.getIntExtra("rom_id", -1);
        if ((0 < mRomId) || (mRomId > MbsConf.MAX_ROM_ID)) {
            finish();
        }

        PreferenceCategory categoryPref = (PreferenceCategory)findPreference(KEY_LABEL_CATEGORY);
        categoryPref.setTitle("ROM" + mRomId);

        mLabelText = (PreferenceScreen)findPreference(KEY_LABEL_TEXT);
        mLabelText.setSummary(Misc.getCurrentValueText(this, mMbsConf.getLabel(mRomId)));
        mLabelText.setOnPreferenceClickListener(this);

        mSystemPart = (ListPreference)findPreference(KEY_SYSTEM_PART);
        mSystemPart.setEntries(PART_ENTRIES);
        mSystemPart.setEntryValues(PART_ENTRY_VALUES);
        String sysPart = mMbsConf.getSystemPartition(mRomId);
        mSystemPart.setValue(sysPart);
        mSystemPart.setSummary(Misc.getCurrentValueText(
                this, Misc.getEntryFromEntryValue(PART_ENTRIES, PART_ENTRY_VALUES, sysPart)));
        mSystemPart.setOnPreferenceChangeListener(this);

        mSystemImg = (PreferenceScreen)findPreference(KEY_SYSTEM_IMG);
        mSystemImg.setSummary(Misc.getCurrentValueText(this, mMbsConf.getSystemImage(mRomId)));
        mSystemImg.setOnPreferenceClickListener(this);
        mSystemPath = (PreferenceScreen)findPreference(KEY_SYSTEM_PATH);
        mSystemPath.setSummary(Misc.getCurrentValueText(this, mMbsConf.getSystemPath(mRomId)));
        mSystemPath.setOnPreferenceClickListener(this);

        mDataPart = (ListPreference)findPreference(KEY_DATA_PART);
        mDataPart.setEntries(PART_ENTRIES);
        mDataPart.setEntryValues(PART_ENTRY_VALUES);
        String dataPart = mMbsConf.getDataPartition(mRomId);
        mDataPart.setValue(dataPart);
        mDataPart.setSummary(Misc.getCurrentValueText(
                this, Misc.getEntryFromEntryValue(PART_ENTRIES, PART_ENTRY_VALUES, dataPart)));

        mDataImg = (PreferenceScreen)findPreference(KEY_DATA_IMG);
        mDataImg.setSummary(Misc.getCurrentValueText(this, mMbsConf.getDataImage(mRomId)));
        mDataImg.setOnPreferenceClickListener(this);
        mDataPath = (PreferenceScreen)findPreference(KEY_DATA_PATH);
        mDataPath.setSummary(Misc.getCurrentValueText(this, mMbsConf.getDataPath(mRomId)));
        mDataPath.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        String value = objValue.toString();
        if (preference == mSystemPart) {
            mMbsConf.setSystemPartition(mRomId, value);
            mSystemPart.setSummary(Misc.getCurrentValueText(
                    this, Misc.getEntryFromEntryValue(PART_ENTRIES, PART_ENTRY_VALUES, value)));
        } else if (preference == mDataPart) {
            mMbsConf.setDataPartition(mRomId, value);
            mDataPart.setSummary(Misc.getCurrentValueText(
                    this, Misc.getEntryFromEntryValue(PART_ENTRIES, PART_ENTRY_VALUES, value)));
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mSystemImg) {
            Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
            intent.putExtra("title", getText(R.string.select_img_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".zip");
            this.startActivityForResult(intent, REQUEST_SYS_IMG_PATH);

        } else if (preference == mSystemPath) {
            TextInputDialog dlg = new TextInputDialog(this);
            dlg.setFinishTextInputListener(new TextInputDialog.FinishTextInputListener() {
                @Override
                public void onFinishTextInput(CharSequence input) {
                    String inputText = input.toString();
                    inputText = inputText.replace("\n", "").trim();
                    mMbsConf.setSystemPath(mRomId, inputText);
                    mSystemPath.setSummary((Misc.getCurrentValueText(mContext, inputText)));
                }
            });
            dlg.show(R.string.backup, R.string.path, mMbsConf.getSystemPath(mRomId));

        } else if (preference == mDataImg) {
            Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
            intent.putExtra("title", getText(R.string.select_img_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".zip");
            this.startActivityForResult(intent, REQUEST_DATA_IMG_PATH);

        } else if (preference == mDataPath) {
            TextInputDialog dlg = new TextInputDialog(this);
            dlg.setFinishTextInputListener(new TextInputDialog.FinishTextInputListener() {
                @Override
                public void onFinishTextInput(CharSequence input) {
                    String inputText = input.toString();
                    inputText = inputText.replace("\n", "").trim();
                    mMbsConf.setDataPath(mRomId, inputText);
                    mDataPath.setSummary((Misc.getCurrentValueText(mContext, inputText)));
                }
            });
            dlg.show(R.string.backup, R.string.path, mMbsConf.getDataPath(mRomId));

        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            String path = intent.getStringExtra("path");
            if (requestCode == REQUEST_SYS_IMG_PATH) {
                mMbsConf.setSystemImage(mRomId, path);
                mSystemImg.setSummary(path);

            } else if (requestCode == REQUEST_DATA_IMG_PATH) {
                mMbsConf.setDataImage(mRomId, path);
                mDataImg.setSummary(path);

            }
        }
    }
}
