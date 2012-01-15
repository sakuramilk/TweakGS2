/*
 * Copyright (C) 2011 sakuramilk <c.sakuramilk@gmail.com>
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

package net.sakuramilk.TweakGS2.Dock;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class DockSetting extends SettingManager {

    public static final String KEY_DOCK_EMULATE = "dock_emulate";
    public static final String KEY_DOCK_MANUAL = "dock_manual";

    public static final String DOCK_EMU_DISABLE_INDEX = "0";
    public static final String DOCK_EMU_DISABLE_VALUE = "13";
    public static final String DOCK_EMU_SW_INDEX = "1";
    public static final String DOCK_EMU_SW_VALUE = "13";
    public static final String DOCK_EMU_HW_INDEX = "2";
    public static final String DOCK_EMU_HW_VALUE = "4";

    private SysFs mDockEmulate = new SysFs("/sys/devices/platform/s3c2410-i2c.5/i2c-5/5-0066/max8997-muic/fake_cable_type");

    public DockSetting(Context context) {
        super(context);
    }

    public boolean isEnableDockEmulate() {
        return mDockEmulate.exists();
    }

    public String getDockEmulate() {
        return mDockEmulate.read();
    }

    public void setDockEmulate(String value) {
        mDockEmulate.write(value);
    }

    public String loadDockEmulate() {
        String ret = getStringValue(KEY_DOCK_EMULATE);
        if (ret == null) {
            return DOCK_EMU_DISABLE_INDEX;
        }
        return ret;
    }

    public void saveDockEmulate(String value) {
        setValue(KEY_DOCK_EMULATE, value);
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
