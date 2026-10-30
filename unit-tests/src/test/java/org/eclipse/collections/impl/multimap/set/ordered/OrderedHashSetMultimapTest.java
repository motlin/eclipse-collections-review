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

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.Multimap;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.api.multimap.bag.MutableBagMultimap;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.api.multimap.orderedset.ImmutableOrderedSetMultimap;
import org.eclipse.collections.api.multimap.orderedset.MutableOrderedSetMultimap;
import org.eclipse.collections.api.multimap.set.SetMultimap;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.multimap.AbstractMutableMultimapTestCase;
import org.eclipse.collections.impl.multimap.bag.HashBagMultimap;
import org.eclipse.collections.impl.multimap.list.FastListMultimap;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedHashSet;
import org.eclipse.collections.impl.test.SerializeTestHelper;
import org.eclipse.collections.impl.test.Verify;
import org.eclipse.collections.impl.tuple.Tuples;
import org.eclipse.collections.impl.utility.Iterate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Test of {@link OrderedHashSetMultimap}.
 */
public class OrderedHashSetMultimapTest extends AbstractMutableMultimapTestCase
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

    @Override
    @Test
    public void toImmutable()
    {
        super.toImmutable();

        MutableOrderedSetMultimap<String, Integer> multimap = this.newMultimapWithKeysValues("One", 3, "One", 1, "One", 2, "One", 1);
        ImmutableOrderedSetMultimap<String, Integer> immutable = multimap.toImmutable();
        assertInstanceOf(ImmutableOrderedSetMultimapImpl.class, immutable);
        assertEquals(FastList.newListWith(3, 1, 2), immutable.get("One").toList());
        assertEquals(FastList.newListWith(3, 1, 2), immutable.toMutable().get("One").toList());
    }

    @Override
    @Test
    public void serialization()
    {
        super.serialization();

        MutableOrderedSetMultimap<Integer, Integer> map = this.newMultimap();
        map.putAll(1, FastList.newListWith(4, 1, 3, 2));
        map.putAll(2, FastList.newListWith(5, 2, 4, 3));
        Verify.assertPostSerializedEqualsAndHashCode(map);

        MutableOrderedSetMultimap<Integer, Integer> deserialized = SerializeTestHelper.serializeDeserialize(map);
        assertEquals(FastList.newListWith(4, 1, 3, 2), deserialized.get(1).toList());
        assertEquals(FastList.newListWith(5, 2, 4, 3), deserialized.get(2).toList());

        deserialized.putAll(3, FastList.newListWith(10, 8, 9));
        assertEquals(FastList.newListWith(10, 8, 9), deserialized.get(3).toList());
    }

    @Test
    @Override
    public void putAllPairs()
    {
        super.putAllPairs();

        MutableMultimap<Integer, String> multimap = this.newMultimapWithKeysValues(1, "One", 2, "2");
        MutableList<Pair<Integer, String>> pairs = Lists.mutable.of(Tuples.pair(1, "One"));
        assertFalse(multimap.putAllPairs(pairs));
        MutableMultimap<Integer, String> expected = this.newMultimapWithKeysValues(1, "One", 2, "2");
        assertEquals(expected, multimap);
    }

    @Override
    @Test
    public void flip()
    {
        SetMultimap<String, Integer> multimap = this.newMultimapWithKeysValues("Less than 2", 1, "Less than 3", 1, "Less than 3", 2, "Less than 3", 2);
        SetMultimap<Integer, String> flipped = multimap.flip();
        assertEquals(Sets.immutable.with("Less than 3"), flipped.get(2));
        assertEquals(Sets.immutable.with("Less than 2", "Less than 3"), flipped.get(1));
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        MutableOrderedSetMultimap<String, Integer> multimap = this.newMultimap();
        multimap.putAll("One", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("Two", FastList.newListWith(5, 4, 3, 2, 2));
        MutableOrderedSetMultimap<String, Integer> selectedMultimap = multimap.selectKeysValues((key, value) -> "Two".equals(key) && (value % 2 == 0));
        MutableOrderedSetMultimap<String, Integer> expectedMultimap = OrderedHashSetMultimap.newMultimap();
        expectedMultimap.putAll("Two", FastList.newListWith(4, 2));
        Verify.assertSetMultimapsEqual(expectedMultimap, selectedMultimap);
        assertEquals(FastList.newListWith(4, 2), selectedMultimap.get("Two").toList());
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        MutableOrderedSetMultimap<String, Integer> multimap = this.newMultimap();
        multimap.putAll("One", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("Two", FastList.newListWith(5, 4, 3, 2, 2));
        MutableOrderedSetMultimap<String, Integer> rejectedMultimap = multimap.rejectKeysValues((key, value) -> "Two".equals(key) || (value % 2 == 0));
        MutableOrderedSetMultimap<String, Integer> expectedMultimap = OrderedHashSetMultimap.newMultimap();
        expectedMultimap.putAll("One", FastList.newListWith(3, 1));
        Verify.assertSetMultimapsEqual(expectedMultimap, rejectedMultimap);
        assertEquals(FastList.newListWith(3, 1), rejectedMultimap.get("One").toList());
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        super.selectKeysMultiValues();

        MutableOrderedSetMultimap<Integer, Integer> multimap = this.newMultimap();
        multimap.putAll(1, FastList.newListWith(4, 3, 1));
        multimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        multimap.putAll(3, FastList.newListWith(5, 4, 3, 2, 2));
        multimap.putAll(4, FastList.newListWith(4, 3, 1));
        MutableOrderedSetMultimap<Integer, Integer> selectedMultimap = multimap.selectKeysMultiValues((key, values) -> key % 2 == 0 && Iterate.sizeOf(values) > 3);
        MutableOrderedSetMultimap<Integer, Integer> expectedMultimap = OrderedHashSetMultimap.newMultimap();
        expectedMultimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        Verify.assertSetMultimapsEqual(expectedMultimap, selectedMultimap);
        assertEquals(FastList.newListWith(5, 4, 3, 2), selectedMultimap.get(2).toList());
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        super.rejectKeysMultiValues();

        MutableOrderedSetMultimap<Integer, Integer> multimap = this.newMultimap();
        multimap.putAll(1, FastList.newListWith(4, 3, 2, 1));
        multimap.putAll(2, FastList.newListWith(5, 4, 3, 2, 2));
        multimap.putAll(3, FastList.newListWith(4, 3, 1, 1));
        multimap.putAll(4, FastList.newListWith(4, 3, 1));
        MutableOrderedSetMultimap<Integer, Integer> rejectedMultimap = multimap.rejectKeysMultiValues((key, values) -> key % 2 == 0 || Iterate.sizeOf(values) > 3);
        MutableOrderedSetMultimap<Integer, Integer> expectedMultimap = OrderedHashSetMultimap.newMultimap();
        expectedMultimap.putAll(3, FastList.newListWith(4, 3, 1, 1));
        Verify.assertSetMultimapsEqual(expectedMultimap, rejectedMultimap);
        assertEquals(FastList.newListWith(4, 3, 1), rejectedMultimap.get(3).toList());
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        MutableOrderedSetMultimap<String, Integer> multimap = this.newMultimap();
        multimap.putAll("1", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("2", FastList.newListWith(5, 4, 3, 2, 2));
        MutableBagMultimap<Integer, String> collectedMultimap1 = multimap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value + "Value"));
        MutableBagMultimap<Integer, String> expectedMultimap1 = HashBagMultimap.newMultimap();
        expectedMultimap1.putAll(1, FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap1.putAll(2, FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        Verify.assertBagMultimapsEqual(expectedMultimap1, collectedMultimap1);

        MutableBagMultimap<Integer, String> collectedMultimap2 = multimap.collectKeysValues((key, value) -> Tuples.pair(1, value + "Value"));
        MutableBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        Verify.assertBagMultimapsEqual(expectedMultimap2, collectedMultimap2);
    }

    @Override
    @Test
    public void collectKeyMultiValues()
    {
        MutableOrderedSetMultimap<String, Integer> multimap1 = this.newMultimap();
        multimap1.putAll("1", FastList.newListWith(4, 3, 2, 1, 1));
        multimap1.putAll("2", FastList.newListWith(5, 4, 3, 2, 2));
        MutableBagMultimap<Integer, String> collectedMultimap1 = multimap1.collectKeyMultiValues(Integer::valueOf, value -> value + "Value");
        MutableBagMultimap<Integer, String> expectedMultimap1 = HashBagMultimap.newMultimap();
        expectedMultimap1.putAll(1, FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap1.putAll(2, FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        Verify.assertBagMultimapsEqual(expectedMultimap1, collectedMultimap1);

        MutableBagMultimap<Integer, String> collectedMultimap2 = multimap1.collectKeyMultiValues(key -> 1, value -> value + "Value");
        MutableBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        Verify.assertBagMultimapsEqual(expectedMultimap2, collectedMultimap2);

        Multimap<String, Integer> multimap2 = this.newMultimap(
                Tuples.pair("1", 1),
                Tuples.pair("1", 1),
                Tuples.pair("1", 12),
                Tuples.pair("2", 2),
                Tuples.pair("2", 2),
                Tuples.pair("3", 3));
        Multimap<Integer, Integer> collectedMultimap3 = multimap2.collectKeyMultiValues(
                key -> 1,
                value -> value % 2 == 0 ? value + 1 : value,
                Multimaps.mutable.set.empty());
        SetMultimap<Integer, Integer> expectedMultimap3 = Multimaps.mutable.set.with(1, 1, 1, 13, 1, 3);
        assertEquals(expectedMultimap3, collectedMultimap3);

        Multimap<Integer, Integer> collectedMultimap4 = multimap2.collectKeyMultiValues(
                key -> 1,
                value -> value % 2 == 0 ? value + 1 : value,
                Multimaps.mutable.list.empty());
        MutableListMultimap<Integer, Integer> expectedMultimap4 = Multimaps.mutable.list.with(1, 1, 1, 3, 1, 13);
        expectedMultimap4.put(1, 3);
        assertEquals(expectedMultimap4.keySet(), collectedMultimap4.keySet());
        assertEquals(expectedMultimap4.get(1).toBag(), collectedMultimap4.get(1).toBag());
    }

    @Override
    @Test
    public void collectValues()
    {
        MutableOrderedSetMultimap<String, Integer> multimap = this.newMultimap();
        multimap.putAll("1", FastList.newListWith(4, 3, 2, 1, 1));
        multimap.putAll("2", FastList.newListWith(5, 4, 3, 2, 2));
        MutableListMultimap<String, String> collectedMultimap = multimap.collectValues(value -> value + "Value");
        MutableListMultimap<String, String> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap.putAll("2", FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        Verify.assertListMultimapsEqual(expectedMultimap, collectedMultimap);
    }
}
