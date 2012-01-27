package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.Parts.WizardPage;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity.OnPageEventListener;
import android.content.Context;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class MultiBootWizardStartPage extends WizardPage
    implements OnPreferenceChangeListener {

    private PreferenceScreen mHeaderPref = null;
    private PreferenceCategory mCategoryPref = null;
    private CheckBoxPreference mSelectImage = null;
    private CheckBoxPreference mCreateImage = null;
    private CheckBoxPreference mUseExtSdPartition = null;

    public MultiBootWizardStartPage(Context context, OnPageEventListener listener) {
        super(context, listener);
    }

    @Override
    public void createPage(
            PreferenceManager preferenceManager, PreferenceScreen rootPreference) {

        rootPreference.removeAll();
        if (mHeaderPref == null) {
            mHeaderPref = preferenceManager.createPreferenceScreen(mContext);
            mHeaderPref.setSelectable(false);
            mHeaderPref.setTitle("ROM設定の追加");
            mHeaderPref.setSummary(
                    "新しいROMの設定を行います\n" +
                    "以下から設定したいタイプを選択してください");
        }
        rootPreference.addPreference(mHeaderPref);
        if (mCategoryPref == null) {
            mCategoryPref = new PreferenceCategory(mContext);
        }
        rootPreference.addPreference(mCategoryPref);

        if (mSelectImage == null) {
            mSelectImage = new CheckBoxPreference(mContext);
            mSelectImage.setTitle("既存ROMイメージ");
            mSelectImage.setSummary("既存のROMイメージを選択して設定を追加します");
            mSelectImage.setChecked(false);
            mSelectImage.setOnPreferenceChangeListener(this);
        }
        rootPreference.addPreference(mSelectImage);

        if (mCreateImage == null) {
            mCreateImage = new CheckBoxPreference(mContext);
            mCreateImage.setTitle("新規ROMイメージ");
            mCreateImage.setSummary("新規にROMイメージを作成して設定を追加します");
            mCreateImage.setChecked(false);
            mCreateImage.setOnPreferenceChangeListener(this);
        }
        rootPreference.addPreference(mCreateImage);

        if (mUseExtSdPartition == null) {
            mUseExtSdPartition = new CheckBoxPreference(mContext);
            mUseExtSdPartition.setTitle("外部SD");
            mUseExtSdPartition.setSummary("外部SDのパーティションを使用して設定を追加します");
            mUseExtSdPartition.setChecked(false);
            mUseExtSdPartition.setOnPreferenceChangeListener(this);
        }
        rootPreference.addPreference(mUseExtSdPartition);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        boolean value = (Boolean)objValue;
        if (preference == mSelectImage) {
            mSelectImage.setChecked(value);
            mSelectImage.setSelectable(!value);
            mCreateImage.setSelectable(value);
            mUseExtSdPartition.setSelectable(value);
            if (value) {
                mCreateImage.setChecked(false);
                mUseExtSdPartition.setChecked(false);
                mOnPageEventListener.onPageEvent(
                        MultiBootWizardEvent.EVENT_RESULT_MODE, MultiBootWizardActivity.MODE_SELECT);
            }
        } else if (preference == mCreateImage) {
            mCreateImage.setChecked(value);
            mCreateImage.setSelectable(!value);
            mSelectImage.setSelectable(value);
            mUseExtSdPartition.setSelectable(value);
            if (value) {
                mSelectImage.setChecked(false);
                mUseExtSdPartition.setChecked(false);
                mOnPageEventListener.onPageEvent(
                        MultiBootWizardEvent.EVENT_RESULT_MODE, MultiBootWizardActivity.MODE_CREATE);
            }
        } else if (preference == mUseExtSdPartition) {
            mUseExtSdPartition.setChecked(value);
            mUseExtSdPartition.setSelectable(!value);
            mSelectImage.setSelectable(value);
            mCreateImage.setSelectable(value);
            if (value) {
                mSelectImage.setChecked(false);
                mCreateImage.setChecked(false);
                mOnPageEventListener.onPageEvent(
                        MultiBootWizardEvent.EVENT_RESULT_MODE, MultiBootWizardActivity.MODE_EXT_SD);
            }
        }
        return false;
    }
}
