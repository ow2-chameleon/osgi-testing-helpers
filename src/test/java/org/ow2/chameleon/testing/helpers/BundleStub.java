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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class BundleStub implements Bundle {

    private BundleContext m_bundleContext;

    public void setBundleContext(BundleContext bc) {
        m_bundleContext = bc;
    }

    public int getState() {
        return 0;
    }

    public void start(int options) throws BundleException {

    }

    public void start() throws BundleException {

    }

    public void stop(int options) throws BundleException {

    }

    public void stop() throws BundleException {

    }

    public void update() throws BundleException {

    }

    public void update(InputStream inputStream) throws BundleException {

    }

    public void uninstall() throws BundleException {

    }

    public Dictionary getHeaders() {
        return null;
    }

    public BundleContext getBundleContext() {
        return m_bundleContext;
    }

    public Map<X509Certificate, List<X509Certificate>> getSignerCertificates(int signersType) {
        return null;
    }

    public Version getVersion() {
        return new Version(1,0,0);
    }

    public <A> A adapt(Class<A> type) {
        return (A) this;
    }

    public File getDataFile(String filename) {
        return null;
    }

    public long getBundleId() {
        return 0;
    }

    public String getLocation() {
        return null;
    }

    public ServiceReference[] getRegisteredServices() {
        return new ServiceReference[0];
    }

    public ServiceReference[] getServicesInUse() {
        return new ServiceReference[0];
    }

    public boolean hasPermission(Object o) {
        return false;
    }

    public URL getResource(String s) {
        return null;
    }

    public Dictionary getHeaders(String s) {
        return null;
    }

    public String getSymbolicName() {
        return null;
    }

    public Class loadClass(String s) throws ClassNotFoundException {
        return null;
    }

    public Enumeration getResources(String s) throws IOException {
        return null;
    }

    public Enumeration getEntryPaths(String s) {
        return null;
    }

    public URL getEntry(String s) {
        return null;
    }

    public long getLastModified() {
        return 0;
    }

    public Enumeration findEntries(String s, String s1, boolean b) {
        return null;
    }

    public int compareTo(Bundle o) {
        return 0;
    }
}
