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

public class MultiBootWizardZipSelectActivity extends WizardPreferenceActivity
implements OnClickListener, OnPreferenceClickListener {

    private static final String KEY_INSTALL_ZIP_FILE = "install_zip_file";

    private static final int REQUEST_INSTALL_ZIP = 1000;
    private static final int REQUEST_NEXT = 1001;

    private PreferenceScreen mInstallZipFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.multi_boot_wizard_select_size_pref);
        
        mBackButton.setVisibility(View.INVISIBLE);
        mNextButton.setOnClickListener(this);

        mInstallZipFile = (PreferenceScreen)findPreference(KEY_INSTALL_ZIP_FILE);
        mInstallZipFile.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mInstallZipFile) {
            Intent intent = new Intent(getApplicationContext(), FileSelectActivity.class);
            intent.putExtra("title", getText(R.string.select_img_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".zip");
            this.startActivityForResult(intent, REQUEST_INSTALL_ZIP);
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
            if (requestCode == REQUEST_INSTALL_ZIP) {
                String path = intent.getStringExtra("path");
                mInstallZipFile.setSummary(path);
            } else {
                setResult(resultCode, null);
                finish();
            }
        }
    }
}
