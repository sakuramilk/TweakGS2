package net.sakuramilk.TweakGS2.SoundAndVib;

import net.sakuramilk.TweakGS2.Common.Convert;
import net.sakuramilk.TweakGS2.Common.SystemCommand;

public class BootSoundSetting {
    
    public static final String KEY_BOOT_SOUND_ENABLED = "boot_sound_enabled";
    public static final String KEY_BOOT_SOUND_VOLUME = "boot_sound_volume";

    void setBootSoundEnabled(boolean value) {
        SystemCommand.set_prop("persist.sys.nobootsound", Convert.toString(!value));
    }

    boolean getBootSoundEnabled() {
        return !Convert.toBoolean(SystemCommand.get_prop("persist.sys.nobootsound", "0"));
    }

    void setBootSoundVolume(String value) {
        SystemCommand.set_prop("persist.sys.boosound_volume", value);
    }

    String getBootSoundVolume() {
        return SystemCommand.get_prop("persist.sys.boosound_volume", "0.2");
    }
}
