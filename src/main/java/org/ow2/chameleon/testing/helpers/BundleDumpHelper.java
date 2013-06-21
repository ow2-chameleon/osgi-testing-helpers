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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility methods to dump bundles.
 */
public class BundleDumpHelper {

    public static void dumpBundleFilesToDirectory(BundleContext context, File output) throws IOException {
        if (!output.exists()) {
            output.mkdirs();
        }

        for (Bundle bundle : context.getBundles()) {
            if (bundle.getBundleId() == 0) {
                continue; // Don't dump the system bundle.
            }
            System.out.println("Location : " + bundle.getLocation());

            if ("local".equals(bundle.getLocation())) {
                continue; // Pax Exam, when you hug me, I feel so...
            }

            URL location = new URL(bundle.getLocation());
            FileOutputStream outputStream = null;
            if (getBundleVersion(bundle) != null) {
                outputStream = new FileOutputStream(new File(output,
                        bundle.getSymbolicName() + "-" + getBundleVersion(bundle) + ".jar"));
            } else {
                outputStream = new FileOutputStream(new File(output, bundle.getSymbolicName() + ".jar"));
            }

            int read = 0;
            byte[] bytes = new byte[1024];

            InputStream inputStream = location.openStream();
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            inputStream.close();
            outputStream.close();
        }
    }

    public static String getBundleVersion(Bundle bundle) {
        return (String) bundle.getHeaders().get(Constants.BUNDLE_VERSION);
    }

}
