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

import java.io.File;
import java.io.InputStream;
import java.util.*;


public class BundleContextStub implements BundleContext {

    private Map<String, String> m_properties = new HashMap<String, String>();
    private List<ServiceRegistrationStub> m_services = new ArrayList<ServiceRegistrationStub>();
    private BundleStub m_bundle;


    public BundleContextStub(BundleStub bundle) {
        m_bundle = bundle;
        m_bundle.setBundleContext(this);
    }

    public BundleContextStub addProperty(String name, String value) {
        m_properties.put(name, value);
        return this;
    }

    public BundleContextStub addService(String svc, Object svcObject) {
        m_services.add(new ServiceRegistrationStub(svc, svcObject));
        return this;
    }

    public String getProperty(String s) {
        return m_properties.get(s);
    }

    public Bundle getBundle() {
        return m_bundle;
    }

    public Bundle installBundle(String s) throws BundleException {
        throw new UnsupportedOperationException("Stub");
    }

    public Bundle installBundle(String s, InputStream inputStream) throws BundleException {
        throw new UnsupportedOperationException("Stub");
    }

    public Bundle getBundle(long l) {
        throw new UnsupportedOperationException("Stub");
    }

    public Bundle[] getBundles() {
        throw new UnsupportedOperationException("Stub");
    }

    public void addServiceListener(ServiceListener serviceListener, String s) throws InvalidSyntaxException {
        throw new UnsupportedOperationException("Stub");
    }

    public void addServiceListener(ServiceListener serviceListener) {
        throw new UnsupportedOperationException("Stub");
    }

    public void removeServiceListener(ServiceListener serviceListener) {
        throw new UnsupportedOperationException("Stub");
    }

    public void addBundleListener(BundleListener bundleListener) {
        throw new UnsupportedOperationException("Stub");
    }

    public void removeBundleListener(BundleListener bundleListener) {
        throw new UnsupportedOperationException("Stub");
    }

    public void addFrameworkListener(FrameworkListener frameworkListener) {
        throw new UnsupportedOperationException("Stub");
    }

    public void removeFrameworkListener(FrameworkListener frameworkListener) {
        throw new UnsupportedOperationException("Stub");
    }

    public ServiceRegistration registerService(String[] itfs, Object svc, Dictionary props) {
        throw new UnsupportedOperationException("Stub");
    }

    public ServiceRegistration registerService(String s, Object o, Dictionary dictionary) {
        ServiceRegistrationStub reg = new ServiceRegistrationStub(s, o);
        m_services.add(reg);
        return reg;
    }

    public ServiceReference[] getServiceReferences(String itf, String filter) throws InvalidSyntaxException {
        List<ServiceReference> refs = new ArrayList<ServiceReference>();
        for (ServiceRegistrationStub reg: m_services) {
            if (itf == null) {
                refs.add(reg.getReference());
            } else if (itf.equals(reg.m_interface)) {
                refs.add(reg.getReference());
            }
        }
        if (refs.size() == 0) {
            return null;
        }
        return refs.toArray(new ServiceReference[refs.size()]);
    }

    public ServiceReference[] getAllServiceReferences(String s, String s1) throws InvalidSyntaxException {
        List<ServiceReference> refs = new ArrayList<ServiceReference>();
        for (ServiceRegistrationStub reg: m_services) {
            refs.add(reg.getReference());
        }
        return refs.toArray(new ServiceReference[refs.size()]);
    }

    public ServiceReference getServiceReference(String itf) {
        for (ServiceRegistrationStub reg: m_services) {
            if (itf.equals(reg.m_interface)) {
                return reg.getReference();
            }
        }
        return null;
    }

    public Object getService(ServiceReference serviceReference) {
        return ((ServiceReferenceStub) serviceReference).m_reg.m_svcObject;
    }

    public boolean ungetService(ServiceReference serviceReference) {
        return false;
    }

    public File getDataFile(String s) {
        throw new UnsupportedOperationException("Stub");
    }

    public Filter createFilter(String s) throws InvalidSyntaxException {
        throw new UnsupportedOperationException("Stub");
    }

    class ServiceRegistrationStub implements  ServiceRegistration {

        public ServiceReferenceStub m_ref = new ServiceReferenceStub(this);
        public String m_interface;
        public Object m_svcObject;

        public ServiceRegistrationStub(String itf, Object svcObject) {
            m_interface = itf;
            m_svcObject = svcObject;
        }

        public ServiceReference getReference() {
            return m_ref;
        }

        public void setProperties(Dictionary dictionary) {
            return;
        }

        public void unregister() {
            m_services.remove(this);
            m_ref = null;
        }
    }

    class ServiceReferenceStub implements ServiceReference {

        public ServiceRegistrationStub m_reg;

        public ServiceReferenceStub(ServiceRegistrationStub reg) {
            m_reg = reg;
        }

        public Object getProperty(String s) {
            return null;
        }

        public String[] getPropertyKeys() {
            return new String[0];
        }

        public Bundle getBundle() {
            return null;
        }

        public Bundle[] getUsingBundles() {
            return null;
        }

        public boolean isAssignableTo(Bundle bundle, String s) {
            return true;
        }
    }


}
