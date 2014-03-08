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

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * A couple of method to ease interactions with the configuration admin.
 */
public class ConfigAdminHelper {

    private final BundleContext context;
    private final OSGiHelper osgi;

    public ConfigAdminHelper(BundleContext context) {
        this.context = context;
        this.osgi = new OSGiHelper(context);
    }

    public void dispose() {
        osgi.dispose();
    }

    public void deleteAllConfigurations() {
        ConfigurationAdmin admin = getConfigurationAdmin();
        if (admin == null) {
            return;
        }

        Configuration[] configurations = null;
        try {
            configurations = admin.listConfigurations(null);
        } catch (Exception e) {
            // Ignore it.
        }

        if (configurations != null) {
            for (Configuration configuration : configurations) {
                try {
                    System.out.println("Deleting configuration " + formatConfiguration(configuration));
                    configuration.delete();
                    System.out.println("Configuration " + formatConfiguration(configuration) + " deleted");
                } catch (Exception e) {
                    // Ignore it.
                }
            }
            // Wait a bit until all configurations are deleted.
            TimeUtils.grace(1000);
        }
    }

    private String formatConfiguration(Configuration configuration) {
        if (configuration.getFactoryPid() == null) {
            return configuration.getPid();
        } else {
            return configuration.getPid() + " (factory: " + configuration.getFactoryPid() + ")";
        }
    }

    public ConfigurationAdmin getConfigurationAdmin() {
        return osgi.getServiceObject(ConfigurationAdmin.class);
    }

}
