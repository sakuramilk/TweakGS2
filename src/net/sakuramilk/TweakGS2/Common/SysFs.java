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

import java.io.File;

public class SysFs {

    private File mFile;
    private String mPermission;

    public SysFs(String path) {
        this(path, null);
    }

    public SysFs(String path, String permission) {
        mFile = new File(path);
        mPermission = permission;
    }

    public boolean exists() {
        return mFile.exists();
    }

    public String read() {
        String[] values = readMuitiLine();
        if (values != null) {
            return values[0];
        }
        return null;
    }

    public String[] readMuitiLine() {
        if (!mFile.exists()) {
            return null;
        }
        String command = "cat " + mFile.getPath() + "\n";
        if (!mFile.canRead()) {
            RootProcess process = new RootProcess();
            process.init();
            if (mPermission != null) {
                process.write("chmod " + mPermission + " " + mFile.getPath() + "\n");
            }
            process.write(command);
            String[] ret = process.read();
            process.term();
            return ret;
        } else {
            return RuntimeExec.execute(command, true);
        }
    }

    public void write(String data) {
        if (!mFile.exists()) {
            return;
        }
        String command = "echo " + data + " > " + mFile.getPath() + "\n";
        if (!mFile.canWrite()) {
            RootProcess process = new RootProcess();
            process.init();
            if (mPermission != null) {
                process.write("chmod " + mPermission + " " + mFile.getPath() + "\n");
            }
            process.write(command);
            process.term();
        } else {
            RuntimeExec.execute(command, false);
        }
    }

    public String getPath() {
        return mFile.getPath();
    }
}
