package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.TextInputDialog;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.View;
import android.view.View.OnClickListener;

public class MultiBootWizardInputLabelActivity extends WizardPreferenceActivity
implements OnClickListener, OnPreferenceClickListener {

    private static final String KEY_ROM_LABEL = "rom_label";

    private static final int REQUEST_NEXT = 1000;

    private PreferenceScreen mRomLabel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.multi_boot_wizard_select_size_pref);
        
        mBackButton.setVisibility(View.INVISIBLE);
        mNextButton.setOnClickListener(this);

        mRomLabel = (PreferenceScreen)findPreference(KEY_ROM_LABEL);
        mRomLabel.setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mRomLabel) {
            TextInputDialog dlg = new TextInputDialog(this);
            dlg.setFinishTextInputListener(new TextInputDialog.FinishTextInputListener() {
                @Override
                public void onFinishTextInput(CharSequence input) {
                    String inputName = input.toString();
                    inputName = inputName.replace("\n", "").trim();
                    mRomLabel.setSummary(inputName);
                }
            });
            dlg.show(R.string.rom_label_title, R.string.rom_label_message, Misc.getDateString());
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
            setResult(resultCode, null);
            finish();
        }
    }
}
