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

import net.sakuramilk.util.Convert;
import net.sakuramilk.util.Misc;
import net.sakuramilk.util.RootProcess;
import net.sakuramilk.util.SettingManager;
import net.sakuramilk.util.SysFs;

public class NotificationSetting extends SettingManager {

    public static final String KEY_NOTIFY_LED_TIMEOUT = "notification_led_timeout";
    public static final String KEY_NOTIFY_LED_FADEOUT = "notification_led_fadeout";
    public static final String KEY_NOTIFY_BLN_ENABLED = "notification_bln_enabled";
    public static final String KEY_NOTIFY_BLN_TIMEOUT = "notification_bln_timeout";
    public static final String KEY_NOTIFY_BLN_EFFECT = "notification_bln_effect";
    public static final String KEY_NOTIFY_BLN_BLINK_ON_INTERVAL = "notification_bln_blink_on_interval";
    public static final String KEY_NOTIFY_BLN_BLINK_OFF_INTERVAL = "notification_bln_blink_off_interval";
    public static final String KEY_NOTIFY_BLN_ON_INCOMING = "notification_bln_on_incoming";
    public static final String KEY_NOTIFY_BLN_TEST = "notification_bln_test";

    public static final String BLN_EFFECT_NONE = "0";
    public static final String BLN_EFFECT_BLINKING = "1";
    public static final String BLN_EFFECT_BREATHING = "2";

    private final SysFs mSysFsLedTimeout = new SysFs("/sys/devices/virtual/misc/notification/led_timeout");
    private final SysFs mSysFsLedFadeout = new SysFs("/sys/devices/virtual/misc/notification/led_fadeout");
    private final SysFs mSysFsBlnEnabled = new SysFs("/sys/devices/virtual/misc/notification/notification_enabled");
    private final SysFs mSysFsBlnTimeout = new SysFs("/sys/devices/virtual/misc/notification/notification_timeout");

    private final SysFs mSysFsBlnBreathingEnable = new SysFs("/sys/devices/virtual/misc/notification/breathing_enabled");
    //private final SysFs mSysFsBlnBreathingMaxVolt = new SysFs("/sys/devices/virtual/misc/notification/breathing_max_volt");
    //private final SysFs mSysFsBlnBreathingMinVolt = new SysFs("/sys/devices/virtual/misc/notification/breathing_min_volt");
    //private final SysFs mSysFsBlnBreathingStepIncrement = new SysFs("/sys/devices/virtual/misc/notification/breathing_step_increment");
    //private final SysFs mSysFsBlnBreathingStepInterval = new SysFs("/sys/devices/virtual/misc/notification/breathing_step_interval");
    //private final SysFs mSysFsBlnBreathingPause = new SysFs("/sys/devices/virtual/misc/notification/breathing_pause");

    private final SysFs mSysFsBlnBlinkingEnabled = new SysFs("/sys/devices/virtual/misc/notification/blinking_enabled");
    private final SysFs mSysFsBlnBlinkingOnInterval = new SysFs("/sys/devices/virtual/misc/notification/blinking_int_on");
    private final SysFs mSysFsBlnBlinkingOffInterval = new SysFs("/sys/devices/virtual/misc/notification/blinking_int_off");
    private final SysFs mSysFsBlnControl = new SysFs("/sys/devices/virtual/misc/melfas_touchkey/touchkey_bln_control", "0222");
    
    private final SysFs mSysFsTouchkeyBrightness = new SysFs("/sys/class/sec/sec_touchkey/brightness");

    public NotificationSetting(Context context, RootProcess rootProcess) {
        super(context, rootProcess);
    }

    public NotificationSetting(Context context) {
        this(context, null);
    }

    public boolean isEnableLedTimeout() {
        return mSysFsLedTimeout.exists();
    }

    public String getLedTimeout() {
        return mSysFsLedTimeout.read(mRootProcess);
    }

    public void setLedTimeout(String value) {
        mSysFsLedTimeout.write(value, mRootProcess);
        if ("-2".equals(value)) {
            mSysFsTouchkeyBrightness.write("2", mRootProcess);
        } else {
            mSysFsTouchkeyBrightness.write("1", mRootProcess);
        }
    }

    public String loadLedTimeout() {
        return getStringValue(KEY_NOTIFY_LED_TIMEOUT);
    }

    public void saveLedTimeout(String value) {
        setValue(KEY_NOTIFY_LED_TIMEOUT, value);
    }

    public boolean isEnableLedFadeout() {
        return mSysFsLedFadeout.exists();
    }

    public boolean getLedFadeout() {
        return Convert.toBoolean(mSysFsLedFadeout.read(mRootProcess));
    }

    public void setLedFadeout(boolean value) {
        mSysFsLedFadeout.write(Convert.toString(value), mRootProcess);
    }

    public boolean loadLedFadeout() {
        return getBooleanValue(KEY_NOTIFY_LED_FADEOUT);
    }

    public void saveLedFadeout(boolean value) {
        setValue(KEY_NOTIFY_LED_FADEOUT, value);
    }

