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

package net.sakuramilk.TweakGS2.Display;

import java.io.File;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.RootProcess;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class DisplaySetting extends SettingManager {

    public static final String KEY_LCD_TYPE = "disp_lcd_type";
    public static final String KEY_LCD_GAMMA = "disp_lcd_gamma";
    public static final String KEY_MDNIE_ENABLED = "disp_mdnie_enabled";
    public static final String KEY_MDNIE_MODE = "disp_mdnie_mode";
    public static final String KEY_MDNIE_MCM_CB = "disp_mdnie_color_cb";
    public static final String KEY_MDNIE_MCM_CR = "disp_mdnie_color_cr";

    public static final String MDNIE_MCM_ENABLE = "69";
    public static final String MDNIE_MCM_DISABLE = "0";
    
    public static final String LCD_BASE_PATH_KERNEL_3_0 = "/sys/devices/platform/samsung-pd.2/s3cfb.0/spi_gpio.3/spi_master/spi3/spi3.0/lcd/panel";
    public static final String LCD_BASE_PATH_KERNEL_3_0_OLD = "/sys/devices/platform/samsung-pd.2/s3cfb.0/spi_gpio.3/spi3.0/lcd/panel";
    public static final String LCD_BASE_PATH_KERNEL_2_6 = "/sys/devices/platform/samsung-pd.2/s3cfb.0/spi_gpio.3/spi3.0";
    public static final String LCD_USER_LCDTYPE = "/user_lcdtype";
    public static final String LCD_USER_GAMMA_ADJUST = "/user_gamma_adjust";
    private final SysFs mSysFsLcdType;
    private final SysFs mSysFsLcdGamma;
    
    private final SysFs mSysFsMdniePower = new SysFs("/sys/devices/platform/samsung-pd.2/s3cfb.0/mdnie_power");
    private final SysFs mSysFsMdnieForceDisable = new SysFs("/sys/devices/platform/samsung-pd.2/s3cfb.0/mdnie_force_disable");
    public static final String MDNIE_BASE_PATH_KERNEL_3_0 = "/sys/devices/platform/samsung-pd.2/mdnie/mdnie/mdnie";
    public static final String MDNIE_BASE_PATH_KERNEL_2_6 = "/sys/devices/virtual/mdnieset_ui/switch_mdnieset_ui";
    public static final String MDNIE_USER_MODE_CMD = "/user_mode";
    public static final String MDNIE_USER_MCM_CB_CMD = "/user_cb";
    public static final String MDNIE_USER_MCM_CR_CMD = "/user_cr";
    public static final String MDNIE_USER_MODE_CMD_LEGACY = "/mdnieset_user_mode_cmd";
    public static final String MDNIE_USER_MCM_CB_CMD_LEGACY = "/mdnieset_user_mcm_cb_cmd";
    public static final String MDNIE_USER_MCM_CR_CMD_LEGACY = "/mdnieset_user_mcm_cr_cmd";
    private final SysFs mSysFsMdnieMode;
    private final SysFs mSysFsMdnieMcmCb;
    private final SysFs mSysFsMdnieMcmCr;

    public DisplaySetting(Context context, RootProcess rootProcess) {
        super(context, rootProcess);
        if (Misc.getKernelVersion() >= Misc.KERNEL_VER_3_0_0) {
            File file = new File(LCD_BASE_PATH_KERNEL_3_0);
            if (file.exists()) { 
                mSysFsLcdType = new SysFs(LCD_BASE_PATH_KERNEL_3_0 + LCD_USER_LCDTYPE);
                mSysFsLcdGamma = new SysFs(LCD_BASE_PATH_KERNEL_3_0 + LCD_USER_GAMMA_ADJUST);
            } else {
                mSysFsLcdType = new SysFs(LCD_BASE_PATH_KERNEL_3_0_OLD + LCD_USER_LCDTYPE);
                mSysFsLcdGamma = new SysFs(LCD_BASE_PATH_KERNEL_3_0_OLD + LCD_USER_GAMMA_ADJUST);
            }
            mSysFsMdnieMode = new SysFs(MDNIE_BASE_PATH_KERNEL_3_0 + MDNIE_USER_MODE_CMD);
            mSysFsMdnieMcmCb = new SysFs(MDNIE_BASE_PATH_KERNEL_3_0 + MDNIE_USER_MCM_CB_CMD);
            mSysFsMdnieMcmCr = new SysFs(MDNIE_BASE_PATH_KERNEL_3_0 + MDNIE_USER_MCM_CR_CMD);
        } else {
            mSysFsLcdType = new SysFs(LCD_BASE_PATH_KERNEL_2_6 + LCD_USER_LCDTYPE);
            mSysFsLcdGamma = new SysFs(LCD_BASE_PATH_KERNEL_2_6 + LCD_USER_GAMMA_ADJUST);
            mSysFsMdnieMode = new SysFs(MDNIE_BASE_PATH_KERNEL_2_6 + MDNIE_USER_MODE_CMD_LEGACY);
            mSysFsMdnieMcmCb = new SysFs(MDNIE_BASE_PATH_KERNEL_2_6 + MDNIE_USER_MCM_CB_CMD_LEGACY);
            mSysFsMdnieMcmCr = new SysFs(MDNIE_BASE_PATH_KERNEL_2_6 + MDNIE_USER_MCM_CR_CMD_LEGACY);
        }
    }

    public DisplaySetting(Context context) {
        this(context, null);
    }

    public boolean isEnableLcdType() {
        return mSysFsLcdType.exists();
    }

    public String getLcdType() {
        String value = mSysFsLcdType.read(mRootProcess);
        if ("SM2 (A1 line)".equals(value)) {
            return "0";
        } else if ("M2".equals(value)) {
            return "1";
        } else if ("SM2 (A2 line)".equals(value)) {
            return "2";
        } else {
            return "Unknown";
        }
    }

    public String getLcdTypeText(String value) {
        if ("0".equals(value)) {
            return "SM2 (A1 line)";
        } else if ("1".equals(value)) {
            return "M2";
        } else if ("2".equals(value)) {
            return "SM2 (A2 line)";
        } else {
            return "Unknown";
        }
    }

    public void setLcdType(String value) {
        mSysFsLcdType.write(value, mRootProcess);
    }

    public String loadLcdType() {
        return getStringValue(KEY_LCD_TYPE);
    }

    public void saveLcdType(String value) {
        setValue(KEY_LCD_TYPE, value);
    }

    public boolean isEnableLcdGamma() {
        return mSysFsLcdGamma.exists();
    }

    public String getLcdGamma() {
        return mSysFsLcdGamma.read(mRootProcess);
    }

    public void setLcdGamma(String value) {
        mSysFsLcdGamma.write(value, mRootProcess);
    }

    public String loadLcdGamma() {
        return getStringValue(KEY_LCD_GAMMA);
    }

    public void saveLcdGamma(String value) {
        setValue(KEY_LCD_GAMMA, value);
    }

    public boolean isEnableMdnieForceDisable() {
        return mSysFsMdnieForceDisable.exists();
    }

    public boolean getMdnieEnabled() {
        String ret = mSysFsMdnieForceDisable.read(mRootProcess);
        return "0".equals(ret) ? true : false;
    }

    public void setMdnieEnabled(boolean value) {
        mSysFsMdnieForceDisable.write(value ? "0" : "1", mRootProcess);
        mSysFsMdniePower.write(value ? "1" : "0", mRootProcess);
    }

    public boolean loadMdnieEnabled() {
        return getBooleanValue(KEY_MDNIE_ENABLED);
    }

    public void saveMdnieEnabled(boolean value) {
        setValue(KEY_MDNIE_ENABLED, value);
    }

    public boolean isEnableMdnieMode() {
        return mSysFsMdnieMode.exists();
    }

    public String getMdnieMode() {
        return mSysFsMdnieMode.read(mRootProcess);
    }

    public void setMdnieMode(String value) {
        mSysFsMdnieMode.write(value, mRootProcess);
    }

    public boolean loadMdnieMode() {
        return getBooleanValue(KEY_MDNIE_MODE);
    }

    public void saveMdnieMode(boolean value) {
        setValue(KEY_MDNIE_MODE, value);
    }

    public boolean isEnableMdnieMcmCb() {
        return mSysFsMdnieMcmCb.exists();
    }

    public String getMdnieMcmCb() {
        return mSysFsMdnieMcmCb.read(mRootProcess);
    }

    public void setMdnieMcmCb(String value) {
        mSysFsMdnieMcmCb.write(value, mRootProcess);
    }

    public String loadMdnieMcmCb() {
        return getStringValue(KEY_MDNIE_MCM_CB);
    }

    public void saveMdnieMcmCb(String value) {
        setValue(KEY_MDNIE_MCM_CB, value);
    }

    public boolean isEnableMdnieMcmCr() {
        return mSysFsMdnieMcmCr.exists();
    }

    public String getMdnieMcmCr() {
        return mSysFsMdnieMcmCr.read(mRootProcess);
    }

    public void setMdnieMcmCr(String value) {
        mSysFsMdnieMcmCr.write(value, mRootProcess);
    }

    public String loadMdnieMcmCr() {
        return getStringValue(KEY_MDNIE_MCM_CR);
    }

    public void saveMdnieMcmCr(String value) {
        setValue(KEY_MDNIE_MCM_CR, value);
    }

    /*
    public final String KEY_MDNIE_DU_CONTROL_ENABLED = "lcdsharpness_du_control";
    public final String FILE_MDNIE_DU_CONTROL_ENABLED = "/sys/devices/virtual/mdnieset_ui/switch_mdnieset_ui/mdnieset_user_de_control_enabled_cmd";
    public boolean isEnableMdnieDuControlEnabled() {
        File file = new File(FILE_MDNIE_DU_CONTROL_ENABLED);
        return file.exists();
    }
    public String loadMdnieDuControlEnabled(Context context) {
        return loadValue(context, KEY_MDNIE_DU_CONTROL_ENABLED);
    }
    public String getCurrentMdnieDuControlEnabled() {
        return getCurrentValue(FILE_MDNIE_DU_CONTROL_ENABLED);
    }
    public void setMdnieDuControlEnabled(String value) {
        setValue(FILE_MDNIE_DU_CONTROL_ENABLED, value);
    }
    
    public static final String KEY_MDNIE_DU_SHARPNESS = "lcdsharpness_du_sharpness";
    public static final String FILE_MDNIE_DU_SHARPNESS = "/sys/devices/virtual/mdnieset_ui/switch_mdnieset_ui/mdnieset_user_de_sharpness_cmd";
    public static boolean isEnableMdnieDuSharpness() {
        File file = new File(FILE_MDNIE_DU_SHARPNESS);
        return file.exists();
    }
    public String loadMdnieDuSharpness(Context context) {
        return loadValue(context, KEY_MDNIE_DU_SHARPNESS);
    }
    public String getCurrentMdnieDuSharpness() {
        return getCurrentValue(FILE_MDNIE_DU_SHARPNESS);
    }
    public void setMdnieDuSharpness(String value) {
        setValue(FILE_MDNIE_DU_SHARPNESS, value);
    }
    
    public void setRecommended(Context context) {
        saveLcdGamma(context, "-2");
        saveMdnieForceDisable(context, true);
    }
    
    public void setInit(Context context) {
        saveLcdGamma(context, "0");
        saveMdnieForceDisable(context, true);
    }
    */

    @Override
    public void setOnBoot() {
        if (isEnableLcdType()) {
            String value = loadLcdType();
            if (!Misc.isNullOfEmpty(value)) {
                setLcdType(value);
            }
        }
        if (isEnableLcdGamma()) {
            String value = loadLcdGamma();
            if (!Misc.isNullOfEmpty(value)) {
                setLcdGamma(value);
            }
        }
        if (isEnableMdnieMcmCb()) {
            String value = loadMdnieMcmCb();
            if (!Misc.isNullOfEmpty(value)) {
                setMdnieMcmCb(value);
            }
        }
        if (isEnableMdnieMcmCr()) {
            String value = loadMdnieMcmCr();
            if (!Misc.isNullOfEmpty(value)) {
                setMdnieMcmCr(value);
            }
        }
        if (isEnableMdnieMode()) {
            boolean value = loadMdnieMode();
            setMdnieMode(value ? MDNIE_MCM_ENABLE : MDNIE_MCM_DISABLE);
        }
        if (isEnableMdnieForceDisable()) {
            boolean value = loadMdnieEnabled();
            if (value == false) {
                setMdnieEnabled(false);
            }
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        clearValue(KEY_LCD_TYPE);
        clearValue(KEY_LCD_GAMMA);
        clearValue(KEY_MDNIE_ENABLED);
        clearValue(KEY_MDNIE_MODE);
        clearValue(KEY_MDNIE_MCM_CB);
        clearValue(KEY_MDNIE_MCM_CR);
    }
}
