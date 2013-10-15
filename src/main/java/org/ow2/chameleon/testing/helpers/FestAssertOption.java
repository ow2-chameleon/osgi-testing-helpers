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

import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.options.DefaultCompositeOption;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * Pax Exam Option to deploy Fest Assert.
 */
public class FestAssertOption {

    public static Option festAssertAsInProject() {
        return new DefaultCompositeOption(
                wrappedBundle(maven("org.easytesting", "fest-assert").versionAsInProject()),
                wrappedBundle(maven("org.easytesting", "fest-util").versionAsInProject())
        );
    }

    public static Option festAssert(String festAssertVersion, String festUtilVersion) {
        return new DefaultCompositeOption(
                wrappedBundle(maven("org.easytesting", "fest-assert").version(festAssertVersion)),
                wrappedBundle(maven("org.easytesting", "fest-util").version(festUtilVersion))
        );
    }
}
