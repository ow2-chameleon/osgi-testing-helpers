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

import org.osgi.framework.BundleContext;

/**
 * Common helper code.
 */
public abstract class AbstractHelper {

    /**
     * The bundle context.
     */
    protected final BundleContext context;

    public AbstractHelper(BundleContext context) {
        this.context = context;
    }

    public abstract void dispose();

    /**
     * Gets the Bundle Context.
     *
     * @return the bundle context.
     */
    public BundleContext getContext() {
        return context;
    }

}
