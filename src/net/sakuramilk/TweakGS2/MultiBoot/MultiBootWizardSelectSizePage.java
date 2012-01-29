/*
 * Copyright (C) 2011-2012 sakuramilk <c.sakuramilk@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sakuramilk.TweakGS2.MultiBoot;

import android.content.Context;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.WizardPage;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity.OnPageEventListener;

public class MultiBootWizardSelectSizePage extends WizardPage
implements OnPreferenceChangeListener {

    private static final String[] SYSTEM_IMG_SIZE_ENTRIES =
        { "200M", "300M", "400M", "512M", "612M" };
    private static final String[] SYSTEM_IMG_SIZE_VALUES =
        { "200", "300", "400", "512", "612" };
    private static final String SYSTEM_IMG_DEFAULT_SIZE = "612";
    
    private static final String[] DATA_IMG_SIZE_ENTRIES =
        { "100M", "200M", "300M", "400M", "500M", "600M", "600M", "700M", "800M", "900M", "1024M" };
    private static final String[] DATA_IMG_SIZE_VALUES =
        { "100", "200", "300", "400", "500", "600", "700", "800", "900", "1024" };
    private static final String DATA_IMG_DEFAULT_SIZE = "500";

    public MultiBootWizardSelectSizePage(Context context, OnPageEventListener listener) {
        super(context, listener);
    }

    private PreferenceScreen mHeaderPref = null;
    private PreferenceCategory mCategoryPref = null;
    private ListPreference mSystemImageSize = null;
    private ListPreference mDataImageSize = null;

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
                    "既存のイメージを使用するか、新規に作成するか選択してください");
        }
        rootPreference.addPreference(mHeaderPref);
        if (mCategoryPref == null) {
            mCategoryPref = new PreferenceCategory(mContext);
        }
        rootPreference.addPreference(mCategoryPref);

        if (mSystemImageSize == null) {
            mSystemImageSize = new ListPreference(mContext);
            mSystemImageSize.setTitle("SYSTEMサイズ");
            mSystemImageSize.setEntries(SYSTEM_IMG_SIZE_ENTRIES);
            mSystemImageSize.setEntryValues(SYSTEM_IMG_SIZE_VALUES);
            mSystemImageSize.setValue(SYSTEM_IMG_DEFAULT_SIZE);
            mSystemImageSize.setSummary(Misc.getCurrentValueText(mContext,
                    Misc.getEntryFromEntryValue(SYSTEM_IMG_SIZE_ENTRIES, SYSTEM_IMG_SIZE_VALUES, SYSTEM_IMG_DEFAULT_SIZE)));
            mSystemImageSize.setOnPreferenceChangeListener(this);
        }
        rootPreference.addPreference(mSystemImageSize);

        if (mDataImageSize == null) {
            mDataImageSize = new ListPreference(mContext);
            mDataImageSize.setTitle("DATAサイズ");
            mDataImageSize.setEntries(DATA_IMG_SIZE_ENTRIES);
            mDataImageSize.setEntryValues(DATA_IMG_SIZE_VALUES);
            mDataImageSize.setValue(DATA_IMG_DEFAULT_SIZE);
            mDataImageSize.setSummary(Misc.getCurrentValueText(mContext,
                    Misc.getEntryFromEntryValue(DATA_IMG_SIZE_ENTRIES, DATA_IMG_SIZE_VALUES, DATA_IMG_DEFAULT_SIZE)));
            mDataImageSize.setOnPreferenceChangeListener(this);
        }
        rootPreference.addPreference(mDataImageSize);
        
        mOnPageEventListener.onPageEvent(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE, SYSTEM_IMG_DEFAULT_SIZE);
        mOnPageEventListener.onPageEvent(MultiBootWizardEvent.EVENT_RESULT_DATA_IMG_SIZE, DATA_IMG_DEFAULT_SIZE);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        String value = (String)objValue;
        if (preference == mSystemImageSize) {
            mSystemImageSize.setValue(SYSTEM_IMG_DEFAULT_SIZE);
            mSystemImageSize.setSummary(Misc.getCurrentValueText(mContext,
                    Misc.getEntryFromEntryValue(SYSTEM_IMG_SIZE_ENTRIES, SYSTEM_IMG_SIZE_VALUES, value)));
             mOnPageEventListener.onPageEvent(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE, value);
        } else if (preference == mDataImageSize) {
            mDataImageSize.setValue(DATA_IMG_DEFAULT_SIZE);
            mDataImageSize.setSummary(Misc.getCurrentValueText(mContext,
                    Misc.getEntryFromEntryValue(DATA_IMG_SIZE_ENTRIES, DATA_IMG_SIZE_VALUES, value)));
            mOnPageEventListener.onPageEvent(MultiBootWizardEvent.EVENT_RESULT_DATA_IMG_SIZE, value);
        }
        return false;
    }

}
