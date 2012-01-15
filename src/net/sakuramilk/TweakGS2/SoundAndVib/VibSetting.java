package net.sakuramilk.TweakGS2.SoundAndVib;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class VibSetting extends SettingManager {

    public static final String KEY_VIB_LEVEL = "vib_level";

    private final SysFs mSysFsVibLevel = new SysFs("/sys/devices/platform/tspdrv/vibrator_level");
    private final SysFs mSysFsVibMaxLevel = new SysFs("/sys/devices/platform/tspdrv/vibrator_level_max");

    public VibSetting(Context context) {
        super(context);
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
    }

    @Override
    public void reset() {
    }
}
