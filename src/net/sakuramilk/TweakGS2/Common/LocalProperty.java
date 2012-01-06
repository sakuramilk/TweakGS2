package net.sakuramilk.TweakGS2.Common;

public class LocalProperty extends PropertyManager {
    public LocalProperty() {
        this("/data/local.prop");
    }
    protected LocalProperty(String filePath) {
        super(filePath);
    }
}
