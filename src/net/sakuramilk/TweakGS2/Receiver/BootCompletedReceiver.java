/*
 * Copyright (C) 2011 sakuramilk
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

package net.sakuramilk.TweakGS2.Receiver;

import net.sakuramilk.TweakGS2.CpuControl.CpuControlSetting;
import net.sakuramilk.TweakGS2.Dock.DockSetting;
import net.sakuramilk.TweakGS2.SoundAndVib.HwVolumeSetting;
import net.sakuramilk.TweakGS2.SoundAndVib.VibSetting;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {  

    private static final String TAG = "BootCompletedReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {  
        Log.d(TAG, "onReceive");
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //GeneralSetting.setOnBoot(sharedPreferences);

            CpuControlSetting cpuControlSetting = new CpuControlSetting(context);
            cpuControlSetting.setOnBoot();

            CpuControlSetting gpuControlSetting = new CpuControlSetting(context);
            gpuControlSetting.setOnBoot();

            //DisplaySettingManager.setOnBoot(sharedPreferences);

            HwVolumeSetting hwVolumeSetting = new HwVolumeSetting(context);
            hwVolumeSetting.setOnBoot();

            VibSetting vibSetting = new VibSetting(context);
            vibSetting.setOnBoot();

            DockSetting dockSetting = new DockSetting(context);
            dockSetting.setOnBoot();

            //NotificationSettingManager.setOnBoot();
        }
    }
}
