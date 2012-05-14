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
import java.util.ArrayList;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class CpuGovernorSetting extends SettingManager {

    private static final int SAMPING_RATE_MAX = 100000000;

    public class Parameter {
        public static final int TYPE_SEEK_BAR = 0;
        public static final int TYPE_LIST = 1;

        Parameter(String name, int min, int max) {
            this(name, min, max, "");
        }

        Parameter(String name, int min, int max, String unit) {
            this.name = name;
            this.type = TYPE_SEEK_BAR;
            this.min = min;
            this.max = max;
            this.unit = unit;
            this.listEntries = null;
            this.listValues = null;
        }

        Parameter(String name, String[] listEntries, String[] listValues) {
            this.name = name;
            this.type = TYPE_LIST;
            this.listEntries = listEntries;
            this.listValues = listValues;
            this.min = 0;
            this.max = 0;
            this.unit = null;
        }

        public String name;
        public String listEntries[];
        public String listValues[];
        public int type;
        public int min;
        public int max;
        public String unit;
    }

    private static ArrayList<Parameter> mParams;
    private String mGovernor;
    private static final String CTRL_PATH = "/sys/devices/system/cpu/cpufreq";

    protected CpuGovernorSetting(Context context, String governor) {
        super(context);

        mGovernor = governor;
        mParams = new ArrayList<Parameter>();
        
        File[] govParams = new File(CTRL_PATH + "/" + governor).listFiles();
        if (govParams == null) {
            return;
        }

        // setup governor parameter list
        if ("sakuractive".equals(governor)) {
            for (File param : govParams) {
                String fileName = param.getName();
                if (fileName.equals("sampling_rate")) {
                    mParams.add(new Parameter("sampling_rate", 10000, 1000000, "μs"));
                } else if (fileName.equals("up_threshold")) {
                    mParams.add(new Parameter("up_threshold", 1, 100, "%"));
                } else if (fileName.equals("down_threshold")) {
                    mParams.add(new Parameter("down_threshold", 1, 100, "%"));
                } else if (fileName.equals("hotplug_in_sampling_periods")) {
                    mParams.add(new Parameter("hotplug_in_sampling_periods", 0, 0));
                } else if (fileName.equals("hotplug_out_sampling_periods")) {
                    mParams.add(new Parameter("hotplug_out_sampling_periods", 0, 0));
                } else if (fileName.equals("ignore_nice_load")) {
                    mParams.add(new Parameter("ignore_nice_load", 0, 1));
                } else if (fileName.equals("boost_timeout")) {
                    mParams.add(new Parameter("boost_timeout", 0, 10000000, "μs"));
                }
            }
            //mParams.add(new Parameter("down_differential", 0, 0));

        } else if ("lulzactive".equals(governor)) {
            CpuControlSetting cpuControlSetting = new CpuControlSetting(context);
            String[] freqValues = cpuControlSetting.getAvailableFrequencies();
            String[] freqEntries = Misc.getFreqencyEntries(freqValues);
            for (File param : govParams) {
                String fileName = param.getName();
                if (fileName.equals("inc_cpu_load")) {
                    mParams.add(new Parameter("inc_cpu_load", 10, 100));
                } else if (fileName.equals("pump_up_step")) {
                    mParams.add(new Parameter("pump_up_step", 0, freqEntries.length));
                } else if (fileName.equals("pump_down_step")) {
                    mParams.add(new Parameter("pump_down_step", 0, freqEntries.length));
                } else if (fileName.equals("screen_off_min_step")) {
                    mParams.add(new Parameter("screen_off_min_step", 0, freqEntries.length));
                } else if (fileName.equals("up_sample_time")) {
                    mParams.add(new Parameter("up_sample_time", 10000, 50000, "μs"));
                } else if (fileName.equals("down_sample_time")) {
                    mParams.add(new Parameter("down_sample_time", 10000, 100000, "μs"));
                }
            }

        } else if ("smartassV2".equals(governor)) {
            mGovernor = "smartass";
            CpuControlSetting cpuControlSetting = new CpuControlSetting(context);
            String[] freqValues = cpuControlSetting.getAvailableFrequencies();
            String[] freqEntries = Misc.getFreqencyEntries(freqValues);
            for (File param : govParams) {
                String fileName = param.getName();
                if (fileName.equals("max_cpu_load")) {
                    mParams.add(new Parameter("max_cpu_load", 1, 100, "%"));
                } else if (fileName.equals("min_cpu_load")) {
                    mParams.add(new Parameter("min_cpu_load", 1, 100, "%"));
                } else if (fileName.equals("up_rate_us")) {
                    mParams.add(new Parameter("up_rate_us", 1, 100000000, "μs"));
                } else if (fileName.equals("down_rate_us")) {
                    mParams.add(new Parameter("down_rate_us", 1, 100000000, "μs"));
                } else if (fileName.equals("ramp_down_step")) {
                    mParams.add(new Parameter("ramp_down_step", 1, 100000000, "μs"));
                } else if (fileName.equals("ramp_up_step")) {
                    mParams.add(new Parameter("ramp_up_step", 1, 100000000, "μs"));
                } else if (fileName.equals("sample_rate_jiffies")) {
                    mParams.add(new Parameter("sample_rate_jiffies", 1, 1000));
                } else if (fileName.equals("awake_ideal_freq")) {
                    mParams.add(new Parameter("awake_ideal_freq", freqEntries, freqValues));
                } else if (fileName.equals("sleep_ideal_freq")) {
                    mParams.add(new Parameter("sleep_ideal_freq", freqEntries, freqValues));
                } else if (fileName.equals("sleep_wakeup_freq")) {
                    mParams.add(new Parameter("sleep_wakeup_freq", freqEntries, freqValues));
                }
                //mParams.add(new Parameter("debug_mask", 0, 0));
            }

        } else if ("interactiveX".equals(governor)) {
            for (File param : govParams) {
                String fileName = param.getName();
                if (fileName.equals("min_sample_time")) {
                    mParams.add(new Parameter("min_sample_time", 10000, 1000000));
                }
            }

        } else if ("interactive".equals(governor)) {
            CpuControlSetting cpuControlSetting = new CpuControlSetting(context);
            String[] freqValues = cpuControlSetting.getAvailableFrequencies();
            String[] freqEntries = Misc.getFreqencyEntries(freqValues);
            for (File param : govParams) {
                String fileName = param.getName();
                if (fileName.equals("go_maxspeed_load")) {
                    mParams.add(new Parameter("go_maxspeed_load", 5, 100, "%"));
                } else if (fileName.equals("min_sample_time")) {
                    mParams.add(new Parameter("min_sample_time", 10000, 1000000, "μs"));
                } else if (fileName.equals("above_hispeed_delay")) {
                    mParams.add(new Parameter("above_hispeed_delay", 10000, 1000000, "μs"));
                } else if (fileName.equals("boost")) {
                    mParams.add(new Parameter("boost", 0, 1));
                } else if (fileName.equals("go_hispeed_load")) {
                    mParams.add(new Parameter("go_hispeed_load", 5, 100));
                } else if (fileName.equals("hispeed_freq")) {
                    mParams.add(new Parameter("hispeed_freq", freqEntries, freqValues));
                } else if (fileName.equals("input_boost")) {
                    mParams.add(new Parameter("input_boost", 0, 1));
                } else if (fileName.equals("timer_rate")) {
                    mParams.add(new Parameter("min_sample_time", 10000, 1000000, "μs"));
                }
            }

        } else if ("hotplug".equals(governor)) {
            for (File param : govParams) {
                String fileName = param.getName();
                if (fileName.equals("sampling_rate")) {
                    mParams.add(new Parameter("sampling_rate", 10000, 1000000, "μs"));
                } else if (fileName.equals("up_threshold")) {
                    mParams.add(new Parameter("up_threshold", 1, 100, "%"));
                } else if (fileName.equals("down_threshold")) {
                    mParams.add(new Parameter("down_threshold", 1, 100, "%"));
                } else if (fileName.equals("hotplug_in_sampling_periods")) {
                    mParams.add(new Parameter("hotplug_in_sampling_periods", 0, 0));
                } else if (fileName.equals("hotplug_out_sampling_periods")) {
                    mParams.add(new Parameter("hotplug_out_sampling_periods", 0, 0));
                } else if (fileName.equals("ignore_nice_load")) {
                    mParams.add(new Parameter("ignore_nice_load", 0, 1));
                }
            }

        } else if ("wheatley".equals(governor)) {
            SysFs sysFs = new SysFs(CTRL_PATH + "/" + mGovernor + "/sampling_rate_min");
            String samplingRateMin = sysFs.read(mRootProcess);
            mParams.add(new Parameter("sampling_rate", Integer.valueOf(samplingRateMin), SAMPING_RATE_MAX));
            mParams.add(new Parameter("ignore_nice_load", 0, 1));
            mParams.add(new Parameter("powersave_bias", 0, 1000));
            mParams.add(new Parameter("up_threshold", 1, 100, "%"));
            mParams.add(new Parameter("allowed_misses", 0, 100));
            mParams.add(new Parameter("target_residency", 10, 100000));

        } else if ("adaptive".equals(governor)) {
            SysFs sysFs = new SysFs(CTRL_PATH + "/" + mGovernor + "/sampling_rate_min");
            String samplingRateMin = sysFs.read(mRootProcess);
            mParams.add(new Parameter("sampling_rate", Integer.valueOf(samplingRateMin), SAMPING_RATE_MAX));
            mParams.add(new Parameter("ignore_nice_load", 0, 1));
            mParams.add(new Parameter("up_threshold", 1, 100, "%"));

        } else if ("conservative".equals(governor)) {
            SysFs sysFs = new SysFs(CTRL_PATH + "/" + mGovernor + "/sampling_rate_min");
            String samplingRateMin = sysFs.read(mRootProcess);
            mParams.add(new Parameter("sampling_rate", Integer.valueOf(samplingRateMin), SAMPING_RATE_MAX));
            mParams.add(new Parameter("up_threshold", 1, 100, "%"));
            mParams.add(new Parameter("down_threshold", 1, 100, "%"));
            mParams.add(new Parameter("freq_step", 5, 100, "%"));
            mParams.add(new Parameter("ignore_nice_load", 0, 1));
            //mParams.add(new Parameter("sampling_down_factor", 0, 0));

        } else if ("ondemandx".equals(governor)) {
            SysFs sysFs = new SysFs(CTRL_PATH + "/" + mGovernor + "/sampling_rate_min");
            String samplingRateMin = sysFs.read(mRootProcess);
            CpuControlSetting cpuControlSetting = new CpuControlSetting(context);
            String[] freqValues = cpuControlSetting.getAvailableFrequencies();
            String[] freqEntries = Misc.getFreqencyEntries(freqValues);
            mParams.add(new Parameter("sampling_rate", Integer.valueOf(samplingRateMin), SAMPING_RATE_MAX));
            mParams.add(new Parameter("up_threshold", 5, 100, "%"));
            mParams.add(new Parameter("ignore_nice_load", 0, 1));
            mParams.add(new Parameter("powersave_bias", 0, 1000));
            mParams.add(new Parameter("suspend_freq", freqEntries, freqValues));
            //mParams.add(new Parameter("down_differential", 0, 0));

        } else if ("ondemand".equals(governor)) {
            SysFs sysFs = new SysFs(CTRL_PATH + "/" + mGovernor + "/sampling_rate_min");
            String samplingRateMin = sysFs.read(mRootProcess);
            mParams.add(new Parameter("sampling_rate", Integer.valueOf(samplingRateMin), SAMPING_RATE_MAX));
            mParams.add(new Parameter("up_threshold", 5, 100, "%"));
            mParams.add(new Parameter("ignore_nice_load", 0, 1));
            mParams.add(new Parameter("powersave_bias", 0, 1000));

        } else if ("powersave".equals(governor)) {
            // nothing parameter

        } else if ("powersave".equals(governor)) {
            // nothing parameter

        }
    }

    public Parameter[] getParameters() {
        return mParams.toArray(new Parameter[0]);
    }

    public String makeKey(String paramName) {
        return "cpu_" + mGovernor + "_" + paramName;
    }

    public String getValue(String paramName) {
        SysFs sysFs = new SysFs(CTRL_PATH + "/" + mGovernor + "/" + paramName);
        return sysFs.read(mRootProcess);
    }

    public void setValue(String paramName, String value) {
        SysFs sysFs = new SysFs(CTRL_PATH + "/" + mGovernor + "/" + paramName);
        sysFs.write(value, mRootProcess);
    }

    public String loadValue(String paramName) {
        String key = makeKey(paramName);
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
