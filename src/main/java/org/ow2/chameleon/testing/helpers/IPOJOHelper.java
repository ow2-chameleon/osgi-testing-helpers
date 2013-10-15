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
import org.apache.felix.ipojo.HandlerFactory;
import org.apache.felix.ipojo.ServiceContext;
import org.apache.felix.ipojo.architecture.Architecture;
import org.apache.felix.ipojo.metadata.Element;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.Dictionary;
import java.util.List;
import java.util.Properties;

/**
 * iPOJO Helper.
 * This helper helps getting {@link org.apache.felix.ipojo.Factory}, and managing
 * {@link org.apache.felix.ipojo.ComponentInstance}.
 */
public class IPOJOHelper extends AbstractHelper {

    /**
     * The OSGi Helper.
     */
    private final OSGiHelper m_helper;
    private final FactoryHelper m_factoryHelper;
    private final InstanceCreationHelper m_instanceCreationHelper;
    private final MetadataHelper m_metadataHelper;
    private final IPOJOServiceHelper m_serviceHelper;
    private final InstanceHelper m_instanceHelper;

    /**
     * Creates a IPOJOHelper.
     *
     * @param context the bundle context
     */
    public IPOJOHelper(BundleContext context) {
        this(context, new OSGiHelper(context));
    }

    /**
     * Creates a IPOJOHelper.
     *
     * @param context the bundle context
     * @param helper the OSGi helper
     */
    public IPOJOHelper(BundleContext context, OSGiHelper helper) {
        super(context);
        m_helper = helper;
        m_serviceHelper = new IPOJOServiceHelper(context, m_helper);
        m_factoryHelper = new FactoryHelper(context, m_helper, m_serviceHelper);
        m_instanceCreationHelper = new InstanceCreationHelper(context, m_factoryHelper);
        m_instanceHelper = new InstanceHelper(context, m_helper);
        m_metadataHelper = new MetadataHelper(context);
    }

    public void dispose() {
        m_serviceHelper.dispose();
        m_metadataHelper.dispose();
        m_instanceHelper.dispose();
        m_instanceCreationHelper.dispose();
        m_factoryHelper.dispose();
        m_helper.dispose();
    }

    // =============================================================================

