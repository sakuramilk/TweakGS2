package net.sakuramilk.TweakGS2.MultiBoot;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity;

public class MultiBootWizardSelectSizeActivity extends WizardPreferenceActivity
implements OnClickListener, OnPreferenceChangeListener {

    private static final String KEY_SYSTEM_IMG_SIZE = "system_img_size";
    private static final String KEY_DATA_IMG_SIZE = "data_img_size";

    private ListPreference mSystemImageSize;
    private ListPreference mDataImageSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.multi_boot_wizard_select_size_pref);
        
        mBackButton.setVisibility(View.INVISIBLE);
        mNextButton.setOnClickListener(this);

        mSystemImageSize = (ListPreference)findPreference(KEY_SYSTEM_IMG_SIZE);
        mSystemImageSize.setOnPreferenceChangeListener(this);
        mDataImageSize = (ListPreference)findPreference(KEY_DATA_IMG_SIZE);
        mDataImageSize.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mSystemImageSize) {

        } else if (preference == mDataImageSize) {
            Intent intent = new Intent(getApplicationContext(), MultiBootWizardFinishActivity.class);
            startActivityForResult(intent, 1000);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), MultiBootWizardSelectSizeActivity.class);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            setResult(resultCode, null);
            finish();
        }
    }
}
