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
