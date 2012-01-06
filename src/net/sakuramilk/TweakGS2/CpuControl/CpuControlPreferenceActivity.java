package net.sakuramilk.TweakGS2.CpuControl;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.BootProperty;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SysFs;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class CpuControlPreferenceActivity extends PreferenceActivity
    implements OnPreferenceChangeListener, OnPreferenceClickListener {

    private static final String TAG = "CpuControlPreferenceActivity";
    
    private ListPreference mGovernorList;
    private PreferenceScreen mGovernorSetting;
    private CheckBoxPreference mGorvenorSetOnBoot;
    private ListPreference mFreqMax;
    private ListPreference mFreqMin;
    private CheckBoxPreference mFreqSetOnBoot;
    
    private static final String CRTL_PATH = "/sys/devices/system/cpu/cpu0/cpufreq"; 
    private SysFs mAvailableGovernors = new SysFs(CRTL_PATH + "/scaling_available_governors");
    private SysFs mScalingGovernor = new SysFs(CRTL_PATH + "/scaling_governor");
    private SysFs mCpuFreqTable = new SysFs("/sys/power/cpufreq_table");
    private SysFs mScalingMaxFreq = new SysFs(CRTL_PATH + "/scaling_max_freq");
    private SysFs mScalingMinFreq = new SysFs(CRTL_PATH + "/scaling_min_freq");
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cpu_control_pref);
        
        // governor
        mGovernorList = (ListPreference)findPreference("cpu_governor_list");
        String[] availableGovernors = mAvailableGovernors.read();
        String[] values = availableGovernors[0].split(" ");
        
        mGovernorList.setEntries(values);
        mGovernorList.setEntryValues(values);
        String[] scalingGovernor = mScalingGovernor.read();
        mGovernorList.setSummary(Misc.getCurrentValueText(this, scalingGovernor[0]));
        mGovernorList.setValue(scalingGovernor[0]);
        mGovernorList.setOnPreferenceChangeListener(this);
        
        mGovernorSetting = (PreferenceScreen)findPreference("cpu_governor_setting");
        mGovernorSetting.setOnPreferenceClickListener(this);
        
        mGorvenorSetOnBoot = (CheckBoxPreference)findPreference("cpu_governor_set_on_boot");
        mGorvenorSetOnBoot.setOnPreferenceChangeListener(this);
        
        // cpu freq
        String[] freqTable = mCpuFreqTable.read();
        values = freqTable[0].split(" ");
        String[] maxFreqValue = mScalingMaxFreq.read();
        String[] minFreqValue = mScalingMinFreq.read();
        
        mFreqMax = (ListPreference)findPreference("cpu_max_freq");
        mFreqMax.setEntries(values);
        mFreqMax.setEntryValues(values);
        mFreqMax.setValue(maxFreqValue[0]);
        mFreqMax.setOnPreferenceChangeListener(this);
        mFreqMax.setSummary(Misc.getCurrentValueText(this, maxFreqValue[0]));
        
        mFreqMin = (ListPreference)findPreference("cpu_min_freq");
        mFreqMin.setEntries(values);
        mFreqMin.setEntryValues(values);
        mFreqMin.setValue(minFreqValue[0]);
        mFreqMin.setOnPreferenceChangeListener(this);
        mFreqMin.setSummary(Misc.getCurrentValueText(this, minFreqValue[0]));
        
        mFreqSetOnBoot = (CheckBoxPreference)findPreference("cpu_freq_set_on_boot");
        mFreqSetOnBoot.setOnPreferenceChangeListener(this);
        
        // voltage
        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference("cpu_control_root");
        PreferenceScreen pref = prefManager.createPreferenceScreen(this);
        pref.setTitle("voltage level1");
        rootPref.addPreference(pref);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mGovernorList) {
            mScalingGovernor.write(newValue.toString());
            mGovernorList.setSummary(Misc.getCurrentValueText(this, newValue.toString()));
            return true;
            
        } else if (preference == mGorvenorSetOnBoot) {
            return true;
            
        } else if (preference == mFreqMax) {
            mScalingMaxFreq.write(newValue.toString());
            mFreqMax.setSummary(Misc.getCurrentValueText(this, newValue.toString()));
            return true;
            
        } else if (preference == mFreqMin) {
            mScalingMinFreq.write(newValue.toString());
            mFreqMin.setSummary(Misc.getCurrentValueText(this, newValue.toString()));
            return true;
            
        } else if (preference == mFreqSetOnBoot) {
            return true;
            
        }
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference arg0) {
        // TODO 自動生成されたメソッド・スタブ
        return false;
    }
}
