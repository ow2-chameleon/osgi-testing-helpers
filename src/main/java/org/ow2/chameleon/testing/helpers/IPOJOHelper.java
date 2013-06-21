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

import org.apache.felix.ipojo.*;
import org.apache.felix.ipojo.architecture.Architecture;
import org.apache.felix.ipojo.metadata.Element;
import org.apache.felix.ipojo.parser.ManifestMetadataParser;
import org.apache.felix.ipojo.parser.ParseException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ManagedServiceFactory;

import java.util.*;

/**
 * iPOJO Helper.
 * This helper helps getting {@link org.apache.felix.ipojo.Factory}, and managing
 * {@link org.apache.felix.ipojo.ComponentInstance}.
 */
public class IPOJOHelper {

    /**
     * The bundle context.
     */
    private final BundleContext m_context;
    /**
     * List of instances.
     */
    private final List<ComponentInstance> m_instances;
    /**
     * The OSGi Helper.
     */
    private final OSGiHelper m_helper;

    /**
     * Creates a IPOJOHelper.
     *
     * @param context the OSGi Test Case
     */
    public IPOJOHelper(BundleContext context) {
        m_context = context;
        m_instances = new ArrayList<ComponentInstance>();
        m_helper = new OSGiHelper(context);
    }

    /**
     * Creates a new component instance with the given name (and empty
     * configuration), from the factory specified in the given bundle.
     *
     * @param bundle       the bundle from which the component factory is defined.
     * @param factoryName  the name of the component factory, defined in the
     *                     specified bundle.
     * @param instanceName the name of the component instance to create.
     * @return the newly created component instance.
     */
    public static ComponentInstance createComponentInstance(Bundle bundle,
                                                            String factoryName, String instanceName) {

        // Create the instance configuration
        Dictionary<String, String> configuration = new Hashtable<String, String>();
        configuration.put("instance.name", instanceName);

        return createComponentInstance(bundle, factoryName, configuration);
    }

