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
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import net.sakuramilk.TweakGS2.Parts.WizardPage;
import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity.OnPageEventListener;

public class MultiBootWizardFinishPage extends WizardPage {

    private PreferenceScreen mHeaderPref = null;
    private PreferenceCategory mCategoryPref = null;
    private PreferenceScreen mRomLabel = null;
    private PreferenceScreen mSystemImg = null;
    private PreferenceScreen mDataImg = null;
    private PreferenceScreen mZipFile = null;

    public MultiBootWizardFinishPage(Context context, OnPageEventListener listener) {
        super(context, listener);
    }

    @Override
    public void createPage(
            PreferenceManager preferenceManager, PreferenceScreen rootPreference) {

        rootPreference.removeAll();
        if (mHeaderPref == null) {
            mHeaderPref = preferenceManager.createPreferenceScreen(mContext);
            mHeaderPref.setSelectable(false);
            mHeaderPref.setTitle("完了");
            mHeaderPref.setSummary(
                    "以下の情報でROM設定を作成します\n" +
                    "よろしければ完了を押してください");
        }
        rootPreference.addPreference(mHeaderPref);
        if (mCategoryPref == null) {
            mCategoryPref = new PreferenceCategory(mContext);
        }
        rootPreference.addPreference(mCategoryPref);

        if (mRomLabel == null) {
            mRomLabel = preferenceManager.createPreferenceScreen(mContext);
            mRomLabel.setTitle("ラベル");
        }
        rootPreference.addPreference(mRomLabel);

        if (mSystemImg == null) {
            mSystemImg = preferenceManager.createPreferenceScreen(mContext);
            mSystemImg.setTitle("SYSTEMイメージ");
        }
        rootPreference.addPreference(mSystemImg);

        if (mDataImg == null) { 
            mDataImg = preferenceManager.createPreferenceScreen(mContext);
            mDataImg.setTitle("DATAイメージ");
        }
        rootPreference.addPreference(mDataImg);

        if (mZipFile == null) {
            mZipFile = preferenceManager.createPreferenceScreen(mContext);
            mZipFile.setTitle("zipファイル");
        }
        rootPreference.addPreference(mZipFile);
    }
    
    @Override
    public void onCallback(int eventCode, Object objValue) {
        switch (eventCode) {
            case MultiBootWizardEvent.EVENT_RESULT_LABEL:
                mRomLabel.setSummary((String)objValue);
                break;
            case MultiBootWizardEvent.EVENT_RESULT_ZIP_PATH:
                if (objValue == null) {
                    mRomLabel.setSummary("インストールROM: インストールしない");
                } else {
                    mRomLabel.setSummary("インストールROM:" + (String)objValue);
                }
                break;
            case MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_PATH:
                mSystemImg.setSummary("既存: " + (String)objValue);
                break;
            case MultiBootWizardEvent.EVENT_RESULT_DATA_IMG_PATH:
                mDataImg.setSummary("既存: " + (String)objValue);
                break;
            case MultiBootWizardEvent.EVENT_RESULT_SYSTEM_IMG_SIZE:
                mSystemImg.setSummary("新規作成: " + (String)objValue);
                break;
            case MultiBootWizardEvent.EVENT_RESULT_DATA_IMG_SIZE:
                mDataImg.setSummary("新規作成: " + (String)objValue);
                break;
        }
    }
}
