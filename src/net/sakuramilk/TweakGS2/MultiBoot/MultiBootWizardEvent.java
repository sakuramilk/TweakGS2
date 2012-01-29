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

package net.sakuramilk.TweakGS2.MultiBoot;

public class MultiBootWizardEvent {

    public static final int EVENT_REQUEST_MODE = 0;
    public static final int EVENT_RESULT_MODE = 1;

    public static final int EVENT_REQUEST_SYSTEM_IMG_PATH = 2;
    public static final int EVENT_RESULT_SYSTEM_IMG_SIZE = 3;

    public static final int EVENT_REQUEST_DATA_IMG_PATH = 4;
    public static final int EVENT_RESULT_DATA_IMG_SIZE = 5;

    public static final int EVENT_RESULT_SYSTEM_IMG_PATH = 6;
    public static final int EVENT_RESULT_DATA_IMG_PATH = 7;

    public static final int EVENT_REQUEST_ZIP_PATH = 8;
    public static final int EVENT_RESULT_ZIP_PATH = 9;

    public static final int EVENT_REQUEST_LABEL = 10;
    public static final int EVENT_RESULT_LABEL = 11;
}
