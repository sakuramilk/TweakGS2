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

package net.sakuramilk.TweakGS2.Notification;

import android.content.Context;

import net.sakuramilk.TweakGS2.Common.Convert;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class NotificationSetting extends SettingManager {

    public static final String KEY_NOTIFY_LED_TIMEOUT = "notification_led_timeout";
    public static final String KEY_NOTIFY_BLN_ENABLED = "notification_bln_enabled";
    public static final String KEY_NOTIFY_BLN_TIMEOUT = "notification_bln_timeout";
    public static final String KEY_NOTIFY_BLN_BLINKING = "notification_bln_blinking";
    public static final String KEY_NOTIFY_BLN_BREATHING = "notification_bln_breathing";

    public static final String KEY_NOTIFY_BLN_ON_INCOMING = "notification_bln_on_incoming";

    private final SysFs mSysFsLedTimeout = new SysFs("/sys/devices/virtual/misc/notification/led_timeout");
    private final SysFs mSysFsBlnEnabled = new SysFs("/sys/devices/virtual/misc/notification/notification_enabled");
    private final SysFs mSysFsBlnTimeout = new SysFs("/sys/devices/virtual/misc/notification/notification_timeout");
    private final SysFs mSysFsBlnBlinking = new SysFs("/sys/devices/virtual/misc/notification/blinking_enabled");
    private final SysFs mSysFsBlnBreathing = new SysFs("/sys/devices/virtual/misc/notification/breathing_enabled");
    private final SysFs mSysFsBlnControl = new SysFs("/sys/devices/virtual/misc/melfas_touchkey/touchkey_bln_control", "0222");

    public NotificationSetting(Context context) {
        super(context);
    }

    public boolean isEnableLedTimeout() {
        return mSysFsLedTimeout.exists();
    }

    public String getLedTimeout() {
        return mSysFsLedTimeout.read();
    }

    public void setLedTimeout(String value) {
        mSysFsLedTimeout.write(value);
    }

    public String loadLedTimeout() {
        return getStringValue(KEY_NOTIFY_LED_TIMEOUT);
    }

    public void saveLedTimeout(String value) {
        setValue(KEY_NOTIFY_LED_TIMEOUT, value);
    }

    public boolean isEnableBlnEnabled() {
        return mSysFsBlnEnabled.exists();
    }

    public boolean getBlnEnabled() {
        return Convert.toBoolean(mSysFsBlnEnabled.read());
    }

    public void setBlnEnabled(boolean value) {
        mSysFsBlnEnabled.write(Convert.toString(value));
    }

    public boolean loadBlnEnabled() {
        return getBooleanValue(KEY_NOTIFY_BLN_ENABLED);
    }

    public void saveBlnEnabled(boolean value) {
        setValue(KEY_NOTIFY_BLN_ENABLED, value);
    }

    public boolean isEnableBlnTimeout() {
        return mSysFsBlnTimeout.exists();
    }

    public String getBlnTimeout() {
        return mSysFsBlnTimeout.read();
    }

    public void setBlnTimeout(String value) {
        mSysFsBlnTimeout.write(value);
    }

    public String loadBlnTimeout() {
        return getStringValue(KEY_NOTIFY_BLN_TIMEOUT);
    }

    public void saveBlnTimeout(String value) {
        setValue(KEY_NOTIFY_BLN_TIMEOUT, value);
    }

    public boolean isEnableBlnBlinking() {
        return mSysFsBlnBlinking.exists();
    }

    public boolean getBlnBlinking() {
        return Convert.toBoolean(mSysFsBlnBlinking.read());
    }

    public void setBlnBlinking(boolean value) {
        mSysFsBlnBlinking.write(Convert.toString(value));
    }

    public boolean loadBlnBlinking() {
        return getBooleanValue(KEY_NOTIFY_BLN_BLINKING);
    }

    public void saveBlnBlinking(boolean value) {
        setValue(KEY_NOTIFY_BLN_BLINKING, value);
    }

    public boolean isEnableBlnBreathing() {
        return mSysFsBlnBlinking.exists();
    }

    public boolean getBlnBreathing() {
        return Convert.toBoolean(mSysFsBlnBreathing.read());
    }

    public void setBlnBreathing(boolean value) {
        mSysFsBlnBreathing.write(Convert.toString(value));
    }

    public boolean loadBlnBreathing() {
        return getBooleanValue(KEY_NOTIFY_BLN_BREATHING);
    }

    public void saveBlnBreathing(boolean value) {
        setValue(KEY_NOTIFY_BLN_BREATHING, value);
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

    public boolean loadBlnOnIncoming() {
        return getBooleanValue(KEY_NOTIFY_BLN_ON_INCOMING);
    }

    public void saveBlnOnIncoming(boolean value) {
        setValue(KEY_NOTIFY_BLN_ON_INCOMING, value);
    }

    @Override
    public void setOnBoot() {
        if (isEnableLedTimeout()) {
            String value = loadLedTimeout();
            if (!Misc.isNullOfEmpty(value)) {
                setLedTimeout(value);
            }
        }
        if (isEnableBlnEnabled()) {
            setBlnEnabled(loadBlnEnabled());
        }
        if (isEnableBlnTimeout()) {
            String value = loadBlnTimeout();
            if (!Misc.isNullOfEmpty(value)) {
                setBlnTimeout(value);
            }
        }
        if (isEnableBlnBlinking()) {
            setBlnEnabled(loadBlnBlinking());
        }
        if (isEnableBlnBreathing()) {
            setBlnEnabled(loadBlnBreathing());
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        clearValue(KEY_NOTIFY_LED_TIMEOUT);
        clearValue(KEY_NOTIFY_BLN_ENABLED);
        clearValue(KEY_NOTIFY_BLN_TIMEOUT);
        clearValue(KEY_NOTIFY_BLN_BLINKING);
        clearValue(KEY_NOTIFY_BLN_BREATHING);
        clearValue(KEY_NOTIFY_BLN_ON_INCOMING);
    }
}
