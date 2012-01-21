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

package net.sakuramilk.TweakGS2.Parts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmAlertDialog extends AlertDialog {

    private ResultListener mListener = null;

    public interface ResultListener {
        public void onYes();
    }

    public ConfirmAlertDialog(Context context) {
        super(context);

        setIcon(android.R.drawable.ic_dialog_info);
        setButton(BUTTON_POSITIVE, context.getText(android.R.string.yes), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                okButtonPressed();
            }
        });
        setButton(BUTTON_NEGATIVE, context.getText(android.R.string.no), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }

    public void show(Context parent, int title, int message) {
        show(parent, parent.getText(title), parent.getText(message));
    }

    public void show(Context parent, CharSequence title, CharSequence message) {
        setTitle(title);
        setMessage(message);
        show();
    }
    
    public void setResultListener(ResultListener listener) {
        mListener = listener;
    }

    private void okButtonPressed() {
        dismiss();
        if (mListener != null)
                mListener.onYes();
    }
}