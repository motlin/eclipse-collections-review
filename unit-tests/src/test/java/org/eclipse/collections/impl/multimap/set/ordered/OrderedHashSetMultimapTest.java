/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.multimap.set.ordered;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.api.multimap.orderedset.MutableOrderedSetMultimap;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.multimap.list.FastListMultimap;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedHashSet;
import org.eclipse.collections.impl.test.Verify;
import org.eclipse.collections.impl.tuple.Tuples;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Test of {@link OrderedHashSetMultimap}.
 */
public class OrderedHashSetMultimapTest extends AbstractMutableOrderedSetMultimapTestCase
{
    @Override
    protected <K, V> MutableOrderedSetMultimap<K, V> newMultimap()
    {
        return OrderedHashSetMultimap.newMultimap();
    }

    @Override
    protected <K, V> MutableOrderedSetMultimap<K, V> newMultimapWithKeyValue(K key, V value)
    {
        return this.<K, V>newMultimap()
                .withKeyValue(key, value);
    }

    @Override
    protected <K, V> MutableOrderedSetMultimap<K, V> newMultimapWithKeysValues(K key1, V value1, K key2, V value2)
    {
        return this.<K, V>newMultimap()
                .withKeyValue(key1, value1)
                .withKeyValue(key2, value2);
    }

    @Override
    protected <K, V> MutableOrderedSetMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3)
    {
        return this.<K, V>newMultimap()
                .withKeyValue(key1, value1)
                .withKeyValue(key2, value2)
                .withKeyValue(key3, value3);
    }

    @Override
    protected <K, V> MutableOrderedSetMultimap<K, V> newMultimapWithKeysValues(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3,
            K key4, V value4)
    {
        return this.<K, V>newMultimap()
                .withKeyValue(key1, value1)
                .withKeyValue(key2, value2)
                .withKeyValue(key3, value3)
                .withKeyValue(key4, value4);
    }

    @SafeVarargs
    @Override
    protected final <K, V> OrderedHashSetMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return OrderedHashSetMultimap.newMultimap(pairs);
    }

    @Override
    protected <K, V> OrderedHashSetMultimap<K, V> newMultimapFromPairs(Iterable<Pair<K, V>> inputIterable)
    {
        return OrderedHashSetMultimap.newMultimap(inputIterable);
    }

    @SafeVarargs
    @Override
    protected final <V> OrderedHashSet<V> createCollection(V... args)
    {
        OrderedHashSet<V> result = new OrderedHashSet<>();
        result.addAll(FastList.newListWith(args));
        return result;
    }

    @Test
    public void testEmptyConstructor()
    {
        MutableOrderedSetMultimap<Integer, Integer> map = OrderedHashSetMultimap.newMultimap();
        for (int i = 1; i < 6; ++i)
        {
            for (int j = i; j > 0; --j)
            {
                map.put(i, j);
            }
        }
        Verify.assertSize(5, map.keysView().toList());
        for (int i = 1; i < 6; ++i)
        {
            assertEquals(FastList.newList(Interval.fromTo(i, 1)), map.get(i).toList());
        }
    }

    @Test
    public void testMultimapConstructor()
    {
        MutableListMultimap<Integer, Integer> map = FastListMultimap.newMultimap();
        map.putAll(1, FastList.newListWith(3, 1, 2, 1, 3));
        map.putAll(2, FastList.newListWith(5, 4));
        OrderedHashSetMultimap<Integer, Integer> orderedMap = OrderedHashSetMultimap.newMultimap(map);
        assertEquals(FastList.newListWith(3, 1, 2), orderedMap.get(1).toList());
        assertEquals(FastList.newListWith(5, 4), orderedMap.get(2).toList());
    }

    @Test
    public void testCollection()
    {
        OrderedHashSetMultimap<Integer, Integer> setMultimap = OrderedHashSetMultimap.newMultimap();
        MutableOrderedSet<Integer> collection = setMultimap.createCollection();
        assertInstanceOf(OrderedHashSet.class, collection);
        collection.addAll(FastList.newListWith(1, 4, 2, 3, 5, 4));
        assertEquals(FastList.newListWith(1, 4, 2, 3, 5), collection.toList());
        setMultimap.putAll(1, collection);
        setMultimap.put(1, 0);
        assertEquals(Integer.valueOf(0), setMultimap.get(1).getLast());
        assertEquals(FastList.newListWith(1, 4, 2, 3, 5, 0), setMultimap.get(1).toList());
    }

    @Test
    public void testNewEmpty()
    {
        OrderedHashSetMultimap<Object, Integer> expected = OrderedHashSetMultimap.newMultimap();
        OrderedHashSetMultimap<Object, Integer> actual = expected.newEmpty();
        expected.putAll(1, FastList.newListWith(4, 3, 1, 2));
        actual.putAll(1, FastList.newListWith(4, 3, 1, 2));
        Verify.assertMapsEqual(expected.toMap(), actual.toMap());
        assertEquals(FastList.newListWith(4, 3, 1, 2), actual.get(1).toList());
    }

    @Test
    public void pairIterableConstructorTest()
    {
        MutableList<Pair<Integer, String>> testList = FastList.newListWith(
                Tuples.pair(1, "One"),
                Tuples.pair(2, "Two"),
                Tuples.pair(1, "OneOne"),
                Tuples.pair(2, "TwoTwo"),
                Tuples.pair(1, "One"));

        OrderedHashSetMultimap<Integer, String> actual = OrderedHashSetMultimap.newMultimap(testList);

        assertEquals(FastList.newListWith("One", "OneOne"), actual.get(1).toList());
        assertEquals(FastList.newListWith("Two", "TwoTwo"), actual.get(2).toList());
    }
}
