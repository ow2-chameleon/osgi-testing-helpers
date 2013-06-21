/*
 * Copyright 2009 OW2 Chameleon
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

import org.osgi.framework.BundleContext;

/**
 * Utility methods to detect frameworks
 */
public class FrameworkHelper {

    public static boolean isKnopflerfish(BundleContext bc) {
        return bc.getClass().toString().contains("knopflerfish");
    }

    public static boolean isFelix(BundleContext bc) {
        return bc.getClass().toString().contains("felix");
    }

    public static boolean isEquinox(BundleContext bc) {
        return bc.getClass().toString().contains("equinox");
    }
}
