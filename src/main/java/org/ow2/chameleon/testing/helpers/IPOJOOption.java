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

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.options.CompositeOption;
import org.ops4j.pax.exam.options.DefaultCompositeOption;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

/**
 * Pax Exam Option to deploy iPOJO.
 */
public class IPOJOOption {


    public static CompositeOption iPOJO() {
        return new DefaultCompositeOption(
                mavenBundle("org.apache.felix", "org.apache.felix.ipojo").versionAsInProject(),
                mavenBundle("org.ow2.chameleon.testing", "osgi-helpers").versionAsInProject()
        );
    }

    public static Option iPOJOComposite() {
        return mavenBundle("org.apache.felix", "org.apache.felix.ipojo.composite").versionAsInProject();
    }

    public static Option configadmin() {
        return mavenBundle("org.apache.felix", "org.apache.felix.configadmin").versionAsInProject();
    }


}