    public boolean isEnableBlnEnabled() {
        return mSysFsBlnEnabled.exists();
    }

    public boolean getBlnEnabled() {
        return Convert.toBoolean(mSysFsBlnEnabled.read(mRootProcess));
    }

    public void setBlnEnabled(boolean value) {
        mSysFsBlnEnabled.write(Convert.toString(value), mRootProcess);
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
        return mSysFsBlnTimeout.read(mRootProcess);
    }

    public void setBlnTimeout(String value) {
        mSysFsBlnTimeout.write(value, mRootProcess);
    }

    public String loadBlnTimeout() {
        return getStringValue(KEY_NOTIFY_BLN_TIMEOUT);
    }

    public void saveBlnTimeout(String value) {
        setValue(KEY_NOTIFY_BLN_TIMEOUT, value);
    }

    public boolean isEnableBlnEffect() {
        return mSysFsBlnBreathingEnable.exists() && mSysFsBlnBlinkingEnabled.exists();
    }

    public String getBlnEffect() {
        boolean breathing = Convert.toBoolean(mSysFsBlnBreathingEnable.read(mRootProcess));
        boolean blinking = Convert.toBoolean(mSysFsBlnBlinkingEnabled.read(mRootProcess));
        if (breathing) {
            return BLN_EFFECT_BREATHING;
        } else if (blinking) {
            return BLN_EFFECT_BLINKING;
        }
        return BLN_EFFECT_NONE;
    }

    public void setBlnEffect(String value) {
        if (BLN_EFFECT_BREATHING.equals(value)) {
            mSysFsBlnBreathingEnable.write("1", mRootProcess);
        } else if (BLN_EFFECT_BLINKING.equals(value)) {
            mSysFsBlnBlinkingEnabled.write("1", mRootProcess);
        } else {
            mSysFsBlnBreathingEnable.write("0", mRootProcess);
            mSysFsBlnBlinkingEnabled.write("0", mRootProcess);
        }
    }

    public String loadBlnEffect() {
        return getStringValue(KEY_NOTIFY_BLN_EFFECT);
    }

    public void saveBlnEffect(String value) {
        setValue(KEY_NOTIFY_BLN_EFFECT, value);
    }

    public String getBlnBlinkingOnInterval() {
        return mSysFsBlnBlinkingOnInterval.read(mRootProcess);
    }

    public void setBlnBlinkingOnInterval(String value) {
        mSysFsBlnBlinkingOnInterval.write(value, mRootProcess);
    }

    public String loadBlnBlinkingOnInterval() {
        return getStringValue(KEY_NOTIFY_BLN_BLINK_ON_INTERVAL);
    }

    public void saveBlnBlinkingOnInterval(String value) {
        setValue(KEY_NOTIFY_BLN_BLINK_ON_INTERVAL, value);
    }

    public String getBlnBlinkingOffInterval() {
        return mSysFsBlnBlinkingOffInterval.read(mRootProcess);
    }

    public void setBlnBlinkingOffInterval(String value) {
        mSysFsBlnBlinkingOffInterval.write(value, mRootProcess);
    }

    public String loadBlnBlinkingOffInterval() {
        return getStringValue(KEY_NOTIFY_BLN_BLINK_OFF_INTERVAL);
    }

    public void saveBlnBlinkingOffInterval(String value) {
        setValue(KEY_NOTIFY_BLN_BLINK_OFF_INTERVAL, value);
    }

    public boolean isEnableBlnControl() {
        return mSysFsBlnControl.exists();
    }

    public String getBlnControl() {
        return mSysFsBlnControl.read(mRootProcess);
    }

    public void setBlnControl(String value) {
        mSysFsBlnControl.write(value, mRootProcess);
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
        if (isEnableLedFadeout()) {
            boolean value = loadLedFadeout();
            setLedFadeout(value);
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
        if (isEnableBlnEffect()) {
            String value = loadBlnEffect();
            if (!Misc.isNullOfEmpty(value)) {
                setBlnEffect(value);
            }
            value = loadBlnBlinkingOnInterval();
            if (!Misc.isNullOfEmpty(value)) {
                setBlnBlinkingOnInterval(value);
            }
            value = loadBlnBlinkingOffInterval();
            if (!Misc.isNullOfEmpty(value)) {
                setBlnBlinkingOffInterval(value);
            }
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        clearValue(KEY_NOTIFY_LED_TIMEOUT);
        clearValue(KEY_NOTIFY_LED_FADEOUT);
        clearValue(KEY_NOTIFY_BLN_ENABLED);
        clearValue(KEY_NOTIFY_BLN_TIMEOUT);
        clearValue(KEY_NOTIFY_BLN_EFFECT);
        clearValue(KEY_NOTIFY_BLN_BLINK_ON_INTERVAL);
        clearValue(KEY_NOTIFY_BLN_BLINK_OFF_INTERVAL);
        clearValue(KEY_NOTIFY_BLN_ON_INCOMING);
    }
}
