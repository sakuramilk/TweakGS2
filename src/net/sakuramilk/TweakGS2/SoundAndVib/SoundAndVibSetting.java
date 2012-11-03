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

package net.sakuramilk.TweakGS2.SoundAndVib;

import android.content.Context;
import net.sakuramilk.util.Misc;
import net.sakuramilk.util.RootProcess;
import net.sakuramilk.util.SettingManager;
import net.sakuramilk.util.SysFs;

public class SoundAndVibSetting extends SettingManager {

    public static final String KEY_SND_PLAY_CPU_LOCK = "sound_play_freq_lock";
    public static final String KEY_VIB_NORMAL_LEVEL = "vib_normal_level";
    public static final String KEY_VIB_INCOMING_LEVEL = "vib_incoming_level";

    private final SysFs mSysFsSoundPlayFreqLock = new SysFs("/sys/devices/virtual/sound/sound_mc1n2/freq_lock");
    private final SysFs mSysFsVibLevel = new SysFs("/sys/devices/platform/tspdrv/vibrator_level");
    private final SysFs mSysFsVibMaxLevel = new SysFs("/sys/devices/platform/tspdrv/vibrator_level_max");

    public SoundAndVibSetting(Context context, RootProcess rootProcess) {
        super(context, rootProcess);
    }

    public SoundAndVibSetting(Context context) {
        this(context, null);
    }

    public boolean isEnableSoundPlayFreqLock() {
        return mSysFsSoundPlayFreqLock.exists();
    }

    public boolean getSoundPlayFreqLock() {
        String value = mSysFsSoundPlayFreqLock.read(mRootProcess);
        return "0".equals(value) ? false : true;
    }

    public void setSoundPlayFreqLock(boolean value) {
        mSysFsSoundPlayFreqLock.write(value ? "1" : "0", mRootProcess);
    }

    public boolean loadSoundPlayFreqLock() {
        return getBooleanValue(KEY_SND_PLAY_CPU_LOCK);
    }

    public void saveSoundPlayFreqLock(boolean value) {
        setValue(KEY_SND_PLAY_CPU_LOCK, value);
    }

    public boolean isEnableVibLevel() {
        return mSysFsVibLevel.exists();
    }

    public String getVibLevel() {
        return mSysFsVibLevel.read(mRootProcess);
    }

    public void setVibLevel(String value) {
        mSysFsVibLevel.write(value, mRootProcess);
    }

    public String loadVibNormalLevel() {
        return getStringValue(KEY_VIB_NORMAL_LEVEL);
    }

    public void saveVibNormalLevel(String value) {
        setValue(KEY_VIB_NORMAL_LEVEL, value);
    }

    public String loadVibIncomingLevel() {
        return getStringValue(KEY_VIB_INCOMING_LEVEL);
    }

    public void saveVibIncomingLevel(String value) {
        setValue(KEY_VIB_INCOMING_LEVEL, value);
    }

    public String getVibMaxLevel() {
        return mSysFsVibMaxLevel.read(mRootProcess);
    }

    @Override
    public void setOnBoot() {
        boolean freqLock = loadSoundPlayFreqLock();
        setSoundPlayFreqLock(freqLock);

        String vibLevel = loadVibNormalLevel();
        if (!Misc.isNullOfEmpty(vibLevel)) {
            setVibLevel(vibLevel);
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        clearValue(KEY_SND_PLAY_CPU_LOCK);
        clearValue(KEY_VIB_NORMAL_LEVEL);
        clearValue(KEY_VIB_INCOMING_LEVEL);
    }
}
