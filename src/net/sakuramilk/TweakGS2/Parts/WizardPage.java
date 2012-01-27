package net.sakuramilk.TweakGS2.Parts;

import net.sakuramilk.TweakGS2.Parts.WizardPreferenceActivity.OnPageEventListener;
import android.content.Context;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public abstract class WizardPage {

    protected Context mContext;
    protected OnPageEventListener mOnPageEventListener;

    public WizardPage(Context context, OnPageEventListener listener) {
        mContext = context;
        mOnPageEventListener = listener;
    }

    public abstract void createPage(
            PreferenceManager preferenceManager, PreferenceScreen rootPreference);
    
    public void onCallback(int eventCode, Object objValue) {
        // noop
    }
}
