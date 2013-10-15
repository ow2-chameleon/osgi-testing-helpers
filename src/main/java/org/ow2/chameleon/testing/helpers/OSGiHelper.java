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

import org.osgi.framework.*;
import org.osgi.service.packageadmin.PackageAdmin;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Eases the interaction with OSGi in tests.
 * This helper helps getting services, references, bundles...
 * It's the composition of the bundle helper and osgi helper.
 */
public class OSGiHelper extends AbstractHelper {


    private final BundleHelper bundle;
    private final ServiceHelper service;

    public OSGiHelper(BundleContext context) {
        super(context);
        bundle = new BundleHelper(context);
        service = new ServiceHelper(context);
    }

    public void dispose() {
        bundle.dispose();
        service.dispose();
    }

    public void uninstall(Bundle bundle) {
        this.bundle.uninstall(bundle);
    }

    public Bundle install(String url, InputStream stream) {
        return bundle.install(url, stream);
    }

    public Bundle install(String url) {
        return bundle.install(url);
    }

    public Bundle installAndStart(String url) {
        return bundle.installAndStart(url);
    }

    public void start(Bundle bundle) {
        this.bundle.start(bundle);
    }

    public Bundle installAndStart(String url, InputStream stream) {
        return bundle.installAndStart(url, stream);
    }

    public void stop(Bundle bundle) {
        this.bundle.stop(bundle);
    }

    public Bundle getBundle(long bundleId) {
        return bundle.getBundle(bundleId);
    }

    public Bundle getBundle(String name) {
        return bundle.getBundle(name);
    }

    public static boolean isFragment(Bundle bundle) {
        return BundleHelper.isFragment(bundle);
    }

    //===== Service =====


    /**
     * Waits for a service. Fails on timeout.
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param itf     the service interface
     * @param filter  the filter
     * @param timeout the timeout
     * @return a matching service reference.
     */
    public ServiceReference waitForService(String itf, String filter, long timeout) {
        return service.waitForService(itf, filter, timeout);
    }

    /**
     * Returns the service reference of all the services, offering the
     * specified interface.
     *
     * @param itf the interface provided by the searched services.
     * @return all the service references, offering the specified interface
     *         and matching the given filter. If no service matches, an empty array is
     *         returned.
     */
    public <T> List<ServiceReference<T>> getServiceReferencesAsList(Class<T> itf) {
        return service.getServiceReferencesAsList(itf);
    }

    /**
     * Waits for a service. Fails on timeout.
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param itf     the service interface
     * @param filter  the filter
     * @param timeout the timeout
     * @return a matching service reference.
     */
    public <T> T waitForService(Class<T> itf, String filter, long timeout) {
        return service.waitForService(itf, filter, timeout);
    }

    /**
     * Checks if the service is available.
     *
     * @param itf the service interface
     * @param pid the service pid
     * @return <code>true</code> if the service is available, <code>false</code>
     *         otherwise.
     */
    public boolean isServiceAvailableByPID(Class itf, String pid) {
        return service.isServiceAvailableByPID(itf, pid);
    }

    /**
     * Returns the service reference of a service, offering the specified
     * interface.
     *
     * @param itf the interface provided by the searched service.
     * @return a service reference, offering the specified interface. If no service is found,
     *         {@code null} is returned.
     */
    public <T> ServiceReference<T> getServiceReference(Class<T> itf) {
        return service.getServiceReference(itf);
    }

    /**
     * Returns the service object associated with this service reference.
     *
     * @param ref service reference
     * @return the service object.
     */
    public <T> T getServiceObject(ServiceReference<T> ref) {
        return service.getServiceObject(ref);
    }

    /**
     * Checks if the service is available.
     *
     * @param itf the service interface
     * @return <code>true</code> if the service is available, <code>false</code>
     *         otherwise.
     */
    public boolean isServiceAvailable(String itf) {
        return service.isServiceAvailable(itf);
    }

    /**
     * Checks if the service is available.
     *
     * @param itf the service interface
     * @param pid the service pid
     * @return <code>true</code> if the service is available, <code>false</code>
     *         otherwise.
     */
    public boolean isServiceAvailableByPID(String itf, String pid) {
        return service.isServiceAvailableByPID(itf, pid);
    }

    /**
     * Returns the service reference of a service provided offering the
     * specified interface.
     *
     * @param itf the interface provided by the searched service.
     * @return a service reference, offering the specified interface and
     *         matching the given filter. If no service is found, {@code null} is returned.
     */
    public ServiceReference getServiceReference(String itf) {
        return service.getServiceReference(itf);
    }

    /**
     * Returns the service objects, offering the specified interface.
     *
     * @param clazz the interface
     * @return the service objects offering the specified interface.
     */
    public <T> List<T> getServiceObjects(Class<T> clazz) {
        return service.getServiceObjects(clazz);
    }

    /**
     * Returns the service object of a service, offering the specified
     * interface.
     *
     * @param clazz the interface provided by the searched service.
     * @return the service object, offering the specified interface.
     */
    public Object getServiceObject(String clazz) {
        return service.getServiceObject(clazz);
    }

    /**
     * Returns the service object of a service offering the specified
     * interface and matching the given filter.
     *
     * @param clazz  the interface provided by the searched service.
     * @param filter an additional filter (can be {@code null}).
     * @return the service object offering the specified interface and
     *         matching the given filter.
     */
    public <T> T getServiceObject(Class<T> clazz, String filter) {
        return service.getServiceObject(clazz, filter);
    }

    /**
     * Returns the service object of a service offering the specified
     * interface and matching the given filter.
     *
     * @param itf    the interface provided by the searched service.
     * @param filter an additional filter (can be {@code null}).
     * @return the service object, offering the specified interface
     *         and matching the given filter.
     */
    public Object getServiceObject(String itf, String filter) {
        return service.getServiceObject(itf, filter);
    }

