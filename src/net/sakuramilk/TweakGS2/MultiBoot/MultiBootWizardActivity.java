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
import net.sakuramilk.TweakGS2.Parts.WizardPage;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity.OnPageEventListener;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MultiBootWizardActivity extends WizardPreferenceActivity
    implements OnClickListener, OnPageEventListener {

    private PreferenceManager mPrefManager;
    private PreferenceScreen mRootPref;
    private Context mContext;

    private MultiBootWizardStartPage mStartPage;
    private MultiBootWizardSelectSizePage mSelectSizePage;
    private MultiBootWizardSelectImagePage mSelectImagePage;
    private MultiBootWizardSelectZipPage mSelectZipPage;
    private MultiBootWizardInputLabelPage mInputLabelPage;
    private MultiBootWizardFinishPage mFinishPage;
    private WizardPage mRequestPage;

    private static final int STATE_MODE_SELECT = 0;
    private static final int STATE_SELECT_IMG_SIZE = 1;
    private static final int STATE_SELECT_IMG_FILE = 2;
    private static final int STATE_SELECT_ZIP_FILE = 3;
    private static final int STATE_INPUT_LABEL = 4;
    private static final int STATE_COMPLETE = 5;
    private int mState;

    public static final int MODE_NULL = 0;
    public static final int MODE_SELECT = 1;
    public static final int MODE_CREATE = 2;
    public static final int MODE_EXT_SD = 3;
    private int mMode;

    private String mSystemImageSize = null;
    private String mDataImageSize = null;
    private String mSystemImagePath = null;
    private String mDataImagePath = null;
    private String mInstallZipPath = null;
    private String mExtSdSystemPath = null;
    private String mExtSdDataPath = null;
    private String mRomLabel = null;

    private ProgressDialog mProgressDialog;
    private WakeLock mWakeLock;

    Handler mCreateImageFinishHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            mWakeLock.release();
            
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle("情報");
            if (mMode == MODE_CREATE && Misc.isNullOfEmpty(mInstallZipPath)) {
                alertDialogBuilder.setMessage("イメージの作成が完了しました");
                alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            } else {
                alertDialogBuilder.setMessage("イメージの作成が完了しました\nOKを押すとROMのインストールを開始します");
                alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SystemCommand.install_zip(mInstallZipPath);
                    }
                });
            }
            alertDialogBuilder.show();
        }
    };

    private void createImage() {
        
        mWakeLock.acquire();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("情報");
        mProgressDialog.setMessage("イメージを作成しています\nこの操作は数分かかる場合があります");
        mProgressDialog.show();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MbsConf mbsConf = new MbsConf();
                int romId = mbsConf.getNextRomId();
                String extSdPath = Misc.getSdcardPath(false);
                SystemCommand.mkdir(extSdPath + "/mbs");
                String sysImgPath = extSdPath + "/mbs/system_" + romId + ".img";
                String dataImgPath = extSdPath + "/mbs/data_" + romId + ".img";

                mbsConf.setLabel(romId, mRomLabel);
                mbsConf.setSystemPartition(romId, MbsConf.Partition.mmcblk1p1);
                mbsConf.setSystemImage(romId, sysImgPath);
                mbsConf.setDataPartition(romId, MbsConf.Partition.mmcblk1p1);
                mbsConf.setDataImage(romId, dataImgPath);

                int size = Integer.valueOf(mSystemImageSize) * 1024;
                SystemCommand.make_ext4_image(sysImgPath, 1024, size);
                size = Integer.valueOf(mDataImageSize) * 1024;
                SystemCommand.make_ext4_image(dataImgPath, 1024, size);

                mCreateImageFinishHandler.sendEmptyMessage(0);
            }
        });
        thread.start();
    }

    void backState() {
        switch (mState) {
            case STATE_SELECT_IMG_SIZE:
            case STATE_SELECT_IMG_FILE:
            {
                mState = STATE_MODE_SELECT;
                mBackButton.setVisibility(View.INVISIBLE);
                mStartPage.createPage(mPrefManager, mRootPref);
                break;
            }
            case STATE_SELECT_ZIP_FILE:
            {
                mState = STATE_SELECT_IMG_SIZE;
                mSelectSizePage.createPage(mPrefManager, mRootPref);
                break;
            }
            case STATE_INPUT_LABEL:
            {
                switch (mMode) {
                    case MODE_SELECT:
                        mState = STATE_SELECT_IMG_SIZE;
                        mSelectSizePage.createPage(mPrefManager, mRootPref);
                        break;
                    case MODE_CREATE:
                        mState = STATE_SELECT_ZIP_FILE;
                        mSelectZipPage.createPage(mPrefManager, mRootPref);
                        break;
                }
                break;
            }
            case STATE_COMPLETE:
            {
                mState = STATE_INPUT_LABEL;
                mNextButton.setText(R.string.next);
                mInputLabelPage.createPage(mPrefManager, mRootPref);
                break;
            }
        }
    }

    void nextState() {
        switch (mState) {
            case STATE_MODE_SELECT:
            {
                switch (mMode) {
                    case MODE_SELECT:
                    {
                        mState = STATE_SELECT_IMG_FILE;
                        mBackButton.setVisibility(View.VISIBLE);
                        mSelectImagePage.createPage(mPrefManager, mRootPref);
                        break;
                    }
                    case MODE_CREATE:
                    {
                        mState = STATE_SELECT_IMG_SIZE;
                        mBackButton.setVisibility(View.VISIBLE);
                        mSelectSizePage.createPage(mPrefManager, mRootPref);
                        break;
                    }
                    case MODE_EXT_SD:
                    {
                        if (mMode == MODE_EXT_SD) {
                            File sysPartFile = new File(MbsConf.Partition.mmcblk1p2);
                            File dataPartFile = new File(MbsConf.Partition.mmcblk1p3);
                            if (sysPartFile.exists()) {
                                mExtSdSystemPath = MbsConf.Partition.mmcblk1p2;
                            } else {
                                Toast.makeText(this, "外部SDカードの第2パーティションが見つかりません", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            if (dataPartFile.exists()) {
                                mExtSdDataPath = MbsConf.Partition.mmcblk1p3;
                            } else {
                                Toast.makeText(this, "外部SDカードの第3パーティションが見つかりません", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        mState = STATE_INPUT_LABEL;
                        mBackButton.setVisibility(View.VISIBLE);
                        mInputLabelPage.createPage(mPrefManager, mRootPref);
                        break;
                    }
                    default:
                        Toast.makeText(this, "選択してください", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            }
            case STATE_SELECT_IMG_SIZE:
            {
                if (Misc.isNullOfEmpty(mSystemImageSize) || Misc.isNullOfEmpty(mDataImageSize)) {
                    Toast.makeText(this, "選択してください", Toast.LENGTH_SHORT).show();
                } else {
                    long availableSize = Misc.getAvailableSdcardSize(false) / 1024 / 1024;
                    long imageSize = Long.valueOf(mSystemImageSize) + Long.valueOf(mDataImageSize);
                    if (availableSize < imageSize) {
                        Toast.makeText(this, "外部SDカードの空き容量が足りません", Toast.LENGTH_SHORT).show();
                    } else {
                        mState = STATE_SELECT_ZIP_FILE;
                        mSelectZipPage.createPage(mPrefManager, mRootPref);
                    }
                }
                break;
            }
            case STATE_SELECT_IMG_FILE:
            {
                if (Misc.isNullOfEmpty(mSystemImagePath)) {
                    Toast.makeText(this, "選択してください", Toast.LENGTH_SHORT).show();
                } else {
                    mState = STATE_INPUT_LABEL;
                    mInputLabelPage.createPage(mPrefManager, mRootPref);
                }
                break;
            }
            case STATE_SELECT_ZIP_FILE:
            {
                mState = STATE_INPUT_LABEL;
                mInputLabelPage.createPage(mPrefManager, mRootPref);
                break;
            }
            case STATE_INPUT_LABEL:
            {
                if (Misc.isNullOfEmpty(mRomLabel)) {
                    Toast.makeText(this, "入力してください", Toast.LENGTH_SHORT).show();
                } else {
                    mState = STATE_COMPLETE;
                    mNextButton.setText(R.string.complete);
                    mFinishPage.createPage(mPrefManager, mRootPref);
                    if (mMode == MODE_SELECT) {
                        if (mSystemImagePath != null) {
                            mFinishPage.onCallback(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE, mSystemImagePath);
                        }
                        if (mDataImagePath != null) {
                            mFinishPage.onCallback(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE, mDataImagePath);
                        }
                    } else if (mMode == MODE_CREATE) {
                        if (mSystemImageSize != null) {
                            mFinishPage.onCallback(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE, mSystemImageSize);
                        }
                        if (mDataImageSize != null) {
                            mFinishPage.onCallback(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE, mDataImageSize);
                        }
                        if (mInstallZipPath != null) {
                            mFinishPage.onCallback(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE, mInstallZipPath);
                        }
                    } else if (mMode == MODE_EXT_SD) {
                        if (mExtSdSystemPath != null) {
                            mFinishPage.onCallback(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE, mExtSdSystemPath);
                        }
                        if (mExtSdDataPath != null) {
                            mFinishPage.onCallback(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE, mExtSdDataPath);
                        }
                    }
                    if (mRomLabel != null) {
                        mFinishPage.onCallback(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE, mRomLabel);
                    }
                }
                break;
            }
            case STATE_COMPLETE:
            {
                createImage();
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.multi_boot_wizard_pref);

        mContext = this;
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "MultiBootWizard");

        mBackButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPrefManager = getPreferenceManager();
        mRootPref = (PreferenceScreen)mPrefManager.findPreference("root_pref");
        mState = STATE_MODE_SELECT;
        mMode = MODE_NULL;

        mStartPage = new MultiBootWizardStartPage(this, this);
        mSelectSizePage = new MultiBootWizardSelectSizePage(this, this);
        mSelectImagePage = new MultiBootWizardSelectImagePage(this, this);
        mSelectZipPage = new MultiBootWizardSelectZipPage(this, this);
        mInputLabelPage = new MultiBootWizardInputLabelPage(this, this);
        mFinishPage = new MultiBootWizardFinishPage(this, this);

        mBackButton.setVisibility(View.INVISIBLE);
        mStartPage.createPage(mPrefManager, mRootPref);
    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        switch (id) {
            case R.id.back_button:
                backState();
                break;
            case R.id.next_button:
                nextState();
                break;
        }
    }

    @Override
    public void onPageEvent(int eventCode, Object objValue) {
        switch (mState) {
            case STATE_MODE_SELECT:
            {
                switch (eventCode) {
                    case MultiBootWizardEvent.EVENT_RESULT_MODE:
                        mMode = (Integer)objValue;
                        break;
                }
                break;
            }
            case STATE_SELECT_IMG_SIZE:
            {
                switch (eventCode) {
                    case MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE:
                        mSystemImageSize = (String)objValue;
                        break;
                    case MultiBootWizardEvent.EVENT_RESULT_DATA_IMG_SIZE:
                        mDataImageSize = (String)objValue;
                        break;
                }
                break;
            }
            case STATE_SELECT_IMG_FILE:
            {
                switch (eventCode) {
                    case MultiBootWizardEvent.EVENT_REQUEST_SYSTEM_IMG_PATH:
                        mRequestPage = mSelectImagePage;
                        this.startActivityForResult((Intent)objValue, eventCode);
                        break;
                    case MultiBootWizardEvent.EVENT_REQUEST_DATA_IMG_PATH:
                        mRequestPage = mSelectImagePage;
                        this.startActivityForResult((Intent)objValue, eventCode);
                        break;
                    case MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_PATH:
                        mSystemImagePath = (String)objValue;
                        break;
                    case MultiBootWizardEvent.EVENT_RESULT_DATA_IMG_PATH:
                        mDataImagePath = (String)objValue;
                        break;
                }
                break;
            }
            case STATE_SELECT_ZIP_FILE:
            {
                switch (eventCode) {
                    case MultiBootWizardEvent.EVENT_REQUEST_ZIP_PATH:
                        mRequestPage = mSelectZipPage;
                        this.startActivityForResult((Intent)objValue, eventCode);
                        break;
                    case MultiBootWizardEvent.EVENT_RESULT_ZIP_PATH:
                        mInstallZipPath = (String)objValue;
                        break;
                }
                break;
            }
            case STATE_INPUT_LABEL:
            {
                switch (eventCode) {
                    case MultiBootWizardEvent.EVENT_RESULT_LABEL:
                        mRomLabel = (String)objValue;
                        break;
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == MultiBootWizardEvent.EVENT_REQUEST_SYSTEM_IMG_PATH) {
                String path = intent.getStringExtra("path");
                mRequestPage.onCallback(requestCode, path);
            } else if (requestCode == MultiBootWizardEvent.EVENT_REQUEST_DATA_IMG_PATH) {
                String path = intent.getStringExtra("path");
                mRequestPage.onCallback(requestCode, path);
            } else if (requestCode == MultiBootWizardEvent.EVENT_REQUEST_ZIP_PATH) {
                String path = intent.getStringExtra("path");
                mRequestPage.onCallback(requestCode, path);
            }
        }
    }
}
