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

public class SystemProperty extends PropertyManager {

    public SystemProperty() {
        super("/system/build.prop");
    }

    @Override
    public void setValue(String key, String value) {
        SystemCommand.remount_system_rw();
        super.setValue(key, value);
        SystemCommand.remount_system_ro();
    }

    public void backup() {
        File file = new File("/system/build.prop.tg2.bk");
        if (!file.exists()) {
            SystemCommand.remount_system_rw();
            SystemCommand.copy("/system/build.prop", "/system/build.prop.tg2.bk");
            SystemCommand.remount_system_ro();
        }
    }

    public void restore() {
        SystemCommand.remount_system_rw();
        SystemCommand.move("/system/build.prop.tg2.bk", "/system/build.prop");
        SystemCommand.remount_system_ro();
    }
}