    /**
     * Returns the handler factory with the given name in the local bundle.
     *
     * @param factoryName the name of the handler factory to retrieve.
     * @return the handler factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public HandlerFactory getHandlerFactory(String factoryName) {
        return m_factoryHelper.getHandlerFactory(factoryName);
    }

    /**
     * Returns the component factory with the given name in the local bundle.
     *
     * @param factoryName the name of the factory to retrieve.
     * @return the component factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public Factory getFactory(String factoryName) {
        return m_factoryHelper.getFactory(factoryName);
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
        return m_factoryHelper.getFactory(context, factoryName);
    }

    // =============================================================================


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
    public ComponentInstance createComponentInstance(String factoryName, Properties configuration) {
        return m_instanceCreationHelper.createComponentInstance(factoryName, configuration);
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
    public ComponentInstance createComponentInstance(String factoryName, String instanceName, Dictionary<String,
            String> configuration) {
        return m_instanceCreationHelper.createComponentInstance(factoryName, instanceName, configuration);
    }

    /**
     * Gets a created instance from the instance name.
     *
     * @param name the instance name.
     * @return the created {@link org.apache.felix.ipojo.ComponentInstance} or <code>null</code>
     *         if the instance was not created during the session.
     */
    public ComponentInstance getInstanceByName(String name) {
        return m_instanceCreationHelper.getInstanceByName(name);
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
    public ComponentInstance createComponentInstance(String factoryName, String instanceName) {
        return m_instanceCreationHelper.createComponentInstance(factoryName, instanceName);
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
    public ComponentInstance createComponentInstance(String factoryName, Dictionary<String, String> configuration) {
        return m_instanceCreationHelper.createComponentInstance(factoryName, configuration);
    }

    /**
     * Creates a new component instance with no configuration.
     *
     * @param factoryName the name of the component factory, in the local
     *                    bundle.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName) {
        return m_instanceCreationHelper.createComponentInstance(factoryName);
    }

    // =============================================================================

    /**
     * Checks whether the instance with the given name is valid.
     * This method relies on the Architecture service, and on the iPOJO's 1.10 semantic of the architecture service.
     *
     * @param instanceName the instance name.
     * @return true if the instance is valid, false otherwise. If the instance's architecture cannot be found,
     *         false is returned.
     */
    public boolean isInstanceStopped(String instanceName) {
        return m_instanceHelper.isInstanceStopped(instanceName);
    }

    /**
     * Checks whether the instance with the given name is invalid.
     * This method relies on the Architecture service.
     *
     * @param instanceName the instance name.
     * @return true if the instance is valid, false otherwise. If the instance's architecture cannot be found,
     *         false is returned.
     */
    public boolean isInstanceInvalid(String instanceName) {
        return m_instanceHelper.isInstanceInvalid(instanceName);
    }

    /**
     * Checks whether the instance with the given name is valid.
     * This method relies on the Architecture service.
     *
     * @param instanceName the instance name.
     * @return true if the instance is valid, false otherwise. If the instance's architecture cannot be found,
     *         false is returned.
     */
    public boolean isInstanceValid(String instanceName) {
        return m_instanceHelper.isInstanceValid(instanceName);
    }

    public boolean isInstanceInvalid(ComponentInstance ci) {
        return m_instanceHelper.isInstanceInvalid(ci);
    }

    /**
     * Gets the architecture of the instance named 'name'
     *
     * @param name the instance name
     * @return the architecture service, {@literal null} if not found.
     */
    public Architecture getArchitectureByName(String name) {
        return m_instanceHelper.getArchitectureByName(name);
    }

    public boolean isInstanceValid(ComponentInstance ci) {
        return m_instanceHelper.isInstanceValid(ci);
    }


    // =============================================================================


    /**
     * Returns the instance metadata of a component defined in the current bundle.
     *
     * @param component the name of the locally defined component.
     * @return the list of instance metadata of the component with the given name,
     *         defined in this given bundle, or {@code null} if not found.
     */
    public Element[] getInstanceMetadata(String component) {
        return m_metadataHelper.getInstanceMetadata(component);
    }

    /**
     * Returns the instance metadatas of the component with the given name,
     * defined in the given bundle.
     *
     * @param bundle    the bundle from which the component is defined.
     * @param component the name of the defined component.
     * @return the list of instance metadata of the component with the given name,
     *         defined in the given bundle, or {@code null} if not found.
     */
    public static Element[] getInstanceMetadata(Bundle bundle, String component) {
        return MetadataHelper.getInstanceMetadata(bundle, component);
    }

    /**
     * Returns the metadata description of the component defined in the current bundle.
     *
     * @param component the name of the locally defined component.
     * @return the metadata description of the component with the given name,
     *         defined in this given bundle, or {@code null} if not found.
     */
    public Element getMetadata(String component) {
        return m_metadataHelper.getMetadata(component);
    }

    /**
     * Returns the metadata description of the component with the given name,
     * defined in the given bundle.
     *
     * @param bundle    the bundle from which the component is defined.
     * @param component the name of the defined component.
     * @return the metadata description of the component with the given name,
     *         defined in the given bundle, or {@code null} if not found.
     */
    public static Element getMetadata(Bundle bundle, String component) {
        return MetadataHelper.getMetadata(bundle, component);
    }

    // =============================================================================


    /**
     * Returns the service reference of a service registered in the specified
     * service context, offering the specified interface and having the given
     * name.
     *
     * @param serviceContext the service context in which services are searched.
     * @param itf            the interface provided by the searched service.
     * @param name           the name of the searched service.
     * @return a service registered in the specified service context, offering
     *         the specified interface and having the given name.
     */
    public <T> ServiceReference<T> getServiceReferenceByName(ServiceContext serviceContext, Class<T> itf, String name) {
        return m_serviceHelper.getServiceReferenceByName(serviceContext, itf, name);
    }

    /**
     * Returns the service reference of a service provided by the specified
     * bundle, offering the specified interface and having the given name.
     *
     * @param itf    the interface provided by the searched service.
     * @param name   the name of the searched service.
     * @return a service provided by the specified bundle, offering the
     *         specified interface and having the given name.
     */
    public <T> T getServiceObjectByName(Class<T> itf, String name) {
        return m_serviceHelper.getServiceObjectByName(itf, name);
    }

    /**
     * Returns the service objects of the services registered in the specified
     * service context, offering the specified interface and matching the given
     * filter.
     *
     * @param serviceContext the service context in which services are searched.
     * @param itf            the interface provided by the searched services.
     * @param filter         an additional filter (can be {@code null}).
     * @return the service objects provided by the specified bundle, offering
     *         the specified interface and matching the given filter.
     */
    public <T> List<T> getServiceObjects(ServiceContext serviceContext, Class<T> itf, String filter) {
        return m_serviceHelper.getServiceObjects(serviceContext, itf, filter);
    }

    /**
     * Checks the availability of a service inside the given service context.
     *
     * @param sc  the service context
     * @param itf the service interface to found
     * @param pid the pid of the service
     * @return <code>true</code> if the service is available in the service
     *         context, <code>false</code> otherwise.
     */
    public boolean isServiceAvailableByPID(ServiceContext sc, String itf, String pid) {
        return m_serviceHelper.isServiceAvailableByPID(sc, itf, pid);
    }

    /**
     * Returns the service reference of all the services registered in the
     * specified service context, offering the specified interface and matching
     * the given filter.
     *
     * @param serviceContext the service context in which services are searched.
     * @param itf            the interface provided by the searched services.
     * @param filter         an additional filter (can be {@code null}).
     * @return all the service references registered in the specified service
     *         context, offering the specified interface and matching the given
     *         filter. If no service matches, an empty array is returned.
     */
    public ServiceReference[] getServiceReferences(ServiceContext serviceContext, String itf, String filter) {
        return m_serviceHelper.getServiceReferences(serviceContext, itf, filter);
    }

    /**
     * Returns the service reference of a service registered in the specified
     * service context, offering the specified interface and matching the given
     * filter.
     *
     * @param serviceContext the service context in which services are searched.
     * @param itf            the interface provided by the searched service.
     * @param filter         an additional filter (can be {@code null}).
     * @return a service reference registered in the specified service context,
     *         offering the specified interface and matching the given filter.
     *         If no service is found, {@code null} is returned.
     */
    public <T> ServiceReference<T> getServiceReference(ServiceContext serviceContext, Class<T> itf, String filter) {
        return m_serviceHelper.getServiceReference(serviceContext, itf, filter);
    }

    /**
     * Returns the service object of a service registered in the specified
     * service context, offering the specified interface and matching the given
     * filter.
     *
     * @param serviceContext the service context in which the service is
     *                       searched.
     * @param itf            the interface provided by the searched service.
     * @param filter         an additional filter (can be {@code null}).
     * @return the service object provided by the specified bundle, offering the
     *         specified interface and matching the given filter.
     */
    public <T> T getServiceObject(ServiceContext serviceContext, Class<T> itf, String filter) {
        return m_serviceHelper.getServiceObject(serviceContext, itf, filter);
    }

    /**
     * Checks if the service is available.
     *
     * @param itf  the service interface
     * @param name the service provider name
     * @return <code>true</code> if the service is available, <code>false</code>
     *         otherwise.
     */
    public boolean isServiceAvailableByName(String itf, String name) {
        return m_serviceHelper.isServiceAvailableByName(itf, name);
    }

    /**
     * Returns the service reference of all the services registered in the
     * specified service context, offering the specified interface and matching
     * the given filter.
     *
     * @param serviceContext the service context in which services are searched.
     * @param itf            the interface provided by the searched services.
     * @param filter         an additional filter (can be {@code null}).
     * @return all the service references registered in the specified service
     *         context, offering the specified interface and matching the given
     *         filter. If no service matches, an empty array is returned.
     */
    public <T> ServiceReference<T>[] getServiceReferences(ServiceContext serviceContext, Class<T> itf, String filter) {
        return m_serviceHelper.getServiceReferences(serviceContext, itf, filter);
    }

    /**
     * Checks the availability of a service inside the given service context.
     *
     * @param sc   the service context
     * @param itf  the service interface to found
     * @param name the service provider name
     * @return <code>true</code> if the service is available in the service
     *         context, <code>false</code> otherwise.
     */
    public boolean isServiceAvailableByName(ServiceContext sc, String itf, String name) {
        return m_serviceHelper.isServiceAvailableByName(sc, itf, name);
    }

    /**
     * Returns the service reference of a service provided by the specified
     * bundle, offering the specified interface and having the given name.
     *
     * @param itf    the interface provided by the searched service.
     * @param name   the name of the searched service.
     * @return a service provided by the specified bundle, offering the
     *         specified interface and having the given name.
     */
    public ServiceReference getServiceReferenceByName(String itf, String name) {
        return m_serviceHelper.getServiceReferenceByName(itf, name);
    }

    /**
     * Returns the service reference of a service registered in the specified
     * service context, offering the specified interface and having the given
     * name.
     *
     * @param serviceContext the service context in which services are searched.
     * @param itf            the interface provided by the searched service.
     * @param name           the name of the searched service.
     * @return a service registered in the specified service context, offering
     *         the specified interface and having the given name.
     */
    public ServiceReference getServiceReferenceByName(ServiceContext serviceContext, String itf, String name) {
        return m_serviceHelper.getServiceReferenceByName(serviceContext, itf, name);
    }

    /**
     * Checks the availability of a service inside the given service context.
     *
     * @param sc  the service context
     * @param itf the service interface to found
     * @return <code>true</code> if the service is available in the service
     *         context, <code>false</code> otherwise.
     */
    public boolean isServiceAvailable(ServiceContext sc, String itf) {
        return m_serviceHelper.isServiceAvailable(sc, itf);
    }

    /**
     * Returns the service reference of the service registered in the specified
     * service context, offering the specified interface and having the given
     * persistent ID.
     *
     * @param serviceContext the service context in which services are searched.
     * @param itf            the interface provided by the searched service.
     * @param pid            the persistent ID of the searched service.
     * @return a service registered in the specified service context, offering
     *         the specified interface and having the given persistent ID.
     */
    public ServiceReference getServiceReferenceByPID(ServiceContext serviceContext, String itf, String pid) {
        return m_serviceHelper.getServiceReferenceByPID(serviceContext, itf, pid);
    }

    /**
     * Returns the service object of a service registered in the specified
     * service context, offering the specified interface and matching the given
     * filter.
     *
     * @param serviceContext the service context in which the service is
     *                       searched.
     * @param itf            the interface provided by the searched service.
     * @param filter         an additional filter (can be {@code null}).
     * @return the service object provided by the specified bundle, offering the
     *         specified interface and matching the given filter.
     */
    public Object getServiceObject(ServiceContext serviceContext, String itf, String filter) {
        return m_serviceHelper.getServiceObject(serviceContext, itf, filter);
    }

    /**
     * Returns the service reference of a service registered in the specified
     * service context, offering the specified interface and matching the given
     * filter.
     *
     * @param serviceContext the service context in which services are searched.
     * @param itf            the interface provided by the searched service.
     * @param filter         an additional filter (can be {@code null}).
     * @return a service reference registered in the specified service context,
     *         offering the specified interface and matching the given filter.
     *         If no service is found, {@code null} is returned.
     */
    public ServiceReference getServiceReference(ServiceContext serviceContext, String itf, String filter) {
        return m_serviceHelper.getServiceReference(serviceContext, itf, filter);
    }

    /**
     * Returns the service objects of the services registered in the specified
     * service context, offering the specified interface and matching the given
     * filter.
     *
     * @param serviceContext the service context in which services are searched.
     * @param itf            the interface provided by the searched services.
     * @param filter         an additional filter (can be {@code null}).
     * @return the service objects provided by the specified bundle, offering
     *         the specified interface and matching the given filter.
     */
    public Object[] getServiceObjects(ServiceContext serviceContext, String itf, String filter) {
        return m_serviceHelper.getServiceObjects(serviceContext, itf, filter);
    }
}
