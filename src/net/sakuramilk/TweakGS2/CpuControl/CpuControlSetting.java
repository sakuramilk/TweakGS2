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

import android.content.Context;
import android.os.Build;
import net.sakuramilk.TweakGS2.Common.Misc;
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

    public String loadScalingGovernor() {
        return getStringValue(KEY_CPU_GOV_SETTING);
    }

    public void saveScalingGovernor(String value) {
        setValue(KEY_CPU_GOV_SETTING, value);
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

    public String loadScalingMaxFreq() {
        return getStringValue(KEY_CPU_MAX_FREQ);
    }

    public void saveScalingMaxFreq(String value) {
        setValue(KEY_CPU_MAX_FREQ, value);
    }

    public String getScalingMinFreq() {
        return mSysFsScalingMinFreq.read();
    }

    public void setScalingMinFreq(String value) {
        mSysFsScalingMinFreq.write(value);
    }

    public String loadScalingMinFreq() {
        return getStringValue(KEY_CPU_MIN_FREQ);
    }

    public void saveScalingMinFreq(String value) {
        setValue(KEY_CPU_MIN_FREQ, value);
    }
    
    public boolean loadSetOnBoot() {
        return getBooleanValue(KEY_CPU_FREQ_SET_ON_BOOT, false);
    }

    @Override
    public void setOnBoot() {
        if (loadSetOnBoot()) {
            String value = loadScalingGovernor();
            if (!Misc.isNullOfEmpty(value)) {
                setScalingGovernor(value);
            }
            value = loadScalingMaxFreq();
            if (!Misc.isNullOfEmpty(value)) {
                setScalingMaxFreq(value);
            }
            value = loadScalingMinFreq();
            if (!Misc.isNullOfEmpty(value)) {
                setScalingMinFreq(value);
            }
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
        clearValue(KEY_CPU_FREQ_SET_ON_BOOT);
    }
}
