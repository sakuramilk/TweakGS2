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
        process.write("echo \"install_zip(\\\"" + targetZip + "\\\");\" > /cache/recovery/extendedcommand\n");
        process.term();
    }

    public static void backup_rom(String targetDir) {
        Log.d(TAG, "execute backup_rom target=" + targetDir);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("echo \"backup_rom(\\\"" + targetDir + "\\\");\" >> /cache/recovery/extendedcommand\n");
        process.term();
    }

    public static void restore_rom(String targetDir) {
        Log.d(TAG, "execute restore_rom target=" + targetDir);

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("echo \"restore_rom(\\\"" + targetDir + "\\\");\" > /cache/recovery/extendedcommand\n");
        process.term();
    }

    public static void start_dock() {
        String[] commands = { "su", "-c",
                "am broadcast -a android.intent.action.DOCK_EVENT --ei android.intent.extra.DOCK_STATE 1\n" };
        RuntimeExec.execute(commands, false);
    }

    public static void stop_dock() {
        String[] commands = { "su", "-c",
                "am broadcast -a android.intent.action.DOCK_EVENT --ei android.intent.extra.DOCK_STATE 0\n" };
        RuntimeExec.execute(commands, false);
    }

    public static void mount(String format, String device, String mountPoint) {
        String[] commands = { "su", "-c",
                "mount -t " + format + " -o rw " + device + " " + mountPoint + "\n" };
        RuntimeExec.execute(commands, false);
    }

    public static void umount(String mountPoint) {
        String[] commands = { "su", "-c",
            "umount " + mountPoint + "\n" };
        RuntimeExec.execute(commands, false);
    }

    public static void gsm_network_tweak() {
        Log.d(TAG, "execute gsm_network_tweak");

        RootProcess process = new RootProcess();
        if (!process.init()) {
            return;
        }
        process.write("echo 0 > /proc/sys/net/ipv4/tcp_ecn");
        process.write("echo 1 > /proc/sys/net/ipv4/route/flush");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_rfc1337");
        process.write("echo 0 > /proc/sys/net/ipv4/ip_no_pmtu_disc");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_sack");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_fack");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_window_scaling");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_timestamps");
        process.write("echo 4096 39000 187000 > /proc/sys/net/ipv4/tcp_rmem");
        process.write("echo 4096 39000 187000 > /proc/sys/net/ipv4/tcp_wmem");
        process.write("echo 187000 187000 187000 > /proc/sys/net/ipv4/tcp_mem");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_no_metrics_save");
        process.write("echo 1 > /proc/sys/net/ipv4/tcp_moderate_rcvbuf");
        process.term();
    }
}
