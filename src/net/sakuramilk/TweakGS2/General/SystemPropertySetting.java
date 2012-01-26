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
import net.sakuramilk.TweakGS2.Common.SystemCommand;
import net.sakuramilk.TweakGS2.Common.TweakPropery;

public class SystemPropertySetting extends SettingManager {

    public static final String KEY_BOOT_SOUND = "sysprop_boot_sound";
    public static final String KEY_CAMERA_SOUND = "sysprop_camera_sound";
    public static final String KEY_LCD_DENSITY = "sysprop_lcd_density";
    public static final String KEY_CRT_EFFECT = "sysprop_crt_effect";
    public static final String KEY_LOGGER = "sysprop_android_logger";
    public static final String KEY_CIFS = "sysprop_cifs";
    public static final String KEY_NTFS = "sysprop_ntfs";

    private final TweakPropery mTweakPorp = new TweakPropery();

    public SystemPropertySetting(Context context) {
        super(context);
    }

    public boolean getBootSound() {
        String ret = mTweakPorp.getValue("audioflinger.bootsnd", "0");
        return "0".equals(ret) ? false : true;
    }

    public void setBootSound(boolean value) {
        mTweakPorp.setValue("audioflinger.bootsnd", (value ? "1" : "0"));
    }

    public boolean getCameraSound() {
        String ret = mTweakPorp.getValue("ro.camera.sound.forced", "0");
        return "0".equals(ret) ? false : true;
    }

    public void setCameraSound(boolean value) {
        mTweakPorp.setValue("ro.camera.sound.forced", (value ? "1" : "0"));
    }

    public String getLcdDensity() {
        String ret = mTweakPorp.getValue("ro.sf.lcd_density", null);
        if (ret == null) {
            return SystemCommand.get_prop("ro.sf.lcd_density", "240");
        }
        return ret;
    }

    public void setLcdDensity(String value) {
        mTweakPorp.setValue("ro.sf.lcd_density", value);
    }

    public boolean getCrtEffect() {
        String ret = SystemCommand.get_prop("persist.tgs2.crt_effect", "0");
        // NOTICE: crt effect enable is false, not enable is true.
        return "0".equals(ret) ? true : false;
    }

    public void setCrtEffect(boolean value) {
        SystemCommand.set_prop("persist.tgs2.crt_effect", (value ? "0" : "1"));
    }

    public boolean getLogger() {
        String ret = SystemCommand.get_prop("persist.tgs2.logger", "0");
        return "0".equals(ret) ? false : true;
    }

    public void setLogger(boolean value) {
        SystemCommand.set_prop("persist.tgs2.logger", (value ? "1" : "0"));
    }

    public boolean getCifs() {
        String ret = SystemCommand.get_prop("persist.tgs2.cifs", "0");
        return "0".equals(ret) ? false : true;
    }

    public void setCifs(boolean value) {
        SystemCommand.set_prop("persist.tgs2.cifs", (value ? "1" : "0"));
    }

    public boolean getNtfs() {
        String ret = SystemCommand.get_prop("persist.tgs2.ntfs", "0");
        return "0".equals(ret) ? false : true;
    }

    public void setNtfs(boolean value) {
        SystemCommand.set_prop("persist.tgs2.ntfs", (value ? "1" : "0"));
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
        mTweakPorp.delete();
        setCrtEffect(true);
        setLogger(false);
        setCifs(false);
        setNtfs(false);
    }
}
