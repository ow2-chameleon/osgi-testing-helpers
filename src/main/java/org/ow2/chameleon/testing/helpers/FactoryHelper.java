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

import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.Handler;
import org.apache.felix.ipojo.HandlerFactory;
import org.apache.felix.ipojo.ServiceContext;
import org.osgi.framework.BundleContext;

/**
 * Retrieves factories.
 */
public class FactoryHelper extends AbstractHelper {
    private final OSGiHelper osgiHelper;
    private final IPOJOServiceHelper serviceHelper;

    public FactoryHelper(BundleContext context, OSGiHelper osgi, IPOJOServiceHelper service) {
        super(context);
        this.osgiHelper = osgi;
        this.serviceHelper = service;
    }

    @Override
    public void dispose() {
        osgiHelper.dispose();
    }

    /**
     * Returns the component factory with the given name.
     * If the factory is not available, it waits for it 10  seconds.
     *
     * @param factoryName the name of the factory to retrieve.
     * @return the component factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public Factory getFactory(String factoryName) {
        return getFactory(factoryName, 0);
    }

    /**
     * Returns the component factory with the given name.
     * If the factory is not available, it adopts the timeout value given as parameter (in milliseconds).
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param factoryName the name of the factory to retrieve.
     * @param timeout     the timeout
     * @return the component factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public Factory getFactory(String factoryName, long timeout) {
        // Waits for the factory.
        return getFactory(factoryName, timeout, true);
    }

    /**
     * Returns the component factory with the given name.
     * If the factory is not available, it adopts the timeout value given as parameter (in milliseconds).
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param factoryName the name of the factory to retrieve.
     * @param timeout     the timeout
     * @param fail        fail the test if there are no factory when the timeout is reached
     * @return the component factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public Factory getFactory(String factoryName, long timeout, boolean fail) {
        // Waits for the factory.
        return osgiHelper.waitForService(Factory.class,
                "(factory.name=" + factoryName + ")", timeout, fail);
    }

    /**
     * Returns the component factory with the given name in the given service context.
     *
     * @param context the service context
     * @param factoryName the name of the factory to retrieve.
     * @return the component factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public Factory getFactory(ServiceContext context, String factoryName) {
        return serviceHelper.getServiceObject(context, Factory.class, "(factory.name=" + factoryName + ")");
    }

    /**
     * Returns the handler factory with the given name in the local bundle.
     * If the factory is not available, it waits for it 10  seconds.
     *
     * @param factoryName the name of the handler factory to retrieve.
     * @return the handler factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public HandlerFactory getHandlerFactory(String factoryName) {
        return getHandlerFactory(factoryName, 0);
    }

    /**
     * Returns the handler factory with the given name in the local bundle.
     * If the factory is not available, it adopts the timeout value given as parameter (in milliseconds).
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param factoryName the name of the handler factory to retrieve.
     * @param timeout     the timeout
     * @return the handler factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public HandlerFactory getHandlerFactory(String factoryName, long timeout) {
        return getHandlerFactory(factoryName, timeout, true);
    }

    /**
     * Returns the handler factory with the given name in the local bundle.
     * If the factory is not available, it adopts the timeout value given as parameter (in milliseconds).
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param factoryName the name of the handler factory to retrieve.
     * @param timeout     the timeout
     * @param fail        fail the test if there are no factory when the timeout is reached
     * @return the handler factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public HandlerFactory getHandlerFactory(String factoryName, long timeout, boolean fail) {
        return osgiHelper.waitForService(HandlerFactory.class,
                "(" + Handler.HANDLER_NAME_PROPERTY + "=" + factoryName + ")", timeout, fail);
    }

}
