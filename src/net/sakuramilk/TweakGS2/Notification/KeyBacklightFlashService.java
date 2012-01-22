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

package net.sakuramilk.TweakGS2.Notification;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class KeyBacklightFlashService extends Service {

    private static Context mContext;
    private static BacklightThread mThread;
    private static final String TAG = "KeyBacklightFlashService";

    class BacklightThread extends Thread {
        private boolean mRunning = false;
        private final int[] pattern = { 200, 90, 200, 90, 200, 90, 200, 760 };
        private NotificationSetting mSetting;

        public void run() {
            mRunning = true;
            mSetting = new NotificationSetting(mContext);

            try {
                Thread.sleep(300);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            while (mRunning) {
                try {
                    int on = 2;
                    for (int t : pattern) {
                        on = (on == 2) ? 1 : 2;
                        mSetting.setBlnControl(String.valueOf(on));
                        Thread.sleep(t);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // turn off before exit
            mSetting.setBlnControl("1");
            
            
            Log.d(TAG, "Thread exit");
        }

        public void exit() {
            mRunning = false;
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int StartId) {
        Log.d(TAG , "OnStart");
        mContext = this;
        mThread = new BacklightThread();
        mThread.start();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG , "OnDestroy");
        mThread.exit();
        mThread.interrupt();
    }
}