    /**
     * Creates a new component instance with the given configuration, from the
     * factory specified in the given bundle.
     *
     * @param bundle        the bundle from which the component factory is defined.
     * @param factoryName   the name of the component factory, defined in the
     *                      specified bundle.
     * @param configuration the configuration of the component instance to
     *                      create.
     * @return the newly created component instance.
     */
    public static ComponentInstance createComponentInstance(Bundle bundle,
                                                            String factoryName, Dictionary<String, String> configuration) {

        // Retrieve the component factory.
        Factory fact = getFactory(bundle, factoryName);

        if (fact == null) {
            // Factory not found...
            throw new IllegalArgumentException(
                    "Cannot find the component factory (" + factoryName
                            + ") in the specified bundle ("
                            + bundle.getSymbolicName() + ").");
        }

        try {
            return fact.createComponentInstance(configuration);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Cannot create the component instance with the given configuration:"
                            + e.getMessage());
        }
    }

    /**
     * Creates a new component instance with the given configuration, from the
     * factory specified in the given bundle.
     *
     * @param bundle        the bundle from which the component factory is defined.
     * @param factoryName   the name of the component factory, defined in the
     *                      specified bundle.
     * @param configuration the configuration of the component instance to
     *                      create.
     * @return the newly created component instance.
     */
    public static ComponentInstance createComponentInstance(Bundle bundle,
                                                            String factoryName, Properties configuration) {

        // Retrieve the component factory.
        Factory fact = getFactory(bundle, factoryName);

        if (fact == null) {
            // Factory not found...
            throw new IllegalArgumentException(
                    "Cannot find the component factory (" + factoryName
                            + ") in the specified bundle ("
                            + bundle.getSymbolicName() + ").");
        }

        try {
            return fact.createComponentInstance(configuration);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Cannot create the component instance with the given configuration:"
                            + e.getMessage());
        }
    }

    /**
     * Creates a new component instance with the given name and configuration,
     * from the factory specified in the given bundle.
     *
     * @param bundle        the bundle from which the component factory is defined.
     * @param factoryName   the name of the component factory, defined in the
     *                      specified bundle.
     * @param instanceName  the name of the component instance to create.
     * @param configuration the configuration of the instance to create.
     * @return the newly created component instance.
     */
    public static ComponentInstance createComponentInstance(Bundle bundle,
                                                            String factoryName, String instanceName, Dictionary<String, String> configuration) {

        // Add the instance name to the configuration
        configuration.put("instance.name", instanceName);

        return createComponentInstance(bundle, factoryName, configuration);
    }

    /**
     * Creates a new component instance with the given name and configuration,
     * from the factory specified in the given bundle.
     *
     * @param bundle        the bundle from which the component factory is defined.
     * @param factoryName   the name of the component factory, defined in the
     *                      specified bundle.
     * @param instanceName  the name of the component instance to create.
     * @param configuration the configuration of the instance to create.
     * @return the newly created component instance.
     */
    public static ComponentInstance createComponentInstance(Bundle bundle,
                                                            String factoryName, String instanceName, Properties configuration) {

        // Add the instance name to the configuration
        configuration.put("instance.name", instanceName);

        return createComponentInstance(bundle, factoryName, configuration);
    }

    /**
     * Creates a new component instance with the given name (and an empty
     * configuration), from the factory specified in the given service context.
     *
     * @param serviceContext the service context in which the component factory
     *                       service is registered.
     * @param factoryName    the name of the component factory, defined in the
     *                       specified service context.
     * @param instanceName   the name of the component instance to create.
     * @return the newly created component instance.
     */
    public static ComponentInstance createComponentInstance(
            ServiceContext serviceContext, String factoryName,
            String instanceName) {

        // Create the instance configuration
        Dictionary<String, String> configuration = new Hashtable<String, String>();
        configuration.put("instance.name", instanceName);

        return createComponentInstance(serviceContext, factoryName,
                configuration);
    }

    /**
     * Creates a new component instance with the given name and configuration,
     * from the factory specified in the given service context.
     *
     * @param serviceContext the service context in which the component factory
     *                       service is registered.
     * @param factoryName    the name of the component factory, defined in the
     *                       specified service context.
     * @param configuration  the configuration of the instance to create.
     * @return the newly created component instance.
     */
    public static ComponentInstance createComponentInstance(
            ServiceContext serviceContext, String factoryName,
            Dictionary<String, String> configuration) {

        // Retrieve the component factory.
        Factory fact = getFactory(serviceContext, factoryName);

        if (fact == null) {
            // Factory not found...
            throw new IllegalArgumentException(
                    "Cannot find the component factory (" + factoryName
                            + ") in the specified service context.");
        }

        try {
            return fact.createComponentInstance(configuration);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Cannot create the component instance with the given configuration: "
                            + e.getMessage());
        }
    }

    /**
     * Creates a new component instance with the given name and configuration,
     * from the factory specified in the given service context.
     *
     * @param serviceContext the service context in which the component factory
     *                       service is registered.
     * @param factoryName    the name of the component factory, defined in the
     *                       specified service context.
     * @param instanceName   the name of the component instance to create.
     * @param configuration  the configuration of the instance to create.
     * @return the newly created component instance.
     */
    public static ComponentInstance createComponentInstance(
            ServiceContext serviceContext, String factoryName,
            String instanceName, Dictionary<String, String> configuration) {

        // Add the instance name to the configuration
        configuration.put("instance.name", instanceName);

        return createComponentInstance(serviceContext, factoryName,
                configuration);
    }

    /**
     * Returns the component factory with the given name in the given bundle.
     * If the factory is not available, we wait for 1 second.
     *
     * @param bundle      the bundle from which the component factory is defined.
     * @param factoryName the name of the defined factory.
     * @return the component factory with the given name in the given bundle, or
     *         {@code null} if not found.
     */
    public static Factory getFactory(Bundle bundle, String factoryName) {
        // Waits for the factory.
        new OSGiHelper(bundle.getBundleContext()).waitForService(Factory.class.getName(),
                "(factory.name=" + factoryName + ")", 1000, false);

        ServiceReference[] refs;
        try {
            // Retrieves the component factories services in the bundle.
            refs = bundle.getBundleContext().getServiceReferences(
                    Factory.class.getName(),
                    "(factory.name=" + factoryName + ")");
            if (refs != null) {
                return (Factory) bundle.getBundleContext().getService(refs[0]);
            }

            // Factory not found...
            return null;

        } catch (InvalidSyntaxException e) {
            throw new IllegalArgumentException(
                    "Cannot get the component factory services: "
                            + e.getMessage());
        }
    }

    /**
     * Returns the component factory with the given name, registered in the
     * given service context.
     *
     * @param serviceContext the service context in which the factory service is
     *                       defined.
     * @param factoryName    the name of the factory.
     * @return the component factory with the given name, registered in the
     *         given service context.
     */
    public static Factory getFactory(ServiceContext serviceContext,
                                     String factoryName) {
        ServiceReference[] refs;
        try {
            // Retrieves the component factories services in the service
            // context.
            refs = serviceContext.getServiceReferences(Factory.class.getName(),
                    "(factory.name=" + factoryName + ")");
            if (refs != null) {
                return (Factory) serviceContext.getService(refs[0]);
            }
            return null;

        } catch (InvalidSyntaxException e) {
            System.err.println("Cannot get the factory " + factoryName + " : "
                    + e.getMessage());
            return null;
        }
    }

    /**
     * Returns the handler factory with the given name in the given bundle.
     *
     * @param bundle      the bundle from which the handler factory is defined.
     * @param factoryName the name of the handler factory to retrieve.
     * @return the handler factory with the given name in the given bundle, or
     *         {@code null} if not found.
     */
    public static HandlerFactory getHandlerFactory(Bundle bundle,
                                                   String factoryName) {
        new OSGiHelper(bundle.getBundleContext()).waitForService(HandlerFactory.class.getName(),
                "(" + Handler.HANDLER_NAME_PROPERTY + "=" + factoryName + ")", 1000, false);

        ServiceReference[] refs;
        try {
            // Retrieves the handler factories services in the bundle.
            refs = bundle.getBundleContext().getServiceReferences(
                    HandlerFactory.class.getName(),
                    "(" + Handler.HANDLER_NAME_PROPERTY + "=" + factoryName
                            + ")");
            if (refs != null) {
                return (HandlerFactory) bundle.getBundleContext().getService(
                        refs[0]);
            }

            // Factory not found...
            return null;
        } catch (InvalidSyntaxException e) {
            throw new IllegalArgumentException(
                    "Cannot get the handler factory services: "
                            + e.getMessage());
        }
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

        // Retrieves the component description from the bundle's manifest.
        String elem = (String) bundle.getHeaders().get("iPOJO-Components");
        if (elem == null) {
            throw new IllegalArgumentException(
                    "Cannot find iPOJO-Components descriptor in the specified bundle ("
                            + bundle.getSymbolicName()
                            + "). Not an iPOJO bundle.");
        }

        // Parses the retrieved description and find the component with the
        // given name.
        try {
            Element element = ManifestMetadataParser.parseHeaderMetadata(elem);
            Element[] childs = element.getElements("component");
            for (Element child : childs) {
                String name = child.getAttribute("name");
                String clazz = child.getAttribute("classname");
                if (name != null && name.equalsIgnoreCase(component)) {
                    return child;
                }
                if (clazz.equalsIgnoreCase(component)) {
                    return child;
                }
            }

            // Component not found...
            return null;

        } catch (ParseException e) {
            throw new IllegalStateException(
                    "Cannot parse the components from specified bundle ("
                            + bundle.getSymbolicName() + "): " + e.getMessage());
        }
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

        // Retrieves the component description from the bundle's manifest.
        String elem = (String) bundle.getHeaders().get("iPOJO-Components");
        if (elem == null) {
            throw new IllegalArgumentException(
                    "Cannot find iPOJO-Components descriptor in the specified bundle ("
                            + bundle.getSymbolicName()
                            + "). Not an iPOJO bundle.");
        }

        // Parses the retrieved description and find the component with the
        // given name.
        List<Element> list = new ArrayList<Element>();
        try {
            Element element = ManifestMetadataParser.parseHeaderMetadata(elem);
            Element[] childs = element.getElements("instance");
            for (Element child : childs) {
                String name = child.getAttribute("component");
                if (name != null && name.equalsIgnoreCase(component)) {
                    list.add(child);
                }
            }

            if (list.isEmpty()) {
                // Component not found...
                return null;
            } else {
                return list.toArray(new Element[list.size()]);
            }

        } catch (ParseException e) {
            throw new IllegalStateException(
                    "Cannot parse the components from specified bundle ("
                            + bundle.getSymbolicName() + "): " + e.getMessage());
        }
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
    public static Object getServiceObject(ServiceContext serviceContext,
                                          String itf, String filter) {
        ServiceReference ref = getServiceReference(serviceContext, itf, filter);
        if (ref != null) {
            return serviceContext.getService(ref);
        } else {
            return null;
        }
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
    public static Object[] getServiceObjects(ServiceContext serviceContext,
                                             String itf, String filter) {
        ServiceReference[] refs = getServiceReferences(serviceContext, itf,
                filter);
        if (refs != null) {
            Object[] list = new Object[refs.length];
            for (int i = 0; i < refs.length; i++) {
                list[i] = serviceContext.getService(refs[i]);
            }
            return list;
        } else {
            return new Object[0];
        }
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
    public static ServiceReference getServiceReference(
            ServiceContext serviceContext, String itf, String filter) {
        ServiceReference[] refs = getServiceReferences(serviceContext, itf,
                filter);
        if (refs.length != 0) {
            return refs[0];
        } else {
            // No service found
            return null;
        }
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
    public static ServiceReference getServiceReferenceByPID(
            ServiceContext serviceContext, String itf, String pid) {
        String filter = "(" + "service.pid" + "=" + pid + ")";
        ServiceReference[] refs = getServiceReferences(serviceContext, itf,
                filter);
        if (refs == null) {
            return null;
        } else if (refs.length == 1) {
            return refs[0];
        } else {
            throw new IllegalStateException(
                    "A service lookup by PID returned several providers ("
                            + refs.length + ")" + " for " + itf + " with pid="
                            + pid);
        }
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
    public static ServiceReference[] getServiceReferences(
            ServiceContext serviceContext, String itf, String filter) {
        ServiceReference[] refs;
        try {
            // Get all the service references
            refs = serviceContext.getServiceReferences(itf, filter);
        } catch (InvalidSyntaxException e) {
            throw new IllegalArgumentException(
                    "Cannot get service references: " + e.getMessage());
        }
        if (refs == null) {
            return new ServiceReference[0];
        } else {
            return refs;
        }
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
    public static ServiceReference getServiceReferenceByName(
            ServiceContext serviceContext, String itf, String name) {
        String filter;
        if (itf.equals(Factory.class.getName())
                || itf.equals(ManagedServiceFactory.class.getName())) {
            filter = "(" + "factory.name" + "=" + name + ")";
        } else if (itf.equals(Architecture.class.getName())) {
            filter = "(" + "architecture.instance" + "=" + name + ")";
        } else {
            filter = "(" + "instance.name" + "=" + name + ")";
        }
        return getServiceReference(serviceContext, itf, filter);
    }

    /**
     * Checks the availability of a service inside the given service context.
     *
     * @param sc  the service context
     * @param itf the service interface to found
     * @return <code>true</code> if the service is available in the service
     *         context, <code>false</code> otherwise.
     */
    public static boolean isServiceAvailable(ServiceContext sc, String itf) {
        ServiceReference ref = getServiceReference(sc, itf, null);
        return ref != null;
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
    public static boolean isServiceAvailableByName(ServiceContext sc,
                                                   String itf, String name) {
        ServiceReference ref = getServiceReferenceByName(sc, itf, name);
        return ref != null;
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
    public static boolean isServiceAvailableByPID(ServiceContext sc,
                                                  String itf, String pid) {
        ServiceReference ref = getServiceReferenceByPID(sc, itf, pid);
        return ref != null;
    }

    /**
     * Returns the service reference of a service provided by the specified
     * bundle, offering the specified interface and having the given name.
     *
     * @param bundle the bundle from which the service is searched.
     * @param itf    the interface provided by the searched service.
     * @param name   the name of the searched service.
     * @return a service provided by the specified bundle, offering the
     *         specified interface and having the given name.
     */
    public static ServiceReference getServiceReferenceByName(Bundle bundle,
                                                             String itf, String name) {
        String filter;
        if (itf.equals(Factory.class.getName())
                || itf.equals(ManagedServiceFactory.class.getName())) {
            filter = "(" + "factory.name" + "=" + name + ")";
        } else if (itf.equals(Architecture.class.getName())) {
            filter = "(" + "architecture.instance" + "=" + name + ")";
        } else {
            filter = "(" + "instance.name" + "=" + name + ")";
        }

        // We wait if it's not available.
        new OSGiHelper(bundle.getBundleContext()).waitForService(itf, filter, 1000, false);

        return OSGiHelper.getServiceReference(bundle, itf, filter);
    }

    /**
     * Disposes created instances.
     */
    public void dispose() {
        for (ComponentInstance instance : m_instances) {
            instance.dispose();
        }
        m_helper.dispose();
        m_instances.clear();
    }

    /**
     * Gets a created instance from the instance name.
     *
     * @param name the instance name.
     * @return the created {@link org.apache.felix.ipojo.ComponentInstance} or <code>null</code>
     *         if the instance was not created during the session.
     */
    public ComponentInstance getInstanceByName(String name) {
        for (ComponentInstance instance : m_instances) {
            if (instance.getInstanceName()
                    .equals(name)) {
                return instance;
            }
        }
        return null;
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
        ComponentInstance ci = createComponentInstance(m_context.getBundle(),
                factoryName, instanceName);
        m_instances.add(ci);
        return ci;
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
        ComponentInstance ci = createComponentInstance(m_context.getBundle(),
                factoryName, configuration);
        m_instances.add(ci);
        return ci;
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
        ComponentInstance ci = createComponentInstance(m_context.getBundle(),
                factoryName, configuration);
        m_instances.add(ci);
        return ci;
    }

    /**
     * Creates a new component instance with no configuration, from the factory
     * specified in the local bundle.
     *
     * @param factoryName the name of the component factory, in the local
     *                    bundle.
     * @return the newly created component instance.
     */
    public ComponentInstance createComponentInstance(String factoryName) {
        ComponentInstance ci = createComponentInstance(m_context.getBundle(),
                factoryName, (Dictionary<String, String>) null);
        m_instances.add(ci);
        return ci;
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
        ComponentInstance ci = createComponentInstance(m_context.getBundle(),
                factoryName, instanceName, configuration);
        m_instances.add(ci);
        return ci;
    }

    /**
     * Returns the component factory with the given name in the local bundle.
     *
     * @param factoryName the name of the factory to retrieve.
     * @return the component factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public Factory getFactory(String factoryName) {
        return getFactory(m_context.getBundle(), factoryName);
    }

    /**
     * Returns the handler factory with the given name in the local bundle.
     *
     * @param factoryName the name of the handler factory to retrieve.
     * @return the handler factory with the given name in the local bundle, or
     *         {@code null} if not found.
     */
    public HandlerFactory getHandlerFactory(String factoryName) {
        return getHandlerFactory(m_context.getBundle(), factoryName);
    }

    /**
     * Returns the metadata description of the component defined in this bundle.
     *
     * @param component the name of the locally defined component.
     * @return the metadata description of the component with the given name,
     *         defined in this given bundle, or {@code null} if not found.
     */
    public Element getMetadata(String component) {
        return getMetadata(m_context.getBundle(), component);
    }

    /**
     * Returns the instance metadata of a component defined in this bundle.
     *
     * @param component the name of the locally defined component.
     * @return the list of instance metadata of the component with the given name,
     *         defined in this given bundle, or {@code null} if not found.
     */
    public Element[] getInstanceMetadata(String component) {
        return getInstanceMetadata(m_context.getBundle(), component);
    }

    /**
     * Returns the service reference of a service provided by the local bundle,
     * offering the specified interface and having the given name.
     *
     * @param itf  the interface provided by the searched service.
     * @param name the name of the searched service.
     * @return a service provided by the specified bundle, offering the
     *         specified interface and having the given name.
     */
    public ServiceReference getServiceReferenceByName(String itf, String name) {
        return getServiceReferenceByName(m_context.getBundle(), itf, name);
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
        ServiceReference ref = getServiceReferenceByName(itf, name);
        return ref != null;
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
        Architecture architecture = getArchitectureByName(instanceName);
        return architecture != null && architecture.getInstanceDescription().getState() == ComponentInstance.VALID;
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
        Architecture architecture = getArchitectureByName(instanceName);
        return architecture != null && architecture.getInstanceDescription().getState() == ComponentInstance.INVALID;
    }

    /**
     * Checks whether the instance with the given name is valid.
     * This method relies on the Architecture service, and on the iPOJO's 1.10 semantic of the architecture service.
     *
     * @param instanceName the instance name.
     * @return true if the instance is valid, false otherwise. If the instance's architecture cannot be found,
     *         false is returned.
     */
    public boolean isInstanceStopped(String instanceName) {
        Architecture architecture = getArchitectureByName(instanceName);
        return architecture != null && architecture.getInstanceDescription().getState() == ComponentInstance.STOPPED;
    }

    public boolean isInstanceValid(ComponentInstance ci) {
        return ci != null && ci.getState() == ComponentInstance.VALID;
    }

    public boolean isInstanceInvalid(ComponentInstance ci) {
        return ci != null && ci.getState() == ComponentInstance.INVALID;
    }

    /**
     * Gets the architecture of the instance named 'name'
     *
     * @param name the instance name
     * @return the architecture service, {@literal null} if not found.
     */
    public Architecture getArchitectureByName(String name) {
        return m_helper.getServiceObject(Architecture.class, "(architecture.instance=" + name + ")");
    }


}
