package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.SystemCommand;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MultiBootWizardActivity extends WizardPreferenceActivity
    implements OnClickListener, OnPreferenceChangeListener, OnPreferenceClickListener {

    private static final int STATE_SELECT_SIZE = 0;
    private static final int STATE_SELECT_ZIP = 1;
    private static final int STATE_CREATE_IMAGE = 2;
    
    private static final String KEY_SELECT_SYSTEM_SIZE = "system_size";
    private static final String KEY_SELECT_DATA_SIZE = "data_size";

    private int mState;
    private int mRomId;
    private ProgressDialog mProgressDialog; 

    private static final String[] SYSTEM_SIZE_ENTRIES = { "200M", "300M", "400M", "512M", "612M" };
    private static final String[] SYSTEM_SIZE_ENTRY_VALUES = { "200", "300", "400", "512", "612" };
    private static final String SYSTEM_SIZE_DEFAULT_VALUE = "612";

    private static final String[] DATA_SIZE_ENTRIES = { "200M", "300M", "400M", "500M", "600M", "700M", "800M", "900M", "1000M" };
    private static final String[] DATA_SIZE_ENTRY_VALUES = { "200", "300", "400", "500", "600", "700", "800", "900", "1000" };
    private static final String DATA_SIZE_DEFAULT_VALUE = "500";
    
    private ListPreference mSelectSystemSize = null;
    private ListPreference mSelectDataSize = null;
    private PreferenceScreen mSelectZipFile = null;
    private String mSystemSize;
    private String mDataSize;
    private String mZipPath = null;

    Handler mCreateImageFinishHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            SystemCommand.install_zip(mZipPath);
        }
    };
    
    private void createPreference() {
        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = prefManager.createPreferenceScreen(this);
        
        switch (mState) {
            case STATE_SELECT_SIZE:
            {
                mBackButton.setVisibility(View.INVISIBLE);
                PreferenceScreen pref = prefManager.createPreferenceScreen(this);
                pref.setTitle("新規ROMを作成します");
                pref.setSummary(
                        "SYSTEMイメージとDATAイメージのサイズを指定してください\n" +
                        "注意: DATAイメージのサイズを設定しない場合はdataパーティションを共用します");
                pref.setSelectable(false);
                rootPref.addPreference(pref);
    
                if (mSelectSystemSize == null) {
                    mSelectSystemSize = new ListPreference(this);
                    mSelectSystemSize.setKey(KEY_SELECT_SYSTEM_SIZE);
                    mSelectSystemSize.setTitle("SYSTEMサイズ");
                    mSelectSystemSize.setEntries(SYSTEM_SIZE_ENTRIES);
                    mSelectSystemSize.setEntryValues(SYSTEM_SIZE_ENTRY_VALUES);
                    mSelectSystemSize.setValue(SYSTEM_SIZE_DEFAULT_VALUE);
                    mSelectSystemSize.setSummary(SYSTEM_SIZE_DEFAULT_VALUE + "M");
                    mSelectSystemSize.setOnPreferenceChangeListener(this);
                }
                rootPref.addPreference(mSelectSystemSize);

                if (mSelectDataSize == null) {
                    mSelectDataSize = new ListPreference(this);
                    mSelectDataSize.setKey(KEY_SELECT_DATA_SIZE);
                    mSelectDataSize.setTitle("DATAサイズ");
                    mSelectDataSize.setEntries(DATA_SIZE_ENTRIES);
                    mSelectDataSize.setEntryValues(DATA_SIZE_ENTRY_VALUES);
                    mSelectDataSize.setValue(DATA_SIZE_DEFAULT_VALUE);
                    mSelectDataSize.setSummary(DATA_SIZE_DEFAULT_VALUE + "M");
                    mSelectDataSize.setOnPreferenceChangeListener(this);
                }
                rootPref.addPreference(mSelectDataSize);
                break;
            }
            case STATE_SELECT_ZIP:
            {
                mBackButton.setVisibility(View.VISIBLE);
                PreferenceScreen pref = prefManager.createPreferenceScreen(this);
                pref.setTitle("ROMのインストール");
                pref.setSummary(
                        "インストールするCWM形式のzipファイルを指定します");
                pref.setSelectable(false);
                rootPref.addPreference(pref);

                if (mSelectZipFile == null) {
                    mSelectZipFile = prefManager.createPreferenceScreen(this);
                    mSelectZipFile.setTitle("zipファイル");
                    mSelectZipFile.setOnPreferenceClickListener(this);
                }
                rootPref.addPreference(mSelectZipFile);
                break;
            }
            case STATE_CREATE_IMAGE:
            {
                mBackButton.setVisibility(View.VISIBLE);
                PreferenceScreen pref = prefManager.createPreferenceScreen(this);
                pref.setTitle("ROMイメージを作成します");
                pref.setSummary(
                        "以下の内容よければ作成ボタンを選択してください\n" +
                        "イメージ作成後、recoveryモードでinstallします");
                pref.setSelectable(false);
                rootPref.addPreference(pref);
                pref = prefManager.createPreferenceScreen(this);
                mSystemSize = mSelectSystemSize.getValue();
                pref.setTitle("SYSTEMサイズ");
                pref.setSummary(mSystemSize + "M");
                pref.setSelectable(false);
                rootPref.addPreference(pref);
                pref = prefManager.createPreferenceScreen(this);
                mDataSize = mSelectDataSize.getValue();
                pref.setTitle("DATAサイズ");
                pref.setSummary(mDataSize + "M");
                pref.setSelectable(false);
                rootPref.addPreference(pref);
                pref = prefManager.createPreferenceScreen(this);
                pref.setTitle("zipファイル");
                pref.setSummary(mZipPath);
                pref.setSelectable(false);
                rootPref.addPreference(pref);
                break;
            }
        }
        
        setPreferenceScreen(rootPref);
    }

    private void createImage() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("情報");
        mProgressDialog.setMessage("イメージを作成しています\nこの操作は数分かかる場合があります");
        mProgressDialog.show();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int size = Integer.valueOf(mSystemSize) * 1024;
                SystemCommand.make_ext4_image("/sdcard/mbs/system.img", 1024, size);
                size = Integer.valueOf(mDataSize) * 1024;
                SystemCommand.make_ext4_image("/sdcard/mbs/data.img", 1024, size);

                mCreateImageFinishHandler.sendEmptyMessage(0);
            }
        });
        thread.start(); 
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.multi_boot_wizard_title);

        mBackButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);

        Intent intent = getIntent();
        mRomId = intent.getIntExtra("rom_id", -1);
        if (mRomId == -1) {
            finish();
        }
        mState = intent.getIntExtra("state", STATE_SELECT_SIZE);
        
        mSystemSize = SYSTEM_SIZE_DEFAULT_VALUE;
        mDataSize = DATA_SIZE_DEFAULT_VALUE;
        
        createPreference();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.back_button:
                switch (mState) {
                    case STATE_SELECT_ZIP:
                    {
                        mState = STATE_SELECT_SIZE;
                        createPreference();
                        break;
                    }
                    case STATE_CREATE_IMAGE:
                    {
                        mState = STATE_SELECT_ZIP;
                        createPreference();
                        break;
                    }
                }
                break;
            case R.id.next_button:
                switch (mState) {
                    case STATE_SELECT_SIZE:
                    {
                        mState = STATE_SELECT_ZIP;
                        createPreference();
                        break;
                    }
                    case STATE_SELECT_ZIP:
                    {
                        if (mZipPath == null) {
                            Toast.makeText(this, "zipファイルを選択してください", Toast.LENGTH_SHORT);
                        } else {
                            mState = STATE_CREATE_IMAGE;
                            createPreference();
                        }
                        break;
                    }
                    case STATE_CREATE_IMAGE:
                    {
                        createImage();
                        break;
                    }
                }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (mSelectSystemSize == preference) {
            mSelectSystemSize.setSummary(objValue.toString() + "M");
        } else if (mSelectDataSize == preference) {
            mSelectDataSize.setSummary(objValue.toString() + "M");
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mSelectZipFile) {
            Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
            intent.putExtra("title", getText(R.string.select_zip_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".zip");
            this.startActivityForResult(intent, 1000);
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            mZipPath = intent.getStringExtra("path");
            mSelectZipFile.setSummary(mZipPath);
        }
    }
}
