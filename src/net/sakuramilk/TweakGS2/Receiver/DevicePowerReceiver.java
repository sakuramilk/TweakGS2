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

import net.sakuramilk.util.SystemCommand;
import net.sakuramilk.TweakGS2.Dock.DockSetting;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DevicePowerReceiver extends BroadcastReceiver {

    private static final String TAG = "TweakGS2::DevicePowerReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {  
        Log.d(TAG, "onReceive");

        DockSetting dockSetting = new DockSetting(context);

        String savedValue = dockSetting.loadDockEmulate();
        if (DockSetting.DOCK_EMU_HW_INDEX.equals(savedValue) ||
            DockSetting.DOCK_EMU_DISABLE_INDEX.equals(savedValue)) {
            return;
        }

        String curValue = dockSetting.getDockEmulate();
        Log.d(TAG, "currentValue=" + curValue + ", savedValue=" + savedValue);
        if (DockSetting.DOCK_EMU_SW_VALUE.equals(curValue)) {    
            if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                Log.d(TAG, "dock mode start");
                SystemCommand.start_dock();
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                Log.d(TAG, "dock mode end");
                SystemCommand.stop_dock();
            }
        }
    }
}
