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
