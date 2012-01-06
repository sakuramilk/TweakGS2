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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.http.protocol.HTTP;

public class RootProcess {

    private Process mProcess = null;
    private DataOutputStream mOutputStream = null;
    private DataInputStream mInputStream = null;

    public boolean init() {
        try {
            mProcess = Runtime.getRuntime().exec("su");
            mOutputStream = new DataOutputStream(mProcess.getOutputStream());
            mInputStream = new DataInputStream(mProcess.getInputStream());

            if (write("su -v\n")) {
                String[] results = read();
                for (String line : results) {
                    if (line.length() > 0) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
        }
        return false;
    }

    public void term() {
        if (mInputStream != null) {
            try {
                mInputStream.close();
            } catch (IOException e) {
            }
        }
        
        if (mOutputStream != null) {
            try {
                if (mProcess != null) {
                    mOutputStream.writeBytes("exit\n");
                    mOutputStream.flush();
                    try {
                        mProcess.waitFor();
                    } catch (InterruptedException e) {
                    }
                }
                mOutputStream.close();
            } catch (IOException e) {
            }
        }

        if(mProcess != null){
            mProcess.destroy();
        }

        mOutputStream = null;
        mInputStream = null;
        mProcess = null;
    }

    public String[] read() {
        if (mInputStream != null) {
            String ret = "";
            int size = 0;
            byte[] buffer = new byte[1024];
            try {
                do {
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        ret += new String(buffer, 0, size, HTTP.UTF_8);
                    }
                } while (mInputStream.available() > 0);
            } catch (IOException e) {
            }
            return ret.split("\n");
        }
        return null;
    }

    public boolean write(String command) {
        if (mOutputStream != null) {
            try {
                mOutputStream.writeBytes(command);
                mOutputStream.flush();
                return true;
            } catch (IOException e) {
            }
        }
        return false;
    }
}
