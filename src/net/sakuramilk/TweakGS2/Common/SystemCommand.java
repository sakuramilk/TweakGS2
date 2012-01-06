package net.sakuramilk.TweakGS2.Common;

import android.util.Log;

public class SystemCommand {

    private static final String TAG = "SystemCommand";

    public static void reboot(String action) {
        Log.d(TAG, "execute reboot action=" + action);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }

        process.write("sync\n");
        process.write("sync\n");
        process.write("sync\n");
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        
        if ("recovery".equals(action) || "download".equals(action)) {
            process.write("reboot " + action + "\n");
        } else {
            process.write("reboot\n");
        }
        
        process.term();
    }

    public static void install_zip(String targetZip) {
        Log.d(TAG, "execute install_zip target=" + targetZip);
        
        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("echo \"install_zip(\\\"" + targetZip + "\\\");\" > /cache/recovery/extendedcommand");
        process.term();
    }

    public static void backup_rom(String targetDir) {
        Log.d(TAG, "execute backup_rom target=" + targetDir);
        
        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("echo \"backup_rom(\\\"" + targetDir + "\\\");\" >> /cache/recovery/extendedcommand");
        process.term();
    }

    public static void restore_rom(String targetDir) {
        Log.d(TAG, "execute restore_rom target=" + targetDir);
        
        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("echo \"restore_rom(\\\"" + targetDir + "\\\");\" > /cache/recovery/extendedcommand");
        process.term();
    }
}
