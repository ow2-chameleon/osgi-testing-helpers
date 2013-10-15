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
     *
     * @param factoryName  the name of the component factory, defined in the
     *                     local bundle.
     * @param instanceName the name of the component instance to create.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     String instanceName) {
        return createComponentInstance(factoryName, instanceName, null);
    }

    /**
     * Creates a new component instance with the given configuration, from the
     * factory specified in the local bundle.
     *
     * @param factoryName   the name of the component factory, in the local
     *                      bundle.
     * @param configuration the configuration of the component instance to
     *                      create.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     Dictionary<String, String> configuration) {
        Factory factory = factoryHelper.getFactory(factoryName);
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
     *
     * @param factoryName   the name of the component factory, in the local
     *                      bundle.
     * @param configuration the configuration of the component instance to
     *                      create.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     Properties configuration) {
        Factory factory = factoryHelper.getFactory(factoryName);
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
     *
     * @param factoryName the name of the component factory, in the local
     *                    bundle.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName) {
        return createComponentInstance(factoryName, (Dictionary<String, String>) null);
    }

    /**
     * Creates a new component instance with the given name and configuration,
     * from the factory specified in the given bundle.
     *
     * @param factoryName   the name of the component factory, defined in the
     *                      specified bundle.
     * @param instanceName  the name of the component instance to create.
     * @param configuration the configuration of the instance to create.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName,
                                                     String instanceName, Dictionary<String, String> configuration) {

        if (configuration == null) {
            configuration = new Hashtable<String, String>();
        }
        configuration.put(Factory.INSTANCE_NAME_PROPERTY, instanceName);
        return createComponentInstance(factoryName, configuration);
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
