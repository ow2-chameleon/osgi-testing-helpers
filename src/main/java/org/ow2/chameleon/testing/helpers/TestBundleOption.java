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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.tinybundles.core.TinyBundle;
import org.ops4j.pax.tinybundles.core.TinyBundles;
import org.osgi.framework.Constants;
import org.ow2.chameleon.testing.tinybundles.ipojo.IPOJOStrategy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.ops4j.pax.exam.CoreOptions.bundle;

/**
 * Builds a bundle from the src/main/java sources.
 */
public class TestBundleOption {

    public static Option testBundle(List<String> extraExports, boolean deleteTestBundle) {
        File out = new File("target/tested/test-bundle.jar");
        if (out.exists()) {
            if (deleteTestBundle) {
                out.delete();
            } else {
                try {
                    return bundle(out.toURI().toURL().toExternalForm());
                } catch (MalformedURLException e) {
                    // Ignore it.
                }
            }
        }

        TinyBundle tested = TinyBundles.bundle();

        // We look inside target/classes to find the class and resources
        File classes = new File("target/classes");
        Collection<File> files = FileUtils.listFilesAndDirs(classes, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        List<String> exports = new ArrayList<String>();
        for (File file : files) {
            if (file.isDirectory()) {
                // By convention we export of .services and .service package
                if (file.getAbsolutePath().contains("/services")  || file.getAbsolutePath().contains("/service")) {
                    String path = file.getAbsolutePath().substring(classes.getAbsolutePath().length() +1);
                    String packageName = path.replace('/', '.');
                    exports.add(packageName);
                }
            } else {
                // We need to compute the path
                String path = file.getAbsolutePath().substring(classes.getAbsolutePath().length() +1);
                try {
                    tested.add(path, file.toURI().toURL());
                } catch (MalformedURLException e) {
                    // Ignore it.
                }
                System.out.println(file.getName() + " added to " + path);
            }
        }

        // Added extra imports.
        exports.addAll(extraExports);

        String clause = "";
        for (String export : exports) {
            if (export.length() > 0) { export += ", "; }
            clause += export;
        }

        System.out.println("Exported packages : " + clause);

        InputStream inputStream = tested
                .set(Constants.BUNDLE_SYMBOLICNAME, BaseTest.TEST_BUNDLE_SYMBOLIC_NAME)
                .set(Constants.IMPORT_PACKAGE, "*")
                .set(Constants.EXPORT_PACKAGE, clause)
                .build(IPOJOStrategy.withiPOJO(new File("src/main/resources")));

        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, out);
            return bundle(out.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Cannot compute the url of the manipulated bundle");
        } catch (IOException e) {
            throw new RuntimeException("Cannot write of the manipulated bundle");
        }
    }



}
