package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.util.Misc;
import net.sakuramilk.util.SystemCommand;
import net.sakuramilk.widget.ApplyButtonPreferenceActivity;
import net.sakuramilk.widget.TextInputDialog;
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
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class ImageCreatePreferenceActivity extends ApplyButtonPreferenceActivity
    implements OnPreferenceClickListener, OnClickListener, OnPreferenceChangeListener {

    private static final String TMP_MOUNT_DIR = "/data/TweakGS2/mnt/tmp";
    private static final int REQUEST_IMG_DIR = 1000;

    private PreferenceScreen mDstDirPref;
    private PreferenceScreen mDstFilePref;
    private ListPreference mImageSizePref;
    private String mDstDir = null;
    private String mDstFile = null;
    private String mImageSize = null;
    private Context mContext;
    private String mDevicePath;
    private String mRootPath;
    private boolean mNeedUnmount = false;
    private WakeLock mWakeLock;
    private ProgressDialog mProgressDialog;

    Handler mCreateImageFinishHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            mWakeLock.release();
            
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle("情報");
            alertDialogBuilder.setMessage("イメージの作成が完了しました");
            alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    String path = mDstDir + "/" + mDstFile;
                    path = path.replace("//", "/");
                    intent.putExtra("path", path);
                    setResult(RESULT_OK, intent); 
                    finish();
                }
            });
            alertDialogBuilder.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.image_create_pref);
        
        mContext = this;
        Intent intent = getIntent();
        String target = intent.getStringExtra("target");
        mDevicePath = intent.getStringExtra("device_path");
        
        String path;
        if (MbsConf.Partition.mmcblk0p11.equals(mDevicePath)) {
            path = Misc.getSdcardPath(true);
        } else if (MbsConf.Partition.mmcblk1p1.equals(mDevicePath)) {
            path = Misc.getSdcardPath(false);
        } else {
            path = TMP_MOUNT_DIR;
        }
        mRootPath = path;

        mDstDirPref = (PreferenceScreen)findPreference("dst_dir");
        mDstDirPref.setOnPreferenceClickListener(this);
        mDstDir = "/";
        mDstDirPref.setSummary(Misc.getCurrentValueText(this, mDstDir));

        mDstFilePref = (PreferenceScreen)findPreference("dst_file");
        mDstFilePref.setOnPreferenceClickListener(this);

        mImageSizePref = (ListPreference)findPreference("img_size");
        mImageSizePref.setOnPreferenceChangeListener(this);
        if ("system".equals(target)) {
            mImageSizePref.setEntries(R.array.system_image_size_entries);
            mImageSizePref.setEntryValues(R.array.system_image_size_values);
        } else {
            mImageSizePref.setEntries(R.array.data_image_size_entries);
            mImageSizePref.setEntryValues(R.array.data_image_size_values);
        }

        mApplyButton.setOnClickListener(this);
        mApplyButton.setText(R.string.create);
        mApplyButton.setEnabled(false);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mDstDirPref) {
            if (!MbsConf.Partition.mmcblk0p12.equals(mDevicePath) &&
                !MbsConf.Partition.mmcblk1p1.equals(mDevicePath)) {
                SystemCommand.umount(TMP_MOUNT_DIR);
                SystemCommand.mount(mDevicePath, TMP_MOUNT_DIR, null, null);
                mNeedUnmount = true;
            }
            Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
            intent.putExtra("title", getText(R.string.select_dir_title));
            intent.putExtra("path", mRootPath);
            intent.putExtra("chroot", mRootPath);
            intent.putExtra("select", "dir");
            this.startActivityForResult(intent, REQUEST_IMG_DIR);

        } else if (preference == mDstFilePref) {
            TextInputDialog dlg = new TextInputDialog(this);
            dlg.setFinishTextInputListener(new TextInputDialog.FinishTextInputListener() {
                @Override
                public void onFinishTextInput(CharSequence input) {
                    String inputText = input.toString();
                    inputText = inputText.replace("\n", "").trim();
                    int index = inputText.lastIndexOf(".img");
                    int imgExtPos = inputText.length() - ".img".length();
                    if (index == -1 || index != imgExtPos) {
                        inputText += ".img";
                    }
                    mDstFile = inputText;
                    mDstFilePref.setSummary((Misc.getCurrentValueText(mContext, inputText)));
                    if (mDstDir != null && mDstFile != null && mImageSize != null) {
                        mApplyButton.setEnabled(true);
                    }
                }
            });
            dlg.show(R.string.rom_label_title, R.string.rom_label_message, mDstFile == null ? ".img" : mDstFile);

        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mImageSizePref) {
            String path;
            if (MbsConf.Partition.mmcblk0p12.equals(mDevicePath)) {
                path = Misc.getSdcardPath(true);
            } else if (MbsConf.Partition.mmcblk1p1.equals(mDevicePath)) {
                path = Misc.getSdcardPath(false);
            } else {
                path = TMP_MOUNT_DIR;
                SystemCommand.umount(TMP_MOUNT_DIR);
                SystemCommand.mount(mDevicePath, TMP_MOUNT_DIR, null, null);
            }
            long availableSize = Misc.getAvailableSize(path);
            long size = Integer.valueOf((String)objValue) * 1024;
            SystemCommand.umount(TMP_MOUNT_DIR);
            if (availableSize <= size) {
                Toast.makeText(this, "空き容量が足りません", Toast.LENGTH_SHORT).show();
            } else {         
                mImageSize = (String)objValue;
                mImageSizePref.setValue((String)objValue);
                mImageSizePref.setSummary(Misc.getCurrentValueText(this,
                        Misc.getEntryFromEntryValue(mImageSizePref.getEntries(), mImageSizePref.getEntryValues(), (String)objValue)));
                if (mDstDir != null && mDstFile != null && mImageSize != null) {
                    mApplyButton.setEnabled(true);
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Tgs2ImageCreate");
        mWakeLock.acquire();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("情報");
        mProgressDialog.setMessage("イメージを作成しています\nこの操作は数分かかる場合があります");
        mProgressDialog.show();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                int size = Integer.valueOf(mImageSize) * 1024;
                String path = mRootPath + mDstDir + "/" + mDstFile;
                SystemCommand.make_ext4_image(path, 1024, size);
                mCreateImageFinishHandler.sendEmptyMessage(0);
            }
        });
        thread.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMG_DIR) {
                if (mNeedUnmount) {
                    mNeedUnmount = false;
                    SystemCommand.umount(TMP_MOUNT_DIR);
                }
                String path = intent.getStringExtra("path");
                mDstDir = path;
                mDstDirPref.setSummary(Misc.getCurrentValueText(this, path));
                if (mDstDir != null && mDstFile != null && mImageSize != null) {
                    mApplyButton.setEnabled(true);
                }
            }
        }
    }
}
