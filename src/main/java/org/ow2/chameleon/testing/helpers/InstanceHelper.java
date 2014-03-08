/*
* Copyright 2014 OW2 Chameleon
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
import org.apache.felix.ipojo.architecture.Architecture;
import org.osgi.framework.BundleContext;

/**
 * Lookup and Architecture.
 */
public class InstanceHelper extends AbstractHelper {
    private final OSGiHelper helper;

    public InstanceHelper(BundleContext context, OSGiHelper helper) {
        super(context);
        this.helper = helper;
    }

    @Override
    public void dispose() {
        //To change body of implemented methods use File | Settings | File Templates.
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
        return helper.getServiceObject(Architecture.class, "(architecture.instance=" + name + ")");
    }
}
