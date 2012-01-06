package net.sakuramilk.TweakGS2.Common;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.http.protocol.HTTP;

public class RuntimeExec {
    
    public static String[] execute(String command, boolean needResponce) {
        try {
            String[] cmd = { "/sbin/sh", "-c", command };
            Process process = Runtime.getRuntime().exec(cmd);
            if (needResponce) {
                DataInputStream inputStream = new DataInputStream(process.getInputStream());
                if (inputStream != null) {
                    String ret = "";
                    int size = 0;
                    byte[] buffer = new byte[1024];
                    try {
                        do {
                            size = inputStream.read(buffer);
                            if (size > 0) {
                                ret += new String(buffer, 0, size, HTTP.UTF_8);
                            }
                        } while (inputStream.available() > 0);
                    } catch (IOException e) {
                    }
                    return ret.split("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
