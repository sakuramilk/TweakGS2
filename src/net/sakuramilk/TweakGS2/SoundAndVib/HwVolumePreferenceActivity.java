package net.sakuramilk.TweakGS2.SoundAndVib;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SysFs;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference;
import net.sakuramilk.TweakGS2.Parts.SeekBarPreference.OnSeekBarPreferenceDoneListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class HwVolumePreferenceActivity extends PreferenceActivity
    implements OnSeekBarPreferenceDoneListener {

    private SeekBarPreference mAvolHp;
    private SeekBarPreference mAvolHpGain;
    private SeekBarPreference mAvolRc;
    private SeekBarPreference mAvolSp;
    private SeekBarPreference mDvolDacMaster;
    private SeekBarPreference mDvolDacAtt;
    private SeekBarPreference mDvolDir0;
    private SeekBarPreference mDvolDir0Att;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.hw_volume_pref);

        mAvolHp = (SeekBarPreference)findPreference("avol_hp");
        if (mSysFsAvolHp.exists()) {
            mAvolHp.setEnabled(true);
            String[] curValue = mSysFsAvolHp.read();
            mAvolHp.setSummary(Misc.getCurrentValueText(this, curValue[0]));
            mAvolHp.setValue(20, -20, Integer.parseInt(curValue[0]));
            mAvolHp.setOnPreferenceDoneListener(this);
        }

        mAvolHpGain = (SeekBarPreference)findPreference("avol_hp_gain");
        if (mSysFsAvolHpGain.exists()) {
            mAvolHpGain.setEnabled(true);
            String[] curValue = mSysFsAvolHpGain.read();
            mAvolHpGain.setSummary(Misc.getCurrentValueText(this, curValue[0]));
            mAvolHpGain.setValue(3, -3, Integer.parseInt(curValue[0]));
            mAvolHpGain.setOnPreferenceDoneListener(this);
        }

        mAvolRc = (SeekBarPreference)findPreference("avol_rc");
        if (mSysFsAvolRc.exists()) {
            mAvolRc.setEnabled(true);
            String[] curValue = mSysFsAvolRc.read();
            mAvolRc.setSummary(Misc.getCurrentValueText(this, curValue[0]));
            mAvolRc.setValue(20, -20, Integer.parseInt(curValue[0]));
            mAvolRc.setOnPreferenceDoneListener(this);
        }

        mAvolSp = (SeekBarPreference)findPreference("avol_sp");
        if (mSysFsAvolSp.exists()) {
            mAvolSp.setEnabled(true);
            String[] curValue = mSysFsAvolSp.read();
            mAvolSp.setSummary(Misc.getCurrentValueText(this, curValue[0]));
            mAvolSp.setValue(20, -20, Integer.parseInt(curValue[0]));
            mAvolSp.setOnPreferenceDoneListener(this);
        }

        mDvolDacMaster = (SeekBarPreference)findPreference("dvol_dac_master");
        if (mSysFsDvolDacMaster.exists()) {
            mDvolDacMaster.setEnabled(true);
            String[] curValue = mSysFsDvolDacMaster.read();
            mDvolDacMaster.setSummary(Misc.getCurrentValueText(this, curValue[0]));
            mDvolDacMaster.setValue(20, -20, Integer.parseInt(curValue[0]));
            mDvolDacMaster.setOnPreferenceDoneListener(this);
        }

        mDvolDacAtt = (SeekBarPreference)findPreference("dvol_dac_att");
        if (mSysFsDvolDacAtt.exists()) {
            mDvolDacAtt.setEnabled(true);
            String[] curValue = mSysFsDvolDacAtt.read();
            mDvolDacAtt.setSummary(Misc.getCurrentValueText(this, curValue[0]));
            mDvolDacAtt.setValue(20, -20, Integer.parseInt(curValue[0]));
            mDvolDacAtt.setOnPreferenceDoneListener(this);
        }

        mDvolDir0 = (SeekBarPreference)findPreference("dvol_dir0");
        if (mSysFsDvolDir0.exists()) {
            mDvolDir0.setEnabled(true);
            String[] curValue = mSysFsDvolDir0.read();
            mDvolDir0.setSummary(Misc.getCurrentValueText(this, curValue[0]));
            mDvolDir0.setValue(20, -20, Integer.parseInt(curValue[0]));
            mDvolDir0.setOnPreferenceDoneListener(this);
        }

        mDvolDir0Att = (SeekBarPreference)findPreference("dvol_dir0_att");
        if (mSysFsDvolDir0Att.exists()) {
            mDvolDir0Att.setEnabled(true);
            String[] curValue = mSysFsDvolDir0Att.read();
            mDvolDir0Att.setSummary(Misc.getCurrentValueText(this, curValue[0]));
            mDvolDir0Att.setValue(20, -20, Integer.parseInt(curValue[0]));
            mDvolDir0Att.setOnPreferenceDoneListener(this);
        }
    }

    @Override
    public boolean onPreferenceDone(Preference preference, String newValue) {
        if (mAvolHp == preference) {
            mSysFsAvolHp.write(newValue);
            mAvolHp.setSummary(Misc.getCurrentValueText(this, newValue));
            mSysFsUpdateVolume.write("1");
            return true;

        } else if (mAvolHpGain == preference) {
            mSysFsAvolHpGain.write(newValue);
            mAvolHpGain.setSummary(Misc.getCurrentValueText(this, newValue));
            mSysFsUpdateVolume.write("1");
            return true;

        } else if (mAvolRc == preference) {
            mSysFsAvolRc.write(newValue);
            mAvolRc.setSummary(Misc.getCurrentValueText(this, newValue));
            mSysFsUpdateVolume.write("1");
            return true;

        } else if (mAvolSp == preference) {
            mSysFsAvolSp.write(newValue);
            mAvolSp.setSummary(Misc.getCurrentValueText(this, newValue));
            mSysFsUpdateVolume.write("1");
            return true;

        } else if (mDvolDacMaster == preference) {
            mSysFsDvolDacMaster.write(newValue);
            mDvolDacMaster.setSummary(Misc.getCurrentValueText(this, newValue));
            mSysFsUpdateVolume.write("1");
            return true;

        } else if (mDvolDacAtt == preference) {
            mSysFsDvolDacAtt.write(newValue);
            mDvolDacAtt.setSummary(Misc.getCurrentValueText(this, newValue));
            mSysFsUpdateVolume.write("1");
            return true;

        } else if (mDvolDir0 == preference) {
            mSysFsDvolDir0.write(newValue);
            mDvolDir0.setSummary(Misc.getCurrentValueText(this, newValue));
            mSysFsUpdateVolume.write("1");
            return true;

        } else if (mDvolDir0Att == preference) {
            mSysFsDvolDir0Att.write(newValue);
            mDvolDir0Att.setSummary(Misc.getCurrentValueText(this, newValue));
            mSysFsUpdateVolume.write("1");
            return true;

        }
        return false;
    }    
}
