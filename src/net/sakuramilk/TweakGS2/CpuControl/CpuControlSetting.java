package net.sakuramilk.TweakGS2.CpuControl;

import android.content.Context;
import android.os.Build;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class CpuControlSetting extends SettingManager {

    public static final String KEY_CPU_GOV_LIST = "cpu_governor_list";
    public static final String KEY_CPU_GOV_SETTING = "cpu_governor_setting";
    public static final String KEY_CPU_GOV_SET_ON_BOOT = "cpu_governor_set_on_boot";
    public static final String KEY_CPU_MAX_FREQ = "cpu_max_freq";
    public static final String KEY_CPU_MIN_FREQ = "cpu_min_freq";
    public static final String KEY_CPU_FREQ_SET_ON_BOOT = "cpu_freq_set_on_boot";

    private static final String CRTL_PATH = "/sys/devices/system/cpu/cpu0/cpufreq";
    private final SysFs mSysFsAvailableGovernors = new SysFs(CRTL_PATH + "/scaling_available_governors");
    private final SysFs mSysFsScalingGovernor = new SysFs(CRTL_PATH + "/scaling_governor");
    private final SysFs mSysFsCpuAvailableFrequenciesGB = new SysFs(CRTL_PATH + "/scaling_available_frequencies");
    private final SysFs mSysFsCpuAvailableFrequenciesICS = new SysFs("/sys/power/cpufreq_table");
    private final SysFs mSysFsScalingMaxFreq = new SysFs(CRTL_PATH + "/scaling_max_freq");
    private final SysFs mSysFsScalingMinFreq = new SysFs(CRTL_PATH + "/scaling_min_freq");

    public CpuControlSetting(Context context) {
        super(context);
    }

    public String[] getAvailableGovernors() {
        String values = mSysFsAvailableGovernors.read();
        if (values != null) {
            return values.split(" ");
        }
        return null;
    }

    public String getScalingGovernor() {
        return mSysFsScalingGovernor.read();
    }

    public void setScalingGovernor(String value) {
        mSysFsScalingGovernor.write(value);
    }

    public String[] getAvailableFrequencies() {
        String values;
        if (Build.VERSION.SDK_INT >= 14) {
            values = mSysFsCpuAvailableFrequenciesICS.read();
        } else {
            values = mSysFsCpuAvailableFrequenciesGB.read();
        }
        if (values != null) {
            return values.split(" ");
        }
        return null;
    }

    public String getScalingMaxFreq() {
        return mSysFsScalingMaxFreq.read();
    }

    public void setScalingMaxFreq(String value) {
        mSysFsScalingMaxFreq.write(value);
    }

    public String getScalingMinFreq() {
        return mSysFsScalingMinFreq.read();
    }

    public void setScalingMinFreq(String value) {
        mSysFsScalingMaxFreq.write(value);
    }

    @Override
    public void setOnBoot() {
        // TODO 自動生成されたメソッド・スタブ
    }

    @Override
    public void setRecommend() {
        // TODO 自動生成されたメソッド・スタブ
    }

    @Override
    public void reset() {
        // TODO 自動生成されたメソッド・スタブ
    }
}
