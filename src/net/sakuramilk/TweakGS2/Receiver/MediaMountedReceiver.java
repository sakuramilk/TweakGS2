package net.sakuramilk.TweakGS2.Receiver;

import net.sakuramilk.TweakGS2.General.GeneralSetting;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MediaMountedReceiver extends BroadcastReceiver {  

    private static final String TAG = "TweakGS2::MediaScannerReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {  
        if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
        	Log.d(TAG, "receive ACTION_MEDIA_MOUNTED");
        	GeneralSetting setting = new GeneralSetting(context);
        	setting.setOnMediaMounted();
        }
    }
}