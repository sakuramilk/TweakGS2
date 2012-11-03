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

package net.sakuramilk.TweakGS2.CpuControl;

import java.io.File;

import android.content.Context;
import net.sakuramilk.util.Misc;
import net.sakuramilk.util.RootProcess;
import net.sakuramilk.util.SettingManager;
import net.sakuramilk.util.SysFs;

public class CpuControlSetting extends SettingManager {

    public static final String KEY_CPU_GOV_LIST = "cpu_governor_list";
    public static final String KEY_CPU_GOV_SETTING = "cpu_governor_setting";
    public static final String KEY_CPU_GOV_SET_ON_BOOT = "cpu_governor_set_on_boot";
    public static final String KEY_CPU_MAX_FREQ = "cpu_max_freq";
    public static final String KEY_CPU_MIN_FREQ = "cpu_min_freq";
    public static final String KEY_CPU_MAX_SUSPEND_FREQ = "cpu_max_suspend_freq";
    public static final String KEY_CPU_MIN_SUSPEND_FREQ = "cpu_min_suspend_freq";
    public static final String KEY_CPU_FREQ_SET_ON_BOOT = "cpu_freq_set_on_boot";
    public static final String KEY_CPU_VOLT_SET_ON_BOOT = "cpu_volt_set_on_boot";

    private static final String CRTL_PATH = "/sys/devices/system/cpu/cpu0/cpufreq";
    private static final String PATH_SCALING_AVAILABLE_FREQS_KERNEL_3_0 = "/sys/power/cpufreq_table";
    private static final String PATH_SCALING_AVAILABLE_FREQS_KERNEL_2_6 = CRTL_PATH + "/scaling_available_frequencies";
    
    private final SysFs mSysFsAvailableGovernors = new SysFs(CRTL_PATH + "/scaling_available_governors");
    private final SysFs mSysFsScalingGovernor = new SysFs(CRTL_PATH + "/scaling_governor");
    private final SysFs mSysFsCpuAvailableFrequencies;
    private final SysFs mSysFsScalingMaxFreq = new SysFs(CRTL_PATH + "/scaling_max_freq");
    private final SysFs mSysFsScalingMinFreq = new SysFs(CRTL_PATH + "/scaling_min_freq");
    private final SysFs mSysFsScalingMaxSuspendFreq = new SysFs(CRTL_PATH + "/scaling_max_suspend_freq");
    private final SysFs mSysFsScalingMinSuspendFreq = new SysFs(CRTL_PATH + "/scaling_min_suspend_freq");

    public CpuControlSetting(Context context, RootProcess rootProcess) {
        super(context, rootProcess);
        if (Misc.getKernelVersion() >= Misc.KERNEL_VER_3_0_0) {
            File file = new File(PATH_SCALING_AVAILABLE_FREQS_KERNEL_2_6);
            if (file.exists()) {
                mSysFsCpuAvailableFrequencies = new SysFs(PATH_SCALING_AVAILABLE_FREQS_KERNEL_2_6);
            } else {
                mSysFsCpuAvailableFrequencies = new SysFs(PATH_SCALING_AVAILABLE_FREQS_KERNEL_3_0);
            }
        } else {
            mSysFsCpuAvailableFrequencies = new SysFs(PATH_SCALING_AVAILABLE_FREQS_KERNEL_2_6);
        }
    }

    public CpuControlSetting(Context context) {
        this(context, null);
    }

    public String[] getAvailableGovernors() {
        String values = mSysFsAvailableGovernors.read(mRootProcess);
        if (values != null) {
            return values.split(" ");
        }
        return null;
    }

    public String getScalingGovernor() {
        return mSysFsScalingGovernor.read(mRootProcess);
    }

    public void setScalingGovernor(String value) {
        mSysFsScalingGovernor.write(value, mRootProcess);
    }

    public String loadScalingGovernor() {
        return getStringValue(KEY_CPU_GOV_LIST);
    }

    public void saveScalingGovernor(String value) {
        setValue(KEY_CPU_GOV_LIST, value);
    }
    
    public boolean loadGovernorSetOnBoot() {
        return getBooleanValue(KEY_CPU_GOV_SET_ON_BOOT, false);
    }

    public String[] getAvailableFrequencies() {
        String values;
        values = mSysFsCpuAvailableFrequencies.read(mRootProcess);
        if (values != null) {
            return values.split(" ");
        }
        // FIXME: default kernel is not found target sysfs.
        return new String[] { "1200000", "1000000", "800000", "500000", "200000" };
    }

