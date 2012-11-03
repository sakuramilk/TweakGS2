package net.sakuramilk.TweakGS2.SoundAndVib;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.util.Misc;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

public class BootSoundPreferenceActivity extends PreferenceActivity
    implements OnPreferenceChangeListener {

    private BootSoundSetting mSetting;
    private CheckBoxPreference mBootSoundEnabled;
    private ListPreference mBootSoundVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.boot_sound_pref);

        mSetting = new BootSoundSetting();

        mBootSoundEnabled = (CheckBoxPreference)findPreference(BootSoundSetting.KEY_BOOT_SOUND_ENABLED);
        mBootSoundEnabled.setChecked(mSetting.getBootSoundEnabled());
        mBootSoundEnabled.setOnPreferenceChangeListener(this);

        mBootSoundVolume = (ListPreference)findPreference(BootSoundSetting.KEY_BOOT_SOUND_VOLUME);
        String value = mSetting.getBootSoundVolume();
        mBootSoundVolume.setValue(value);
        mBootSoundVolume.setSummary(Misc.getCurrentValueText(this,
                Misc.getEntryFromEntryValue(mBootSoundVolume.getEntries(), mBootSoundVolume.getEntryValues(), value)));
        mBootSoundVolume.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mBootSoundEnabled) {
            boolean value = (Boolean)newValue;
            mSetting.setBootSoundEnabled(value);
            mBootSoundEnabled.setChecked(value);

        } else if (preference == mBootSoundVolume) {
            String value = (String)newValue;
            mSetting.setBootSoundVolume(value);
            mBootSoundVolume.setValue(value);
            mBootSoundVolume.setSummary(Misc.getCurrentValueText(this,
                    Misc.getEntryFromEntryValue(mBootSoundVolume.getEntries(), mBootSoundVolume.getEntryValues(), value)));

        }
        return false;
    }
}
