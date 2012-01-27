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

import net.sakuramilk.TweakGS2.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends DialogPreference
    implements SeekBar.OnSeekBarChangeListener, OnClickListener {

    private static final String TAG = "SeekBarPreference";
    private SeekBar mSeekBar;
    private TextView mTextView;
    private Button mButtonUp1;
    private Button mButtonUp50;
    private Button mButtonDown1;
    private Button mButtonDown50;
    private int mPreValue = 0;
    private int mValue = 0;
    private int mMax;
    private int mMin;
    private String mUnit = "";
    private int mStep = 0;
    private int mValueScale;

    public interface OnSeekBarPreferenceDoneListener {
        boolean onPreferenceDone(Preference preference, String newValue);
    }

    OnSeekBarPreferenceDoneListener mOnDoneListener = null;

    public void setOnPreferenceDoneListener(OnSeekBarPreferenceDoneListener onPreferenceDoneListener) {
        mOnDoneListener = onPreferenceDoneListener;
    }

    protected boolean callDoneListener(String objValue) {
        return mOnDoneListener == null ? true : mOnDoneListener.onPreferenceDone(this, objValue);
    }

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.seekbar_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    public void setValue(int max, int min, int value) {
        setValue(max, min, 0, value);
    }

    public void setValue(int max, int min, int step, int value) {
        mMax = max;
        mMin = min;
        if (mStep == 0) {
            mStep = 1;
        }
        mValue = value;
        mPreValue = value;
    }

    public void updateValue(int value) {
        mValue = value;
        mPreValue = value;
    }

    public void setUnit(String unit) {
        mUnit = unit;
    }
    
    public String getUnit() {
        return mUnit;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mSeekBar = getSeekBar(view);
        mSeekBar.setKeyProgressIncrement(mStep);
        mSeekBar.setMax(mMax - mMin);
        mSeekBar.setProgress(mValue - mMin);
        mSeekBar.setOnSeekBarChangeListener(this);
        mTextView = getTextView(view);
        mTextView.setText(String.valueOf(mValue) + mUnit);

        if (mSeekBar.getMax() > 300) {
            mValueScale = 50;
        } else {
            mValueScale = 5;
        }

        mButtonUp1 = (Button)view.findViewById(R.id.buttonUp1);
        mButtonUp1.setOnClickListener(this);
        mButtonUp50 = (Button)view.findViewById(R.id.buttonUp50);
        mButtonUp50.setText("+" + Integer.toString(mValueScale));
        mButtonUp50.setOnClickListener(this);
        mButtonDown1 = (Button)view.findViewById(R.id.buttonDown1);
        mButtonDown1.setOnClickListener(this);
        mButtonDown50 = (Button)view.findViewById(R.id.buttonDown50);
        mButtonDown50.setText("-" + Integer.toString(mValueScale));
        mButtonDown50.setOnClickListener(this);
    }

    protected static SeekBar getSeekBar(View dialogView) {
        return (SeekBar)dialogView.findViewById(R.id.seekbar);
    }

    protected static TextView getTextView(View dialogView) {
        return (TextView)dialogView.findViewById(R.id.textView);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String key = this.getKey();
            String value = String.valueOf(mValue);
            Log.v(TAG, "callDoneListener(" + value + ")");
            if (callDoneListener(value)) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getContext());
                Editor ed = settings.edit();
                ed.putString(key, value);
                ed.commit();
            }
        } else {
            mValue = mPreValue;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // noop
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        // noop
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mValue = progress + mMin;
        String value = String.valueOf(mValue);
        mTextView.setText(value + mUnit);
        callChangeListener(value);
    }

    public void onClick(View v) {
        int i, max;
        i = mSeekBar.getProgress();
        max = mSeekBar.getMax();
        if (v == mButtonUp1) {
            if (i < max) {
                mSeekBar.setProgress(i + 1);
            }
        } else if (v == mButtonUp50) {
            if ((i + mValueScale) > max) {
                mSeekBar.setProgress(max);
            } else {
                mSeekBar.setProgress(i + mValueScale);
            }
        } else if (v == mButtonDown1) {
            if (i > 0) {
                mSeekBar.setProgress(i - 1);
            }
        } else if (v == mButtonDown50) {
            if ((i - mValueScale) > 0) {
                mSeekBar.setProgress(i - mValueScale);
            } else {
                mSeekBar.setProgress(0);
            }
        }
    }
}
