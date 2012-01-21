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

package net.sakuramilk.TweakGS2.Common;

import java.io.DataInputStream;
import java.io.IOException;

import org.apache.http.protocol.HTTP;

public class RuntimeExec {

    public static String[] execute(String[] commands, boolean needResponce) {
        try {
            Process process = Runtime.getRuntime().exec(commands);
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

    public static String[] execute(String shell, String command, boolean needResponce) {
        String[] commands = { shell, "-c", command };
        return execute(commands, needResponce);
    }

    public static String[] execute(String command, boolean needResponce) {
        return execute("/system/bin/sh", command, needResponce);
    }
}
