package net.sakuramilk.TweakGS2.GpuControl;

import java.util.ArrayList;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class GpuControlSetting extends SettingManager {

    public static final String KEY_GPU_HIGH_FREQ = "gpu_high_freq";
    public static final String KEY_GPU_HIGH_VOLT = "gpu_high_volt";
    public static final String KEY_GPU_LOW_FREQ = "gpu_low_freq";
    public static final String KEY_GPU_LOW_VOLT = "gpu_low_volt";
    public static final String KEY_GPU_SET_ON_BOOT = "gpu_set_on_boot";

    private final SysFs mSysFsClkCtrl = new SysFs("/sys/devices/virtual/misc/gpu_clock_control/gpu_control");
    private final SysFs mSysFsVoltCtrl = new SysFs("/sys/devices/virtual/misc/gpu_voltage_control/gpu_control");

    public GpuControlSetting(Context context) {
        super(context);
    }

    public boolean isEnableFreqCtrl() {
        return mSysFsClkCtrl.exists();
    }

    public Integer[] getFreq() {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        String[] values = mSysFsClkCtrl.readMuitiLine();
        String[] lowValue = values[0].split(" ");
        String[] highValue = values[1].split(" ");
        ret.add(Integer.parseInt(highValue[1]));
        ret.add(Integer.parseInt(lowValue[1]));
        return ret.toArray(new Integer[0]);
    }

    public void setFreq(int highFreq, int lowFreq) {
        mSysFsClkCtrl.write(String.format("%d %d", lowFreq, highFreq));
    }

    public void saveFreq(int highFreq, int lowFreq) {
        setValue(KEY_GPU_HIGH_FREQ, String.valueOf(highFreq));
        setValue(KEY_GPU_LOW_FREQ, String.valueOf(lowFreq));
    }

    public boolean isEnableVoltageCtrl() {
        return mSysFsVoltCtrl.exists();
    }

    public Integer[] getVolt() {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        String[] values = mSysFsVoltCtrl.readMuitiLine();
        String[] lowValue = values[0].split(" ");
        String[] highValue = values[1].split(" ");
        ret.add(Integer.parseInt(highValue[1]) / 1000);
        ret.add(Integer.parseInt(lowValue[1]) / 1000);
        return ret.toArray(new Integer[0]);
    }

    public void setVolt(int highVolt, int lowVolt) {
        mSysFsVoltCtrl.write(String.format("%d %d", lowVolt, highVolt));
    }

    public void saveVolt(int highVolt, int lowVolt) {
        setValue(KEY_GPU_HIGH_VOLT, String.valueOf(highVolt * 1000));
        setValue(KEY_GPU_LOW_VOLT, String.valueOf(lowVolt * 1000));
    }

    public boolean loadSetOnBoot() {
        return getBoolValue(KEY_GPU_SET_ON_BOOT);
    }

    public void saveSetOnBoot(boolean setOnBoot) {
        setValue(KEY_GPU_SET_ON_BOOT, setOnBoot);
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
