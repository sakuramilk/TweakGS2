package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Parts.WizardPage;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity.OnPageEventListener;
import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;

public class MultiBootWizardSelectZipPage extends WizardPage
implements OnPreferenceClickListener {

    private PreferenceScreen mHeaderPref = null;
    private PreferenceCategory mCategoryPref = null;
    private PreferenceScreen mInstallZipFile = null;

    public MultiBootWizardSelectZipPage(Context context, OnPageEventListener listener) {
        super(context, listener);
    }

    @Override
    public void createPage(
            PreferenceManager preferenceManager, PreferenceScreen rootPreference) {

        rootPreference.removeAll();
        if (mHeaderPref == null) {
            mHeaderPref = preferenceManager.createPreferenceScreen(mContext);
            mHeaderPref.setSelectable(false);
            mHeaderPref.setTitle("インストールするROMの選択");
            mHeaderPref.setSummary(
                    "CWM Recoveryでインストール可能なzipファイルを選択してください\n" +
                    "ここでインストールしないで、後でインストールすることも出来ます");
        }
        rootPreference.addPreference(mHeaderPref);
        
        if (mCategoryPref == null) {
            mCategoryPref = new PreferenceCategory(mContext);
        }
        rootPreference.addPreference(mCategoryPref);

        if (mInstallZipFile == null) {
            mInstallZipFile = preferenceManager.createPreferenceScreen(mContext);
            mInstallZipFile.setTitle("zipファイル");
            mInstallZipFile.setOnPreferenceClickListener(this);
        }
        rootPreference.addPreference(mInstallZipFile);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mInstallZipFile) {
            Intent intent = new Intent(mContext, FileSelectActivity.class);
            intent.putExtra("title", mContext.getText(R.string.select_img_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".zip");
            mOnPageEventListener.onPageEvent(
                    MultiBootWizardEvent.EVENT_REQUEST_ZIP_PATH, intent);
        }
        return false;
    }

    @Override
    public void onCallback(int eventCode, Object objValue) {
        String path = (String)objValue;
        if (eventCode == MultiBootWizardEvent.EVENT_REQUEST_ZIP_PATH) {
            if (path != null) {
                mInstallZipFile.setSummary(path);
                mOnPageEventListener.onPageEvent(MultiBootWizardEvent.EVENT_RESULT_ZIP_PATH, path);
            }
        }
    }
}
