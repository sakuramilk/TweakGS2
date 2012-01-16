package net.sakuramilk.TweakGS2.GpuControl;

import java.util.ArrayList;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class GpuControlSetting extends SettingManager {

    public static final String KEY_GPU_FREQ_BASE = "gpu_freq_step";
    public static final String KEY_GPU_VOLT_BASE = "gpu_volt_step";
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
        for (String value : values) {
            String[] v = value.split(" ");
            ret.add(Integer.parseInt(v[1]));
        }
        return ret.toArray(new Integer[0]);
    }

    public void setFreq(Integer[] freqs) {
        String values = String.valueOf(freqs[0]);
        for (int i = 1; i < values.length() ; i++) {
            values += " " + String.valueOf(freqs[i]);
        }
        mSysFsClkCtrl.write(values);
    }

    public Integer[] loadFreq() {
        String value = null;
        int i = 0;
        ArrayList<Integer> ret = new ArrayList<Integer>();

        while (true) {
            value = getStringValue(KEY_GPU_FREQ_BASE + i, null);
            if (value == null) {
                break;
            }
            ret.add(Integer.parseInt(value));
            i++;
        }
        return ret.toArray(new Integer[0]);
    }

    public void saveFreq(Integer[] freqs) {
        for (int i = 0; i < freqs.length ; i++) {
            setValue(KEY_GPU_FREQ_BASE + i, String.valueOf(freqs[i]));
        }
    }

    public boolean isEnableVoltageCtrl() {
        return mSysFsVoltCtrl.exists();
    }

    public Integer[] getVolt() {
        ArrayList<Integer> ret = new ArrayList<Integer>();
        String[] values = mSysFsVoltCtrl.readMuitiLine();
        for (String value : values) {
            String[] v = value.split(" ");
            ret.add(Integer.parseInt(v[1]) / 1000);
        }
        return ret.toArray(new Integer[0]);
    }

    public void setVolt(Integer[] volts) {
        String values = String.valueOf(volts[0]);
        for (int i = 1; i < values.length() ; i++) {
            values += " " + String.valueOf(volts[i] * 1000);
        }
        mSysFsVoltCtrl.write(values);
    }

    public Integer[] loadVolt() {
        String value = null;
        int i = 0;
        ArrayList<Integer> ret = new ArrayList<Integer>();

        while (true) {
            value = getStringValue(KEY_GPU_VOLT_BASE + i, null);
            if (value == null) {
                break;
            }
            ret.add(Integer.parseInt(value));
            i++;
        }
        return ret.toArray(new Integer[0]);
    }

    public void saveVolt(Integer[] volts) {
        for (int i = 0; i < volts.length ; i++) {
            setValue(KEY_GPU_VOLT_BASE + i, String.valueOf(volts[i]));
        }
    }

    public boolean loadSetOnBoot() {
        return getBooleanValue(KEY_GPU_SET_ON_BOOT);
    }

    public void saveSetOnBoot(boolean setOnBoot) {
        setValue(KEY_GPU_SET_ON_BOOT, setOnBoot);
    }

    @Override
    public void setOnBoot() {
        Integer[] volts = loadVolt();
        if (volts != null) {
            saveVolt(volts);
        }
        Integer[] freqs = loadFreq();
        if (freqs != null) {
            saveFreq(freqs);
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        int i = 0;
        while (true) {
            String value = getStringValue(KEY_GPU_FREQ_BASE + i, null);
            if (value == null) {
                break;
            }
            clearValue(KEY_GPU_FREQ_BASE + i);
            i++;
        }
        i = 0;
        while (true) {
            String value = getStringValue(KEY_GPU_VOLT_BASE + i, null);
            if (value == null) {
                break;
            }
            clearValue(KEY_GPU_VOLT_BASE + i);
            i++;
        }
    }
}
