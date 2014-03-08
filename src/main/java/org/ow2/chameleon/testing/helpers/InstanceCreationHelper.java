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

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.Factory;
import org.osgi.framework.BundleContext;

import java.util.*;

import static junit.framework.Assert.fail;

/**
 * Eases the creation of component instances.
 */
public class InstanceCreationHelper extends AbstractHelper {

    private final FactoryHelper factoryHelper;
    protected List<ComponentInstance> instances = new ArrayList<ComponentInstance>();

    public InstanceCreationHelper(BundleContext context, FactoryHelper factory) {
        super(context);
        this.factoryHelper = factory;
    }

    @Override
    public void dispose() {
        for (ComponentInstance instance : instances) {
            instance.dispose();
        }
    }

    /**
     * Creates a new component instance with the given name (and empty
     * configuration), from the factory specified in the local bundle.
     * If the factory is not available, it waits for it 10 seconds.
     *
     * @param factoryName  the name of the component factory, defined in the
     *                     local bundle.
     * @param instanceName the name of the component instance to create.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     String instanceName) {
        return createComponentInstance(factoryName, instanceName, 0);
    }

    /**
     * Creates a new component instance with the given name (and empty
     * configuration), from the factory specified in the local bundle.
     * If the factory is not available, it adopts the timeout value given as parameter (in milliseconds).
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param factoryName  the name of the component factory, defined in the
     *                     local bundle.
     * @param instanceName the name of the component instance to create.
     * @param timeout      the timeout
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     String instanceName,
                                                     long timeout) {
        return createComponentInstance(factoryName, instanceName, null, timeout);
    }

    /**
     * Creates a new component instance with the given configuration, from the
     * factory specified in the local bundle.
     * If the factory is not available, it waits for it 10 seconds.
     *
     * @param factoryName   the name of the component factory, in the local
     *                      bundle.
     * @param configuration the configuration of the component instance to
     *                      create.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     Dictionary<String, String> configuration) {
       return createComponentInstance(factoryName, configuration, 0);
    }

    /**
     * Creates a new component instance with the given configuration, from the
     * factory specified in the local bundle.
     * If the factory is not available, it adopts the timeout value given as parameter (in milliseconds).
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param factoryName   the name of the component factory, in the local
     *                      bundle.
     * @param configuration the configuration of the component instance to
     *                      create.
     * @param timeout       the timeout
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     Dictionary<String, String> configuration,
                                                     long timeout) {
        Factory factory = factoryHelper.getFactory(factoryName, timeout);
        try {
            ComponentInstance instance = factory.createComponentInstance(configuration);
            instances.add(instance);
            return instance;
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return null;
    }


    /**
     * Creates a new component instance with the given configuration, from the
     * factory specified in the local bundle.
     * If the factory is not available, it waits for it 10 seconds.
     *
     * @param factoryName   the name of the component factory, in the local
     *                      bundle.
     * @param configuration the configuration of the component instance to
     *                      create.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     Properties configuration) {
        return createComponentInstance(factoryName, configuration, 0);
    }

    /**
     * Creates a new component instance with the given configuration, from the
     * factory specified in the local bundle.
     * If the factory is not available, it adopts the timeout value given as parameter (in milliseconds).
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param factoryName   the name of the component factory, in the local
     *                      bundle.
     * @param configuration the configuration of the component instance to
     *                      create.
     * @param timeout       the timeout
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     Properties configuration,
                                                     long timeout) {
        Factory factory = factoryHelper.getFactory(factoryName, timeout);
        try {
            ComponentInstance instance = factory.createComponentInstance(configuration);
            instances.add(instance);
            return instance;
        } catch (Exception e) {
            fail(e.getMessage());
        }
        return null;
    }


    /**
     * Creates a new component instance with no configuration.
     * If the factory is not available, it waits for it 10 seconds.
     *
     * @param factoryName the name of the component factory, in the local
     *                    bundle.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName) {
        return createComponentInstance(factoryName, 0);
    }


    /**
     * Creates a new component instance with no configuration.
     * If the factory is not available, it adopts the timeout value given as parameter (in milliseconds).
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param factoryName the name of the component factory, in the local
     *                    bundle.
     * @param timeout     the timeout
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName, long timeout) {
        return createComponentInstance(factoryName, (Dictionary<String, String>) null, timeout);
    }

    /**
     * Creates a new component instance with the given name and configuration,
     * from the factory specified in the given bundle.
     * If the factory is not available, it waits for it 10 seconds.
     *
     * @param factoryName   the name of the component factory, defined in the
     *                      specified bundle.
     * @param instanceName  the name of the component instance to create.
     * @param configuration the configuration of the instance to create.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     String instanceName, Dictionary<String, String> configuration) {
        return createComponentInstance(factoryName, instanceName, configuration);
    }

    /**
     * Creates a new component instance with the given name and configuration,
     * from the factory specified in the given bundle.
     * If the factory is not available, it adopts the timeout value given as parameter (in milliseconds).
     * If timeout is set to 0, it sets the timeout to 10s.
     *
     * @param factoryName   the name of the component factory, defined in the
     *                      specified bundle.
     * @param instanceName  the name of the component instance to create.
     * @param configuration the configuration of the instance to create.
     * @param timeout       the timeout
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     String instanceName, Dictionary<String, String> configuration,
                                                     long timeout) {

        if (configuration == null) {
            configuration = new Hashtable<String, String>();
        }
        configuration.put(Factory.INSTANCE_NAME_PROPERTY, instanceName);
        return createComponentInstance(factoryName, configuration, timeout);
    }

    /**
     * Gets a created instance from the instance name.
     *
     * @param name the instance name.
     * @return the created {@link org.apache.felix.ipojo.ComponentInstance} or <code>null</code>
     *         if the instance was not created during the session.
     */
    public ComponentInstance getInstanceByName(String name) {
        for (ComponentInstance instance : instances) {
            if (instance.getInstanceName()
                    .equals(name)) {
                return instance;
            }
        }
        return null;
    }


}
