/*
 * Copyright 2014 OW2 Chameleon
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ow2.chameleon.testing.helpers;

/**
 * Handle time
 */
public class TimeUtils {

    public static int TIME_FACTOR = 1;

    /**
     * Waits the specified time.
     * The waited time is: TIME_FACTOR * time.
     * @param time the time
     */
    public static void grace(int time) {
        try {
            if (TIME_FACTOR <= 0) {
                Thread.sleep(time);
            } else {
                Thread.sleep(time * TIME_FACTOR);
            }
        } catch (InterruptedException e) {
            // Ignore it.
        }
    }
}
