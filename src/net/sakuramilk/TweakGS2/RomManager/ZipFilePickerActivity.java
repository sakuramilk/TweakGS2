package net.sakuramilk.TweakGS2.RomManager;

import java.io.File;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.SystemCommand;
import net.sakuramilk.TweakGS2.Parts.FilePickerActivity;

public class ZipFilePickerActivity extends FilePickerActivity {

    private static final String TAG = "ZipFilePickerActivity";

    @Override
    public void onFilePicked(String path, String mode) {
        Log.i(TAG, "selected zip file path = " + path);
        //final Activity activity = this;
        final File file = new File(path);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(file.getName());
        alertDialogBuilder.setMessage(R.string.do_install_zip);
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SystemCommand.install_zip(file.getPath());
                SystemCommand.reboot("recovery");
                //activity.finish();
            }
        });
        alertDialogBuilder.setNegativeButton(android.R.string.no, null);
        alertDialogBuilder.create().show();
    }
}