    public String getScalingMaxFreq() {
        return mSysFsScalingMaxFreq.read(mRootProcess);
    }

    public void setScalingMaxFreq(String value) {
        mSysFsScalingMaxFreq.write(value, mRootProcess);
    }

    public String loadScalingMaxFreq() {
        return getStringValue(KEY_CPU_MAX_FREQ);
    }

    public void saveScalingMaxFreq(String value) {
        setValue(KEY_CPU_MAX_FREQ, value);
    }

    public String getScalingMinFreq() {
        return mSysFsScalingMinFreq.read(mRootProcess);
    }

    public void setScalingMinFreq(String value) {
        mSysFsScalingMinFreq.write(value, mRootProcess);
    }

    public String loadScalingMinFreq() {
        return getStringValue(KEY_CPU_MIN_FREQ);
    }

    public void saveScalingMinFreq(String value) {
        setValue(KEY_CPU_MIN_FREQ, value);
    }

    public boolean isEnableSuspendFreq() {
        return mSysFsScalingMaxSuspendFreq.exists();
    }

    public String getScalingMaxSuspendFreq() {
        return mSysFsScalingMaxSuspendFreq.read(mRootProcess);
    }

    public void setScalingMaxSuspendFreq(String value) {
        mSysFsScalingMaxSuspendFreq.write(value, mRootProcess);
    }

    public String loadScalingMaxSuspendFreq() {
        return getStringValue(KEY_CPU_MAX_SUSPEND_FREQ);
    }

    public void saveScalingMaxSuspendFreq(String value) {
        setValue(KEY_CPU_MAX_SUSPEND_FREQ, value);
    }

    public String getScalingMinSuspendFreq() {
        return mSysFsScalingMinSuspendFreq.read(mRootProcess);
    }

    public void setScalingMinSuspendFreq(String value) {
        mSysFsScalingMinSuspendFreq.write(value, mRootProcess);
    }

    public String loadScalingMinSuspendFreq() {
        return getStringValue(KEY_CPU_MIN_SUSPEND_FREQ);
    }

    public void saveScalingMinSuspendFreq(String value) {
        setValue(KEY_CPU_MIN_SUSPEND_FREQ, value);
    }

    public boolean loadFreqSetOnBoot() {
        return getBooleanValue(KEY_CPU_FREQ_SET_ON_BOOT, false);
    }

    public boolean loadVoltSetOnBoot() {
        return getBooleanValue(KEY_CPU_VOLT_SET_ON_BOOT, false);
    }

    @Override
    public void setOnBoot() {
        String value;
        if (loadGovernorSetOnBoot()) {
            value = loadScalingGovernor();
            if (!Misc.isNullOfEmpty(value)) {
                setScalingGovernor(value);
                CpuGovernorSetting cpuGovSetting = new CpuGovernorSetting(mContext, value);
                cpuGovSetting.setOnBoot();
            }
        }
        if (loadFreqSetOnBoot()) {
            value = loadScalingMaxFreq();
            if (!Misc.isNullOfEmpty(value)) {
                setScalingMaxFreq(value);
            }
            value = loadScalingMinFreq();
            if (!Misc.isNullOfEmpty(value)) {
                setScalingMinFreq(value);
            }
            if (isEnableSuspendFreq()) {
                value = loadScalingMaxSuspendFreq();
                if (!Misc.isNullOfEmpty(value)) {
                    setScalingMaxSuspendFreq(value);
                }
                value = loadScalingMinSuspendFreq();
                if (!Misc.isNullOfEmpty(value)) {
                    setScalingMinSuspendFreq(value);
                }
            }
        }
        if (loadVoltSetOnBoot()) {
            CpuVoltageSetting cpuVoltSetting = new CpuVoltageSetting(mContext);
            cpuVoltSetting.setOnBoot();
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        clearValue(KEY_CPU_GOV_SETTING);
        clearValue(KEY_CPU_GOV_SET_ON_BOOT);
        clearValue(KEY_CPU_MAX_FREQ);
        clearValue(KEY_CPU_MIN_FREQ);
        clearValue(KEY_CPU_MAX_SUSPEND_FREQ);
        clearValue(KEY_CPU_MIN_SUSPEND_FREQ);
        clearValue(KEY_CPU_FREQ_SET_ON_BOOT);
        clearValue(KEY_CPU_VOLT_SET_ON_BOOT);
        CpuVoltageSetting cpuVoltSetting = new CpuVoltageSetting(mContext);
        cpuVoltSetting.reset();
    }
}
