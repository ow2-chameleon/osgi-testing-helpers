package org.ow2.chameleon.testing.helpers;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
* Service Reference Stub.
*/
class ServiceReferenceStub implements ServiceReference {

    public ServiceRegistrationStub m_reg;

    public ServiceReferenceStub(ServiceRegistrationStub reg) {
        m_reg = reg;
    }

    public Object getProperty(String s) {
        return m_reg.m_properties.get(s);
    }

    public String[] getPropertyKeys() {
        List<String> keys = new ArrayList<String>();
        Enumeration<String> ks = m_reg.m_properties.keys();
        while (ks.hasMoreElements()) {
            keys.add(ks.nextElement());
        }
        return keys.toArray(new String[keys.size()]);
    }

    public Bundle getBundle() {
        return m_reg.m_context.getBundle();
    }

    public Bundle[] getUsingBundles() {
        return new Bundle[0];
    }

    public boolean isAssignableTo(Bundle bundle, String s) {
        return true;
    }

    public int compareTo(Object reference) {
        return 0;
    }
}
