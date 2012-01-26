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

package net.sakuramilk.TweakGS2.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Parts.ConfirmAlertDialog;

public class Misc {

    static final SysFs sSysFsAospRom = new SysFs("proc/sys/kernel/aosp_rom_mode");
    static int sAospRomMode = -1;

    public static String getSdcardPath(boolean isInternal) {
        if (isInternal) {
            // internal sdcard path is fixed /mnt/sdcard
            return "/mnt/sdcard";
        } else {
            // external sdcard path search
            File file = new File("/mnt/emmc"); // aosp gingerbread
            if (file.exists()) {
                return "/mnt/emmc";
            } else {
                file = new File("/mnt/extsdcard"); // aosp ics
                if (file.exists()) {
                    return "/mnt/extsdcard";
                }
            }
            return "/mnt/sdcard/external_sd"; // samsung gb/ics
        }
    }

    public static String getDateString() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final int second = calendar.get(Calendar.SECOND);
        return String.format("%04d-%02d-%02d-%02d.%02d.%02d", year, month, day, hour, minute, second); 
    }

    public static boolean isNullOfEmpty(String value) {
        return (value == null || "".equals(value));
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i < children.length; i++) {
                boolean ret = deleteDir(new File(dir, children[i]));
                if (!ret) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static String getCurrentValueText(Context context, String value) {
        return context.getText(R.string.current_value) + " " + value;
    }

    public static String getCurrentValueText(Context context, int resId) {
        return context.getText(R.string.current_value) + " " + context.getText(resId);
    }

    public static String getCurrentAndSavedValueText(Context context, String curValue, String savedValue) {
        return context.getText(R.string.current_value) + " " + curValue + " " +
                context.getText(R.string.saved_value) + " " + ((savedValue == null) ? context.getText(R.string.none): savedValue);
    }

    public static void sleep(long msec) {
        try {
            Thread.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAospRom() {
        if (sAospRomMode == -1) {
            String value = sSysFsAospRom.read();
            sAospRomMode = ("1".equals(value)) ? 1 : 0;
        }
        return (sAospRomMode == 1) ? true : false;
    }
    
    public static String getVersionName(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 1).versionName;
        } catch (NameNotFoundException e) {
            version = "";
        }
        return version;
    }
    
    public static void confirmReboot(Context context, int message) {
        final ConfirmAlertDialog dlg = new ConfirmAlertDialog(context);
        dlg.setResultListener(new ConfirmAlertDialog.ResultListener() {
            @Override
            public void onYes() {
                SystemCommand.reboot(null);
            }
        });
        dlg.show(context, android.R.string.dialog_alert_title, message);
    }

    public static final int KERNEL_VER_3_0_0 = 3000;
    public static final int KERNEL_VER_2_6_0 = 2600;

    public static int getKernelVersion() {
        String ret = SystemCommand.uname("-r");
        String[] ver = ret.substring(0, ret.indexOf('-')).split("\\.");
        return (Integer.valueOf(ver[0]) * 1000 +
                 Integer.valueOf(ver[1]) * 100 +
                 Integer.valueOf(ver[2]));
    }
    
    public static String[] getFreqencyEntries(String[] frequencyValues) {
        ArrayList<String> list = new ArrayList<String>();
        for (String freq : frequencyValues) {
            list.add(String.valueOf(Integer.parseInt(freq) / 1000) + "MHz");
        }
        return list.toArray(new String[0]);
    }

    public static String getEntryFromEntryValue(String[] entries, String[] entryValues, String value) {
        for (int i = 0; i < entries.length; i++) {
            if (entryValues[i].equals(value)) {
                return entries[i];
            }
        }
        return value; // if not found value, return safe value.
    }

    public static String getEntryFromEntryValue(CharSequence[] entries, CharSequence[] entryValues, String value) {
        for (int i = 0; i < entries.length; i++) {
            if (entryValues[i].toString().equals(value)) {
                return entries[i].toString();
            }
        }
        return value; // if not found value, return safe value.
    }
}
