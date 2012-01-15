package net.sakuramilk.TweakGS2.General;

import net.sakuramilk.TweakGS2.Common.SystemProperty;

public class SystemPropertySetting {

    public static final String KEY_BOOT_SOUND = "sysprop_boot_sound";
    public static final String KEY_CAMERA_SOUND = "sysprop_camera_sound";
    public static final String KEY_CRT_EFFECT = "sysprop_crt_effect";
    public static final String KEY_LOGGER = "sysprop_android_logger";
    public static final String KEY_CIFS = "sysprop_cifs";
    public static final String KEY_NTFS = "sysprop_ntfs";

    private final SystemProperty mSysPorp = new SystemProperty();

    public boolean getBootSound() {
        String ret = mSysPorp.getValue("audioflinger.bootsnd");
        return "0".equals(ret) ? false : true;
    }

    public void setBootSound(boolean value) {
        mSysPorp.setValue("audioflinger.bootsnd", (value ? "1" : "0"));
    }

    public boolean getCameraSound() {
        String ret = mSysPorp.getValue("ro.camera.sound.forced");
        return "0".equals(ret) ? false : true;
    }

    public void setCameraSound(boolean value) {
        mSysPorp.setValue("ro.camera.sound.forced", (value ? "1" : "0"));
    }

    public boolean getCrtEffect() {
        String ret = mSysPorp.getValue("conf.animateScreenLights");
        // NOTICE: crt effect enable is false, not enable is true.
        return "0".equals(ret) ? true : false;
    }

    public void setCrtEffect(boolean value) {
        mSysPorp.setValue("conf.animateScreenLights", (value ? "1" : "0"));
    }

    public boolean getLogger() {
        String ret = mSysPorp.getValue("conf.androidLogger");
        return "0".equals(ret) ? false : true;
    }

    public void setLogger(boolean value) {
        mSysPorp.setValue("conf.androidLogger", (value ? "1" : "0"));
    }

    public boolean getCifs() {
        String ret = mSysPorp.getValue("conf.cifs");
        return "0".equals(ret) ? false : true;
    }

    public void setCifs(boolean value) {
        mSysPorp.setValue("conf.cifs", (value ? "1" : "0"));
    }

    public boolean getNtfs() {
        String ret = mSysPorp.getValue("conf.ntfs");
        return "0".equals(ret) ? false : true;
    }

    public void setNtfs(boolean value) {
        mSysPorp.setValue("conf.ntfs", (value ? "1" : "0"));
    }
}
