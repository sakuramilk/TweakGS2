package net.sakuramilk.TweakGS2.General;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class VirtualMemorySetting extends SettingManager {

    public static final String KEY_VM_SWAPPINESS = "vm_swappiness";
    public static final String KEY_VM_VFS_CACHE_PRESSURE = "vm_vfs_cache_pressure";
    public static final String KEY_VM_DIRTY_EXPIRE_CENTISECS = "vm_dirty_expire_centisecs";
    public static final String KEY_VM_DIRTY_RATIO = "vm_dirty_ratio";
    public static final String KEY_VM_DIRTY_WRITEBACK_CENTISECS = "vm_dirty_writeback_centisecs";
    public static final String KEY_VM_DIRTY_BACKGROUND_RATIO = "vm_dirty_background_ratio";

    public final SysFs mSysFsVmSwappiness = new SysFs("/proc/sys/vm/swappiness");
    public final SysFs mSysFsVfsCachePressure = new SysFs("/proc/sys/vm/vfs_cache_pressure");
    public final SysFs mSysFsVmDirtyExpireCentisecs = new SysFs("/proc/sys/vm/dirty_expire_centisecs");
    public final SysFs mSysFsVmDirtyRatio = new SysFs("/proc/sys/vm/dirty_ratio");
    public final SysFs mSysFsVmDirtyWritebackCentisecs = new SysFs("/proc/sys/vm/dirty_writeback_centisecs");
    public final SysFs mSysFsVmDirtyBackgroundRatio = new SysFs("/proc/sys/vm/dirty_background_ratio");

    public VirtualMemorySetting(Context context) {
        super(context);
    }

    public String getVmSwappiness() {
        return mSysFsVmSwappiness.read();
    }

    public void setVmSwappiness(String value) {
        mSysFsVmSwappiness.write(value);
    }

    public String loadVmSwappiness() {
        return getStringValue(KEY_VM_SWAPPINESS);
    }

    public void saveVmSwappiness(String value) {
        setValue(KEY_VM_SWAPPINESS, value);
    }

    public String loadVmVfsCachePressure() {
        return getStringValue(KEY_VM_VFS_CACHE_PRESSURE);
    }

    public void saveVmVfsCachePressure(String value) {
        setValue(KEY_VM_VFS_CACHE_PRESSURE, value);
    }

    public String getVmVfsCachePressure() {
        return mSysFsVfsCachePressure.read();
    }

    public void setVmVfsCachePressure(String value) {
        mSysFsVfsCachePressure.write(value);
    }

    public String getVmDirtyExpireCentisecs() {
        return mSysFsVmDirtyExpireCentisecs.read();
    }

    public void setVmDirtyExpireCentisecs(String value) {
        mSysFsVmDirtyExpireCentisecs.write(value);
    }

    public String loadVmDirtyExpireCentisecs() {
        return getStringValue(KEY_VM_DIRTY_EXPIRE_CENTISECS);
    }

    public void saveVmDirtyExpireCentisecs(String value) {
        setValue(KEY_VM_DIRTY_EXPIRE_CENTISECS, value);
    }

    public String getVmDirtyWritebackCentisecs() {
        return mSysFsVmDirtyWritebackCentisecs.read();
    }

    public void setVmDirtyWritebackCentisecs(String value) {
        mSysFsVmDirtyWritebackCentisecs.write(value);
    }

    public String loadVmDirtyWritebackCentisecs() {
        return getStringValue(KEY_VM_DIRTY_WRITEBACK_CENTISECS);
    }

    public void saveVmDirtyWritebackCentisecs(String value) {
        setValue(KEY_VM_DIRTY_WRITEBACK_CENTISECS, value);
    }

    public String loadVmDirtyRatio() {
        return getStringValue(KEY_VM_DIRTY_RATIO);
    }

    public void saveVmDirtyRatio(String value) {
        setValue(KEY_VM_DIRTY_RATIO, value);
    }

    public String getVmDirtyRatio() {
        return mSysFsVmDirtyRatio.read();
    }

    public void setVmDirtyRatio(String value) {
        mSysFsVmDirtyRatio.write(value);
    }

    public String loadVmDirtyBackgroundRatio() {
        return getStringValue(KEY_VM_DIRTY_BACKGROUND_RATIO);
    }

    public void saveVmDirtyBackgroundRatio(String value) {
        setValue(KEY_VM_DIRTY_BACKGROUND_RATIO, value);
    }

    public String getVmDirtyBackgroundRatio() {
        return mSysFsVmDirtyBackgroundRatio.read();
    }

    public void setVmDirtyBackgroundRatio(String value) {
        mSysFsVmDirtyBackgroundRatio.write(value);
    }

    @Override
    public void setOnBoot() {
        // TODO 自動生成されたメソッド・スタブ
    }

    @Override
    public void reset() {
        // TODO 自動生成されたメソッド・スタブ
    }
}
