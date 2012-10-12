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
import android.graphics.Color;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class TextInputDialog extends AlertDialog {

    private TextView mInputPrompt;
    private EditText mInputField;
    private FinishTextInputListener mListener = null;

    public interface FinishTextInputListener {
        public void onFinishTextInput(CharSequence input);
    }

    public TextInputDialog(Context context) {
        this(context, android.R.string.ok, android.R.string.cancel);
    }

    public TextInputDialog(Context context, int okBut, int cancelBut) {
    	this(context, context.getText(okBut), context.getText(cancelBut));
    }

    public TextInputDialog(Context context, CharSequence okBut, CharSequence cancelBut) {
        super(context);

        setIcon(android.R.drawable.ic_dialog_info);
        setView(createInputView(context));
        setButton(BUTTON_POSITIVE, okBut, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                okButtonPressed();
            }
        });
        setButton(BUTTON_NEGATIVE, cancelBut, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
    }

    private View createInputView(Context context) {
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);
        LayoutParams lp;

        mInputPrompt = new TextView(context);
        mInputPrompt.setGravity(Gravity.LEFT);
        mInputPrompt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mInputPrompt.setTextColor(Color.WHITE);
        mInputPrompt.setText("Enter text:");

        lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 20, 20, 10);
        view.addView(mInputPrompt, lp);

        mInputField = new EditText(context);
        mInputField.setHorizontallyScrolling(true);
        mInputField.setGravity(Gravity.FILL_HORIZONTAL);
        mInputField.setTextAppearance(context, android.R.attr.textAppearanceMedium);
        mInputField.setInputType(InputType.TYPE_CLASS_TEXT);
        mInputField.setImeOptions(EditorInfo.IME_ACTION_DONE);

        lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 10, 20, 20);
        view.addView(mInputField, lp);

        return view;
    }
    
    public EditText getInputField() {
    	return mInputField;
    }

    public void setFinishTextInputListener(FinishTextInputListener listener) {
        mListener = listener;
    }

    public void show(int title, int text) {
        show(title, text, "");
    }

    public void show(int title, int text, String dflt) {
        setTitle(title);
        mInputPrompt.setText(text);
        mInputField.setText(dflt);
        show();
    }

    public void show(String title, String text) {
        show(title, text, "");
    }

    public void show(String title, String text, String dflt) {
        setTitle(title);
        mInputPrompt.setText(text);
        mInputField.setText(dflt);
        show();
    }

    private void okButtonPressed() {
        dismiss();
        if (mListener != null)
            mListener.onFinishTextInput(mInputField.getText());
    }
}
