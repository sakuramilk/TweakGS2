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

package net.sakuramilk.TweakGS2.Receiver;

import net.sakuramilk.TweakGS2.CpuControl.CpuControlSetting;
import net.sakuramilk.TweakGS2.Display.DisplaySetting;
import net.sakuramilk.TweakGS2.Dock.DockSetting;
import net.sakuramilk.TweakGS2.General.GeneralSetting;
import net.sakuramilk.TweakGS2.General.LowMemKillerSetting;
import net.sakuramilk.TweakGS2.General.VirtualMemorySetting;
import net.sakuramilk.TweakGS2.GpuControl.GpuControlSetting;
import net.sakuramilk.TweakGS2.Notification.NotificationSetting;
import net.sakuramilk.TweakGS2.SoundAndVib.HwVolumeSetting;
import net.sakuramilk.TweakGS2.SoundAndVib.SoundAndVibSetting;
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
            // check safe mode

            // General
            GeneralSetting generalSetting = new GeneralSetting(context);
            Log.d(TAG, "start: General Setting");
            generalSetting.setOnBoot();
            LowMemKillerSetting lowMemKillerSetting = new LowMemKillerSetting(context);
            Log.d(TAG, "start: LowMemKiller Setting");
            lowMemKillerSetting.setOnBoot();
            VirtualMemorySetting vmSetting = new VirtualMemorySetting(context);
            Log.d(TAG, "start: VirtualMemory Setting");
            vmSetting.setOnBoot();

            // CpuControl
            CpuControlSetting cpuControlSetting = new CpuControlSetting(context);
            Log.d(TAG, "start: CpuControl Setting");
            cpuControlSetting.setOnBoot();

            // GpuControl
            GpuControlSetting gpuControlSetting = new GpuControlSetting(context);
            Log.d(TAG, "start: GpuControl Setting");
            gpuControlSetting.setOnBoot();

            // SoundAndVib
            SoundAndVibSetting soundAndVibSetting = new SoundAndVibSetting(context);
            Log.d(TAG, "start: SoundAndVib Setting");
            soundAndVibSetting.setOnBoot();
            HwVolumeSetting hwVolumeSetting = new HwVolumeSetting(context);
            Log.d(TAG, "start: HwVolume Setting");
            hwVolumeSetting.setOnBoot();

            // Display
            DisplaySetting displaySetting = new DisplaySetting(context);
            Log.d(TAG, "start: Display Setting");
            displaySetting.setOnBoot();

            // Notification
            NotificationSetting notifySetting = new NotificationSetting(context);
            Log.d(TAG, "start: Notification Setting");
            notifySetting.setOnBoot();

            // Dock
            DockSetting dockSetting = new DockSetting(context);
            Log.d(TAG, "start: Dock Setting");
            dockSetting.setOnBoot();
        }
    }
}
