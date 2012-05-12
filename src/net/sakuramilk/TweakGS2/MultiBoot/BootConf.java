package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.Common.PropertyManager;
import net.sakuramilk.TweakGS2.Common.SystemCommand;

public class BootConf extends PropertyManager {

    private static final String CONF_KEY_BOOT_ROM = "ro.boot.rom";

    public BootConf() {
        super("/xdata/boot.conf");

        // if not exists, create prop file.
        if (!mFile.exists()) {
            SystemCommand.touch(mFile.getPath(), "0666");
        }
    }
    
    public String getBootRom() {
        return getValue(CONF_KEY_BOOT_ROM, "primary");
    }

    public void setBootRom(String rom) {
        setValue(CONF_KEY_BOOT_ROM, rom);
    }
}
