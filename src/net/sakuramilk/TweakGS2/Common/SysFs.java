package net.sakuramilk.TweakGS2.Common;

import java.io.File;

public class SysFs {
    
    private File mFile;
    
    public SysFs(String path) {
        mFile = new File(path);
    }
    
    public boolean isExist() {
        return mFile.exists();
    }
    
    public String[] read() {
        String command = "cat " + mFile.getPath() + "\n";
        if (!mFile.canRead()) {
            RootProcess process = new RootProcess();
            process.init();
            process.write(command);
            String[] ret = process.read();
            process.term();
            return ret;
        } else {
            return RuntimeExec.execute(command, true);
        }
    }
    
    public void write(String data) {
        String command = "echo " + data + " > " + mFile.getPath() + "\n";
        if (!mFile.canWrite()) {
            RootProcess process = new RootProcess();
            process.init();
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