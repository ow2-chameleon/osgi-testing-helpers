package org.ow2.chameleon.testing.helpers;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Manages OSGi services.
 */
public class ServiceHelper extends AbstractHelper {



    /**
     * List of get references.
     */
    private final List<ServiceReference> m_references = new ArrayList<ServiceReference>();

    public ServiceHelper(BundleContext context) {
        super(context);
    }

    @Override
    public void dispose() {
        for (ServiceReference ref : m_references) {
            context.ungetService(ref);
        }
        m_references.clear();
    }

    /**
     * Checks if the service is available.
     *
     * @param itf the service interface
     * @return <code>true</code> if the service is available, <code>false</code>
     *         otherwise.
     */
    public boolean isServiceAvailable(String itf) {
        return getServiceReference(itf) != null;
    }

    /**
     * Checks if the service is available.
     *
     * @param itf the service interface
     * @return <code>true</code> if the service is available, <code>false</code>
     *         otherwise.
     */
    public boolean isServiceAvailable(Class itf) {
        return getServiceReference(itf) != null;
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
        return getServiceReferenceByPID(itf, pid) != null;
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
        return getServiceReferenceByPID(itf, pid) != null;
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
        ServiceReference ref = getServiceReference(itf, filter);
        if (ref != null) {
            m_references.add(ref);
            return context.getService(ref);
        } else {
            return null;
        }
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
        ServiceReference ref = getServiceReference(clazz.getName(), filter);
        if (ref != null) {
            m_references.add(ref);
            return clazz.cast(context.getService(ref));
        } else {
            return null;
        }
    }

    /**
     * Returns the service object of the service having the given pid.
     * @param clazz the interface
     * @param pid the pid
     * @return the service object, {@code null} if the service is not available.
     */
    public <T> T getServiceObjectByPid(Class<T> clazz, String pid) {
        ServiceReference<T> ref = getServiceReferenceByPID(clazz, pid);
        if (ref == null) {
            return null;
        } else {
            return getServiceObject(ref);
        }
    }

    /**
     * Returns the service object of a service, offering the specified
     * interface.
     *
     * @param clazz the interface provided by the searched service.
     * @return the service object, offering the specified interface.
     */
    public <T> T getServiceObject(Class<T> clazz) {
        return getServiceObject(clazz, null);
    }

    /**
     * Returns the service object of a service, offering the specified
     * interface.
     *
     * @param clazz the interface provided by the searched service.
     * @return the service object, offering the specified interface.
     */
    public Object getServiceObject(String clazz) {
        return getServiceObject(clazz, null);
    }

    /**
     * Returns the service object associated with this service reference.
     *
     * @param ref service reference
     * @return the service object.
     */
    public <T> T getServiceObject(ServiceReference<T> ref) {
        if (ref != null) {
            m_references.add(ref);
            return context.getService(ref);
        } else {
            return null;
        }
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
        ServiceReference[] refs = getServiceReferences(itf, filter);
        if (refs != null) {
            Object[] list = new Object[refs.length];
            for (int i = 0; i < refs.length; i++) {
                m_references.add(refs[i]);
                list[i] = context.getService(refs[i]);
            }
            return list;
        } else {
            return new Object[0];
        }
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
        List<ServiceReference<T>> references = getServiceReferencesAsList(clazz, filter);
        List<T> list = new ArrayList<T>();

        for (ServiceReference<T> reference : references) {
            list.add(getServiceObject(reference));
            m_references.add(reference);
        }

        return list;
    }

    /**
     * Returns the service objects, offering the specified interface.
     *
     * @param clazz the interface
     * @return the service objects offering the specified interface.
     */
    public <T> List<T> getServiceObjects(Class<T> clazz) {
        return getServiceObjects(clazz, null);
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
        ServiceReference[] references = getServiceReferences(itf, filter);
        if (references == null  || references.length == 0) {
            return null;
        } else {
            return references[0];
        }
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
        ServiceReference<T>[] references = getServiceReferences(itf, filter);
        if (references == null  || references.length == 0) {
            return null;
        } else {
            return references[0];
        }
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
        return getServiceReference(itf, null);
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
        return getServiceReference(itf, null);
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
        String filter = "(" + Constants.SERVICE_PID + "=" + pid + ")";
        return getServiceReference(itf, filter);
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
        String filter = "(" + Constants.SERVICE_PID + "=" + pid + ")";
        return getServiceReference(itf, filter);
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
        ServiceReference[] references;
        try {
            references = context.getServiceReferences(itf, filter);
            if (references == null) {
                return new ServiceReference[0];
            } else {
                return references;
            }
        } catch (InvalidSyntaxException e) {
            fail(e.getMessage());
        }
        return null;
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
        List<ServiceReference<T>> list = getServiceReferencesAsList(itf, filter);
        if (list == null) {
            // Error case.
            return null;
        } else {
            //noinspection unchecked
            return list.<ServiceReference<T>>toArray(new ServiceReference[list.size()]);
        }
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
        Collection<ServiceReference<T>> references;
        List<ServiceReference<T>> list = new ArrayList<ServiceReference<T>>();
        try {
            references = context.getServiceReferences(itf, filter);
            if (references != null) {
                list.addAll(references);
            }
            return list;
        } catch (InvalidSyntaxException e) {
            fail(e.getMessage());
        }
        return null;
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
        return getServiceReferencesAsList(itf, null);
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
        if (timeout == 0) {
            timeout = 10000 * TimeUtils.TIME_FACTOR; // Default 10 seconds.
        } else {
            timeout = timeout * TimeUtils.TIME_FACTOR;
        }
        ServiceReference[] refs = getServiceReferences(itf, filter);
        long begin = System.currentTimeMillis();
        if (refs.length != 0) {
            return refs[0];
        } else {
            while (refs.length == 0) {
                try {
                    Thread.sleep(5 * TimeUtils.TIME_FACTOR);
                } catch (InterruptedException e) {
                    // Interrupted
                }
                long now = System.currentTimeMillis();

                if ((now - begin) > timeout) {
                    if (fail) {
                        fail("Timeout ... no services matching with the request after " + timeout + " ms");
                    } else {
                        System.err.println("Timeout ... no services matching with the request after " + timeout +
                                " ms");
                        return null;
                    }
                }
                refs = getServiceReferences(itf, filter);
            }
            return refs[0];
        }
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
    public ServiceReference waitForService(String itf, String filter, long timeout) {
        return waitForService(itf, filter, timeout, true);
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
        return waitForService(itf, filter, timeout, true);
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
        if (timeout == 0) {
            timeout = 10000; // Default 10 seconds.
        }
        ServiceReference<T>[] refs = getServiceReferences(itf, filter);
        long begin = System.currentTimeMillis();
        if (refs.length != 0) {
            return getServiceObject(refs[0]);
        } else {
            while (refs.length == 0) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    // Interrupted
                }
                long now = System.currentTimeMillis();

                if ((now - begin) > timeout) {
                    if (fail) {
                        fail("Timeout ... no services matching with the request after " + timeout + " ms");
                    } else {
                        System.err.println("Timeout ... no services matching with the request after " + timeout +
                                " ms");
                        return null;
                    }
                }
                refs = getServiceReferences(itf, filter);
            }
            return getServiceObject(refs[0]);
        }
    }
}
