package org.ow2.chameleon.testing.helpers;

import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Service Registration Stub.
 */
class ServiceRegistrationStub implements ServiceRegistration {

    public final String m_interface;
    public final Object m_svcObject;
    final Dictionary<String, ?> m_properties;
    final BundleContextStub m_context;
    public ServiceReferenceStub m_ref = new ServiceReferenceStub(this);

    public ServiceRegistrationStub(BundleContextStub bundleContextStub, String itf, Object svcObject) {
        this(bundleContextStub, itf, svcObject, new Hashtable<String, Object>());
    }

    public ServiceRegistrationStub(BundleContextStub context, String itf, Object svcObject,
                                   Dictionary<String, ?> properties) {
        this.m_context = context;
        m_interface = itf;
        m_svcObject = svcObject;
        m_properties = properties;
    }

    public ServiceReference getReference() {
        return m_ref;
    }

    public void setProperties(Dictionary dictionary) {
        throw new UnsupportedOperationException("Operation not supported");
    }

    public void unregister() {
        m_context.m_services.remove(this);
        m_ref = null;
    }
}
