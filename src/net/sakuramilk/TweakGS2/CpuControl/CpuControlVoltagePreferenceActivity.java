package net.sakuramilk.TweakGS2.CpuControl;

import java.util.ArrayList;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Parts.ApplyButtonPreferenceActivity;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

public class CpuControlVoltagePreferenceActivity extends ApplyButtonPreferenceActivity
    implements OnSeekBarPreferenceDoneListener, OnClickListener, OnPreferenceChangeListener {

    private CpuControlVoltageSetting mSetting;
    private CheckBoxPreference mSetOnBoot;
    private ArrayList<SeekBarPreference> mFreqPrefList;
    private boolean mSetOnBootChecked;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cpu_control_voltage_pref);
        mSetting = new CpuControlVoltageSetting(this);

        CpuControlSetting cpuSetting = new CpuControlSetting(this);
        String[] availableFrequencies = cpuSetting.getAvailableFrequencies();
        ArrayList<String> list = new ArrayList<String>();
        for (String freq : availableFrequencies) {
            list.add(String.valueOf(Integer.parseInt(freq) / 1000) + "MHz");
        }
        String[] availableFreqEntries = list.toArray(new String[0]);

        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference(CpuControlVoltageSetting.KEY_CPU_VOLT_ROOT_PREF);
        if (mSetting.isEnableVoltageControl()) {
            CheckBoxPreference mSetOnBoot = new CheckBoxPreference(this);
            mSetOnBoot.setKey(CpuControlVoltageSetting.KEY_CPU_VOLT_CTRL_SET_ON_BOOT);
            mSetOnBoot.setTitle(R.string.set_on_boot);
            rootPref.addPreference(mSetOnBoot);
            mSetOnBootChecked = mSetting.loadSetOnBoot();
            
            mFreqPrefList = new ArrayList<SeekBarPreference>();
            String[] voltTable = mSetting.getVoltageTable();
            int i = 0;
            for (String curVolt : voltTable) {
                //PreferenceScreen pref = prefManager.createPreferenceScreen(this);
                SeekBarPreference pref = new SeekBarPreference(this, null);
                String freq = String.valueOf(Integer.parseInt(availableFrequencies[i]) / 1000);
                String key = CpuControlVoltageSetting.KEY_CPU_VOLT_CTRL_BASE + freq;
                String savedVolt = mSetting.loadVoltage(key);
                pref.setKey(key);
                pref.setTitle(availableFreqEntries[i]);
                pref.setSummary(Misc.getCurrentAndSavedValueText(this,
                        curVolt + "mV",
                        (savedVolt == null ? getText(R.string.none).toString() : savedVolt + "mV")));
                rootPref.addPreference(pref);
                mFreqPrefList.add(pref);
                i++;
            }
        } else {
            PreferenceScreen pref = prefManager.createPreferenceScreen(this);
            pref.setTitle(R.string.not_support);
            rootPref.addPreference(pref);
        }
    }

    @Override
    public void onClick(View v) {
        mApplyButton.setEnabled(false);
        mSetOnBootChecked = mSetOnBoot.isChecked();
        mSetting.saveSetOnBoot(mSetOnBootChecked);
        /*
        mSetting.saveFreq(mHighFreqValue, mLowFreqValue);
        mSetting.saveVolt(mHighVoltValue, mLowVoltValue);
        mSetting.setFreq(mHighFreqValue, mLowFreqValue);
        mSetting.setVolt(mHighVoltValue, mLowVoltValue);
        */
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        mApplyButton.setEnabled(true);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mApplyButton.isEnabled()) {
            mSetting.saveSetOnBoot(mSetOnBootChecked);
        }
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        mApplyButton.setEnabled(true);
        return false;
    }
}
