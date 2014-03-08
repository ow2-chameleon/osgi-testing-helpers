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

import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.options.CompositeOption;
import org.ow2.chameleon.testing.helpers.constants.Constants;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.jar.JarFile;


/**
 * Pax Exam Option to deploy a chameleon. The chameleon must be unzipped somewhere.
 * This is generally done using the maven-dependency-plugin:
 * <pre>
 * &lt;plugin&gt;
 *   &lt;groupId&gt;org.apache.maven.plugins&lt;/groupId&gt;
 *   &lt;artifactId&gt;maven-dependency-plugin&lt;/artifactId&gt;
 *   &lt;executions&gt;
 *       &lt;execution&gt;
 *           &lt;id&gt;unzip-chameleon&lt;/id&gt;
 *           &lt;phase&gt;compile&lt;/phase&gt;
 *           &lt;goals&gt;
 *               &lt;goal&gt;unpack-dependencies&lt;/goal&gt;
 *           &lt;/goals&gt;
 *           &lt;configuration&gt;
 *               &lt;outputDirectory&gt;${project.build.directory}/chameleon&lt;/outputDirectory&gt;
 *               &lt;overWriteReleases&gt;false&lt;/overWriteReleases&gt;
 *               &lt;overWriteSnapshots&gt;true&lt;/overWriteSnapshots&gt;
 *               &lt;excludeTransitive&gt;true&lt;/excludeTransitive&gt;
 *               &lt;includeArtifactIds&gt;CHAMELEON&lt;/includeArtifactIds&gt;
 *               &lt;includeTypes&gt;zip&lt;/includeTypes&gt;
 *           &lt;/configuration&gt;
 *       &lt;/execution&gt;
 *   &lt;/executions&gt;
 * &lt;/plugin&gt;
 * </pre>
 * Once the chameleon is unzipped, this option takes care to configure the chameleon correctly:
 * <ul>
 * <li>configure the boot classpath and expose chameleon packages</li>
 * <li>deploy bundles from core, runtime and application</li>
 * <li>configuration of the deploy folder</li>
 * <li>deploy the osgi-helper</li>
 * <li>loads the chameleon properties and system.properties</li>
 * </ul>
 *
 * Chameleon properties and system properties can be overridden / extended using {@link ChameleonOption#chameleonProperties(Hashtable)}
 * and  {@link ChameleonOption#chameleonSystemProperties(Hashtable)}
 *
 */
public class ChameleonOption implements CompositeOption {

    private File m_chameleonBaseDir;
    private String m_runtimeDir = "runtime";
    private String m_applicationDir = "application";
    private String m_deploy = "deploy";
    private File m_chameleonPropsFile;
    private String m_chameleonPropsName = "chameleon.properties";
    private Hashtable<String, String> m_chameleonProperties;
    private File m_chameleonSystemPropsFile;
    private Hashtable<String, String> m_chameleonSystemProperties;
    private String m_chameleonSystemPropsName = "system.properties";

    public static ChameleonOption chameleon() {
        return new ChameleonOption();
    }

    public static ChameleonOption chameleon(File dir) {
        return new ChameleonOption().chameleonDirectory(dir);
    }

    public static ChameleonOption chameleon(String dir) {
        return new ChameleonOption().chameleonDirectory(dir);
    }

    public ChameleonOption chameleonDirectory(File dir) {
        m_chameleonBaseDir = dir;
        return this;
    }

    public ChameleonOption chameleonDirectory(String dir) {
        m_chameleonBaseDir = new File(dir);
        return this;
    }

    public ChameleonOption runtime(String directoryName) {
        m_runtimeDir = directoryName;
        return this;
    }

    public ChameleonOption application(String directoryName) {
        m_applicationDir = directoryName;
        return this;
    }

    public ChameleonOption deploy(String directoryName) {
        m_deploy = directoryName;
        return this;
    }

    public ChameleonOption chameleonProperties(File props) {
        m_chameleonPropsFile = props;
        return this;
    }

    public ChameleonOption chameleonProperties(Hashtable<String, String> props) {
        m_chameleonProperties = props;
        return this;
    }

    public ChameleonOption chameleonProperties(String props) {
        m_chameleonPropsName = props;
        return this;
    }

    public ChameleonOption chameleonSystemProperties(File props) {
        m_chameleonSystemPropsFile = props;
        return this;
    }

    public ChameleonOption chameleonSystemProperties(Hashtable<String, String> props) {
        m_chameleonSystemProperties = props;
        return this;
    }

    public ChameleonOption chameleonSystemProperties(String props) {
        m_chameleonSystemPropsName = props;
        return this;
    }

