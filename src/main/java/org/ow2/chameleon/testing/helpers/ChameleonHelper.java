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

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * Helper class initializing a chameleon.
 * This class is used with the {@link ChameleonOption}. The helper deploys instances (.cfg files)
 * from the runtime and application folders of the targeted chameleon.
 *
 * <code>
 *  @RunWith(JUnit4TestRunner.class)
 *  public class MCDServerTest {
 *
 *   @Inject
 *   private BundleContext context;
 *
 *   private OSGiHelper osgi;
 *   private IPOJOHelper ipojo;
 *   private ChameleonHelper chameleon;
 *
 *   @Configuration
 *   public static Option[] configure() throws Exception {
 *       return ChameleonOption.chameleon(new File("target/chameleon")).getOptions();
 *   }
 *
 *   @Before
 *   public void setup() throws Exception {
 *       osgi = new OSGiHelper(context);
 *       ipojo = new IPOJOHelper(context);
 *       chameleon = new ChameleonHelper(context);
 *   }
 *
 *   @After
 *   public void tearDown() {
 *       osgi.dispose();
 *       ipojo.dispose();
 *       chameleon.dispose();
 *   }
 *
 *   ...
 * </code>
 *
 */
public class ChameleonHelper {


    private List<Configuration> m_configurations = new ArrayList<Configuration>();

    public ChameleonHelper(BundleContext context) throws IOException {
        m_configurations.clear();

        // Look for all cfg file
        String basedir = System.getProperty("chameleon.basedir");
        File root = new File(basedir);
        if (! root.isDirectory() ) {
            throw new IllegalStateException("Chameleon base directory not found : " + root.getAbsolutePath());
        }

        File chameleonRuntime = new File(root, "runtime"); //TODO Works only if this is the default directory
        File chameleonApp = new File(root, "application"); //TODO Works only if this is the default directory

        ConfigurationAdmin ca = getConfigurationAdmin(context);
        if (ca != null) {
            if (chameleonRuntime.isDirectory()) {
                manageConfiguration(chameleonRuntime, ca);
            }
            if (chameleonApp.isDirectory()) {
                manageConfiguration(chameleonApp, ca);
            }
        }

    }

    private void manageConfiguration(File chameleonRuntime,
            ConfigurationAdmin ca) throws IOException {
        File[] configurations = chameleonRuntime.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return (file.getName().endsWith(".cfg"));
            }
        });

        parseConfiguration(ca, configurations);

    }

    private void parseConfiguration(ConfigurationAdmin ca, File[] configs)
            throws IOException {
        for (File f : configs) {
            Properties p = new Properties();
            InputStream in = new FileInputStream(f);
            p.load(in);
            in.close();
            String[] pid = parsePid(f.getName());
            Hashtable ht = new Hashtable();
            ht.putAll(p);
            Configuration config = getConfiguration(pid[0], pid[1], ca);
            if (config.getBundleLocation() != null) {
                config.setBundleLocation(null);
            }
            config.update(ht);
            m_configurations.add(config);
        }

    }

    /**
     * Gets the configuration admin service.
     * @param context the bundle context.
     * @return the Configuration Admin service object
     * @throws Exception if the configuration admin is unavailable.
     */
    private static ConfigurationAdmin getConfigurationAdmin(BundleContext context) {

        OSGiHelper helper = new OSGiHelper(context);
        try {
            helper.waitForService(ConfigurationAdmin.class.getName(), null, 3000);
        } catch (Throwable e) {
            // ignore it.
        }

        // Should be there !
        ServiceReference ref = context
                .getServiceReference(ConfigurationAdmin.class.getName());
        if (ref != null) {
            return (ConfigurationAdmin) context.getService(ref);
        }

        return null;
    }

    /**
     * Parses cfg file associated PID. This supports both ManagedService PID and
     * ManagedServiceFactory PID
     * @param path the path
     * @return structure {pid, factory pid} or {pid, <code>null</code> if not a
     *         Factory configuration.
     */
    static String[] parsePid(String path) {
        String pid = path.substring(0, path.length() - ".cfg".length());
        int n = pid.indexOf('-');
        if (n > 0) {
            String factoryPid = pid.substring(n + 1);
            pid = pid.substring(0, n);
            return new String[] {pid, factoryPid};
        } else {
            return new String[] {pid, null};
        }
    }

    /**
     * Gets a Configuration object.
     * @param pid the pid
     * @param factoryPid the factory pid
     * @param cm the config admin service
     * @return the Configuration object (used to update the configuration)
     * @throws Exception if the Configuration object cannot be retrieved
     */
    static Configuration getConfiguration(String pid, String factoryPid,
            ConfigurationAdmin cm) throws IOException {
        Configuration newConfiguration = null;
        if (factoryPid != null) {
            newConfiguration = cm.createFactoryConfiguration(pid, null);
        } else {
            newConfiguration = cm.getConfiguration(pid, null);
        }
        return newConfiguration;
    }

    public void dispose() {
        for (Configuration conf : m_configurations) {
            try {
                conf.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        m_configurations.clear();
    }



}
