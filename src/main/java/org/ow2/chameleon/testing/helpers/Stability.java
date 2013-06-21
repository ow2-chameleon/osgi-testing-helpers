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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import static org.ow2.chameleon.testing.helpers.OSGiHelper.isFragment;

/**
 * Stability helper
 */
public class Stability {


    /**
     * Waits for stability:
     * <ul>
     * <li>all bundles are activated
     * <li>service count is stable
     * </ul>
     * If the stability can't be reached after a specified time,
     * the method throws a {@link IllegalStateException}.
     * @param context the bundle context
     * @throws IllegalStateException when the stability can't be reach after a several attempts.
     */
    public static void waitForStability(BundleContext context) throws IllegalStateException {
        waitForBundleStability(context);
        waitForServiceStability(context);
    }

    private static void waitForServiceStability(BundleContext context) {
        boolean serviceStability = false;
        int count = 0;
        int count1 = 0;
        int count2 = 0;
        while (! serviceStability && count < 500) {
            try {
                ServiceReference[] refs = context.getServiceReferences((String) null, null);
                count1 = refs.length;
                Thread.sleep(500 * TimeUtils.TIME_FACTOR);
                refs = context.getServiceReferences((String) null, null);
                count2 = refs.length;
                serviceStability = count1 == count2;
            } catch (Exception e) {
                System.err.println(e);
                serviceStability = false;
                // Nothing to do, while recheck the condition
            }
            count++;
        }

        if (count == 500) {
            System.err.println("Service stability isn't reached after 500 tries (" + count1 + " != " + count2);
            throw new IllegalStateException("Cannot reach the service stability");
        }
    }

    public static void waitForBundleStability(BundleContext context) {
        boolean bundleStability = getBundleStability(context);
        int count = 0;
        while (!bundleStability && count < 500) {
            try {
                Thread.sleep(100 * TimeUtils.TIME_FACTOR);
            } catch (InterruptedException e) {
                // Interrupted
            }
            count++;
            bundleStability = getBundleStability(context);
        }

        if (count == 500) {
            System.err.println("Bundle stability isn't reached after 500 tries");
            for (Bundle bundle : context.getBundles()) {
                System.out.println("Bundle " + bundle.getBundleId() + " - " + bundle.getSymbolicName() + " -> " +
                        bundle.getState());
            }
            throw new IllegalStateException("Cannot reach the bundle stability");
        }
    }

    /**
     * Are bundle stables.
     * @param bc the bundle context
     * @return <code>true</code> if every bundles are activated.
     */
    public static boolean getBundleStability(BundleContext bc) {
        boolean stability = true;
        Bundle[] bundles = bc.getBundles();
        for (Bundle bundle : bundles) {
            if (isFragment(bundle)) {
                stability = stability && (bundle.getState() == Bundle.RESOLVED);
            } else {
                stability = stability && (bundle.getState() == Bundle.ACTIVE);
            }
        }
        return stability;
    }

}
