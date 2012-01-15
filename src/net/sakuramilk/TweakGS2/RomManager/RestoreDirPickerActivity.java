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

package net.sakuramilk.TweakGS2.RomManager;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.util.Log;
import net.sakuramilk.TweakGS2.R;
import net.sakuramilk.TweakGS2.Common.Misc;
import net.sakuramilk.TweakGS2.Common.SystemCommand;
import net.sakuramilk.TweakGS2.Parts.FilePickerActivity;
import net.sakuramilk.TweakGS2.Parts.TextInputDialog;

public class RestoreDirPickerActivity extends FilePickerActivity {

    private static final String TAG = "DirPickerActivity";

    private final int ACTION_RENAME = 0;
    private final int ACTION_DELETE = 1;
    private File mSelectedDir;
    private Context mContext;
    private final FilePickerActivity mActivity = this;
    private boolean mNeedReload = false;

    @Override
    public void onFilePicked(String path, String mode) {
        Log.i(TAG, "selected dir path = " + path);

        mContext = this;

        if ("restore".equals(mode)) {
            final File file = new File(path);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(file.getName());
            alertDialogBuilder.setMessage(R.string.do_restore);
            alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SystemCommand.restore_rom(file.getPath());
                    SystemCommand.reboot("recovery");
                }
            });
            alertDialogBuilder.setNegativeButton(android.R.string.no, null);
            alertDialogBuilder.create().show();
        } else if ("manage".equals(mode)) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            final CharSequence[] actions = { getText(R.string.backup_manage_rename),
                                              getText(R.string.backup_manage_delete) };

            mSelectedDir = new File(path);
            alertDialogBuilder.setTitle(mSelectedDir.getName());
            alertDialogBuilder.setItems(actions, mOnClickListener);
            alertDialogBuilder.create().show();
        }
    }

    private final DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            final String dirName = mSelectedDir.getName();
            final String dirPath = mSelectedDir.getPath();
            final String dirParent = mSelectedDir.getParent();

            switch (which) {
                case ACTION_RENAME:
                    {
                        final TextInputDialog dlg = new TextInputDialog(mContext, android.R.string.ok, android.R.string.cancel);
                        dlg.setFinishTextInputListener(new TextInputDialog.FinishTextInputListener() {
                            public void onFinishTextInput(CharSequence input) {
                                String inputName = input.toString();
                                inputName = inputName.replace("\n", "").trim();
                                File file = new File(dirPath);
                                File dstFile = new File(dirParent + "/" + inputName);
                                file.renameTo(dstFile);
                                mNeedReload = true;
                            }
                        });
                        dlg.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (mNeedReload) {
                                    mNeedReload = false;
                                    mActivity.reload();
                                }
                            }
                        });
                        dlg.show(R.string.backup_manage_rename, R.string.backup_manage_rename_message, dirName);
                    }
                    break;
                case ACTION_DELETE:
                    {
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setTitle(R.string.backup_manage_delete);
                        alertDialogBuilder.setMessage(R.string.backup_manage_delete_confirm);
                        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(dirPath);
                                Misc.deleteDir(file);
                                mNeedReload = true;
                            }
                        });
                        alertDialogBuilder.setNegativeButton(android.R.string.no, null);
                        AlertDialog dlg = alertDialogBuilder.create();
                        dlg.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                if (mNeedReload) {
                                    mNeedReload = false;
                                    mActivity.reload();
                                }
                            }
                        });
                        dlg.show();
                    }
                    break;
            }
        }
    };
}
