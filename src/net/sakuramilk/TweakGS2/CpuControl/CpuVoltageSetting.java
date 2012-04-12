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

import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;
import android.content.Context;

public class CpuVoltageSetting extends SettingManager {

    public static final String KEY_CPU_VOLT_ROOT_PREF = "root_pref";
    public static final String KEY_CPU_VOLT_CTRL_BASE = "cpu_vc_";
    public static final String KEY_ASV_GROUP_CATEGORY = "cpu_voltage_asv_group_category";
    public static final String KEY_ASV_GROUP = "asv_group";

    private static final String CRTL_PATH = "/sys/devices/system/cpu/cpu0/cpufreq";
    private final SysFs mSysFsUV_mV_table = new SysFs(CRTL_PATH + "/UV_mV_table");
    private final SysFs mSysFsAsvGroup = new SysFs(CRTL_PATH + "/asv_group");

    public CpuVoltageSetting(Context context) {
        super(context);
    }

    public boolean isEnableVoltageControl() {
        return mSysFsUV_mV_table.exists();
    }

    public String getAsvGroup() {
    	String value = mSysFsAsvGroup.read();
    	if (!Misc.isNullOfEmpty(value)) {
    		String[] v = value.split(" ");
    		return v[1];
    	}
    	return null;
    }

    public void setAsvGroup(String value) {
    	mSysFsAsvGroup.write(value);
    }

    public String loadAsvGroup() {
        return getStringValue(KEY_ASV_GROUP);
    }

    public void saveAsvGroup(String value) {
        setValue(KEY_ASV_GROUP, value);
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

    public void setVoltageTable(String[] voltageTable) {
        String value = "";
        for (String volt : voltageTable) {
            value += volt + " ";
        }
        mSysFsUV_mV_table.write(value);
    }

    public String loadVoltage(String key) {
        return getStringValue(key);
    }

    @Override
    public void setOnBoot() {
    	String asvGroup = loadAsvGroup();
    	if (!Misc.isNullOfEmpty(asvGroup)) {
    		setAsvGroup(asvGroup);
    	}

        String[] voltTable = getVoltageTable();
        CpuControlSetting cpuSetting = new CpuControlSetting(mContext);
        String[] availableFrequencies = cpuSetting.getAvailableFrequencies();
        for (int i = 0; i < voltTable.length; i++) {
            String freq = String.valueOf(Integer.parseInt(availableFrequencies[i]) / 1000);
            String volt = loadVoltage(KEY_CPU_VOLT_CTRL_BASE + freq);
            if (!Misc.isNullOfEmpty(volt)) {
                voltTable[i] = volt;
            }
        }
        setVoltageTable(voltTable);
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
    	clearValue(KEY_ASV_GROUP);
        String[] voltTable = getVoltageTable();
        for (int i = 0; i < voltTable.length; i++) {
            clearValue(KEY_CPU_VOLT_CTRL_BASE + i);
        }
    }
}
