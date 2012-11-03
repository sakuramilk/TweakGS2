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

package net.sakuramilk.TweakGS2.SoundAndVib;

import android.content.Context;
import net.sakuramilk.util.Misc;
import net.sakuramilk.util.RootProcess;
import net.sakuramilk.util.SettingManager;
import net.sakuramilk.util.SysFs;

public class HwVolumeSetting extends SettingManager {

    public static final String KEY_AVOL_HP = "avol_hp";
    public static final String KEY_AVOL_HP_GAIN = "avol_hp_gain";
    public static final String KEY_AVOL_RC = "avol_rc";
    public static final String KEY_AVOL_SP = "avol_sp";
    public static final String KEY_DVOL_DAC_MASTER = "dvol_dac_master";
    public static final String KEY_DVOL_DAC_ATT = "dvol_dac_att";
    public static final String KEY_DVOL_DIR0 = "dvol_dir0";
    public static final String KEY_DVOL_DIR0_ATT = "dvol_dir0_att";

    public static final int AVOL_HP_MAX = 20;
    public static final int AVOL_HP_MIN = -20;
    public static final int AVOL_HP_GAIN_MAX = 3;
    public static final int AVOL_HP_GAIN_MIN = -3;
    public static final int AVOL_RC_MAX = 20;
    public static final int AVOL_RC_MIN = -20;
    public static final int AVOL_SP_MAX = 20;
    public static final int AVOL_SP_MIN = -20;
    public static final int DVOL_DAC_MASTER_MAX = 20;
    public static final int DVOL_DAC_MASTER_MIN = -20;
    public static final int DVOL_DAC_ATT_MAX = 20;
    public static final int DVOL_DAC_ATT_MIN = -20;
    public static final int DVOL_DIR0_MAX = 20;
    public static final int DVOL_DIR0_MIN = -20;
    public static final int DVOL_DIR0_ATT_MAX = 20;
    public static final int DVOL_DIR0_ATT_MIN = -20;

    private static final String CTRL_PATH = "/sys/devices/virtual/sound/sound_mc1n2";
    private final SysFs mSysFsAvolHp = new SysFs(CTRL_PATH + "/AVOL_HP");
    private final SysFs mSysFsAvolHpGain = new SysFs(CTRL_PATH + "/AVOL_HP_GAIN");
    private final SysFs mSysFsAvolRc = new SysFs(CTRL_PATH + "/AVOL_RC");
    private final SysFs mSysFsAvolSp = new SysFs(CTRL_PATH + "/AVOL_SP");
    private final SysFs mSysFsDvolDacMaster = new SysFs(CTRL_PATH + "/DVOL_DAC_MASTER");
    private final SysFs mSysFsDvolDacAtt = new SysFs(CTRL_PATH + "/DVOL_DAC_ATT");
    private final SysFs mSysFsDvolDir0 = new SysFs(CTRL_PATH + "/DVOL_DIR0");
    private final SysFs mSysFsDvolDir0Att = new SysFs(CTRL_PATH + "/DVOL_DIR0_ATT");
    private final SysFs mSysFsUpdateVolume = new SysFs(CTRL_PATH + "/update_volume");

    public HwVolumeSetting(Context context, RootProcess rootProcess) {
        super(context, rootProcess);
    }

    public HwVolumeSetting(Context context) {
        this(context, null);
    }

    // AVOL_HP
    public boolean isEnableAvolHp() {
        return mSysFsAvolHp.exists();
    }

    public String getAvolHp() {
        return mSysFsAvolHp.read(mRootProcess);
    }

    public void setAvolHp(String value) {
        mSysFsAvolHp.write(value, mRootProcess);
    }

    public String loadAvolHp() {
        String ret = getStringValue(KEY_AVOL_HP);
        if (ret == null) {
            return "0";
        }
        return ret;
    }

    public void saveAvolHp(String value) {
        setValue(KEY_AVOL_HP, value);
    }

    // AVOL_HP_GAIN
    public boolean isEnableAvolHpGain() {
        return mSysFsAvolHpGain.exists();
    }

    public String getAvolHpGain() {
        return mSysFsAvolHpGain.read(mRootProcess);
    }

    public void setAvolHpGain(String value) {
        mSysFsAvolHpGain.write(value, mRootProcess);
    }

    public String loadAvolHpGain() {
        String ret = getStringValue(KEY_AVOL_HP_GAIN);
        if (ret == null) {
            return "0";
        }
        return ret;
    }

    public void saveAvolHpGain(String value) {
        setValue(KEY_AVOL_HP_GAIN, value);
    }

    // AVOL_RC
    public boolean isEnableAvolRc() {
        return mSysFsAvolRc.exists();
    }

    public String getAvolRc() {
        return mSysFsAvolRc.read(mRootProcess);
    }

    public void setAvolRc(String value) {
        mSysFsAvolRc.write(value, mRootProcess);
    }

    public String loadAvolRc() {
        String ret = getStringValue(KEY_AVOL_RC);
        if (ret == null) {
            return "0";
        }
        return ret;
    }

