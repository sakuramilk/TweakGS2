package net.sakuramilk.TweakGS2.CpuControl;

import java.util.ArrayList;

import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;
import android.content.Context;

public class CpuControlVoltageSetting extends SettingManager {

    public static final String KEY_CPU_VOLT_ROOT_PREF = "root_pref";
    public static final String KEY_CPU_VOLT_CTRL_BASE = "cpu_vc_";
    public static final String KEY_CPU_VOLT_CTRL_SET_ON_BOOT = "vc_set_on_boot";

    private static final String CRTL_PATH = "/sys/devices/system/cpu/cpu0/cpufreq";
    private final SysFs mSysFsUV_mV_table = new SysFs(CRTL_PATH + "/UV_mV_table");

    public CpuControlVoltageSetting(Context context) {
        super(context);
    }

    public boolean isEnableVoltageControl() {
        return mSysFsUV_mV_table.exists();
    }

    public String[] getVoltageTable() {
        ArrayList<String> ret = new ArrayList<String>();
        String[] values = mSysFsUV_mV_table.readMuitiLine();
        for (String value : values) {
            String voltage = value.substring(value.indexOf(" ") + 1).split(" ")[0];
            ret.add(voltage);
        }
        return ret.toArray(new String[0]);
    }

    public String loadVoltage(String key) {
        return getStringValue(key);
    }
    
    public boolean loadSetOnBoot() {
        return getBooleanValue(KEY_CPU_VOLT_CTRL_SET_ON_BOOT, false);
    }

    public void saveSetOnBoot(boolean value) {
        setValue(KEY_CPU_VOLT_CTRL_SET_ON_BOOT, true);
    }

    @Override
    public void setOnBoot() {
        if(loadSetOnBoot()) {
            
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        // TODO 自動生成されたメソッド・スタブ
    }
}
