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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public abstract class SettingManager {

    private SharedPreferences mSharedPref;

    protected SettingManager(Context context) {
        mSharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    protected String getStringValue(String key) {
        return mSharedPref.getString(key, null);
    }

    protected String getStringValue(String key, String defaultValue) {
        return mSharedPref.getString(key, defaultValue);
    }

    protected boolean getBooleanValue(String key) {
        return mSharedPref.getBoolean(key, false);
    }

    protected boolean getBooleanValue(String key, boolean defaultValue) {
        return mSharedPref.getBoolean(key, defaultValue);
    }

    protected void setValue(String key, String value) {
        Editor ed = mSharedPref.edit();
        ed.putString(key, value);
        ed.commit();
    }

    protected void setValue(String key, boolean value) {
        Editor ed = mSharedPref.edit();
        ed.putBoolean(key, value);
        ed.commit();
    }

    protected void clearValue(String key) {
        Editor ed = mSharedPref.edit();
        ed.remove(key);
        ed.commit();
    }

    public abstract void setOnBoot();
    public abstract void setRecommend();
    public abstract void reset();
}
