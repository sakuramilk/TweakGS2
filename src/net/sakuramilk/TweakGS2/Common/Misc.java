/*
 * Copyright (C) 2011 sakuramilk <c.sakuramilk@gmail.com>
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
import java.util.Calendar;

import android.content.Context;

import net.sakuramilk.TweakGS2.R;

public class Misc {
    
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
    
    public static void sleep(long msec) {
        try {
            Thread.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
