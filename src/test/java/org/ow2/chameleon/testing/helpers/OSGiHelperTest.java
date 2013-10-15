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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;

import static org.fest.assertions.Assertions.*;

public class OSGiHelperTest {

    private BundleContextStub m_bc;
    private OSGiHelper m_helper;
    private BundleStub m_bundle;

    @Before
    public void setUp() {
        m_bundle = new BundleStub();
        m_bc = new BundleContextStub(m_bundle);
        m_helper = new OSGiHelper(m_bc);
    }

    @After
    public void tearDown() {
        m_helper.dispose();
    }

    @Test
    public void isServiceAvailable() {
        assertFalse(m_helper.isServiceAvailable(Runnable.class.getName()));
        assertFalse(m_helper.isServiceAvailable(Runnable.class));

        m_bc.addService(Runnable.class.getName(), new Object());

        assertThat(m_helper.isServiceAvailable(Runnable.class.getName())).isTrue();
        assertThat(m_helper.isServiceAvailable(Runnable.class)).isTrue();
    }



    @Test
    public void getServiceReference() {
        assertNull(m_helper.getServiceReference(Runnable.class.getName()));
        assertNull(m_helper.getServiceReference(Runnable.class));

        assertNull(m_helper.getServiceReference(Runnable.class.getName(), null));
        assertNull(m_helper.getServiceReference(Runnable.class, null));

        assertEquals(0, m_helper.getServiceReferences(Runnable.class.getName(), null).length);
        assertEquals(0, m_helper.getServiceReferences(Runnable.class, null).length);

        m_bc
                .addService(Runnable.class.getName(), new Object())
                .addService(List.class.getName(), new Object())
                .addService(List.class.getName(), new Object());


        assertNotNull(m_helper.getServiceReference(Runnable.class.getName()));
        assertNotNull(m_helper.getServiceReference(Runnable.class));

        assertNotNull(m_helper.getServiceReference(Runnable.class.getName(), null));
        assertNotNull(m_helper.getServiceReference(Runnable.class, null));

        assertEquals(2, m_helper.getServiceReferences(List.class.getName(), null).length);
        assertEquals(2, m_helper.getServiceReferences(List.class, null).length);

    }

    @Test
    public void getService() {
        assertNull(m_helper.getServiceObject(Runnable.class.getName(), null));
        assertNull(m_helper.getServiceObject(Runnable.class, null));
        assertNull(m_helper.getServiceObject(Runnable.class));


        assertEquals(0, m_helper.getServiceObjects(Runnable.class.getName(), null).length);
        assertEquals(0, m_helper.getServiceObjects(Runnable.class).size());

        m_bc
                .addService(Runnable.class.getName(), new Runnable() {
                    public void run() { }
                } )
                .addService(List.class.getName(), new ArrayList())
                .addService(List.class.getName(), new ArrayList());

        Object o = m_helper.getServiceObject(Runnable.class.getName(), null);
        assertNotNull(o);
        assertTrue(o instanceof Runnable);

        o = m_helper.getServiceObject(Runnable.class);
        assertNotNull(o);

        o = m_helper.getServiceObject(Runnable.class.getName(), null);
        assertNotNull(o);
        assertTrue(o instanceof  Runnable);

        o = m_helper.getServiceObject(Runnable.class, null);
        assertNotNull(o);

        Object[] os = m_helper.getServiceObjects(List.class.getName(), null);
        assertEquals(2, os.length);
        assertTrue(os[0] instanceof List);
        assertTrue(os[1] instanceof List);

        List<List> oss = m_helper.getServiceObjects(List.class, null);
        assertEquals(2, oss.size());

        oss = m_helper.getServiceObjects(List.class);
        assertEquals(2, oss.size());
    }



}
