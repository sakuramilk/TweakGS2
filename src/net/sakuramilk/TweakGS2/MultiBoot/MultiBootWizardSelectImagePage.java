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

public class MultiBootWizardSelectImagePage extends WizardPage
    implements OnPreferenceClickListener {

    private PreferenceScreen mHeaderPref = null;
    private PreferenceCategory mCategoryPref = null;
    private PreferenceScreen mSystemImageFile = null;
    private PreferenceScreen mDataImageFile = null;

    public MultiBootWizardSelectImagePage(Context context, OnPageEventListener listener) {
        super(context, listener);
    }

    @Override
    public void createPage(
            PreferenceManager preferenceManager, PreferenceScreen rootPreference) {

        rootPreference.removeAll();
        if (mHeaderPref == null) { 
            mHeaderPref = preferenceManager.createPreferenceScreen(mContext);
            mHeaderPref.setSelectable(false);
            mHeaderPref.setTitle("既存ROMイメージの選択");
            mHeaderPref.setSummary(
                    "既存のROMイメージを使用します\n" +
                    "SYSTEMイメージとDATAイメージを選択してください\n" +
                    "注意: DATAイメージを設定しない場合はdataパーティションを共用します");
        }
        rootPreference.addPreference(mHeaderPref);
        if (mCategoryPref == null) {
            mCategoryPref = new PreferenceCategory(mContext);
        }
        rootPreference.addPreference(mCategoryPref);

        if (mSystemImageFile == null) {
            mSystemImageFile = preferenceManager.createPreferenceScreen(mContext);
            mSystemImageFile.setTitle("SYSTEMイメージ");
            mSystemImageFile.setOnPreferenceClickListener(this);
        }
        rootPreference.addPreference(mSystemImageFile);
        
        if (mDataImageFile == null) {
            mDataImageFile = preferenceManager.createPreferenceScreen(mContext);
            mDataImageFile.setTitle("DATAイメージ");
            mDataImageFile.setOnPreferenceClickListener(this);
        }
        rootPreference.addPreference(mDataImageFile);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference == mSystemImageFile) {
            Intent intent = new Intent(mContext, FileSelectActivity.class);
            intent.putExtra("title", mContext.getText(R.string.select_img_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".img");
            mOnPageEventListener.onPageEvent(
                    MultiBootWizardEvent.EVENT_REQUEST_SYSTEM_IMG_PATH, intent);
        } else if (preference == mDataImageFile) {
            Intent intent = new Intent(mContext, FileSelectActivity.class);
            intent.putExtra("title", mContext.getText(R.string.select_img_title));
            intent.putExtra("select", "file");
            intent.putExtra("filter", ".img");
            mOnPageEventListener.onPageEvent(
                    MultiBootWizardEvent.EVENT_REQUEST_DATA_IMG_PATH, intent);
        }
        return false;
    }

    @Override
    public void onCallback(int eventCode, Object objValue) {
        String path = (String)objValue;
        if (eventCode == MultiBootWizardEvent.EVENT_REQUEST_SYSTEM_IMG_PATH) {
            if (path != null) {
                mSystemImageFile.setSummary(path);
                mOnPageEventListener.onPageEvent(MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_PATH, path);
            } else {
                mDataImageFile.setSummary(path);
                mOnPageEventListener.onPageEvent(MultiBootWizardEvent.EVENT_RESULT_DATA_IMG_PATH, path);
            }
        }
    }
}
