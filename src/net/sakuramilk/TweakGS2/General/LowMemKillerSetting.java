package net.sakuramilk.TweakGS2.General;

import android.content.Context;
import net.sakuramilk.TweakGS2.Common.SettingManager;
import net.sakuramilk.TweakGS2.Common.SysFs;

public class LowMemKillerSetting extends SettingManager {

    public static final String KEY_LOWMEM_FORGROUND_APP = "lowmem_forground_app";            
    public static final String KEY_LOWMEM_VISIBLE_APP = "lowmem_visible_app";
    public static final String KEY_LOWMEM_SECONDARY_SERVER = "lowmem_secondary_server";
    public static final String KEY_LOWMEM_HIDDEN_APP = "lowmem_hidden_app";
    public static final String KEY_LOWMEM_CONTENT_PROVIDER = "lowmem_content_provider";
    public static final String KEY_LOWMEM_EMPTY_APP = "lowmem_empty_app";

    public static int MEM_FREE_MAX = ((80 * 1024) / 4); // 80M
    public static int MEM_FREE_MIN = ((5 * 1024) / 4); // 5M

    private static final String KEY_LOWMEM = "lowmem";

    public final SysFs mSysFsLowMemKillerMinFree = new SysFs("/sys/module/lowmemorykiller/parameters/minfree");

    public LowMemKillerSetting(Context context) {
        super(context);
    }

    public String[] getLowMemKillerMinFree() {
        String value = mSysFsLowMemKillerMinFree.read();
        if (value == null) {
            return null;
        }
        return value.split(",");
    }

    public void setLowMemKillerMinFree(String[] values) {
        String value = values[0];
        for (int i = 1; i<values.length; i++) {
            value += "," + values[i];
        }
        mSysFsLowMemKillerMinFree.write(value);
    }

    public String[] loadLowMemKillerMinFree() {
        String value = getStringValue(KEY_LOWMEM);
        if (value == null) {
            return null;
        }
        return value.split(",");
    }

    public void saveLowMemKillerMinFree(String[] values) {
        String value = values[0];
        for (int i = 1; i<values.length; i++) {
            value += "," + values[i];
        }
        setValue(KEY_LOWMEM, value);
    }

    @Override
    public void setOnBoot() {
        // TODO 自動生成されたメソッド・スタブ
    }

    @Override
    public void setRecommend() {
        String[] values = { "2560", "4096", "6144", "12288", "14336", "18432" };
        setLowMemKillerMinFree(values);
        saveLowMemKillerMinFree(values);
    }

    @Override
    public void reset() {
        // TODO 自動生成されたメソッド・スタブ
    }
}
