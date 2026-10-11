/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.map.mutable.ordered;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.collections.api.collection.MutableCollection;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.api.ordered.OrderedIterable;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.test.IterableTestCase.assertIterablesEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class OrderedMapAdapterTreeMapTest extends OrderedMapAdapterTest
{
    @Override
    public boolean supportsNullKeys()
    {
        return false;
    }

    @Override
    public boolean supportsNonComparableKeys()
    {
        return false;
    }

    @Override
    public <T> MutableOrderedMap<Object, T> newWith(T... elements)
    {
        // Keys count down like OrderedMapAdapterTest's, so a reverse-ordered TreeMap iterates them in insertion order
        int i = elements.length;
        MutableOrderedMap<Object, T> result = OrderedMapAdapter.adapt(new TreeMap<Object, T>(Collections.reverseOrder()));
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

        MutableOrderedMap<K, V> result = OrderedMapAdapter.adapt(new TreeMap<>());
        for (int i = 0; i < elements.length; i += 2)
        {
            assertNull(result.put((K) elements[i], (V) elements[i + 1]));
        }
        return result;
    }

    @Override
    @Test
    public void Iterable_toString()
    {
        // A TreeMap delegate sorts its keys, not insertion order
        Map<String, Integer> map = this.newWithKeysValues("Two", 2, "One", 1);
        assertEquals("[One, Two]", map.keySet().toString());
        assertEquals("[1, 2]", map.values().toString());
        assertEquals("[One=1, Two=2]", map.entrySet().toString());
        assertEquals("{One=1, Two=2}", map.toString());

        MapIterable<String, Integer> mapIterable = this.newWithKeysValues("Two", 2, "One", 1);
        assertEquals("[One, Two]", mapIterable.keysView().toString());
        assertEquals("[1, 2]", mapIterable.valuesView().toString());
        assertEquals("[One:1, Two:2]", mapIterable.keyValuesView().toString());
        assertEquals("[1, 2]", mapIterable.asLazy().toString());

        assertEquals("{}", this.newWithKeysValues().toString());
        assertEquals(
                "{10=4, 9=4, 8=4, 7=4, 6=3, 5=3, 4=3, 3=2, 2=2, 1=1}",
                this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1).toString());
        assertEquals(
                "[10, 9, 8, 7, 6, 5, 4, 3, 2, 1]",
                this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1).keysView().toString());
        assertEquals(
                "[4, 4, 4, 4, 3, 3, 3, 2, 2, 1]",
                this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1).valuesView().toString());
        assertEquals(
                "[10:4, 9:4, 8:4, 7:4, 6:3, 5:3, 4:3, 3:2, 2:2, 1:1]",
                this.newWith(4, 4, 4, 4, 3, 3, 3, 2, 2, 1).keyValuesView().toString());

        // A TreeMap can't compare a map key with anything
        Map<Object, Object> selfKey = this.newWithKeysValues();
        assertThrows(ClassCastException.class, () -> selfKey.put(selfKey, "value"));

        Map<Object, Object> selfValue = this.newWithKeysValues();
        selfValue.put("key", selfValue);
        assertEquals("{key=(this Map)}", selfValue.toString());
    }

    @Override
    @Test
    public void Map_putAll()
    {
        Map<Integer, String> map = this.newWithKeysValues(3, "Three", 2, "2");
        map.putAll(this.newWithKeysValues(2, "Two", 1, "One"));
        Map<Integer, String> expected = this.newWithKeysValues(3, "Three", 2, "Two", 1, "One");
        assertIterablesEqual(expected, map);

        assertThrows(NullPointerException.class, () -> map.putAll(null));
        map.putAll(Map.of());
        assertIterablesEqual(expected, map);

        Map<Integer, String> map2 = this.newWithKeysValues(3, "Three", 2, "2");
        Map<Integer, String> hashMapToAdd = new LinkedHashMap<>();
        hashMapToAdd.put(2, "Two");
        hashMapToAdd.put(1, "One");
        map2.putAll(hashMapToAdd);
        assertIterablesEqual(expected, map2);

        // A TreeMap delegate sorts its keys, not insertion order, so putAll() doesn't keep the source's order
        MutableOrderedMap<String, Integer> sorted = this.newWithKeysValues("A", 1, "B", 2);
        LinkedHashMap<String, Integer> source = new LinkedHashMap<>();
        source.put("D", 4);
        source.put("C", 3);
        sorted.putAll(source);
        assertIterablesEqual(Lists.immutable.with("A", "B", "C", "D"), Lists.immutable.withAll(sorted.keySet()));
    }

    @Override
    @Test
    public void MapIterable_flipUniqueValues()
    {
        // A TreeMap delegate sorts its keys, not insertion order, so the flipped map follows the sorted keys One, Three, Two
        MapIterable<String, Integer> map = this.newWithKeysValues("Three", 3, "Two", 2, "One", 1);
        MapIterable<Integer, String> result = map.flipUniqueValues();
        assertEquals(this.newWithKeysValues(3, "Three", 2, "Two", 1, "One"), result);
        assertIterablesEqual(Lists.immutable.with(1, 3, 2), result.keysView().toList());
        assertIterablesEqual(Lists.immutable.with("One", "Three", "Two"), result.valuesView().toList());

        assertThrows(IllegalStateException.class, () -> this.newWithKeysValues(1, "2", 2, "2").flipUniqueValues());
    }

    @Override
    @Test
    public void MapIterable_forEach()
    {
        MapIterable<Object, Integer> map = this.newWith(3, 3, 3, 2, 2, 1);
        MutableCollection<Integer> forEach = this.newMutableForFilter();
        map.forEach((key, value) -> forEach.add(value + 10));
        assertIterablesEqual(this.newMutableForFilter(13, 13, 13, 12, 12, 11), forEach);

        // A TreeMap delegate sorts its keys, not insertion order
        MapIterable<Integer, String> map2 = this.newWithKeysValues(3, "Three", 2, "Two", 1, "Three");
        MutableCollection<String> forEach2 = this.newMutableForFilter();
        map2.forEach((key, value) -> forEach2.add(key + value));
        assertIterablesEqual(this.newMutableForFilter("1Three", "2Two", "3Three"), forEach2);
    }

    @Override
    @Test
    public void MapIterable_forEachKeyValue()
    {
        MapIterable<Object, Integer> map = this.newWith(3, 3, 3, 2, 2, 1);
        MutableCollection<Integer> forEachKeyValue = this.newMutableForFilter();
        map.forEachKeyValue((key, value) -> forEachKeyValue.add(value + 10));
        assertIterablesEqual(this.newMutableForFilter(13, 13, 13, 12, 12, 11), forEachKeyValue);

        MutableCollection<Integer> forEachValue = this.newMutableForFilter();
        map.forEachValue(value -> forEachValue.add(value + 10));
        assertIterablesEqual(this.newMutableForFilter(13, 13, 13, 12, 12, 11), forEachValue);

        // A TreeMap delegate sorts its keys, not insertion order
        MapIterable<Integer, String> map2 = this.newWithKeysValues(3, "Three", 2, "Two", 1, "Three");
        MutableCollection<String> forEachKeyValue2 = this.newMutableForFilter();
        map2.forEachKeyValue((key, value) -> forEachKeyValue2.add(key + value));
        assertIterablesEqual(this.newMutableForFilter("1Three", "2Two", "3Three"), forEachKeyValue2);

        MutableCollection<Object> forEachKey = this.newMutableForFilter();
        map2.forEachKey(key -> forEachKey.add(key + 1));
        assertIterablesEqual(this.newMutableForFilter(2, 3, 4), forEachKey);

        MutableOrderedMap<Integer, String> withGap = this.newWithKeysValues(4, "Four", 3, "Three", 2, "Two", 1, "One");
        withGap.removeKey(3);
        MutableList<String> collected = Lists.mutable.empty();
        withGap.forEachKeyValue((key, value) -> collected.add(key + value));
        assertIterablesEqual(Lists.immutable.with("1One", "2Two", "4Four"), collected);
    }

    @Override
    @Test
    public void OrderedIterable_corresponds()
    {
        OrderedIterable<Integer> iterable = this.newWith(3, 2, 1);
        assertTrue(iterable.corresponds(Lists.mutable.with(3, 2, 1), Integer::equals));
        assertFalse(iterable.corresponds(Lists.mutable.with(1, 2, 3), Integer::equals));
        assertFalse(iterable.corresponds(Lists.mutable.with(1, 2), Integer::equals));
        assertTrue(this.<Integer>newWith().corresponds(Lists.mutable.empty(), Integer::equals));

        OrderedIterable<Integer> iterableWithDuplicates = this.newWith(3, 3, 3, 2, 2, 1);
        assertTrue(iterableWithDuplicates.corresponds(Lists.mutable.with(3, 3, 3, 2, 2, 1), Integer::equals));
        assertFalse(iterableWithDuplicates.corresponds(Lists.mutable.with(3, 2, 1), Integer::equals));

        // A TreeMap delegate sorts its keys, not insertion order
        MutableOrderedMap<Integer, Integer> withGap = this.newWithKeysValues(4, 4, 3, 3, 2, 2, 1, 1);
        withGap.removeKey(3);
        assertTrue(withGap.corresponds(Lists.mutable.with(1, 2, 4), Integer::equals));
        assertFalse(withGap.corresponds(Lists.mutable.with(4, 2, 1), Integer::equals));
    }

    @Override
    @Test
    public void OrderedIterable_detectIndex()
    {
        OrderedIterable<Integer> iterable = this.newWith(3, 2, 1);
        assertEquals(0, iterable.detectIndex(each -> each == 3));
        assertEquals(1, iterable.detectIndex(each -> each == 2));
        assertEquals(2, iterable.detectIndex(each -> each == 1));
        assertEquals(-1, iterable.detectIndex(each -> each == 99));
        assertEquals(-1, this.<Integer>newWith().detectIndex(each -> true));

        OrderedIterable<Integer> iterableWithDuplicates = this.newWith(3, 3, 3, 2, 2, 1);
        assertEquals(0, iterableWithDuplicates.detectIndex(each -> each == 3));
        assertEquals(3, iterableWithDuplicates.detectIndex(each -> each == 2));
        assertEquals(5, iterableWithDuplicates.detectIndex(each -> each == 1));

        // A TreeMap delegate sorts its keys, not insertion order
        MutableOrderedMap<Integer, Integer> withGap = this.newWithKeysValues(4, 4, 3, 3, 2, 2, 1, 1);
        withGap.removeKey(3);
        assertEquals(0, withGap.detectIndex(each -> each == 1));
        assertEquals(1, withGap.detectIndex(each -> each == 2));
        assertEquals(2, withGap.detectIndex(each -> each == 4));
    }

    @Override
    @Test
    public void OrderedIterable_distinct()
    {
        assertIterablesEqual(Lists.immutable.with(3, 2, 1), this.newWith(3, 2, 1).distinct().toList());
        assertIterablesEqual(Lists.immutable.with(3, 2, 1, 0), this.newWith(3, 3, 3, 2, 2, 1, 0, 0).distinct().toList());

        // A TreeMap delegate sorts its keys, not insertion order
        MutableOrderedMap<Integer, Integer> withGap = this.newWithKeysValues(4, 4, 3, 3, 2, 2, 1, 1);
        withGap.removeKey(3);
        assertIterablesEqual(Lists.immutable.with(1, 2, 4), withGap.distinct().toList());
    }

    @Override
    @Test
    public void ReversibleIterable_drop()
    {
        // A TreeMap delegate sorts its keys, not insertion order, so drop() drops the lowest keys
        OrderedMap<Integer, String> orderedMap = this.newWithKeysValues(3, "Three", 2, "Two", 1, "Three");
        assertIterablesEqual(this.newWithKeysValues(1, "Three", 2, "Two", 3, "Three"), orderedMap.drop(0));
        assertIterablesEqual(this.newWithKeysValues(2, "Two", 3, "Three"), orderedMap.drop(1));
        assertIterablesEqual(this.newWithKeysValues(3, "Three"), orderedMap.drop(2));
        assertIterablesEqual(this.newWithKeysValues(), orderedMap.drop(3));
        assertIterablesEqual(this.newWithKeysValues(), orderedMap.drop(4));
        assertIterablesEqual(this.newWithKeysValues(), orderedMap.drop(Integer.MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> orderedMap.drop(-1));

        MutableOrderedMap<Integer, String> withGap = this.newWithKeysValues(4, "Four", 3, "Three", 2, "Two", 1, "One");
        withGap.removeKey(3);
        assertIterablesEqual(this.newWithKeysValues(2, "Two", 4, "Four"), withGap.drop(1));
        assertIterablesEqual(this.newWithKeysValues(4, "Four"), withGap.drop(2));
    }

    @Override
    @Test
    public void ReversibleIterable_take()
    {
        // A TreeMap delegate sorts its keys, not insertion order, so take() takes the lowest keys
        OrderedMap<Integer, String> orderedMap = this.newWithKeysValues(3, "Three", 2, "Two", 1, "Three");
        assertIterablesEqual(this.newWithKeysValues(), orderedMap.take(0));
        assertIterablesEqual(this.newWithKeysValues(1, "Three"), orderedMap.take(1));
        assertIterablesEqual(this.newWithKeysValues(1, "Three", 2, "Two"), orderedMap.take(2));
        assertIterablesEqual(this.newWithKeysValues(1, "Three", 2, "Two", 3, "Three"), orderedMap.take(3));
        assertIterablesEqual(this.newWithKeysValues(1, "Three", 2, "Two", 3, "Three"), orderedMap.take(4));
        assertIterablesEqual(this.newWithKeysValues(1, "Three", 2, "Two", 3, "Three"), orderedMap.take(Integer.MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> orderedMap.take(-1));

        MutableOrderedMap<Integer, String> withGap = this.newWithKeysValues(4, "Four", 3, "Three", 2, "Two", 1, "One");
        withGap.removeKey(3);
        assertIterablesEqual(this.newWithKeysValues(1, "One", 2, "Two"), withGap.take(2));
        assertIterablesEqual(this.newWithKeysValues(1, "One", 2, "Two", 4, "Four"), withGap.take(4));
    }

    @Override
    @Test
    public void ReversibleIterable_toReversed()
    {
        // toReversed() can't return a TreeMap-backed map, since a TreeMap delegate sorts its keys, not insertion order
        OrderedMap<String, Integer> map = this.newWithKeysValues("A", 1, "B", 2, "C", 3);
        OrderedMap<String, Integer> reversed = map.toReversed();
        assertEquals(this.newWithKeysValues("C", 3, "B", 2, "A", 1), reversed);
        assertIterablesEqual(Lists.immutable.with("C", "B", "A"), reversed.keysView().toList());
        assertIterablesEqual(Lists.immutable.with(3, 2, 1), reversed.toList());
        assertIterablesEqual(this.newWithKeysValues(), this.<String, Integer>newWithKeysValues().toReversed());
        assertIterablesEqual(map, reversed.toReversed());

        MutableOrderedMap<String, Integer> withGap = this.newWithKeysValues("A", 1, "B", 2, "C", 3, "D", 4);
        withGap.removeKey("B");
        OrderedMap<String, Integer> withGapReversed = withGap.toReversed();
        assertEquals(this.newWithKeysValues("D", 4, "C", 3, "A", 1), withGapReversed);
        assertIterablesEqual(Lists.immutable.with("D", "C", "A"), withGapReversed.keysView().toList());
    }
}
