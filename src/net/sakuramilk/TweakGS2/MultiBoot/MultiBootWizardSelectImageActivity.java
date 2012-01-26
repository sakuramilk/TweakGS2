package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;

public class MultiBootWizardSelectImageActivity extends WizardPreferenceActivity
    implements OnClickListener, OnPreferenceClickListener {

    private static final String KEY_SYSTEM_IMG_FILE = "system_img_file";
    private static final String KEY_DATA_IMG_FILE = "data_img_file";
    
    private static final int REQUEST_SYSTEM_IMG = 1000;
    private static final int REQUEST_DATA_IMG = 1001;
    private static final int REQUEST_NEXT = 1002;
    
    private PreferenceScreen mSystemImageFile;
    private PreferenceScreen mDataImageFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.multi_boot_wizard_select_size_pref);
        
        mBackButton.setVisibility(View.INVISIBLE);
        mNextButton.setOnClickListener(this);

        mSystemImageFile = (PreferenceScreen)findPreference(KEY_SYSTEM_IMG_FILE);
        mSystemImageFile.setOnPreferenceClickListener(this);
        mDataImageFile = (PreferenceScreen)findPreference(KEY_DATA_IMG_FILE);
        mDataImageFile.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mSystemImageFile) {
            Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
            intent.putExtra("title", getText(R.string.select_img_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".img");
            this.startActivityForResult(intent, REQUEST_SYSTEM_IMG);
        } else if (preference == mDataImageFile) {
            Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
            intent.putExtra("title", getText(R.string.select_img_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".img");
            this.startActivityForResult(intent, REQUEST_DATA_IMG);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), MultiBootWizardSelectSizeActivity.class);
        startActivityForResult(intent, REQUEST_NEXT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SYSTEM_IMG) {
                String path = intent.getStringExtra("path");
                mSystemImageFile.setSummary(path);
            } else if (requestCode == REQUEST_SYSTEM_IMG) {
                String path = intent.getStringExtra("path");
                mDataImageFile.setSummary(path);
            } else {
                setResult(resultCode, null);
                finish();
            }
        }
    }
}
