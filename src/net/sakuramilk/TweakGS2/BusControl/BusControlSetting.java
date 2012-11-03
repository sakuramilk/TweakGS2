/*
 * Copyright (C) 2012 sakuramilk <c.sakuramilk@gmail.com>
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

package net.sakuramilk.TweakGS2.BusControl;

import android.content.Context;
import net.sakuramilk.util.Misc;
import net.sakuramilk.util.RootProcess;
import net.sakuramilk.util.SettingManager;
import net.sakuramilk.util.SysFs;

public class BusControlSetting extends SettingManager {

    public static final String KEY_BUS_ASV_GROUP = "bus_asv_group";
    public static final String KEY_BUS_UP_THRESHOLD = "bus_up_threshold";
    public static final String KEY_BUS_DOWN_THRESHOLD = "bus_down_threshold";
    public static final String KEY_BUS_SET_ON_BOOT = "bus_set_on_boot";

    public static final int THRESHOLD_MAX = 100;
    public static final int THRESHOLD_MIN = 1;

    private static final String CRTL_PATH = "/sys/devices/system/cpu/cpufreq";
    private final SysFs mSysFsAsvGroup = new SysFs(CRTL_PATH + "/busfreq_asv_group");
    private final SysFs mSysFsUpThreshold = new SysFs(CRTL_PATH + "/busfreq_up_threshold");
    private final SysFs mSysFsDownThreshold = new SysFs(CRTL_PATH + "/busfreq_down_threshold");
    //private final SysFs mSysFsCurrentLevel = new SysFs(CRTL_PATH + "/busfreq_current_level");
    //private final SysFs mSysFsTimeInState = new SysFs(CRTL_PATH + "/busfreq_time_in_state");
    

    public BusControlSetting(Context context, RootProcess rootProcess) {
        super(context, rootProcess);
    }

    public BusControlSetting(Context context) {
        this(context, null);
    }

    public boolean isEnableAvsGroup() {
        return mSysFsAsvGroup.exists();
    }

    public String getAsvGroup() {
        return mSysFsAsvGroup.read(mRootProcess);
    }

    public void setAsvGroup(String value) {
        mSysFsAsvGroup.write(value, mRootProcess);
    }

    public String loadAsvGroup() {
        return getStringValue(KEY_BUS_ASV_GROUP);
    }

    public void saveAsvGroup(String value) {
        setValue(KEY_BUS_ASV_GROUP, value);
    }

    public boolean isEnableUpThreshold() {
        return mSysFsUpThreshold.exists();
    }

    public String getUpThreshold() {
        return mSysFsUpThreshold.read(mRootProcess);
    }

    public void setUpThreshold(String value) {
        mSysFsUpThreshold.write(value, mRootProcess);
    }

    public String loadUpThreshold() {
        return getStringValue(KEY_BUS_UP_THRESHOLD);
    }

    public void saveUpThreshold(String value) {
        setValue(KEY_BUS_UP_THRESHOLD, value);
    }

    public boolean isEnableDownThreshold() {
        return mSysFsDownThreshold.exists();
    }

    public String getDownThreshold() {
        return mSysFsDownThreshold.read(mRootProcess);
    }

    public void setDownThreshold(String value) {
        mSysFsDownThreshold.write(value, mRootProcess);
    }

    public String loadDownThreshold() {
        return getStringValue(KEY_BUS_DOWN_THRESHOLD);
    }

    public void saveDownThreshold(String value) {
        setValue(KEY_BUS_DOWN_THRESHOLD, value);
    }

    public boolean loadSetOnBoot() {
        return getBooleanValue(KEY_BUS_SET_ON_BOOT);
    }

    public void saveSetOnBoot(boolean setOnBoot) {
        setValue(KEY_BUS_SET_ON_BOOT, setOnBoot);
    }

    @Override
    public void setOnBoot() {
        if (!loadSetOnBoot()) {
            return;
        }

        String value;
        if (isEnableAvsGroup()) {
            value = loadAsvGroup();
            if (!Misc.isNullOfEmpty(value)) {
                setAsvGroup(value);
            }
        }
        if (isEnableUpThreshold()) {
            value = loadUpThreshold();
            if (!Misc.isNullOfEmpty(value)) {
                setUpThreshold(value);
            }
        }
        if (isEnableDownThreshold()) {
            value = loadDownThreshold();
            if (!Misc.isNullOfEmpty(value)) {
                setDownThreshold(value);
            }
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
    	clearValue(KEY_BUS_ASV_GROUP);
    	clearValue(KEY_BUS_UP_THRESHOLD);
    	clearValue(KEY_BUS_DOWN_THRESHOLD);
    	clearValue(KEY_BUS_SET_ON_BOOT);
    }
}
