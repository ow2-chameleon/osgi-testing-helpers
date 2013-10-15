package org.ow2.chameleon.testing.helpers;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Eases the manipulation of `bundle` objects.
 */
public class BundleHelper extends AbstractHelper {

    private List<Bundle> m_bundles = new ArrayList<Bundle>();

    public BundleHelper(BundleContext context) {
        super(context);
    }

    @Override
    public void dispose() {
        for (Bundle bundle : m_bundles) {
            try {
                bundle.uninstall();
            } catch (BundleException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Checks whether the given bundle is a fragment (or an extension).
     *
     * @param bundle the bundle to check
     * @return {@literal true} if bundle is a fragment, {@literal false} otherwise
     */
    public static boolean isFragment(Bundle bundle) {
        return bundle != null && bundle.getHeaders().get(Constants.FRAGMENT_HOST) != null;
    }


    /**
     * Installs a bundle.
     * Fails if the bundle cannot be installed.
     * Be aware that you have to uninstall the bundle yourself.
     *
     * @param url bundle url
     * @return the installed bundle
     */
    public Bundle install(String url) {
        try {
            Bundle bundle = context.installBundle(url);
            m_bundles.add(bundle);
            return bundle;
        } catch (BundleException e) {
            fail("Cannot install the bundle " + url + " : " + e.getMessage());
        }
        return null; // Can not happen
    }

    /**
     * Installs a bundle.
     * Fails if the bundle cannot be installed.
     * Be aware that you have to uninstall the bundle yourself.
     *
     * @param url    bundle url
     * @param stream input stream containing the bundle
     * @return the installed bundle
     */
    public Bundle install(String url, InputStream stream) {
        try {
            Bundle bundle = context.installBundle(url, stream);
            m_bundles.add(bundle);
            return bundle;
        } catch (BundleException e) {
            fail("Cannot install the bundle " + url + " : " + e.getMessage());
        }
        return null; // Can not happen
    }

    /**
     * Installs and starts a bundle.
     * Fails if the bundle cannot be installed or an error occurs
     * during startup. Be aware that you have to uninstall the bundle
     * yourself.
     *
     * @param url the bundle url
     * @return the Bundle object.
     */
    public Bundle installAndStart(String url) {
        Bundle bundle = install(url);
        try {
            bundle.start();
        } catch (BundleException e) {
            fail("Cannot start the bundle " + url + " : " + e.getMessage());
        }
        return bundle;
    }

    /**
     * Installs and starts a bundle.
     * Fails if the bundle cannot be installed or an error occurs
     * during startup. Be aware that you have to uninstall the bundle
     * yourself.
     *
     * @param url    the bundle url
     * @param stream input stream containing the bundle
     * @return the Bundle object.
     */
    public Bundle installAndStart(String url, InputStream stream) {
        Bundle bundle = install(url, stream);
        try {
            bundle.start();
        } catch (BundleException e) {
            fail("Cannot start the bundle " + url + " : " + e.getMessage());
        }
        return bundle;
    }

    /**
     * Get the bundle by its id.
     *
     * @param bundleId the bundle id.
     * @return the bundle with the given id.
     */
    public Bundle getBundle(long bundleId) {
        return context.getBundle(bundleId);
    }

    /**
     * Gets a bundle by its symbolic name.
     * Fails if no bundle matches.
     *
     * @param name the symbolic name of the bundle
     * @return the bundle object.
     */
    public Bundle getBundle(String name) {
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            if (name.equals(bundle.getSymbolicName())) {
                return bundle;
            }
        }
        fail("No bundles with the given symbolic name " + name);
        return null; // should not happen
    }

    /**
     * Uninstalls the given bundle.
     * @param bundle the bundle
     */
    public void uninstall(Bundle bundle) {
        try {
            bundle.uninstall();
            m_bundles.remove(bundle);
        } catch (BundleException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Stops the given bundle.
     * @param bundle the bundle
     */
    public void stop(Bundle bundle) {
        try {
            bundle.stop();
        } catch (BundleException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Starts the given bundle.
     * @param bundle the bundle
     */
    public void start(Bundle bundle) {
        try {
            bundle.start();
        } catch (BundleException e) {
            fail(e.getMessage());
        }
    }
}
