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
import net.sakuramilk.TweakGS2.Common.TweakPropery;

public class SystemPropertySetting extends SettingManager {

    public static final String KEY_BOOT_SOUND = "sysprop_boot_sound";
    public static final String KEY_CAMERA_SOUND = "sysprop_camera_sound";
    public static final String KEY_CRT_EFFECT = "sysprop_crt_effect";
    public static final String KEY_LOGGER = "sysprop_android_logger";
    public static final String KEY_CIFS = "sysprop_cifs";
    public static final String KEY_NTFS = "sysprop_ntfs";

    private final TweakPropery mTweakPorp = new TweakPropery();

    protected SystemPropertySetting(Context context) {
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

    public boolean getCrtEffect() {
        String ret = mTweakPorp.getValue("conf.animateScreenLights", "0");
        // NOTICE: crt effect enable is false, not enable is true.
        return "0".equals(ret) ? true : false;
    }

    public void setCrtEffect(boolean value) {
        mTweakPorp.setValue("conf.animateScreenLights", (value ? "1" : "0"));
    }

    public boolean getLogger() {
        String ret = mTweakPorp.getValue("conf.androidLogger", "0");
        return "0".equals(ret) ? false : true;
    }

    public void setLogger(boolean value) {
        mTweakPorp.setValue("conf.androidLogger", (value ? "1" : "0"));
    }

    public boolean getCifs() {
        String ret = mTweakPorp.getValue("conf.cifs", "0");
        return "0".equals(ret) ? false : true;
    }

    public void setCifs(boolean value) {
        mTweakPorp.setValue("conf.cifs", (value ? "1" : "0"));
    }

    public boolean getNtfs() {
        String ret = mTweakPorp.getValue("conf.ntfs", "0");
        return "0".equals(ret) ? false : true;
    }

    public void setNtfs(boolean value) {
        mTweakPorp.setValue("conf.ntfs", (value ? "1" : "0"));
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
    }
}
