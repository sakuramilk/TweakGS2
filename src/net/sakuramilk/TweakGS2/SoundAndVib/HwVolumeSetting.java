package net.sakuramilk.TweakGS2.SoundAndVib;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class HwVolumeSetting extends SettingManager {

    public static final String KEY_AVOL_HP = "avol_hp";
    public static final String KEY_AVOL_HP_GAIN = "avol_hp_gain";
    public static final String KEY_AVOL_RC = "avol_rc";
    public static final String KEY_AVOL_SP = "avol_sp";
    public static final String KEY_DVOL_DAC_MASTER = "dvol_dac_master";
    public static final String KEY_DVOL_DAC_ATT = "dvol_dac_att";
    public static final String KEY_DVOL_DIR0 = "dvol_dir0";
    public static final String KEY_DVOL_DIR0_ATT = "dvol_dir0_att";

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

    public HwVolumeSetting(Context context) {
        super(context);
    }

    // AVOL_HP
    public boolean isEnableAvolHp() {
        return mSysFsAvolHp.exists();
    }

    public String getAvolHp() {
        return mSysFsAvolHp.read();
    }

    public void setAvolHp(String value) {
        mSysFsAvolHp.write(value);
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
        return mSysFsAvolHpGain.read();
    }

    public void setAvolHpGain(String value) {
        mSysFsAvolHpGain.write(value);
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
        return mSysFsAvolRc.read();
    }

    public void setAvolRc(String value) {
        mSysFsAvolRc.write(value);
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
        return mSysFsAvolSp.read();
    }

    public void setAvolSp(String value) {
        mSysFsAvolSp.write(value);
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
        return mSysFsDvolDacMaster.read();
    }

    public void setDvolDacMaster(String value) {
        mSysFsDvolDacMaster.write(value);
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
        return mSysFsDvolDacAtt.read();
    }

    public void setDvolDacAtt(String value) {
        mSysFsDvolDacAtt.write(value);
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
        return mSysFsDvolDir0.read();
    }
    
    public void setDvolDir0(String value) {
        mSysFsDvolDir0.write(value);
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
        return mSysFsDvolDir0Att.read();
    }

    public void setDvolDir0Att(String value) {
        mSysFsDvolDir0Att.write(value);
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
        mSysFsUpdateVolume.write("1");
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
