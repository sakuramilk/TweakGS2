package net.sakuramilk.TweakGS2.Service;

import net.sakuramilk.TweakGS2.BusControl.BusControlSetting;
import net.sakuramilk.util.Convert;
import net.sakuramilk.util.RootProcess;
import net.sakuramilk.util.SysFs;
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
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BootCompletedService extends Service {

    private static final String TAG = "TweakGS2::BootCompletedService";
    private static Context mContext;
    private static BootCompletedThread mThread;
    private static final SysFs mSafeMode = new SysFs("/proc/sys/kernel/safe_mode");
    private static final SysFs mBootCompleted = new SysFs("/proc/sys/kernel/boot_completed");

    class BootCompletedThread extends Thread {
        public void run() {
            RootProcess rootProcess = new RootProcess();
            Log.d(TAG, "Root init s");
            rootProcess.init();
            Log.d(TAG, "Root init e");

            // check safe mode
            if (mSafeMode.exists()) {
            	if (Convert.toBoolean(mSafeMode.read(rootProcess))) {
            		Log.i(TAG, "Safe mode");
            		return;
            	}
            }

            if (mBootCompleted.exists()) {
            	if (Convert.toBoolean(mBootCompleted.read(rootProcess))) {
            		Log.d(TAG, "Already initialized");
            		return;
            	}
            }

            // General
            GeneralSetting generalSetting = new GeneralSetting(mContext, rootProcess);
            Log.d(TAG, "start: General Setting");
            generalSetting.setOnBoot();
            LowMemKillerSetting lowMemKillerSetting = new LowMemKillerSetting(mContext, rootProcess);
            Log.d(TAG, "start: LowMemKiller Setting");
            lowMemKillerSetting.setOnBoot();
            VirtualMemorySetting vmSetting = new VirtualMemorySetting(mContext, rootProcess);
            Log.d(TAG, "start: VirtualMemory Setting");
            vmSetting.setOnBoot();
    
            // CpuControl
            CpuControlSetting cpuControlSetting = new CpuControlSetting(mContext, rootProcess);
            Log.d(TAG, "start: CpuControl Setting");
            cpuControlSetting.setOnBoot();

            // BusControl
            BusControlSetting busControlSetting = new BusControlSetting(mContext, rootProcess);
            Log.d(TAG, "start: BusControl Setting");
            busControlSetting.setOnBoot();

            // GpuControl
            GpuControlSetting gpuControlSetting = new GpuControlSetting(mContext, rootProcess);
            Log.d(TAG, "start: GpuControl Setting");
            gpuControlSetting.setOnBoot();
    
            // SoundAndVib
            SoundAndVibSetting soundAndVibSetting = new SoundAndVibSetting(mContext, rootProcess);
            Log.d(TAG, "start: SoundAndVib Setting");
            soundAndVibSetting.setOnBoot();
            HwVolumeSetting hwVolumeSetting = new HwVolumeSetting(mContext, rootProcess);
            Log.d(TAG, "start: HwVolume Setting");
            hwVolumeSetting.setOnBoot();
    
            // Notification
            NotificationSetting notifySetting = new NotificationSetting(mContext, rootProcess);
            Log.d(TAG, "start: Notification Setting");
            notifySetting.setOnBoot();
    
            // Dock
            DockSetting dockSetting = new DockSetting(mContext, rootProcess);
            Log.d(TAG, "start: Dock Setting");
            dockSetting.setOnBoot();
    
            // Display
            DisplaySetting displaySetting = new DisplaySetting(mContext, rootProcess);
            Log.d(TAG, "start: Display Setting");
            displaySetting.setOnBoot();

            if (mBootCompleted.exists()) {
            	mBootCompleted.write("1", rootProcess);
            }

            rootProcess.term();
            rootProcess = null;
        }
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int StartId) {
        Log.d(TAG , "OnStart");
        mContext = this;
        mThread = new BootCompletedThread();
        mThread.start();
        try {
            mThread.join();
        } catch (InterruptedException e) {
        }
        Log.d(TAG , "OnStart stopSelf");
        this.stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG , "OnDestroy");
        mThread.interrupt();
        mThread = null;
    }
}
