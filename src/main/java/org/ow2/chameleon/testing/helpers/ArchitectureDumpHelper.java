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

import org.apache.felix.ipojo.ComponentInstance;
import org.apache.felix.ipojo.Factory;
import org.apache.felix.ipojo.HandlerFactory;
import org.apache.felix.ipojo.architecture.Architecture;
import org.apache.felix.ipojo.architecture.InstanceDescription;
import org.osgi.framework.BundleContext;

/**
 * Dump all architectures.
 */
public class ArchitectureDumpHelper {

    public static void dumpArchitecture(BundleContext context) {
        OSGiHelper helper = new OSGiHelper(context);
        StringBuilder builder = new StringBuilder();
        try {
            for (Architecture arch : helper.getServiceObjects(Architecture.class)) {
                InstanceDescription description = arch.getInstanceDescription();
                builder.append("Instance : ").append(description.getName()).append(" - ").append(formatInstanceState
                        (description.getState())).append("\n");
                builder.append(description.getDescription().toString());
                builder.append("\n---------------------------------------\n");
            }
        } finally {
            helper.dispose();
        }
        System.out.println(builder.toString());
    }

    public static void dumpHandlers(BundleContext context) {
        OSGiHelper helper = new OSGiHelper(context);
        StringBuilder builder = new StringBuilder();
        try {
            for (HandlerFactory hf : helper.getServiceObjects(HandlerFactory.class)) {
                builder.append("Handler : ").append(hf.getHandlerName()).append(" - ").append(formatFactoryState(hf
                        .getState())).append("\n");
                builder.append(hf.getDescription().toString());
                builder.append("\n---------------------------------------\n");
            }
        } finally {
            helper.dispose();
        }
        System.out.println(builder.toString());
    }

    private static String formatInstanceState(int state) {
        switch (state) {
            case ComponentInstance.VALID:
                return "VALID";
            case ComponentInstance.INVALID:
                return "INVALID";
            case ComponentInstance.DISPOSED:
                return "DISPOSED";
            case ComponentInstance.STOPPED:
                return "STOPPED";
            default:
                return "UNKNOWN";
        }
    }

    private static String formatFactoryState(int state) {
        switch (state) {
            case Factory.VALID:
                return "VALID";
            case Factory.INVALID:
                return "INVALID";
            default:
                return "UNKNOWN";
        }
    }

}
