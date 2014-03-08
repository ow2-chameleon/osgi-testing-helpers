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

import org.apache.felix.ipojo.metadata.Element;
import org.apache.felix.ipojo.parser.ManifestMetadataParser;
import org.apache.felix.ipojo.parser.ParseException;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves iPOJO metadata
 */
public class MetadataHelper extends AbstractHelper {
    public MetadataHelper(BundleContext context) {
        super(context);
    }

    @Override
    public void dispose() {
        // Nothing to cleanup
    }

    /**
     * Returns the metadata description of the component with the given name,
     * defined in the given bundle.
     *
     * @param bundle    the bundle from which the component is defined.
     * @param component the name of the defined component.
     * @return the metadata description of the component with the given name,
     *         defined in the given bundle, or {@code null} if not found.
     */
    public static Element getMetadata(Bundle bundle, String component) {

        // Retrieves the component description from the bundle's manifest.
        String elem = (String) bundle.getHeaders().get("iPOJO-Components");
        if (elem == null) {
            throw new IllegalArgumentException(
                    "Cannot find iPOJO-Components descriptor in the specified bundle ("
                            + bundle.getSymbolicName()
                            + "). Not an iPOJO bundle.");
        }

        // Parses the retrieved description and find the component with the
        // given name.
        try {
            Element element = ManifestMetadataParser.parseHeaderMetadata(elem);
            Element[] childs = element.getElements("component");
            for (Element child : childs) {
                String name = child.getAttribute("name");
                String clazz = child.getAttribute("classname");
                if (name != null && name.equalsIgnoreCase(component)) {
                    return child;
                }
                if (clazz.equalsIgnoreCase(component)) {
                    return child;
                }
            }

            // Component not found...
            return null;

        } catch (ParseException e) {
            throw new IllegalStateException(
                    "Cannot parse the components from specified bundle ("
                            + bundle.getSymbolicName() + "): " + e.getMessage());
        }
    }

    /**
     * Returns the instance metadatas of the component with the given name,
     * defined in the given bundle.
     *
     * @param bundle    the bundle from which the component is defined.
     * @param component the name of the defined component.
     * @return the list of instance metadata of the component with the given name,
     *         defined in the given bundle, or {@code null} if not found.
     */
    public static Element[] getInstanceMetadata(Bundle bundle, String component) {

        // Retrieves the component description from the bundle's manifest.
        String elem = (String) bundle.getHeaders().get("iPOJO-Components");
        if (elem == null) {
            throw new IllegalArgumentException(
                    "Cannot find iPOJO-Components descriptor in the specified bundle ("
                            + bundle.getSymbolicName()
                            + "). Not an iPOJO bundle.");
        }

        // Parses the retrieved description and find the component with the
        // given name.
        List<Element> list = new ArrayList<Element>();
        try {
            Element element = ManifestMetadataParser.parseHeaderMetadata(elem);
            Element[] childs = element.getElements("instance");
            for (Element child : childs) {
                String name = child.getAttribute("component");
                if (name != null && name.equalsIgnoreCase(component)) {
                    list.add(child);
                }
            }

            if (list.isEmpty()) {
                // Component not found...
                return null;
            } else {
                return list.toArray(new Element[list.size()]);
            }

        } catch (ParseException e) {
            throw new IllegalStateException(
                    "Cannot parse the components from specified bundle ("
                            + bundle.getSymbolicName() + "): " + e.getMessage());
        }
    }

    /**
     * Returns the metadata description of the component defined in the current bundle.
     *
     * @param component the name of the locally defined component.
     * @return the metadata description of the component with the given name,
     *         defined in this given bundle, or {@code null} if not found.
     */
    public Element getMetadata(String component) {
        return getMetadata(context.getBundle(), component);
    }

    /**
     * Returns the instance metadata of a component defined in the current bundle.
     *
     * @param component the name of the locally defined component.
     * @return the list of instance metadata of the component with the given name,
     *         defined in this given bundle, or {@code null} if not found.
     */
    public Element[] getInstanceMetadata(String component) {
        return getInstanceMetadata(context.getBundle(), component);
    }
}
