/*
 * Copyright (c) 2021 Two Sigma.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.map.mutable.ordered;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;
import org.eclipse.collections.test.map.MapKeySetTestCase;
import org.eclipse.collections.test.map.MapValuesCollectionTestCase;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.test.IterableTestCase.assertIterablesEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class OrderedMapAdapterTest implements MutableOrderedMapTestCase
{
    private static final long CURRENT_TIME_MILLIS = System.currentTimeMillis();

    @Override
    public <T> MutableOrderedMap<Object, T> newWith(T... elements)
    {
        int i = elements.length;
        MutableOrderedMap<Object, T> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (T each : elements)
        {
            assertNull(result.put(i, each));
            i--;
        }
        return result;
    }

    @Override
    public <K, V> MutableOrderedMap<K, V> newWithKeysValues(Object... elements)
    {
        if (elements.length % 2 != 0)
        {
            fail(String.valueOf(elements.length));
        }

        MutableOrderedMap<K, V> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (int i = 0; i < elements.length; i += 2)
        {
            assertNull(result.put((K) elements[i], (V) elements[i + 1]));
        }
        return result;
    }

    @Override
    @Test
    public void ReversibleIterable_detectLastIndex()
    {
        MutableOrderedMapTestCase.super.ReversibleIterable_detectLastIndex();

        // A NavigableMap delegate is searched from the end
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("A", 1);
        treeMap.put("B", 2);
        treeMap.put("C", 1);
        treeMap.put("D", 3);
        MutableOrderedMap<String, Integer> navigable = OrderedMapAdapter.adapt(treeMap);

        MutableList<Integer> evaluated = Lists.mutable.empty();
        assertEquals(2, navigable.detectLastIndex(value ->
        {
            evaluated.add(value);
            return value == 1;
        }));
        assertIterablesEqual(Lists.immutable.with(3, 1), evaluated);
        assertEquals(-1, navigable.detectLastIndex(value -> value == 99));
    }

    @Override
    @Test
    public void OrderedIterable_forEach_from_to()
    {
        // TODO Support indexed traversal for OrderedMapAdapter.
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).forEach(0, 1, each -> { }));
    }

    @Override
    @Test
    public void OrderedIterable_forEachWithIndex_from_to()
    {
        // TODO Support indexed traversal for OrderedMapAdapter.
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).forEachWithIndex(0, 1, (each, index) -> { }));
    }

    @Override
    @Test
    public void ReversibleIterable_reverseForEach()
    {
        // TODO Support reverseForEach for OrderedMapAdapter (depends on forEach(int, int)).
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).reverseForEach(each -> { }));
    }

    @Nested
    public class KeySetView implements MapKeySetTestCase
    {
        @Override
        public OrderingType getOrderingType()
        {
            return OrderingType.INSERTION_ORDER;
        }

        @Override
        public boolean allowsSerialization()
        {
            return false;
        }

        @SafeVarargs
        @Override
        public final <T> Set<T> newWith(T... elements)
        {
            Random random = new Random(CURRENT_TIME_MILLIS);
            MutableOrderedMap<T, Object> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
            for (T element : elements)
            {
                assertNull(result.put(element, random.nextDouble()));
            }
            return result.keySet();
        }
    }

    @Nested
    public class ValuesCollectionView implements MapValuesCollectionTestCase
    {
        @Override
        public OrderingType getOrderingType()
        {
            return OrderingType.INSERTION_ORDER;
        }

        @Override
        public boolean allowsSerialization()
        {
            return false;
        }

        @SafeVarargs
        @Override
        public final <T> Collection<T> newWith(T... elements)
        {
            return OrderedMapAdapterTest.this.newWith(elements).values();
        }
    }
}
