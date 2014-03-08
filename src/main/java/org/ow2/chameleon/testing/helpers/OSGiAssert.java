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

import org.junit.Assert;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;

/**
 * A set of assertions simplifying OSGi tests.
 */
public class OSGiAssert {

    /**
     * The underlying OSGi helper.
     */
    private OSGiHelper m_helper;

    /**
     * Creates an OSGiAssert
     * @param context the bundle context
     */
    public OSGiAssert(BundleContext context) {
        Assert.assertNotNull("The context cannot be null", context);
        m_helper = new OSGiHelper(context);
    }

    /**
     * Checks whether the given filter is syntactically valid.
     * @param filter the filter
     */
    public void assertFilter(String filter) {
        assertFilter(null, filter);
    }

    /**
     * Checks whether the given filter is syntactically valid.
     * @param message the message
     * @param filter the filter
     */
    public void assertFilter(String message, String filter) {
        Assert.assertNotNull(message, filter);
        try {
            m_helper.getContext().createFilter(filter);
        } catch (InvalidSyntaxException e) {
            Assert.fail(message + " : " + e.getMessage());
        }
    }

    /**
     * Checks whether the service is available.
     * @param service the service class
     */
    public void assertServiceAvailable(Class service) {
        Assert.assertTrue(m_helper.isServiceAvailable(service));
    }

    /**
     * Checks whether the service is available.
     * @param service the service class
     * @param timeout the timeout in milliseconds
     */
    public void assertServiceAvailable(Class service, long timeout) {
        m_helper.waitForService(service, null, timeout);
    }

    /**
     * Checks whether the service is available.
     * @param service the service class name
     */
    public void assertServiceAvailable(String service) {
        Assert.assertTrue(m_helper.isServiceAvailable(service));
    }

    /**
     * Checks whether the service is available.
     * @param service the service class name
     * @param timeout the timeout in milliseconds
     */
    public void assertServiceAvailable(String service, long timeout) {
        m_helper.waitForService(service, null, timeout);
    }

    /**
     * Checks whether the service is available.
     * @param message the message
     * @param service the service class
     */
    public void assertServiceAvailable(String message, Class service) {
        Assert.assertTrue(message, m_helper.isServiceAvailable(service));
    }

    /**
     * Checks whether the service is available.
     * @param message the message
     * @param service the service class
     */
    public void assertServiceAvailable(String message, String service) {
        Assert.assertTrue(message, m_helper.isServiceAvailable(service));
    }

    /**
     * Checks whether the service is unavailable.
     * @param service the service class
     */
    public void assertServiceUnavailable(Class service) {
        Assert.assertFalse(m_helper.isServiceAvailable(service));
    }

    /**
     * Checks whether the service is unavailable.
     * @param service the service class
     */
    public void assertServiceUnavailable(String service) {
        Assert.assertFalse(m_helper.isServiceAvailable(service));
    }

    /**
     * Checks whether the service is unavailable.
     * @param  message the message
     * @param service the service class
     */
    public void assertServiceUnavailable(String message, Class service) {
        Assert.assertFalse(message, m_helper.isServiceAvailable(service));
    }

    /**
     * Checks whether the service is unavailable.
     * @param  message the message
     * @param service the service class
     */
    public void assertServiceUnavailable(String message, String service) {
        Assert.assertFalse(message, m_helper.isServiceAvailable(service));
    }

    /**
     * Checks whether the bundle with the given symbolic name has the given state.
     * This method fails if the bundle is not deployed.
     * @param sn the symbolic name of the bundle
     * @param state the bundle state
     */
    public void assertBundleState(String sn, int state) {
        Bundle bundle = m_helper.getBundle(sn);
        Assert.assertNotNull(bundle);
        Assert.assertEquals(bundle.getState(), state);
    }

    /**
     * Checks whether the bundle with the given symbolic name has the given state.
     * This method fails if the bundle is not deployed.
     * @param message the message
     * @param sn the symbolic name of the bundle
     * @param state the bundle state
     */
    public void assertBundleState(String message, String sn, int state) {
        Bundle bundle = m_helper.getBundle(sn);
        Assert.assertNotNull(message, bundle);
        Assert.assertEquals(message, bundle.getState(), state);
    }

    /**
     * Checks whether a bundle is deployed.
     * @param sn the symbolic name of the bundle
     */
    public void assertBundlePresent(String sn) {
        Bundle bundle = m_helper.getBundle(sn);
        Assert.assertNotNull(bundle);
    }

    /**
     * Checks whether the bundle is deployed
     * @param message the message
     * @param sn the symbolic name
     */
    public void assertBundlePresent(String message, String sn) {
        Bundle bundle = m_helper.getBundle(sn);
        Assert.assertNotNull(message, bundle);
    }

    /**
     * Checks whether the bundle is not deployed.
     * @param sn the symbolic name
     */
    public void assertBundleNotPresent(String sn) {
        Bundle bundle = m_helper.getBundle(sn);
        Assert.assertNull(bundle);
    }

    /**
     * Checks whether the bundle is not deployed.
     * @param message the message
     * @param sn the symbolic name
     */
    public void assertBundleNotPresent(String message, String sn) {
        Bundle bundle = m_helper.getBundle(sn);
        Assert.assertNull(message, bundle);
    }

}