    /**
     * Returns the service reference of the service offering
     * the specified interface and having the given persistent
     * ID.
     *
     * @param itf the interface provided by the searched service.
     * @param pid the persistent ID of the searched service.
     * @return a service offering the specified interface and having
     *         the given persistent ID.
     */
    public <T> ServiceReference<T> getServiceReferenceByPID(Class<T> itf, String pid) {
        return service.getServiceReferenceByPID(itf, pid);
    }

    /**
     * Waits for a service. Fails on timeout.
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param itf     the service interface
     * @param filter  the filter
     * @param timeout the timeout
     * @param fail    fail the test
     * @return a matching service reference.
     */
    public ServiceReference waitForService(String itf, String filter, long timeout, boolean fail) {
        return service.waitForService(itf, filter, timeout, fail);
    }

    /**
     * Returns the service reference of a service, offering the specified
     * interface and matching the given filter.
     *
     * @param itf    the interface provided by the searched service.
     * @param filter an additional filter (can be {@code null}).
     * @return a service reference, offering the specified interface and
     *         matching the given filter. If no service is found, {@code null} is returned.
     */
    public ServiceReference getServiceReference(String itf, String filter) {
        return service.getServiceReference(itf, filter);
    }

    /**
     * Returns the service objects, offering the specified interface
     * and matching the given filter.
     *
     * @param clazz  the interface
     * @param filter an additional filter (can be {@code null}).
     * @return the service objects offering the specified interface
     *         and matching the given filter.
     */
    public <T> List<T> getServiceObjects(Class<T> clazz, String filter) {
        return service.getServiceObjects(clazz, filter);
    }

    /**
     * Checks if the service is available.
     *
     * @param itf the service interface
     * @return <code>true</code> if the service is available, <code>false</code>
     *         otherwise.
     */
    public boolean isServiceAvailable(Class itf) {
        return service.isServiceAvailable(itf);
    }

    /**
     * Returns the service object of the service having the given pid.
     * @param clazz the interface
     * @param pid the pid
     * @return the service object, {@code null} if the service is not available.
     */
    public <T> T getServiceObjectByPid(Class<T> clazz, String pid) {
        return service.getServiceObjectByPid(clazz, pid);
    }

    /**
     * Returns the service reference of a service, offering the specified
     * interface and matching the given filter.
     *
     * @param itf    the interface provided by the searched service.
     * @param filter an additional filter (can be {@code null}).
     * @return a service reference, offering the specified interface and
     *         matching the given filter. If no service is found, {@code null} is returned.
     */
    public <T> ServiceReference<T> getServiceReference(Class<T> itf, String filter) {
        return service.getServiceReference(itf, filter);
    }

    /**
     * Returns the service reference of all the services, offering the
     * specified interface and matching the given filter.
     *
     * @param itf    the interface provided by the searched services.
     * @param filter an additional filter (can be {@code null}).
     * @return all the service references, offering the specified interface
     *         and matching the given filter. If no service matches, an empty array is
     *         returned.
     */
    public <T> ServiceReference<T>[] getServiceReferences(Class<T> itf, String filter) {
        return service.getServiceReferences(itf, filter);
    }

    /**
     * Returns the service objects, offering the specified interface
     * and matching the given filter.
     *
     * @param itf    the interface provided by the searched services.
     * @param filter an additional filter (can be {@code null}).
     * @return the service objects offering the specified interface
     *         and matching the given filter.
     */
    public Object[] getServiceObjects(String itf, String filter) {
        return service.getServiceObjects(itf, filter);
    }

    /**
     * Returns the service reference of the service offering
     * the specified interface and having the given persistent
     * ID.
     *
     * @param itf the interface provided by the searched service.
     * @param pid the persistent ID of the searched service.
     * @return a service offering the specified interface and having
     *         the given persistent ID.
     */
    public ServiceReference getServiceReferenceByPID(String itf, String pid) {
        return service.getServiceReferenceByPID(itf, pid);
    }

    /**
     * Returns the service object of a service, offering the specified
     * interface.
     *
     * @param clazz the interface provided by the searched service.
     * @return the service object, offering the specified interface.
     */
    public <T> T getServiceObject(Class<T> clazz) {
        return service.getServiceObject(clazz);
    }

    /**
     * Returns the service reference of all the services, offering the
     * specified interface and matching the given filter.
     *
     * @param itf    the interface provided by the searched services.
     * @param filter an additional filter (can be {@code null}).
     * @return all the service references, offering the specified interface
     *         and matching the given filter. If no service matches, an empty array is
     *         returned.
     */
    public ServiceReference[] getServiceReferences(String itf, String filter) {
        return service.getServiceReferences(itf, filter);
    }

    /**
     * Returns the service reference of all the services, offering the
     * specified interface and matching the given filter.
     *
     * @param itf    the interface provided by the searched services.
     * @param filter an additional filter (can be {@code null}).
     * @return all the service references, offering the specified interface
     *         and matching the given filter. If no service matches, an empty list is
     *         returned.
     */
    public <T> List<ServiceReference<T>> getServiceReferencesAsList(Class<T> itf, String filter) {
        return service.getServiceReferencesAsList(itf, filter);
    }

    /**
     * Waits for a service. Fails on timeout.
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param itf     the service interface
     * @param filter  the filter
     * @param timeout the timeout
     * @param fail    fail the test if there are no service when the timeout is reached
     * @return a matching service reference.
     */
    public <T> T waitForService(Class<T> itf, String filter, long timeout, boolean fail) {
        return service.waitForService(itf, filter, timeout, fail);
    }

}