    public Option[] getOptions() {
        Option[] options = new Option[0];
        // First we need to be sure it's a chameleon
        if (m_chameleonBaseDir == null) {
            throw new IllegalStateException("The chameleon directory must be set");
        }

        if (! m_chameleonBaseDir.isDirectory()) {
            throw new IllegalStateException("The chameleon directory must be a directory : " + m_chameleonBaseDir.getAbsolutePath());
        }

        File chameleonCore = new File(m_chameleonBaseDir, "core");

        File chameleonRuntime = new File(m_chameleonBaseDir, m_runtimeDir);
        File chameleonApp = new File(m_chameleonBaseDir, m_applicationDir);

        if (! chameleonCore.isDirectory()) {
            throw new IllegalStateException("Invalid chameleon core directory: " + chameleonCore.getAbsolutePath());
        } else {
            options = addChameleonCoreOptions(chameleonCore, options);
            options = addBundles(chameleonCore, options);
        }

        if (chameleonRuntime.isDirectory()) {
            options = addBundles(chameleonRuntime, options);
        }

        if (chameleonApp.isDirectory()) {
            options = addBundles(chameleonApp, options);
        }

        File chameleonDeploy = new File(m_chameleonBaseDir, m_deploy);
        if (chameleonDeploy.isDirectory()) {
            options = OptionUtils.combine(options,
                    CoreOptions.systemProperty("felix.fileinstall.dir").value(chameleonDeploy.getAbsolutePath()));
        }

        Hashtable<String, String> propertyCollector = new Hashtable<String, String>();
        File defaultsysProps = new File(m_chameleonBaseDir, m_chameleonSystemPropsName);
        if (defaultsysProps.isFile()) {
            loadPropertiesFromFile(defaultsysProps, propertyCollector);
        }

        if (m_chameleonSystemPropsFile != null  && m_chameleonSystemPropsFile.isFile()) {
            loadPropertiesFromFile(m_chameleonSystemPropsFile, propertyCollector);
        }

        if (m_chameleonSystemProperties != null) {
            propertyCollector.putAll(m_chameleonSystemProperties);
        }

        File defaultProps = new File(m_chameleonBaseDir, m_chameleonPropsName);
        if (defaultProps.isFile()) {
            loadPropertiesFromFile(defaultProps, propertyCollector);
        }

        if (m_chameleonPropsFile != null  && m_chameleonPropsFile.isFile()) {
            loadPropertiesFromFile(m_chameleonPropsFile, propertyCollector);
        }

        if (m_chameleonProperties != null) {
            propertyCollector.putAll(m_chameleonProperties);
        }

        options = addProperties(options, propertyCollector);

        return options;
    }

    private Option[] addChameleonCoreOptions(File chameleonCore,
            Option[] options) {
        String slf4jApi = getVersionForFile(chameleonCore, "slf4j-api");
        if (slf4jApi == null) {
            throw new IllegalStateException("Cannot extract slf4j-api version");
        }

        String logback = getVersionForFile(chameleonCore, "logback-core");
        if (logback == null) {
            throw new IllegalStateException("Cannot extract logback-core version");
        }

        String core = getVersionForFile(chameleonCore, "core");
        if (core == null) {
            throw new IllegalStateException("Cannot extract chameleon core version");
        }

        return CoreOptions.options(
                CoreOptions
                .bootClasspathLibraries(new String[] {
                        "mvn:org.slf4j/slf4j-api/" + slf4jApi,
                        "mvn:ch.qos.logback/logback-classic/" + logback,
                        "mvn:ch.qos.logback/logback-core/" + logback,
                        "mvn:org.ow2.chameleon/core/" + core }),

                CoreOptions.systemPackage("org.slf4j; version=" + slf4jApi),
        CoreOptions.systemPackage("org.slf4j.impl; version=" + slf4jApi),
        CoreOptions.systemPackage("org.slf4j.spi; version=" + slf4jApi),
        CoreOptions.systemPackage("org.slf4j.helpers; version=" + slf4jApi),


        CoreOptions.systemProperty("chameleon.basedir").value(
                chameleonCore.getAbsolutePath()),

        CoreOptions.bootDelegationPackages("sun.*"),

        CoreOptions.provision("mvn:org.ow2.chameleon.testing/osgi-helpers/" + Constants.VERSION)
        );

    }

    private String getVersionForFile(File directory, final String fileName) {
        File[] files = directory.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return file.getName().startsWith(fileName + "-")  && file.getName().endsWith(".jar");
            }
        });

        if (files.length > 0) {
            String version = files[0].getName().substring(fileName.length() + 1, files[0].getName().length() - 4); // 4 = .jar
            return version;
        } else {
            return null;
        }
    }

    private Option[] addBundles(File chameleonCore, Option[] options) {
        File[] files = chameleonCore.listFiles(new FileFilter() {

            public boolean accept(File file) {
                return file.getName().endsWith(".jar")
                    && isBundle(file)
                    && ! isExcluded(file);
            }
        });

        List<Option> newOptions = new ArrayList<Option>();
        for (File f : files) {
            try {
                Option opt = CoreOptions.provision(f.toURI().toURL().toExternalForm());
                newOptions.add(opt);

            } catch (MalformedURLException e) {
                // ignore it.
            }
        }

        return OptionUtils.combine(options, newOptions.toArray(new Option[newOptions.size()]));
    }

    private void loadPropertiesFromFile(File file, Hashtable<String, String> to) {
        try {
            Properties props = new Properties();
            FileReader reader = new FileReader(file);
            props.load(reader);
            reader.close();
            Set<String> names = props.stringPropertyNames();
            for (String k : names) {
                to.put(k, props.get(k).toString());
            }
        } catch (IOException e) {
            System.err.println("Cannot read " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    private Option[] addProperties(Option[] options, Hashtable<String, String> props) {
        Set<String> names = props.keySet();
        Option[] newOptions = options;
        for (String n : names) {
            String v = props.get(n);
            newOptions = OptionUtils.combine(newOptions, CoreOptions.systemProperty(n).value(v));
        }
        return newOptions;
    }


    private boolean isExcluded(File file) {
        String name = file.getName();
        return (name.startsWith("org.apache.felix.framework-")
                ||  name.startsWith("logback-")
                ||  name.startsWith("slf4j-")
                || name.startsWith("org.apache.felix.gogo.shell-")
                || name.startsWith("org.apache.felix.shell")
                || name.startsWith("org.apache.felix.ipojo.arch-"));
    }

    private boolean isBundle(File file) {
        try {
            JarFile jar = new JarFile(file);
            return jar.getManifest().getMainAttributes()
                .getValue("Bundle-ManifestVersion") != null;
        } catch (IOException e) {
            // Ignore.
        }
        return false;
    }

}
