package net.sakuramilk.TweakGS2.MultiBoot;

import android.os.Bundle;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;
import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity;

public class MultiBootWizardFinishActivity extends WizardPreferenceActivity
    implements OnClickListener {

    private static final String KEY_ROM_LABEL = "rom_label";
    private static final String KEY_SYSTEM_IMG = "system_img";
    private static final String KEY_DATA_IMG = "data_img";
    private static final String KEY_ZIP_FILE = "zip_file";
    
    private PreferenceScreen mRomLabel;
    private PreferenceScreen mSystemImg;
    private PreferenceScreen mDataImg;
    private PreferenceScreen mZipFile;

    //private final MbsConf mMbsConf = new MbsConf();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.multi_boot_wizard_finish_pref);

        mBackButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        
        mRomLabel = (PreferenceScreen)findPreference(KEY_ROM_LABEL);
        mSystemImg = (PreferenceScreen)findPreference(KEY_SYSTEM_IMG);
        mDataImg = (PreferenceScreen)findPreference(KEY_DATA_IMG);
        mZipFile = (PreferenceScreen)findPreference(KEY_ZIP_FILE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_button) {
            
        } else if (id == R.id.next_button) {
            
        }
    }
}
