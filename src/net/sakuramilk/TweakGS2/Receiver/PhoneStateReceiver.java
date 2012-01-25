/*
* Copyright 2011 sakuramilk <c.sakuramilk@gmail.com>
* 
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License as
* published by the Free Software Foundation; either version 2 of 
* the License, or (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.sakuramilk.TweakGS2.Receiver;

import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Notification.KeyBacklightFlashService;
import net.sakuramilk.TweakGS2.Notification.NotificationSetting;
import net.sakuramilk.TweakGS2.SoundAndVib.SoundAndVibSetting;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        final String extraState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.d(TAG, "onReceive state=" + extraState);
        
        final NotificationSetting notificationSetting = new NotificationSetting(context);
        final boolean isBacklightFlash = notificationSetting.loadBacklightFlashOnIncoming();

        final SoundAndVibSetting soundAndVibSetting = new SoundAndVibSetting(context);
        final String vibNormalLevel = soundAndVibSetting.loadVibNormalLevel();
        final String vibIncomingLevel = soundAndVibSetting.loadVibIncomingLevel();

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(extraState)) {
            if (isBacklightFlash) {
                Log.d(TAG, "start service");
                Intent serviceIntent = new Intent(context, KeyBacklightFlashService.class);
                context.startService(serviceIntent);
            }

            if (!Misc.isNullOfEmpty(vibNormalLevel) && !Misc.isNullOfEmpty(vibIncomingLevel)) {
                if (!vibNormalLevel.equals(vibIncomingLevel)) {
                    soundAndVibSetting.setVibLevel(vibIncomingLevel);
                }
            }

        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(extraState) ||
                    TelephonyManager.EXTRA_STATE_IDLE.equals(extraState)) {
            if (isBacklightFlash) {
                Log.d(TAG, "stop service");
                Intent serviceIntent = new Intent(context, KeyBacklightFlashService.class);
                context.stopService(serviceIntent);
            }

            if (!Misc.isNullOfEmpty(vibNormalLevel) && !Misc.isNullOfEmpty(vibIncomingLevel)) {
                if (!vibNormalLevel.equals(vibIncomingLevel)) {
                    soundAndVibSetting.setVibLevel(vibNormalLevel);
                }
            }
        }
    }
}
