/*
 * Copyright (C) 2012 sakuramilk <c.sakuramilk@gmail.com>
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

package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.util.PropertyManager;
import net.sakuramilk.util.SystemCommand;

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
