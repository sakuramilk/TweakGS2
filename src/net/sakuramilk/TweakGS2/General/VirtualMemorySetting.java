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

package net.sakuramilk.TweakGS2.General;

import android.content.Context;
import net.sakuramilk.util.Misc;
import net.sakuramilk.util.RootProcess;
import net.sakuramilk.util.SettingManager;
import net.sakuramilk.util.SysFs;

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

    public VirtualMemorySetting(Context context, RootProcess rootProcess) {
        super(context, rootProcess);
    }

    public VirtualMemorySetting(Context context) {
        this(context, null);
    }

    public String getVmSwappiness() {
        return mSysFsVmSwappiness.read(mRootProcess);
    }

    public void setVmSwappiness(String value) {
        mSysFsVmSwappiness.write(value, mRootProcess);
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
        return mSysFsVfsCachePressure.read(mRootProcess);
    }

    public void setVmVfsCachePressure(String value) {
        mSysFsVfsCachePressure.write(value, mRootProcess);
    }

    public String getVmDirtyExpireCentisecs() {
        return mSysFsVmDirtyExpireCentisecs.read(mRootProcess);
    }

    public void setVmDirtyExpireCentisecs(String value) {
        mSysFsVmDirtyExpireCentisecs.write(value, mRootProcess);
    }

    public String loadVmDirtyExpireCentisecs() {
        return getStringValue(KEY_VM_DIRTY_EXPIRE_CENTISECS);
    }

    public void saveVmDirtyExpireCentisecs(String value) {
        setValue(KEY_VM_DIRTY_EXPIRE_CENTISECS, value);
    }

    public String getVmDirtyWritebackCentisecs() {
        return mSysFsVmDirtyWritebackCentisecs.read(mRootProcess);
    }

    public void setVmDirtyWritebackCentisecs(String value) {
        mSysFsVmDirtyWritebackCentisecs.write(value, mRootProcess);
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
        return mSysFsVmDirtyRatio.read(mRootProcess);
    }

    public void setVmDirtyRatio(String value) {
        mSysFsVmDirtyRatio.write(value, mRootProcess);
    }

    public String loadVmDirtyBackgroundRatio() {
        return getStringValue(KEY_VM_DIRTY_BACKGROUND_RATIO);
    }

    public void saveVmDirtyBackgroundRatio(String value) {
        setValue(KEY_VM_DIRTY_BACKGROUND_RATIO, value);
    }

    public String getVmDirtyBackgroundRatio() {
        return mSysFsVmDirtyBackgroundRatio.read(mRootProcess);
    }

    public void setVmDirtyBackgroundRatio(String value) {
        mSysFsVmDirtyBackgroundRatio.write(value, mRootProcess);
    }

    @Override
    public void setOnBoot() {
        String value = loadVmSwappiness();
        if (!Misc.isNullOfEmpty(value)) {
            setVmSwappiness(value);
        }
        value = loadVmVfsCachePressure();
        if (!Misc.isNullOfEmpty(value)) {
            setVmVfsCachePressure(value);
        }
        value = loadVmDirtyExpireCentisecs();
        if (!Misc.isNullOfEmpty(value)) {
            setVmDirtyExpireCentisecs(value);
        }
        value = loadVmDirtyWritebackCentisecs();
        if (!Misc.isNullOfEmpty(value)) {
            setVmDirtyWritebackCentisecs(value);
        }
        value = loadVmDirtyRatio();
        if (!Misc.isNullOfEmpty(value)) {
            setVmDirtyRatio(value);
        }
        value = loadVmDirtyBackgroundRatio();
        if (!Misc.isNullOfEmpty(value)) {
            setVmDirtyBackgroundRatio(value);
        }
    }

    @Override
    public void setRecommend() {
        setVmSwappiness("10");
        saveVmSwappiness("10");

        setVmVfsCachePressure("50");
        saveVmVfsCachePressure("50");

        setVmDirtyExpireCentisecs("3000");
        saveVmDirtyExpireCentisecs("3000");

        setVmDirtyWritebackCentisecs("500");
        saveVmDirtyWritebackCentisecs("500");

        setVmDirtyRatio("22");
        saveVmDirtyRatio("22");

        setVmDirtyBackgroundRatio("4");
        saveVmDirtyBackgroundRatio("4");
    }

    @Override
    public void reset() {
        clearValue(KEY_VM_SWAPPINESS);
        clearValue(KEY_VM_VFS_CACHE_PRESSURE);
        clearValue(KEY_VM_DIRTY_EXPIRE_CENTISECS);
        clearValue(KEY_VM_DIRTY_RATIO);
        clearValue(KEY_VM_DIRTY_WRITEBACK_CENTISECS);
        clearValue(KEY_VM_DIRTY_BACKGROUND_RATIO);
    }
}
