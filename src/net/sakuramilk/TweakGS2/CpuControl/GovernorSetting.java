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

import java.util.ArrayList;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class GovernorSetting extends SettingManager {

    public class Parameter {
        Parameter(String name, int max, int min) {
            this.name = name;
            this.max = max;
            this.min = min;
        }
        public String name;
        public int max;
        public int min;
    }

    private static ArrayList<Parameter> mParams;
    private String mGovernor;
    private static final String CTRL_PATH = "/sys/devices/system/cpu/cpufreq";

    protected GovernorSetting(Context context, String governor) {
        super(context);
        
        mGovernor = governor;

        // setup governor parameter list
        if ("sakuractive".equals(governor)) {
            mParams = new ArrayList<Parameter>();
            mParams.add(new Parameter("sampling_rate", 0, 0));
            mParams.add(new Parameter("up_threshold", 0, 0));
            mParams.add(new Parameter("down_differential", 0, 0));
            mParams.add(new Parameter("down_threshold", 0, 0));
            mParams.add(new Parameter("sakuractive_in_sampling_periods", 0, 0));
            mParams.add(new Parameter("sakuractive_out_sampling_periods", 0, 0));
            mParams.add(new Parameter("ignore_nice_load", 0, 0));
            mParams.add(new Parameter("io_is_busy", 0, 0));

        } else if ("lulzactive".equals(governor)) {
            mParams.add(new Parameter("debug_mode", 0, 0));
            mParams.add(new Parameter("down_sample_time", 0, 0));
            mParams.add(new Parameter("freq_table", 0, 0));
            mParams.add(new Parameter("inc_cpu_load", 0, 0));
            mParams.add(new Parameter("pump_down_step", 0, 0));
            mParams.add(new Parameter("pump_up_step", 0, 0));
            mParams.add(new Parameter("screen_off_min_step", 0, 0));
            mParams.add(new Parameter("up_sample_time", 0, 0));
            mParams.add(new Parameter("author", 0, 0));
            mParams.add(new Parameter("tuner", 0, 0));
            mParams.add(new Parameter("version", 0, 0));

        } else if ("smartassV2".equals(governor)) {
            mParams.add(new Parameter("awake_ideal_freq", 0, 0));
            mParams.add(new Parameter("debug_mask", 0, 0));
            mParams.add(new Parameter("down_rate_us", 0, 0));
            mParams.add(new Parameter("max_cpu_load", 0, 0));
            mParams.add(new Parameter("min_cpu_load", 0, 0));
            mParams.add(new Parameter("ramp_down_step", 0, 0));
            mParams.add(new Parameter("ramp_up_step", 0, 0));
            mParams.add(new Parameter("sample_rate_jiffies", 0, 0));
            mParams.add(new Parameter("sleep_ideal_freq", 0, 0));
            mParams.add(new Parameter("sleep_wakeup_freq", 0, 0));
            mParams.add(new Parameter("up_rate_us", 0, 0));

        } else if ("interactiveX".equals(governor)) {
            mParams.add(new Parameter("min_sample_time", 0, 0));

        } else if ("interactive".equals(governor)) {
            mParams.add(new Parameter("go_maxspeed_load", 0, 0));
            mParams.add(new Parameter("min_sample_time", 0, 0));

        } else if ("consercative".equals(governor)) {
            mParams.add(new Parameter("down_threshold", 0, 0));
            mParams.add(new Parameter("freq_step", 0, 0));
            mParams.add(new Parameter("ignore_nice_load", 0, 0));
            mParams.add(new Parameter("sampling_down_factor", 0, 0));
            mParams.add(new Parameter("sampling_rate", 0, 0));
            mParams.add(new Parameter("sampling_rate_max", 0, 0));
            mParams.add(new Parameter("sampling_rate_min", 0, 0));
            mParams.add(new Parameter("up_threshold", 0, 0));

        } else if ("ondemandx".equals(governor)) {
            mParams.add(new Parameter("down_differential", 0, 0));
            mParams.add(new Parameter("ignore_nice_load", 0, 0));
            mParams.add(new Parameter("io_is_busy", 0, 0));
            mParams.add(new Parameter("powersave_bias", 0, 0));
            mParams.add(new Parameter("sampling_down_factor", 0, 0));
            mParams.add(new Parameter("sampling_rate", 0, 0));
            mParams.add(new Parameter("sampling_rate_min", 0, 0));
            mParams.add(new Parameter("suspend_freq", 0, 0));
            mParams.add(new Parameter("up_threshold", 0, 0));

        } else if ("ondemand".equals(governor)) {
            mParams.add(new Parameter("ignore_nice_load", 0, 0));
            mParams.add(new Parameter("io_is_busy", 0, 0));
            mParams.add(new Parameter("powersave_bias", 0, 0));
            mParams.add(new Parameter("sampling_rate", 0, 0));
            mParams.add(new Parameter("sampling_rate_max", 0, 0));
            mParams.add(new Parameter("sampling_rate_min", 0, 0));
            mParams.add(new Parameter("up_threshold", 0, 0));

        } else if ("powersave".equals(governor)) {
            // nothing parameter

        } else if ("powersave".equals(governor)) {
            // nothing parameter

        }
    }

    public Parameter[] getParameters() {
        return mParams.toArray(new Parameter[0]);
    }

    public String getValue(String paramName) {
        SysFs sysFs = new SysFs(CTRL_PATH + "/" + mGovernor + "/" + paramName);
        return sysFs.read();
    }

    public String loadValue(String paramName) {
        String key = "cpu_" + mGovernor + "_" + paramName;
        return super.getStringValue(key);
    }

    @Override
    public void setOnBoot() {
        // noop
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        // noop
    }
}
