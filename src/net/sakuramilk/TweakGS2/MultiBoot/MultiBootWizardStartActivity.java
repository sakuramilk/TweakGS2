package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.R;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class MultiBootWizardStartActivity extends PreferenceActivity
    implements OnPreferenceClickListener {

    private static final String KEY_SELECT_IMG = "select_img";
    private static final String KEY_CREATE_IMG = "create_img";
    
    private PreferenceScreen mSelectImage;
    private PreferenceScreen mCreateImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.multi_boot_wizard_start_pref);

        mSelectImage = (PreferenceScreen)findPreference(KEY_SELECT_IMG);
        mCreateImage.setOnPreferenceClickListener(this);
        mSelectImage = (PreferenceScreen)findPreference(KEY_CREATE_IMG);
        mCreateImage.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mSelectImage) {
            Intent intent = new Intent(getApplicationContext(), MultiBootWizardSelectSizeActivity.class);
            startActivityForResult(intent, 1000);
        } else if (preference == mCreateImage) {
            Intent intent = new Intent(getApplicationContext(), MultiBootWizardFinishActivity.class);
            startActivityForResult(intent, 1000);
        }
        return false;
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
