package net.sakuramilk.TweakGS2.BootConfig;

import net.sakuramilk.TweakGS2.Common.PropertyManager;

public class BootConf extends PropertyManager {

    public BootConf(String filePath) {
        super(filePath);
    }
    
    public class Partision {
        public static final String mmcblk0p9 = "/dev/block/mmcblk0p9";
        public static final String mmcblk0p10 = "/dev/block/mmcblk0p10";
        public static final String mmcblk0p11 = "/dev/block/mmcblk0p11";
        public static final String mmcblk1p1 = "/dev/block/mmcblk1p1";
        public static final String mmcblk1p2 = "/dev/block/mmcblk1p2";
        public static final String mmcblk1p3 = "/dev/block/mmcblk1p3";
    }
    
    private static final String CONF_KEY_LABEL = "mbs.rom%d.label";
    private static final String CONF_KEY_SYSTEM_PART = "mbs.rom%d.system.part";
    private static final String CONF_KEY_SYSTEM_IMG = "mbs.rom%d.system.img";
    private static final String CONF_KEY_SYSTEM_PATH = "mbs.rom%d.system.path";
    private static final String CONF_KEY_DATA_PART = "mbs.rom%d.data.part";
    private static final String CONF_KEY_DATA_IMG = "mbs.rom%d.data.img";
    private static final String CONF_KEY_DATA_PATH = "mbs.rom%d.data.path";
    
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
}
