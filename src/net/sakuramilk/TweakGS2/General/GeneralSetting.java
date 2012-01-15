/*
 * Copyright (C) 2011 sakuramilk
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

public class GeneralSetting extends SettingManager {

    public static final String KEY_IO_SCHED = "iosched_type";

    private final SysFs mSysFsIoSheduler = new SysFs("/sys/devices/platform/s3c-mshci.0/mmc_host/mmc0/mmc0:0001/block/mmcblk0/queue/scheduler");

    public GeneralSetting(Context context) {
        super(context);
    }

    public String getCurrentIoScheduler() {
        String value = mSysFsIoSheduler.read();
        if (value != null) {
            String list[] = value.split(" ");
            for (String ioSched : list) {
                if (ioSched.charAt(0) == '[') {
                    return ioSched.substring(1, ioSched.length() - 1); 
                }
            }
        }
        return "";
    }

    public String[] getIoSchedulerList() {
        String value = mSysFsIoSheduler.read();
        if (value != null) {
            String list[] = value.split(" ");
            for (int i = 0; i < list.length ; i++) {
                if (list[i].charAt(0) == '[') {
                    list[i] = list[i].substring(1, list[i].length() - 1); 
                }
            }
            return list;
        }
        return null;
    }

    public void setIoScheduler(String value) {
        mSysFsIoSheduler.write(value);
    }

    public String loadIoScheduler() {
        return getStringValue(KEY_IO_SCHED);
    }

    public void saveIoScheduler(String value) {
        setValue(KEY_IO_SCHED, value);
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
