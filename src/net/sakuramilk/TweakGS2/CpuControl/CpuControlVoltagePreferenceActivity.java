package net.sakuramilk.TweakGS2.CpuControl;

import java.util.ArrayList;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class CpuControlVoltagePreferenceActivity extends PreferenceActivity {

    private CpuControlVoltageSetting mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.cpu_control_voltage_pref);

        CpuControlSetting cpuSetting = new CpuControlSetting(this);
        String[] availableFrequencies = cpuSetting.getAvailableFrequencies();
        ArrayList<String> list = new ArrayList<String>();
        for (String freq : availableFrequencies) {
            list.add(String.valueOf(Integer.parseInt(freq) / 1000) + "MHz");
        }
        String[] availableFreqEntries = list.toArray(new String[0]);

        mSetting = new CpuControlVoltageSetting(this);

        // voltage
        PreferenceManager prefManager = getPreferenceManager();
        PreferenceScreen rootPref = (PreferenceScreen)prefManager.findPreference("pref_root");
        if (mSetting.isEnableVoltageControl()) {
            String[] voltTable = mSetting.getVoltageTable();
            int i = 0;
            for (String curVolt : voltTable) {
                PreferenceScreen pref = prefManager.createPreferenceScreen(this);
                String freq = String.valueOf(Integer.parseInt(availableFrequencies[i]) / 1000);
                String key = CpuControlSetting.KEY_CPU_VOLT_CTRL_BASE + freq;
                String savedVolt = mSetting.loadVoltage(key);
                pref.setKey(key);
                pref.setTitle(availableFreqEntries[i]);
                pref.setSummary(Misc.getCurrentAndSavedValueText(this,
                        curVolt + "mV",
                        (savedVolt == null ? getText(R.string.none).toString() : savedVolt + "mV")));
                rootPref.addPreference(pref);
                i++;
            }
            CheckBoxPreference pref = new CheckBoxPreference(this);
            pref.setKey(CpuControlSetting.KEY_CPU_VOLT_CTRL_SET_ON_BOOT);
            pref.setTitle(R.string.set_on_boot);
            rootPref.addPreference(pref);
        } else {
            PreferenceScreen pref = prefManager.createPreferenceScreen(this);
            pref.setTitle(R.string.not_support);
            rootPref.addPreference(pref);
        }
    }
}
