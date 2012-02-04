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

package net.sakuramilk.TweakGS2.MultiBoot;

import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.PropertyManager;
import net.sakuramilk.TweakGS2.Common.SystemCommand;

public class MbsConf extends PropertyManager {

    public static final int MAX_ROM_ID = 7;

    public MbsConf() {
        super("/xdata/mbs.conf");

        // if not exists, create prop file.
        if (!mFile.exists()) {
            SystemCommand.touch(mFile.getPath(), "0666");
        }
    }

    public class Partition {
        public static final String mmcblk0p9 = "/dev/block/mmcblk0p9";
        public static final String mmcblk0p10 = "/dev/block/mmcblk0p10";
        public static final String mmcblk0p11 = "/dev/block/mmcblk0p11";
        public static final String mmcblk0p12 = "/dev/block/mmcblk0p12";
        public static final String mmcblk1p1 = "/dev/block/mmcblk1p1";
        public static final String mmcblk1p2 = "/dev/block/mmcblk1p2";
        public static final String mmcblk1p3 = "/dev/block/mmcblk1p3";
    }

    private static final String CONF_KEY_BOOT_ROM = "mbs.boot.rom";
    private static final String CONF_KEY_LABEL = "mbs.rom%d.label";
    private static final String CONF_KEY_SYSTEM_PART = "mbs.rom%d.system.part";
    private static final String CONF_KEY_SYSTEM_IMG = "mbs.rom%d.system.img";
    private static final String CONF_KEY_SYSTEM_PATH = "mbs.rom%d.system.path";
    private static final String CONF_KEY_DATA_PART = "mbs.rom%d.data.part";
    private static final String CONF_KEY_DATA_IMG = "mbs.rom%d.data.img";
    private static final String CONF_KEY_DATA_PATH = "mbs.rom%d.data.path";

    public int getBootRomId() {
        return Integer.valueOf(getValue(CONF_KEY_BOOT_ROM, "0"));
    }

    public void setBootRomId(int romId) {
        setValue(CONF_KEY_BOOT_ROM, String.valueOf(romId));        
    }

    public String getLabel(int romId) {
        return getValue(String.format(CONF_KEY_LABEL, romId));
    }

    public void setLabel(int romId, String value) {
        setValue(String.format(CONF_KEY_LABEL, romId), value);
    }

    public String getSystemPartition(int romId) {
        return getValue(String.format(CONF_KEY_SYSTEM_PART, romId));
    }

    public void setSystemPartition(int romId, String value) {
        setValue(String.format(CONF_KEY_SYSTEM_PART, romId), value);
    }

    public String getSystemImage(int romId) {
        return getValue(String.format(CONF_KEY_SYSTEM_IMG, romId));
    }

    public void setSystemImage(int romId, String value) {
        setValue(String.format(CONF_KEY_SYSTEM_IMG, romId), value);
    }

    public String getSystemPath(int romId) {
        return getValue(String.format(CONF_KEY_SYSTEM_PATH, romId));
    }

    public void setSystemPath(int romId, String value) {
        setValue(String.format(CONF_KEY_SYSTEM_PATH, romId), value);
    }

    public String getDataPartition(int romId) {
        return getValue(String.format(CONF_KEY_DATA_PART, romId));
    }

    public void setDataPartition(int romId, String value) {
        setValue(String.format(CONF_KEY_DATA_PART, romId), value);
    }

    public String getDataImage(int romId) {
        return getValue(String.format(CONF_KEY_DATA_IMG, romId));
    }

    public void setDataImage(int romId, String value) {
        setValue(String.format(CONF_KEY_DATA_IMG, romId), value);
    }

    public String getDataPath(int romId) {
        return getValue(String.format(CONF_KEY_DATA_PATH, romId));
    }

    public void setDataPath(int romId, String value) {
        setValue(String.format(CONF_KEY_DATA_PATH, romId), value);
    }

    public int getNextRomId() {
        for (int i = 0; i <= MAX_ROM_ID; i++) {
            if (Misc.isNullOfEmpty(getSystemPartition(i))) {
                return i;
            }
        }
        return -1;
    }

    public void deleteRomId(int romId) {
        setLabel(romId, "");
        setSystemPartition(romId, "");
        setSystemImage(romId, "");
        setSystemPath(romId, "");
        setDataPartition(romId, "");
        setDataImage(romId, "");
        setDataPath(romId, "");

        int bootRomId = getBootRomId();
        if (bootRomId == romId) {
            while (romId > 0) {
                romId--;
                if (!Misc.isNullOfEmpty(getSystemPartition(romId))) {
                    setBootRomId(romId);
                    break;
                }
            }
        }
    }
}
