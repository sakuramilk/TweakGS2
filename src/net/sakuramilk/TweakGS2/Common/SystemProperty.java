package net.sakuramilk.TweakGS2.Common;

public class SystemProperty extends PropertyManager {
    public SystemProperty() {
        this("/system/build.prop");
    }
    protected SystemProperty(String filePath) {
        super(filePath);
    }
}
