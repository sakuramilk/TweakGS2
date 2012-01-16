package net.sakuramilk.TweakGS2.SoundAndVib;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class SoundAndVibSetting extends SettingManager {

    public static final String KEY_SND_PLAY_CPU_LOCK = "sound_play_freq_lock";
    public static final String KEY_VIB_LEVEL = "vib_level";
    public static final String KEY_VIB_INCREASE_ON_INCOMING = "vib_increase_on_incoming";

    private final SysFs mSysFsSoundPlayFreqLock = new SysFs("/sys/devices/virtual/sound/sound_mc1n2/freq_lock");
    private final SysFs mSysFsVibLevel = new SysFs("/sys/devices/platform/tspdrv/vibrator_level");
    private final SysFs mSysFsVibMaxLevel = new SysFs("/sys/devices/platform/tspdrv/vibrator_level_max");

    public SoundAndVibSetting(Context context) {
        super(context);
    }

    public boolean isEnableSoundPlayFreqLock() {
        return mSysFsSoundPlayFreqLock.exists();
    }

    public boolean getSoundPlayFreqLock() {
        String value = mSysFsVibLevel.read();
        return "0".equals(value) ? false : true;
    }

    public void setSoundPlayFreqLock(boolean value) {
        mSysFsVibLevel.write(value ? "1" : "0");
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
        return mSysFsVibLevel.read();
    }

    public void setVibLevel(String value) {
        mSysFsVibLevel.write(value);
    }

    public String loadVibLevel() {
        return getStringValue(KEY_VIB_LEVEL);
    }

    public void saveVibLevel(String value) {
        setValue(KEY_VIB_LEVEL, value);
    }

    public String getVibMaxLevel() {
        return mSysFsVibMaxLevel.read();
    }

    @Override
    public void setOnBoot() {
        boolean freqLock = loadSoundPlayFreqLock();
        setSoundPlayFreqLock(freqLock);

        String vibLevel = loadVibLevel();
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
        clearValue(KEY_VIB_LEVEL);
    }
}
