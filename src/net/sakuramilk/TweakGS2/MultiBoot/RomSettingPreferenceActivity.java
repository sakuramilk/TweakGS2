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

import java.io.File;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SystemCommand;
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
import android.view.KeyEvent;

public class RomSettingPreferenceActivity extends PreferenceActivity
    implements OnPreferenceChangeListener, OnPreferenceClickListener {

    private Context mContext;
    private int mRomId;
    private final MbsConf mMbsConf = new MbsConf();

    private static final String KEY_LABEL_CATEGORY = "rom_label_category";
    private static final String KEY_LABEL_TEXT = "rom_label_text";
    private static final String KEY_SYSTEM_PART = "rom_system_part_list";
    private static final String KEY_SYSTEM_IMG = "rom_system_img_list";
    private static final String KEY_DATA_PART = "rom_data_part_list";
    private static final String KEY_DATA_IMG = "rom_data_img_list";
    private static final String KEY_KERNEL_PART = "rom_kernel_part_list";
    private static final String KEY_KERNEL_IMG = "rom_kernel_img_list";

    private static final int REQUEST_SYS_IMG_PATH = 1000;
    private static final int REQUEST_DATA_IMG_PATH = 1001;
    private static final int REQUEST_KERNEL_IMG_PATH = 1002;

    private static final String TMP_MOUNT_DIR = "/data/TweakGS2/mnt/tmp";
    private static final String[] PART_ENTRIES = {
        "mmcblk0p9(factoryfs)",
        "mmcblk0p10(data)",
        "mmcblk0p11(sdcard)",
        "mmcblk0p12(hidden)",
        "mmcblk1p1(emmc)",
        "mmcblk1p2(emmc)",
        "mmcblk1p3(emmc)",
    };
    private static final String[] PART_ENTRY_VALUES = {
        MbsConf.Partition.mmcblk0p9,
        MbsConf.Partition.mmcblk0p10,
        MbsConf.Partition.mmcblk0p11,
        MbsConf.Partition.mmcblk0p12,
        MbsConf.Partition.mmcblk1p1,
        MbsConf.Partition.mmcblk1p2,
        MbsConf.Partition.mmcblk1p3,
    };

    private PreferenceScreen mLabelText;
    private ListPreference mSystemPart;
    private ListPreference mSystemImg;
    private ListPreference mDataPart;
    private ListPreference mDataImg;
    private ListPreference mKernelPart;
    private ListPreference mKernelImg;
    private boolean mNeedUnmount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.rom_setting_pref);

        File tmp = new File(TMP_MOUNT_DIR);
        if (!tmp.exists()) {
            SystemCommand.mkdir(TMP_MOUNT_DIR);
        }

        mContext = this;
        Intent intent = getIntent();
        mRomId = intent.getIntExtra("rom_id", -1);
        if ((mRomId < 0) || (mRomId > MbsConf.MAX_ROM_ID)) {
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
        if (Misc.isNullOfEmpty(sysPart)) {
            sysPart = MbsConf.Partition.mmcblk0p9;
            mMbsConf.setSystemPartition(mRomId, sysPart);
        }
        mSystemPart.setValue(sysPart);
        mSystemPart.setSummary(Misc.getCurrentValueText(
                this, Misc.getEntryFromEntryValue(PART_ENTRIES, PART_ENTRY_VALUES, sysPart)));
        mSystemPart.setOnPreferenceChangeListener(this);

        mSystemImg = (ListPreference)findPreference(KEY_SYSTEM_IMG);
        mSystemImg.setSummary(Misc.getCurrentValueText(this, mMbsConf.getSystemImage(mRomId)));
        mSystemImg.setOnPreferenceChangeListener(this);

        mDataPart = (ListPreference)findPreference(KEY_DATA_PART);
        mDataPart.setEntries(PART_ENTRIES);
        mDataPart.setEntryValues(PART_ENTRY_VALUES);
        String dataPart = mMbsConf.getDataPartition(mRomId);
        if (Misc.isNullOfEmpty(dataPart)) {
            dataPart = MbsConf.Partition.mmcblk0p10;
            mMbsConf.setDataPartition(mRomId, dataPart);
            mMbsConf.setDataPath(mRomId, "/data" + mRomId);
        }
        mDataPart.setValue(dataPart);
        mDataPart.setSummary(Misc.getCurrentValueText(
                this, Misc.getEntryFromEntryValue(PART_ENTRIES, PART_ENTRY_VALUES, dataPart)));
        mDataPart.setOnPreferenceChangeListener(this);

        mDataImg = (ListPreference)findPreference(KEY_DATA_IMG);
        mDataImg.setSummary(Misc.getCurrentValueText(this, mMbsConf.getDataImage(mRomId)));
        mDataImg.setOnPreferenceChangeListener(this);

        mKernelPart = (ListPreference)findPreference(KEY_KERNEL_PART);
        mKernelPart.setEntries(PART_ENTRIES);
        mKernelPart.setEntryValues(PART_ENTRY_VALUES);
        String kernelPart = mMbsConf.getKernelPartition(mRomId);
        if (Misc.isNullOfEmpty(kernelPart)) {
            kernelPart = MbsConf.Partition.mmcblk0p10;
            mMbsConf.setKernelPartition(mRomId, kernelPart);
        }
        mKernelPart.setValue(kernelPart);
        mKernelPart.setSummary(Misc.getCurrentValueText(
                this, Misc.getEntryFromEntryValue(PART_ENTRIES, PART_ENTRY_VALUES, dataPart)));
        mKernelPart.setOnPreferenceChangeListener(this);

        mKernelImg = (ListPreference)findPreference(KEY_KERNEL_IMG);
        mKernelImg.setSummary(Misc.getCurrentValueText(this, mMbsConf.getKernelImage(mRomId)));
        mKernelImg.setOnPreferenceChangeListener(this);

        //registerForContextMenu(getListView());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        String value = objValue.toString();
        if (preference == mSystemPart) {
            mMbsConf.setSystemPartition(mRomId, value);
            mSystemPart.setValue(value);
            mSystemPart.setSummary(Misc.getCurrentValueText(
                    this, Misc.getEntryFromEntryValue(PART_ENTRIES, PART_ENTRY_VALUES, value)));

        } else if (preference == mSystemImg) {
            if ("modify".equals(value)) {
                String part = mSystemPart.getValue();
                String path;
                if (MbsConf.Partition.mmcblk0p11.equals(part)) {
                    path = Misc.getSdcardPath(true);
                } else if (MbsConf.Partition.mmcblk1p1.equals(part)) {
                    path = Misc.getSdcardPath(false);
                } else {
                    path = TMP_MOUNT_DIR;
                    SystemCommand.umount(TMP_MOUNT_DIR);
                    SystemCommand.mount(part, TMP_MOUNT_DIR, null, null);
                    mNeedUnmount = true;
                }
                Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
                intent.putExtra("title", getText(R.string.select_img_title));
                intent.putExtra("path", path);
                intent.putExtra("chroot", path);
                intent.putExtra("select", "file");
                intent.putExtra("filter", ".img");
                this.startActivityForResult(intent, REQUEST_SYS_IMG_PATH);
            } else if ("create".equals(value)) {
                Intent intent = new Intent(getApplicationContext(), ImageCreatePreferenceActivity.class);
                intent.putExtra("target", "system");
                intent.putExtra("device_path", mSystemPart.getValue());
                this.startActivityForResult(intent, REQUEST_SYS_IMG_PATH);
            } else if ("delete".equals(value)) {
                mMbsConf.setSystemImage(mRomId, "");
                mSystemImg.setSummary(Misc.getCurrentValueText(this, null));
            }

        } else if (preference == mDataPart) {
            mMbsConf.setDataPartition(mRomId, value);
            mDataPart.setValue(value);
            mDataPart.setSummary(Misc.getCurrentValueText(
                    this, Misc.getEntryFromEntryValue(PART_ENTRIES, PART_ENTRY_VALUES, value)));
            if (MbsConf.Partition.mmcblk0p10.equals(value)) {
                mMbsConf.setDataPath(mRomId, "/data" + mRomId);
            } else {
                mMbsConf.setDataPath(mRomId, "/");
            }

        } else if (preference == mDataImg) {
            if ("modify".equals(value)) {
                String part = mDataPart.getValue();
                String path;
                if (MbsConf.Partition.mmcblk0p11.equals(part)) {
                    path = Misc.getSdcardPath(true);
                } else if (MbsConf.Partition.mmcblk1p1.equals(part)) {
                    path = Misc.getSdcardPath(false);
                } else {
                    path = TMP_MOUNT_DIR;
                    SystemCommand.umount(TMP_MOUNT_DIR);
                    SystemCommand.mount(part, TMP_MOUNT_DIR, null, null);
                    mNeedUnmount = true;
                }
                Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
                intent.putExtra("title", getText(R.string.select_img_title));
                intent.putExtra("path", path);
                intent.putExtra("chroot", path);
                intent.putExtra("select", "file");
                intent.putExtra("filter", ".img");
                this.startActivityForResult(intent, REQUEST_DATA_IMG_PATH);
            } else if ("create".equals(value)) {
                Intent intent = new Intent(getApplicationContext(), ImageCreatePreferenceActivity.class);
                intent.putExtra("target", "data");
                intent.putExtra("device_path", mDataPart.getValue());
                this.startActivityForResult(intent, REQUEST_DATA_IMG_PATH);
            } else if ("delete".equals(value)) {
                mMbsConf.setDataImage(mRomId, "");
                mDataImg.setSummary(Misc.getCurrentValueText(this, null));
                if (MbsConf.Partition.mmcblk0p10.equals(mMbsConf.getDataPartition(mRomId))) {
                    mMbsConf.setDataPath(mRomId, "/data" + mRomId);
                } else {
                    mMbsConf.setDataPath(mRomId, "/");
                }
            }

        } else if (preference == mKernelPart) {
            mMbsConf.setKernelPartition(mRomId, value);
            mKernelPart.setValue(value);
            mKernelPart.setSummary(Misc.getCurrentValueText(
                    this, Misc.getEntryFromEntryValue(PART_ENTRIES, PART_ENTRY_VALUES, value)));

        } else if (preference == mKernelImg) {
            if ("modify".equals(value)) {
                String part = mKernelPart.getValue();
                String path;
                if (MbsConf.Partition.mmcblk0p11.equals(part)) {
                    path = Misc.getSdcardPath(true);
                } else if (MbsConf.Partition.mmcblk1p1.equals(part)) {
                    path = Misc.getSdcardPath(false);
                } else {
                    path = TMP_MOUNT_DIR;
                    SystemCommand.umount(TMP_MOUNT_DIR);
                    SystemCommand.mount(part, TMP_MOUNT_DIR, null, null);
                    mNeedUnmount = true;
                }
                Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
                intent.putExtra("title", getText(R.string.select_img_title));
                intent.putExtra("path", path);
                intent.putExtra("chroot", path);
                intent.putExtra("select", "file");
                this.startActivityForResult(intent, REQUEST_KERNEL_IMG_PATH);
            } else if ("delete".equals(value)) {
                mMbsConf.setKernelImage(mRomId, "");
                mKernelImg.setSummary(Misc.getCurrentValueText(this, null));
            }
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mLabelText) {
            TextInputDialog dlg = new TextInputDialog(this);
            dlg.setFinishTextInputListener(new TextInputDialog.FinishTextInputListener() {
                @Override
                public void onFinishTextInput(CharSequence input) {
                    String inputText = input.toString();
                    inputText = inputText.replace("\n", "").trim();
                    mMbsConf.setLabel(mRomId, inputText);
                    mLabelText.setSummary((Misc.getCurrentValueText(mContext, inputText)));
                }
            });
            dlg.show(R.string.rom_label_title, R.string.rom_label_message, mMbsConf.getLabel(mRomId));
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (REQUEST_SYS_IMG_PATH == requestCode) {
                String path = intent.getStringExtra("path");
                mMbsConf.setSystemImage(mRomId, path);
                mSystemImg.setSummary(Misc.getCurrentValueText(this, path));
            } else if (REQUEST_DATA_IMG_PATH == requestCode) {
                String path = intent.getStringExtra("path");
                mMbsConf.setDataImage(mRomId, path);
                mDataImg.setSummary(Misc.getCurrentValueText(this, path));
                mMbsConf.setDataPath(mRomId, "/");
            } else if (REQUEST_KERNEL_IMG_PATH == requestCode) {
                String path = intent.getStringExtra("path");
                mMbsConf.setKernelImage(mRomId, path);
                mKernelImg.setSummary(Misc.getCurrentValueText(this, path));
            }
        }
        if (mNeedUnmount) {
            mNeedUnmount = false;
            SystemCommand.umount(TMP_MOUNT_DIR);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if(e.getAction() == KeyEvent.ACTION_UP) {
                Intent intent = new Intent();
                intent.putExtra("rom_id", mRomId);
                setResult(RESULT_OK, intent);
                finish();
                return false;
            }
            return true;
        }
        return super.dispatchKeyEvent(e);
    }
}
