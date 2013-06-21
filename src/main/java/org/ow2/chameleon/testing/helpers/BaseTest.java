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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.exam.spi.reactors.PerMethod;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ow2.chameleon.testing.helpers.IPOJOOption.iPOJO;
import static org.ow2.chameleon.testing.helpers.IPOJOOption.iPOJOComposite;
import static org.ow2.chameleon.testing.helpers.Stability.waitForStability;
import static org.ow2.chameleon.testing.helpers.TestBundleOption.testBundle;
import static org.ow2.chameleon.testing.helpers.TimeUtils.grace;

/**
 * A class extended by pax exam test case.
 * It contains common bundles and startup options.
 * <p/>
 * By default it builds a bundle from src/main/java and deploys it.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class BaseTest {

    /**
     * The bundle symbolic name of the test bundle.
     */
    public static final String TEST_BUNDLE_SYMBOLIC_NAME = "test-bundle";
    @Inject
    protected org.osgi.framework.BundleContext bc;
    /**
     * The bundle context, available as `bc` and `context`.
     */
    protected BundleContext context;
    /**
     * The OSGi Helper.
     */
    protected OSGiHelper osgiHelper;
    /**
     * The iPOJO Helper.
     */
    protected IPOJOHelper ipojoHelper;


    // Customizable configuration.
    protected boolean quiet = true;
    protected boolean iPOJO = true;
    protected boolean iPOJOComposite = false;
    protected boolean configadmin = false;
    protected boolean testBundle = true;
    protected boolean mockito = false;

    /**
     * The default configuration.
     *
     * @return the set of options.
     * @throws IOException
     */
    public Option[] defaultConfiguration() throws IOException {
        if (quiet()) {
            Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
            root.setLevel(Level.INFO);
        }

        Option[] options = options(
                cleanCaches()
        );

        if (deployiPOJO()) {
            options = OptionUtils.combine(options, iPOJO());
        }

        if (deployiPOJOComposite()) {
            options = OptionUtils.combine(options, iPOJOComposite());
        }

        if (deployConfigAdmin()) {
            options = OptionUtils.combine(options, IPOJOOption.configadmin());
        }

        if (deployTestBundle()) {
            options = OptionUtils.combine(options, testBundle(getExtraExports()));
        }

        if (deployMockito()) {
            options = OptionUtils.combine(options, MockitoOption.junitAndMockitoBundles());
        } else {
            options = OptionUtils.combine(options, junitBundles());
        }

        options = OptionUtils.combine(options, getCustomOptions());

        if (quiet()) {
            options = OptionUtils.combine(options,
                    systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN"));
        }

        String tu = System.getenv("TIME_FACTOR");
        if (tu == null) {
            tu = System.getProperty("TIME_FACTOR");
        }

        if (tu != null) {
            options = OptionUtils.combine(options,
                    systemProperty("TIME_FACTOR").value(tu));
        } else {
            options = OptionUtils.combine(options,
                    systemProperty("TIME_FACTOR").value("1"));
        }

        return options;
    }

    /**
     * Method to override to add custom options.
     *
     * @return the extra options.
     */
    protected Option[] getCustomOptions() {
        return new Option[0];
    }

    /**
     * Method to override to add extra export to the test bundle.
     * By default all `service` and `services` packages are exported.
     *
     * @return the extra packages.
     */
    protected List<String> getExtraExports() {
        return new ArrayList<String>();
    }

    @Configuration
    public Option[] config() throws IOException {
        return defaultConfiguration();
    }

    /**
     * Method to override to disable the quiet mode.
     *
     * @return true to enable the quiet mode
     */
    public boolean quiet() {
        return quiet;
    }

    /**
     * Method to override to instruct pax exam to not deploy iPOJO.
     *
     * @return true to deploy iPOJO
     */
    public boolean deployiPOJO() {
        return iPOJO;
    }

    /**
     * Method to override to instruct pax exam to deploy iPOJO Composite.
     *
     * @return true to deploy iPOJO Composites, false by default
     */
    public boolean deployiPOJOComposite() {
        return iPOJOComposite;
    }

    /**
     * Method to override to instruct pax exam to deploy the Felix Config Admin.
     *
     * @return true to deploy the Felix Config Admin, false by default
     */
    public boolean deployConfigAdmin() {
        return configadmin;
    }

    /**
     * Method to override to instruct pax exam to not deploy the test bundle.
     *
     * @return true to deploy the test bundle.
     */
    public boolean deployTestBundle() {
        return testBundle;
    }

    /**
     * Method to override to instruct pax exam to deploy Mockito and junit bundles.
     *
     * @return true to deploy Mockito and related bundles, false by default
     */
    public boolean deployMockito() {
        return mockito;
    }

    // ==== Code executed on OSGi ===

    @Before
    public void commonSetUp() {
        TimeUtils.TIME_FACTOR = Integer.getInteger("TIME_FACTOR", 1);
        if (TimeUtils.TIME_FACTOR > 1) {
            System.out.println("Time Factor set to " + TimeUtils.TIME_FACTOR);
        } else {
            System.out.println("Time Factor set to " + TimeUtils.TIME_FACTOR);
        }

        System.gc();

        osgiHelper = new OSGiHelper(bc);
        ipojoHelper = new IPOJOHelper(bc);
        context = bc;

        // Dump OSGi Framework information
        String vendor = (String) osgiHelper.getBundle(0).getHeaders().get(Constants.BUNDLE_VENDOR);
        if (vendor == null) {
            vendor = (String) osgiHelper.getBundle(0).getHeaders().get(Constants.BUNDLE_SYMBOLICNAME);
        }
        String version = (String) osgiHelper.getBundle(0).getHeaders().get(Constants.BUNDLE_VERSION);
        System.out.println("OSGi Framework : " + vendor + " - " + version);

        waitForStability(bc);
    }

    @After
    public void commonTearDown() {
        ipojoHelper.dispose();
        osgiHelper.dispose();
        grace(500);
    }

    /**
     * Gets the test bundle.
     *
     * @return the test bundle
     */
    public Bundle getTestBundle() {
        return osgiHelper.getBundle(TEST_BUNDLE_SYMBOLIC_NAME);
    }

    public boolean isKnopflerfish() {
        return FrameworkHelper.isKnopflerfish(bc);
    }

    public BundleContext getContext() {
        return bc;
    }

}
