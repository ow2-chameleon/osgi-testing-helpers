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

    public static final String KF = "knopflerfish";
    public static final String FELIX = "felix";
    public static final String PAX_EXAM_FRAMEWORK = "pax.exam.framework";
    public static final String EQUINOX = "equinox";

    public static boolean isKnopflerfish(BundleContext context) {
        if (context != null) {
            return context.getClass().toString().contains(KF);
        } else {
            String pf = System.getProperty(PAX_EXAM_FRAMEWORK);
            return pf != null && pf.equalsIgnoreCase(KF);
        }
    }

    public static boolean isFelix(BundleContext context) {
        if (context != null) {
            return context.getClass().toString().contains(FELIX);
        } else {
            String pf = System.getProperty(PAX_EXAM_FRAMEWORK);
            return pf != null && pf.equalsIgnoreCase(FELIX);
        }
    }

    public static boolean isEquinox(BundleContext context) {
        if (context != null) {
            return context.toString().contains(EQUINOX) || context.toString().contains("eclipse");
        } else {
            String pf = System.getProperty(PAX_EXAM_FRAMEWORK);
            return pf != null && pf.equalsIgnoreCase(EQUINOX);
        }
    }
}

