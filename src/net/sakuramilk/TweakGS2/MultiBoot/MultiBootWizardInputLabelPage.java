package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Parts.TextInputDialog;
import net.sakuramilk.TweakGS2.Parts.WizardPage;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity.OnPageEventListener;
import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;

public class MultiBootWizardInputLabelPage extends WizardPage
implements OnPreferenceClickListener {

    private PreferenceScreen mHeaderPref = null;
    private PreferenceCategory mCategoryPref = null;
    private PreferenceScreen mRomLabel = null;

    public MultiBootWizardInputLabelPage(Context context, OnPageEventListener listener) {
        super(context, listener);
    }

    @Override
    public void createPage(
            PreferenceManager preferenceManager, PreferenceScreen rootPreference) {

        rootPreference.removeAll();
        if (mHeaderPref == null) {
            mHeaderPref = preferenceManager.createPreferenceScreen(mContext);
            mHeaderPref.setTitle("ROMのラベル");
            mHeaderPref.setSelectable(false);
            mHeaderPref.setSummary(
                    "ROMを識別しやすいように名前をつけてください");
        }
        rootPreference.addPreference(mHeaderPref);
        if (mCategoryPref == null) {
            mCategoryPref = new PreferenceCategory(mContext);
        }
        rootPreference.addPreference(mCategoryPref);

        if (mRomLabel == null) {
            mRomLabel = preferenceManager.createPreferenceScreen(mContext);
            mRomLabel.setTitle("ラベル");
            mRomLabel.setOnPreferenceClickListener(this);
        }
        rootPreference.addPreference(mRomLabel);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mRomLabel) {
            TextInputDialog dlg = new TextInputDialog(mContext);
            dlg.setFinishTextInputListener(new TextInputDialog.FinishTextInputListener() {
                @Override
                public void onFinishTextInput(CharSequence input) {
                    String inputName = input.toString();
                    inputName = inputName.replace("\n", "").trim();
                    mRomLabel.setSummary(inputName);
                    mOnPageEventListener.onPageEvent(MultiBootWizardEvent.EVENT_RESULT_LABEL, inputName);
                }
            });
            dlg.show(R.string.rom_label_title, R.string.rom_label_message, "");
        }
        return false;
    }
}
