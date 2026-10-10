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

import org.eclipse.collections.api.collection.MutableCollection;
import org.eclipse.collections.api.factory.OrderedSets;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.multimap.bag.ImmutableBagMultimap;
import org.eclipse.collections.api.multimap.bag.MutableBagMultimap;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.api.multimap.orderedset.ImmutableOrderedSetMultimap;
import org.eclipse.collections.api.multimap.orderedset.MutableOrderedSetMultimap;
import org.eclipse.collections.api.multimap.set.ImmutableSetMultimap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.ordered.ImmutableOrderedSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.multimap.AbstractImmutableMultimapTestCase;
import org.eclipse.collections.impl.multimap.bag.HashBagMultimap;
import org.eclipse.collections.impl.multimap.list.FastListMultimap;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedHashSet;
import org.eclipse.collections.impl.test.SerializeTestHelper;
import org.eclipse.collections.impl.test.Verify;
import org.eclipse.collections.impl.tuple.Tuples;
import org.eclipse.collections.impl.utility.Iterate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ImmutableOrderedSetMultimapTest extends AbstractImmutableMultimapTestCase
{
    @Override
    protected <K, V> ImmutableOrderedSetMultimap<K, V> classUnderTest()
    {
        return OrderedHashSetMultimap.<K, V>newMultimap().toImmutable();
    }

    @Override
    protected MutableCollection<String> mutableCollection()
    {
        return new OrderedHashSet<>();
    }

    @Test
    public void testConstructor()
    {
        UnifiedMap<Integer, ImmutableOrderedSet<Integer>> map = UnifiedMap.newWithKeysValues(1, OrderedSets.immutable.with(3, 1, 2));
        ImmutableOrderedSetMultimap<Integer, Integer> immutableMap = new ImmutableOrderedSetMultimapImpl<>(map);
        assertEquals(FastList.newListWith(3, 1, 2), immutableMap.get(1).toList());
        Verify.assertSize(3, immutableMap);
        Verify.assertEmpty(immutableMap.newEmpty());
        Verify.assertEmpty(immutableMap.get(2));
    }

    @Test
    public void serialization()
    {
        OrderedHashSetMultimap<Integer, Integer> map = OrderedHashSetMultimap.newMultimap();
        map.putAll(1, FastList.newListWith(4, 1, 3, 2));
        map.putAll(2, FastList.newListWith(5, 2, 4, 3));
        ImmutableOrderedSetMultimap<Integer, Integer> immutableMap = map.toImmutable();
        Verify.assertPostSerializedEqualsAndHashCode(immutableMap);

        ImmutableOrderedSetMultimap<Integer, Integer> deserialized = SerializeTestHelper.serializeDeserialize(immutableMap);
        assertInstanceOf(ImmutableOrderedSetMultimapImpl.class, deserialized);
        assertEquals(FastList.newListWith(4, 1, 3, 2), deserialized.get(1).toList());
        assertEquals(FastList.newListWith(5, 2, 4, 3), deserialized.get(2).toList());
    }

    @Override
    @Test
    public void allowDuplicates()
    {
        // Sets do not allow duplicates
    }

    @Test
    public void forEachKeyMultiValue()
    {
        MutableSet<Pair<String, Iterable<Integer>>> collection = UnifiedSet.newSet();
        MutableOrderedSetMultimap<String, Integer> multimap = OrderedHashSetMultimap.newMultimap();
        multimap.put("Two", 2);
        multimap.put("Two", 1);
        multimap.put("Three", 3);
        multimap.put("Three", 3);
        ImmutableOrderedSetMultimap<String, Integer> immutableMultimap = multimap.toImmutable();
        immutableMultimap.forEachKeyMultiValues((key, values) -> collection.add(Tuples.pair(key, values)));
        MutableSet<Pair<String, MutableSet<Integer>>> expected = Sets.mutable.with(
                Tuples.pair("Two", Sets.mutable.with(2, 1)),
                Tuples.pair("Three", Sets.mutable.with(3, 3)));
        assertEquals(expected, collection);
    }

    @Override
    @Test
    public void flip()
    {
        ImmutableOrderedSetMultimap<String, Integer> multimap = this.<String, Integer>classUnderTest()
                .newWith("Less than 2", 1)
                .newWith("Less than 3", 1)
                .newWith("Less than 3", 2)
                .newWith("Less than 3", 2);
        ImmutableSetMultimap<Integer, String> flipped = multimap.flip();
        assertEquals(Sets.immutable.with("Less than 3"), flipped.get(2));
        assertEquals(Sets.immutable.with("Less than 2", "Less than 3"), flipped.get(1));
    }

    @Override
    @Test
    public void selectKeysValues()
    {
        MutableOrderedSetMultimap<String, Integer> mutableMultimap = OrderedHashSetMultimap.newMultimap();
        mutableMultimap.putAll("One", FastList.newListWith(4, 3, 2, 1, 1));
        mutableMultimap.putAll("Two", FastList.newListWith(5, 4, 3, 2, 2));
        ImmutableOrderedSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableOrderedSetMultimap<String, Integer> selectedMultimap = immutableMap.selectKeysValues((key, value) -> "Two".equals(key) && (value % 2 == 0));
        MutableOrderedSetMultimap<String, Integer> expectedMultimap = OrderedHashSetMultimap.newMultimap();
        expectedMultimap.putAll("Two", FastList.newListWith(4, 2));
        ImmutableOrderedSetMultimap<String, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Verify.assertSetMultimapsEqual(expectedImmutableMultimap, selectedMultimap);
        assertEquals(FastList.newListWith(4, 2), selectedMultimap.get("Two").toList());
    }

    @Override
    @Test
    public void rejectKeysValues()
    {
        MutableOrderedSetMultimap<String, Integer> mutableMultimap = OrderedHashSetMultimap.newMultimap();
        mutableMultimap.putAll("One", FastList.newListWith(4, 3, 2, 1, 1));
        mutableMultimap.putAll("Two", FastList.newListWith(5, 4, 3, 2, 2));
        ImmutableOrderedSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableOrderedSetMultimap<String, Integer> rejectedMultimap = immutableMap.rejectKeysValues((key, value) -> "Two".equals(key) || (value % 2 == 0));
        MutableOrderedSetMultimap<String, Integer> expectedMultimap = OrderedHashSetMultimap.newMultimap();
        expectedMultimap.putAll("One", FastList.newListWith(3, 1));
        ImmutableOrderedSetMultimap<String, Integer> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Verify.assertSetMultimapsEqual(expectedImmutableMultimap, rejectedMultimap);
        assertEquals(FastList.newListWith(3, 1), rejectedMultimap.get("One").toList());
    }

    @Override
    @Test
    public void selectKeysMultiValues()
    {
        MutableOrderedSetMultimap<Integer, String> mutableMultimap = OrderedHashSetMultimap.newMultimap();
        mutableMultimap.putAll(1, FastList.newListWith("4", "3", "1"));
        mutableMultimap.putAll(2, FastList.newListWith("5", "4", "3", "2", "2"));
        mutableMultimap.putAll(3, FastList.newListWith("5", "4", "3", "2", "2"));
        mutableMultimap.putAll(4, FastList.newListWith("4", "3", "1"));
        ImmutableOrderedSetMultimap<Integer, String> immutableMap = mutableMultimap.toImmutable();
        ImmutableOrderedSetMultimap<Integer, String> selectedMultimap = immutableMap.selectKeysMultiValues((key, values) -> key % 2 == 0 && Iterate.sizeOf(values) > 3);
        MutableOrderedSetMultimap<Integer, String> expectedMultimap = OrderedHashSetMultimap.newMultimap();
        expectedMultimap.putAll(2, FastList.newListWith("5", "4", "3", "2", "2"));
        ImmutableOrderedSetMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Verify.assertSetMultimapsEqual(expectedImmutableMultimap, selectedMultimap);
        assertEquals(FastList.newListWith("5", "4", "3", "2"), selectedMultimap.get(2).toList());
    }

    @Override
    @Test
    public void rejectKeysMultiValues()
    {
        MutableOrderedSetMultimap<Integer, String> mutableMultimap = OrderedHashSetMultimap.newMultimap();
        mutableMultimap.putAll(1, FastList.newListWith("5", "4", "3", "2", "1", "1"));
        mutableMultimap.putAll(2, FastList.newListWith("5", "4", "3", "2", "1"));
        mutableMultimap.putAll(3, FastList.newListWith("4", "3", "2", "2"));
        mutableMultimap.putAll(4, FastList.newListWith("5", "4", "3", "1"));
        ImmutableOrderedSetMultimap<Integer, String> immutableMap = mutableMultimap.toImmutable();
        ImmutableOrderedSetMultimap<Integer, String> rejectedMultimap = immutableMap.rejectKeysMultiValues((key, values) -> key % 2 == 0 || Iterate.sizeOf(values) > 4);
        MutableOrderedSetMultimap<Integer, String> expectedMultimap = OrderedHashSetMultimap.newMultimap();
        expectedMultimap.putAll(3, FastList.newListWith("4", "3", "2", "2"));
        ImmutableOrderedSetMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Verify.assertSetMultimapsEqual(expectedImmutableMultimap, rejectedMultimap);
        assertEquals(FastList.newListWith("4", "3", "2"), rejectedMultimap.get(3).toList());
    }

    @Override
    @Test
    public void collectKeysValues()
    {
        MutableOrderedSetMultimap<String, Integer> mutableMultimap = OrderedHashSetMultimap.newMultimap();
        mutableMultimap.putAll("1", FastList.newListWith(4, 3, 2, 1));
        mutableMultimap.putAll("2", FastList.newListWith(5, 4, 3, 2));
        ImmutableOrderedSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableBagMultimap<Integer, String> collectedMultimap = immutableMap.collectKeysValues((key, value) -> Tuples.pair(Integer.valueOf(key), value + "Value"));
        MutableBagMultimap<Integer, String> expectedMultimap = HashBagMultimap.newMultimap();
        expectedMultimap.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value"));
        expectedMultimap.putAll(2, FastList.newListWith("2Value", "3Value", "4Value", "5Value"));
        ImmutableBagMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Verify.assertBagMultimapsEqual(expectedImmutableMultimap, collectedMultimap);

        ImmutableBagMultimap<Integer, String> collectedMultimap2 = immutableMap.collectKeysValues((key, value) -> Tuples.pair(1, value + "Value"));
        MutableBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("2Value", "3Value", "4Value", "5Value"));
        ImmutableBagMultimap<Integer, String> expectedImmutableMultimap2 = expectedMultimap2.toImmutable();
        Verify.assertBagMultimapsEqual(expectedImmutableMultimap2, collectedMultimap2);
    }

    @Override
    @Test
    public void collectKeyMultiValues()
    {
        super.collectKeyMultiValues();

        MutableOrderedSetMultimap<String, Integer> mutableMultimap = OrderedHashSetMultimap.newMultimap();
        mutableMultimap.putAll("1", FastList.newListWith(4, 3, 2, 1));
        mutableMultimap.putAll("2", FastList.newListWith(5, 4, 3, 2));
        ImmutableOrderedSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableBagMultimap<Integer, String> collectedMultimap = immutableMap.collectKeyMultiValues(Integer::valueOf, value -> value + "Value");
        MutableBagMultimap<Integer, String> expectedMultimap = HashBagMultimap.newMultimap();
        expectedMultimap.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value"));
        expectedMultimap.putAll(2, FastList.newListWith("2Value", "3Value", "4Value", "5Value"));
        ImmutableBagMultimap<Integer, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Verify.assertBagMultimapsEqual(expectedImmutableMultimap, collectedMultimap);

        ImmutableBagMultimap<Integer, String> collectedMultimap2 = immutableMap.collectKeyMultiValues(key -> 1, value -> value + "Value");
        MutableBagMultimap<Integer, String> expectedMultimap2 = HashBagMultimap.newMultimap();
        expectedMultimap2.putAll(1, FastList.newListWith("1Value", "2Value", "3Value", "4Value"));
        expectedMultimap2.putAll(1, FastList.newListWith("2Value", "3Value", "4Value", "5Value"));
        ImmutableBagMultimap<Integer, String> expectedImmutableMultimap2 = expectedMultimap2.toImmutable();
        Verify.assertBagMultimapsEqual(expectedImmutableMultimap2, collectedMultimap2);
    }

    @Override
    @Test
    public void collectValues()
    {
        MutableOrderedSetMultimap<String, Integer> mutableMultimap = OrderedHashSetMultimap.newMultimap();
        mutableMultimap.putAll("1", FastList.newListWith(4, 3, 2, 1));
        mutableMultimap.putAll("2", FastList.newListWith(5, 4, 3, 2));
        ImmutableOrderedSetMultimap<String, Integer> immutableMap = mutableMultimap.toImmutable();
        ImmutableListMultimap<String, String> collectedMultimap = immutableMap.collectValues(value -> value + "Value");
        MutableListMultimap<String, String> expectedMultimap = FastListMultimap.newMultimap();
        expectedMultimap.putAll("1", FastList.newListWith("4Value", "3Value", "2Value", "1Value"));
        expectedMultimap.putAll("2", FastList.newListWith("5Value", "4Value", "3Value", "2Value"));
        ImmutableListMultimap<String, String> expectedImmutableMultimap = expectedMultimap.toImmutable();
        Verify.assertListMultimapsEqual(expectedImmutableMultimap, collectedMultimap);
    }
}
