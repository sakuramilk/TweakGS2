package net.sakuramilk.TweakGS2.CpuControl;

import java.util.ArrayList;

import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;
import android.content.Context;

public class CpuControlVoltageSetting extends SettingManager {

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

    @Override
    public void setOnBoot() {
        // TODO 自動生成されたメソッド・スタブ
    }

    @Override
    public void reset() {
        // TODO 自動生成されたメソッド・スタブ
    }
}