    public void saveAvolRc(String value) {
        setValue(KEY_AVOL_RC, value);
    }

    // AVOL_SP
    public boolean isEnableAvolSp() {
        return mSysFsAvolSp.exists();
    }

    public String getAvolSp() {
        return mSysFsAvolSp.read(mRootProcess);
    }

    public void setAvolSp(String value) {
        mSysFsAvolSp.write(value, mRootProcess);
    }

    public String loadAvolSp() {
        String ret = getStringValue(KEY_AVOL_SP);
        if (ret == null) {
            return "0";
        }
        return ret;
    }

    public void saveAvolSp(String value) {
        setValue(KEY_AVOL_SP, value);
    }

    // DVOL_DAC_MASTER
    public boolean isEnableDvolDacMaster() {
        return mSysFsDvolDacMaster.exists();
    }

    public String getDvolDacMaster() {
        return mSysFsDvolDacMaster.read(mRootProcess);
    }

    public void setDvolDacMaster(String value) {
        mSysFsDvolDacMaster.write(value, mRootProcess);
    }

    public String loadDvolDacMaster() {
        String ret = getStringValue(KEY_DVOL_DAC_MASTER);
        if (ret == null) {
            return "0";
        }
        return ret;
    }

    public void saveDvolDacMaster(String value) {
        setValue(KEY_DVOL_DAC_MASTER, value);
    }

    // DVOL_DAC_ATT
    public boolean isEnableDvolDacAtt() {
        return mSysFsDvolDacAtt.exists();
    }

    public String getDvolDacAtt() {
        return mSysFsDvolDacAtt.read(mRootProcess);
    }

    public void setDvolDacAtt(String value) {
        mSysFsDvolDacAtt.write(value, mRootProcess);
    }

    public String loadDvolDacAtt() {
        String ret = getStringValue(KEY_DVOL_DAC_ATT);
        if (ret == null) {
            return "0";
        }
        return ret;
    }

    public void saveDvolDacAtt(String value) {
        setValue(KEY_DVOL_DAC_ATT, value);
    }

    // DVOL_DIR0
    public boolean isEnableDvolDir0() {
        return mSysFsDvolDir0.exists();
    }
    
    public String getDvolDir0() {
        return mSysFsDvolDir0.read(mRootProcess);
    }
    
    public void setDvolDir0(String value) {
        mSysFsDvolDir0.write(value, mRootProcess);
    }
    
    public String loadDvolDir0() {
        String ret = getStringValue(KEY_DVOL_DIR0);
        if (ret == null) {
            return "0";
        }
        return ret;
    }
    
    public void saveDvolDir0(String value) {
        setValue(KEY_DVOL_DIR0, value);
    }

    // DVOL_DIR0_ATT
    public boolean isEnableDvolDir0Att() {
        return mSysFsDvolDir0Att.exists();
    }

    public String getDvolDir0Att() {
        return mSysFsDvolDir0Att.read(mRootProcess);
    }

    public void setDvolDir0Att(String value) {
        mSysFsDvolDir0Att.write(value, mRootProcess);
    }

    public String loadDvolDir0Att() {
        String ret = getStringValue(KEY_DVOL_DIR0_ATT);
        if (ret == null) {
            return "0";
        }
        return ret;
    }

    public void saveDvolDir0Att(String value) {
        setValue(KEY_DVOL_DIR0_ATT, value);
    }

    public void updateVolume() {
        mSysFsUpdateVolume.write("1", mRootProcess);
    }

    @Override
    public void setOnBoot() {
        String value;
        
        value = loadAvolHp();
        if (!Misc.isNullOfEmpty(value)) {
            setAvolHp(value);
        }
        value = loadAvolHpGain();
        if (!Misc.isNullOfEmpty(value)) {
            setAvolHpGain(value);
        }
        value = loadAvolRc();
        if (!Misc.isNullOfEmpty(value)) {
            setAvolRc(value);
        }
        value = loadAvolSp();
        if (!Misc.isNullOfEmpty(value)) {
            setAvolSp(value);
        }
        value = loadDvolDacMaster();
        if (!Misc.isNullOfEmpty(value)) {
            setDvolDacMaster(value);
        }
        value = loadDvolDacAtt();
        if (!Misc.isNullOfEmpty(value)) {
            setDvolDacAtt(value);
        }
        value = loadDvolDir0();
        if (!Misc.isNullOfEmpty(value)) {
            setDvolDir0(value);
        }
        value = loadDvolDir0Att();
        if (!Misc.isNullOfEmpty(value)) {
            setDvolDir0Att(value);
        }
    }

    @Override
    public void setRecommend() {
        // noop
    }

    @Override
    public void reset() {
        clearValue(KEY_AVOL_HP);
        clearValue(KEY_AVOL_HP_GAIN);
        clearValue(KEY_AVOL_RC);
        clearValue(KEY_AVOL_SP);
        clearValue(KEY_DVOL_DAC_MASTER);
        clearValue(KEY_DVOL_DAC_ATT);
        clearValue(KEY_DVOL_DIR0);
        clearValue(KEY_DVOL_DIR0_ATT);
    }
}
