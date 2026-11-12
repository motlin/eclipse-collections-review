/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.set.ordered.mutable;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.collections.impl.JolMemoryTestUtil;
import org.junit.jupiter.api.Test;

/**
 * Compares the memory overhead of {@link OrderedHashSet} with {@link LinkedHashSet} at various sizes.
 * The overhead is the memory retained by the set excluding its elements.
 */
public class OrderedHashSetMemoryTest
{
    private static void assertOverheadEquals(long expected, long expectedCOH, Set<Integer> set, int size)
    {
        Object[] elements = new Object[size];
        for (int i = 0; i < size; i++)
        {
            Integer each = i;
            elements[i] = each;
            set.add(each);
        }
        JolMemoryTestUtil.assertGraphMemoryExcludingEquals(expected, expectedCOH, set, elements);
    }

    // The expected values are for Java 21 and later. A LinkedHashSet instance is 8 bytes smaller before Java 21.
    private static void assertLinkedHashSetOverheadEquals(long expected, long expectedCOH, int size)
    {
        long bytesSmallerBeforeJava21 = Runtime.version().feature() < 21 ? 8L : 0L;
        assertOverheadEquals(expected - bytesSmallerBeforeJava21, expectedCOH - bytesSmallerBeforeJava21, new LinkedHashSet<>(), size);
    }

    @Test
    public void size0()
    {
        assertOverheadEquals(32L, 32L, new OrderedHashSet<>(), 0);
        assertLinkedHashSetOverheadEquals(80L, 72L, 0);
    }

    @Test
    public void size1()
    {
        assertOverheadEquals(120L, 112L, new OrderedHashSet<>(), 1);
        assertLinkedHashSetOverheadEquals(216L, 192L, 1);
    }

    @Test
    public void size10()
    {
        assertOverheadEquals(168L, 168L, new OrderedHashSet<>(), 10);
        assertLinkedHashSetOverheadEquals(576L, 480L, 10);
    }

    @Test
    public void size100()
    {
        assertOverheadEquals(1_768L, 1_768L, new OrderedHashSet<>(), 100);
        assertLinkedHashSetOverheadEquals(5_136L, 4_320L, 100);
    }

    @Test
    public void size1_000()
    {
        assertOverheadEquals(13_720L, 13_712L, new OrderedHashSet<>(), 1_000);
        assertLinkedHashSetOverheadEquals(48_304L, 40_288L, 1_000);
    }

    @Test
    public void size10_000()
    {
        assertOverheadEquals(109_288L, 109_288L, new OrderedHashSet<>(), 10_000);
        assertLinkedHashSetOverheadEquals(465_648L, 385_632L, 10_000);
    }
}
