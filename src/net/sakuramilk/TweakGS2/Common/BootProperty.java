package net.sakuramilk.TweakGS2.Common;

public class BootProperty extends PropertyManager {
    public BootProperty() {
        this("/xdata/boot.conf");
    }
    protected BootProperty(String filePath) {
        super(filePath);
    }
    
    public void setBootRomValue(String value) {
        setValue("ro.boot.rom", value);
    }
    
    public String getBootRomValue() {
        return getValue("ro.boot.rom");
    }
}
