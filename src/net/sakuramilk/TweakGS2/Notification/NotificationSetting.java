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

package net.sakuramilk.TweakGS2.Notification;

import android.content.Context;

import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class NotificationSetting extends SettingManager {

    public static final String KEY_NOTIFY_BACKLIGHT_FLASH_ON_INCOMING = "notify_backlight_flash_on_incoming";

    private final SysFs mSysFsBlnControl = new SysFs("/sys/devices/virtual/misc/melfas_touchkey/touchkey_bln_control");

    public NotificationSetting(Context context) {
        super(context);
    }

    public boolean isEnableBlnControl() {
        return mSysFsBlnControl.exists();
    }

    public String getBlnControl() {
        return mSysFsBlnControl.read();
    }

    public void setBlnControl(String value) {
        mSysFsBlnControl.write(value);
    }

    public boolean loadBacklightFlashOnIncoming() {
        return getBooleanValue(KEY_NOTIFY_BACKLIGHT_FLASH_ON_INCOMING);
    }

    public void saveBacklightFlashOnIncoming(boolean value) {
        setValue(KEY_NOTIFY_BACKLIGHT_FLASH_ON_INCOMING, value);
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
        clearValue(KEY_NOTIFY_BACKLIGHT_FLASH_ON_INCOMING);
    }
}
