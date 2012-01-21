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

package net.sakuramilk.TweakGS2.General;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class LowMemKillerSetting extends SettingManager {

    public static final String KEY_LOWMEM_FORGROUND_APP = "lowmem_forground_app";            
    public static final String KEY_LOWMEM_VISIBLE_APP = "lowmem_visible_app";
    public static final String KEY_LOWMEM_SECONDARY_SERVER = "lowmem_secondary_server";
    public static final String KEY_LOWMEM_HIDDEN_APP = "lowmem_hidden_app";
    public static final String KEY_LOWMEM_CONTENT_PROVIDER = "lowmem_content_provider";
    public static final String KEY_LOWMEM_EMPTY_APP = "lowmem_empty_app";

    public static int MEM_FREE_MAX = ((80 * 1024) / 4); // 80M
    public static int MEM_FREE_MIN = ((5 * 1024) / 4); // 5M

    private static final String KEY_LOWMEM = "lowmem";

    public final SysFs mSysFsLowMemKillerMinFree = new SysFs("/sys/module/lowmemorykiller/parameters/minfree");

    public LowMemKillerSetting(Context context) {
        super(context);
    }

    public String[] getLowMemKillerMinFree() {
        String value = mSysFsLowMemKillerMinFree.read();
        if (value == null) {
            return null;
        }
        return value.split(",");
    }

    public void setLowMemKillerMinFree(String[] values) {
        String value = values[0];
        for (int i = 1; i<values.length; i++) {
            value += "," + values[i];
        }
        mSysFsLowMemKillerMinFree.write(value);
    }

    public String[] loadLowMemKillerMinFree() {
        String value = getStringValue(KEY_LOWMEM);
        if (value == null) {
            return null;
        }
        return value.split(",");
    }

    public void saveLowMemKillerMinFree(String[] values) {
        String value = values[0];
        for (int i = 1; i<values.length; i++) {
            value += "," + values[i];
        }
        setValue(KEY_LOWMEM, value);
    }

    @Override
    public void setOnBoot() {
        String[] values = loadLowMemKillerMinFree();
        if (values != null) {
            setLowMemKillerMinFree(values);
        }
    }

    @Override
    public void setRecommend() {
        // for GB
        String[] values = { "2560", "4096", "6144", "12288", "14336", "18432" };
        // for ICS
        // String[] values = { "8192", "10240", "12288", "14336", "16384", "20480" };
        setLowMemKillerMinFree(values);
        saveLowMemKillerMinFree(values);
    }

    @Override
    public void reset() {
        clearValue(KEY_LOWMEM);
    }
}
