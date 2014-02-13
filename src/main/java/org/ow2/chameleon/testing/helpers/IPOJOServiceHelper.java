package org.ow2.chameleon.testing.helpers;

import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.ServiceContext;
import org.apache.felix.ipojo.architecture.Architecture;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ManagedServiceFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Retrieves services provided by iPOJO instances.
 */
public class IPOJOServiceHelper extends AbstractHelper {

    private final OSGiHelper osgiHelper;

    public IPOJOServiceHelper(BundleContext context, OSGiHelper helper) {
        super(context);
        osgiHelper = helper;
    }

    @Override
    public void dispose() {
        // The owner will dispose the OSGi helper.
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
    public ServiceReference getServiceReference(
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
    public <T> ServiceReference<T> getServiceReference(
            ServiceContext serviceContext, Class<T> itf, String filter) {
        ServiceReference[] refs = getServiceReferences(serviceContext, itf.getName(),
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
    public ServiceReference getServiceReferenceByPID(
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
    public ServiceReference[] getServiceReferences(
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
    public <T> ServiceReference<T>[] getServiceReferences(
            ServiceContext serviceContext, Class<T> itf, String filter) {
        Collection<ServiceReference<T>> refs;
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
            ServiceReference<T>[] t = new ServiceReference[refs.size()];
            return refs.toArray(t);
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
    public ServiceReference getServiceReferenceByName(
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
    public <T> ServiceReference<T> getServiceReferenceByName(
            ServiceContext serviceContext, Class<T> itf, String name) {
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
    public boolean isServiceAvailable(ServiceContext sc, String itf) {
        return getServiceReference(sc, itf, null) != null;
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
    public boolean isServiceAvailableByName(ServiceContext sc,
                                                   String itf, String name) {
        return getServiceReferenceByName(sc, itf, name) != null;
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
    public boolean isServiceAvailableByPID(ServiceContext sc,
                                                  String itf, String pid) {
        return getServiceReferenceByPID(sc, itf, pid) != null;
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
        return getServiceReferenceByName(itf, name, 0);
    }

    /**
     * Returns the service reference of a service provided by the specified
     * bundle, offering the specified interface and having the given name.
     *
     * @param itf     the interface provided by the searched service.
     * @param name    the name of the searched service.
     * @param timeout the timeout
     * @return a service provided by the specified bundle, offering the
     *         specified interface and having the given name.
     */
    public ServiceReference getServiceReferenceByName(String itf, String name, long timeout) {
        return getServiceReferenceByName(itf, name, timeout, true);
    }

    /**
     * Returns the service reference of a service provided by the specified
     * bundle, offering the specified interface and having the given name.
     *
     * @param itf     the interface provided by the searched service.
     * @param name    the name of the searched service.
     * @param timeout the timeout
     * @param fail    fail the test if there are no serviceReference when the timeout is reached
     * @return a service provided by the specified bundle, offering the
     *         specified interface and having the given name.
     */
    public ServiceReference getServiceReferenceByName(String itf, String name, long timeout, boolean fail) {
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
        return osgiHelper.waitForService(itf, filter, timeout, fail);
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
        return getServiceObjectByName(itf, name, 0);
    }

    /**
     * Returns the service reference of a service provided by the specified
     * bundle, offering the specified interface and having the given name.
     *
     * @param itf     the interface provided by the searched service.
     * @param name    the name of the searched service.
     * @param timeout the timeout
     * @return a service provided by the specified bundle, offering the
     *         specified interface and having the given name.
     */
    public <T> T getServiceObjectByName(Class<T> itf, String name, long timeout) {
        return getServiceObjectByName(itf, name, timeout, true);
    }

    /**
     * Returns the service reference of a service provided by the specified
     * bundle, offering the specified interface and having the given name.
     *
     * @param itf    the interface provided by the searched service.
     * @param name   the name of the searched service.
     * @param timeout the timeout
     * @param fail    fail the test if there are no serviceReference when the timeout is reached
     * @return a service provided by the specified bundle, offering the
     *         specified interface and having the given name.
     */
    public <T> T getServiceObjectByName(Class<T> itf, String name, long timeout, boolean fail) {
        String filter;
        if (itf.getName().equals(Factory.class.getName())
                || itf.getName().equals(ManagedServiceFactory.class.getName())) {
            filter = "(" + "factory.name" + "=" + name + ")";
        } else if (itf.getName().equals(Architecture.class.getName())) {
            filter = "(" + "architecture.instance" + "=" + name + ")";
        } else {
            filter = "(" + "instance.name" + "=" + name + ")";
        }

        // We wait if it's not available.
        return osgiHelper.waitForService(itf, filter, timeout, fail);
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
        return getServiceReferenceByName(itf, name) != null;
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
    public Object getServiceObject(ServiceContext serviceContext,
                                          String itf, String filter) {
        ServiceReference ref = getServiceReference(serviceContext, itf, filter);
        if (ref != null) {
            return serviceContext.getService(ref);
        } else {
            return null;
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
    public <T> T getServiceObject(ServiceContext serviceContext,
                                   Class<T> itf, String filter) {
        ServiceReference<T> ref = getServiceReference(serviceContext, itf, filter);
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
    public Object[] getServiceObjects(ServiceContext serviceContext,
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
    public <T> List<T> getServiceObjects(ServiceContext serviceContext,
                                      Class<T> itf, String filter) {
        ServiceReference<T>[] refs = getServiceReferences(serviceContext, itf,
                filter);
        if (refs != null) {
            List<T> list = new ArrayList<T>();
            for (ServiceReference<T> ref : refs) {
                list.add(serviceContext.getService(ref));
            }
            return list;
        } else {
            return Collections.emptyList();
        }
    }


}
