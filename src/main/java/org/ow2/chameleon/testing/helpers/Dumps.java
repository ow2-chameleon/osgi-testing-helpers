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

import org.osgi.framework.BundleContext;

import java.io.File;
import java.io.IOException;

/**
 * Dump utilities.
 */
public class Dumps {

    public static void threadDumps() {
        ThreadDumpHelper.threadDumps();
    }

    public static void dumpBundleFilesToDirectory(BundleContext context, File directory) throws IOException {
         BundleDumpHelper.dumpBundleFilesToDirectory(context, directory);
    }

    public static void dumpArchitectures(BundleContext context) {
        ArchitectureDumpHelper.dumpArchitecture(context);
    }

    public static void dumpHandlers(BundleContext context) {
        ArchitectureDumpHelper.dumpHandlers(context);
    }



}
