/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.set.mutable.ordered;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import org.eclipse.collections.api.factory.Bags;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.OrderedSets;
import org.eclipse.collections.api.factory.Sets;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.ParallelSetIterable;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedHashSet;
import org.eclipse.collections.test.IterableTestCase;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.test.IterableTestCase.assertIterablesEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class OrderedHashSetTest implements MutableOrderedSetTestCase
{
    @SafeVarargs
    @Override
    public final <T> MutableOrderedSet<T> newWith(T... elements)
    {
        MutableOrderedSet<T> result = new OrderedHashSet<>();
        IterableTestCase.addAllTo(elements, result);
        return result;
    }

    private static Object getField(OrderedHashSet<?> set, String fieldName)
    {
        try
        {
            Field field = OrderedHashSet.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(set);
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static int getIndicesHashTableLength(OrderedHashSet<?> set)
    {
        return ((int[]) getField(set, "indicesHashTable")).length;
    }

    private static int getIndicesHashTableOccupied(OrderedHashSet<?> set)
    {
        return (int) getField(set, "indicesHashTableOccupied");
    }

    private static int getOrderedElementsLength(OrderedHashSet<?> set)
    {
        return ((Object[]) getField(set, "orderedElements")).length;
    }

    private static int getOrderedElementsOccupied(OrderedHashSet<?> set)
    {
        return (int) getField(set, "orderedElementsOccupied");
    }

    @Override
    @Test
    public void Collection_add()
    {
        MutableOrderedSetTestCase.super.Collection_add();

        // The factory creates an OrderedHashSet
        assertInstanceOf(OrderedHashSet.class, OrderedSets.mutable.empty());
        assertInstanceOf(OrderedHashSet.class, OrderedSets.mutable.with(3, 2, 1));
        assertInstanceOf(OrderedHashSet.class, OrderedSets.mutable.withAll(Lists.mutable.with(3, 2, 1)));

        // The no-argument constructor allocates no tables until the first insertion
        OrderedHashSet<Integer> lazySet = new OrderedHashSet<>();
        assertNull(getField(lazySet, "indicesHashTable"));
        assertNull(getField(lazySet, "orderedElements"));
        assertFalse(lazySet.contains(1));
        assertFalse(lazySet.remove(1));
        assertFalse(lazySet.iterator().hasNext());
        assertEquals(-1, lazySet.indexOf(1));
        assertNull(lazySet.getFirst());
        assertNull(lazySet.getLast());
        assertEquals("[]", lazySet.toString());
        assertEquals(0, lazySet.hashCode());
        assertEquals(new LinkedHashSet<>(), lazySet);
        lazySet.forEach(each -> fail());
        lazySet.reverseForEach(each -> fail());
        assertIterablesEqual(OrderedSets.mutable.empty(), lazySet.toReversed());
        assertNull(getField(lazySet, "indicesHashTable"));
        assertNull(getField(OrderedHashSet.newSet(), "orderedElements"));

        // The first insertion allocates the default index table capacity of 8, with a usable capacity of 5
        assertTrue(lazySet.add(1));
        assertEquals(8, getIndicesHashTableLength(lazySet));
        assertEquals(5, getOrderedElementsLength(lazySet));

        // The initial capacity sizes the dense storage to at least that many elements
        assertThrows(IllegalArgumentException.class, () -> new OrderedHashSet<>(-1));
        assertThrows(IllegalArgumentException.class, () -> OrderedHashSet.newSet(-1));
        assertEquals(8, getIndicesHashTableLength(new OrderedHashSet<>(0)));
        OrderedHashSet<Integer> presizedSet = OrderedHashSet.newSet(16);
        assertEquals(32, getIndicesHashTableLength(presizedSet));
        assertEquals(21, getOrderedElementsLength(presizedSet));

        // The sixth insertion exhausts the usable capacity and doubles the tables
        OrderedHashSet<Integer> growingSet = new OrderedHashSet<>();
        for (int i = 0; i < 5; i++)
        {
            assertTrue(growingSet.add(i));
        }
        assertEquals(8, getIndicesHashTableLength(growingSet));
        assertEquals(5, getOrderedElementsLength(growingSet));
        assertEquals(5, getIndicesHashTableOccupied(growingSet));
        assertEquals(5, getOrderedElementsOccupied(growingSet));
        assertFalse(growingSet.add(4));
        assertEquals(5, getOrderedElementsOccupied(growingSet));

        assertTrue(growingSet.add(5));
        assertEquals(16, getIndicesHashTableLength(growingSet));
        assertEquals(10, getOrderedElementsLength(growingSet));
        assertEquals(6, getIndicesHashTableOccupied(growingSet));
        assertEquals(6, getOrderedElementsOccupied(growingSet));
        assertEquals(List.of(0, 1, 2, 3, 4, 5), new ArrayList<>(growingSet));

        // Copy constructors and static factories keep the source order
        LinkedHashSet<String> source = new LinkedHashSet<>(List.of("C", "A", "B"));
        OrderedHashSet<String> fromCollection = new OrderedHashSet<>(source);
        assertEquals(List.of("C", "A", "B"), new ArrayList<>(fromCollection));
        source.add("D");
        assertEquals(3, fromCollection.size());
        assertEquals(List.of("C", "A", "B"), new ArrayList<>(OrderedHashSet.newSet(Lists.mutable.with("C", "A", "C", "B"))));
        assertEquals(List.of("C", "A", "B"), new ArrayList<>(OrderedHashSet.newSetWith("C", "A", "C", "B")));
        assertTrue(new OrderedHashSet<>(Lists.mutable.empty()).isEmpty());

        // Null elements are stored in insertion order
        OrderedHashSet<String> nullSet = OrderedHashSet.newSetWith("A", null, "B");
        assertTrue(nullSet.contains(null));
        assertFalse(nullSet.add(null));
        assertEquals(Arrays.asList("A", null, "B"), new ArrayList<>(nullSet));
        assertEquals(1, nullSet.indexOf(null));
        assertTrue(nullSet.remove(null));
        assertFalse(nullSet.contains(null));
        assertEquals(List.of("A", "B"), new ArrayList<>(nullSet));

        // Many colliding hash slots preserve insertion order
        OrderedHashSet<Integer> collisionSet = new OrderedHashSet<>();
        for (int i = 0; i < 100; i++)
        {
            assertTrue(collisionSet.add(i * 17));
        }
        for (int i = 0; i < 100; i++)
        {
            assertTrue(collisionSet.contains(i * 17));
            assertEquals(i, collisionSet.indexOf(i * 17));
        }
    }

    @Override
    @Test
    public void Collection_remove_removeAll()
    {
        MutableOrderedSetTestCase.super.Collection_remove_removeAll();

        // Removing interior elements leaves probe chains intact
        OrderedHashSet<Integer> probeSet = new OrderedHashSet<>();
        for (int i = 0; i < 1000; i++)
        {
            probeSet.add(i);
        }
        for (int i = 100; i < 200; i++)
        {
            assertTrue(probeSet.remove(i));
        }
        for (int i = 2000; i < 2100; i++)
        {
            probeSet.add(i);
        }
        for (int i = 2000; i < 2100; i++)
        {
            assertTrue(probeSet.remove(i));
        }
        assertEquals(900, probeSet.size());
        for (int i = 0; i < 1000; i++)
        {
            assertEquals(i < 100 || i >= 200, probeSet.contains(i));
        }

        // Repeatedly adding and removing the only element reclaims its slot every time
        OrderedHashSet<String> repeatSet = new OrderedHashSet<>();
        for (int i = 0; i < 10_000; i++)
        {
            assertTrue(repeatSet.add("1"));
            assertTrue(repeatSet.remove("1"));
        }
        assertEquals(0, getOrderedElementsOccupied(repeatSet));
        assertEquals(8, getIndicesHashTableLength(repeatSet));

        // Removing the last element reclaims trailing tombstones, cascading past earlier removals
        OrderedHashSet<String> cascadeSet = OrderedHashSet.newSetWith("A", "B", "C");
        assertTrue(cascadeSet.remove("C"));
        assertEquals(2, getOrderedElementsOccupied(cascadeSet));
        assertTrue(cascadeSet.add("D"));
        assertTrue(cascadeSet.remove("B"));
        assertEquals(3, getOrderedElementsOccupied(cascadeSet));
        assertTrue(cascadeSet.remove("D"));
        assertEquals(1, getOrderedElementsOccupied(cascadeSet));
        assertEquals(List.of("A"), new ArrayList<>(cascadeSet));

        // Interior tombstones are skipped by iteration and by the ordered operations
        OrderedHashSet<Integer> tombstoneSet = OrderedHashSet.newSetWith(1, 2, 3, 4, 5);
        assertTrue(tombstoneSet.remove(1));
        assertTrue(tombstoneSet.remove(3));
        assertEquals(5, getOrderedElementsOccupied(tombstoneSet));
        assertEquals(List.of(2, 4, 5), new ArrayList<>(tombstoneSet));
        assertEquals(1, tombstoneSet.indexOf(4));
        assertEquals(-1, tombstoneSet.indexOf(3));
        assertEquals(Integer.valueOf(2), tombstoneSet.getFirst());
        assertEquals(Integer.valueOf(5), tombstoneSet.getLast());
        assertEquals(1, tombstoneSet.detectIndex(each -> each > 2));
        assertEquals(1, tombstoneSet.detectLastIndex(each -> each < 5));
        assertIterablesEqual(OrderedSets.mutable.with(5, 4, 2), tombstoneSet.toReversed());
        assertIterablesEqual(OrderedSets.mutable.with(2, 4), tombstoneSet.take(2));
        assertIterablesEqual(OrderedSets.mutable.with(5), tombstoneSet.drop(2));
        assertIterablesEqual(OrderedSets.mutable.with(4, 5), tombstoneSet.select(each -> each > 2));
        MutableList<Integer> reversed = Lists.mutable.empty();
        tombstoneSet.reverseForEach(reversed::add);
        assertEquals(List.of(5, 4, 2), reversed);
        MutableList<Integer> range = Lists.mutable.empty();
        tombstoneSet.forEachWithIndex(2, 1, (each, index) -> range.add(each * 10 + index));
        assertEquals(List.of(52, 41), range);
        assertEquals("[2, 4, 5]", tombstoneSet.toString());
        assertEquals(OrderedHashSet.newSetWith(2, 4, 5), tombstoneSet);
        assertEquals(new LinkedHashSet<>(List.of(2, 4, 5)).hashCode(), tombstoneSet.hashCode());

        // Hash table tombstones are compacted away without growing the table
        OrderedHashSet<Integer> hashTableTombstoneSet = new OrderedHashSet<>(10);
        for (int i = 0; i < 10; i++)
        {
            hashTableTombstoneSet.add(i);
        }
        for (int i = 9; i >= 5; i--)
        {
            hashTableTombstoneSet.remove(i);
        }
        assertEquals(16, getIndicesHashTableLength(hashTableTombstoneSet));
        assertEquals(10, getIndicesHashTableOccupied(hashTableTombstoneSet));
        assertEquals(5, getOrderedElementsOccupied(hashTableTombstoneSet));
        assertTrue(hashTableTombstoneSet.add(10));
        assertEquals(16, getIndicesHashTableLength(hashTableTombstoneSet));
        assertEquals(10, getOrderedElementsLength(hashTableTombstoneSet));
        assertEquals(6, getIndicesHashTableOccupied(hashTableTombstoneSet));
        assertEquals(6, getOrderedElementsOccupied(hashTableTombstoneSet));
        assertEquals(List.of(0, 1, 2, 3, 4, 10), new ArrayList<>(hashTableTombstoneSet));

        // Dense storage tombstones are compacted away without growing the table
        OrderedHashSet<Integer> denseTombstoneSet = OrderedHashSet.newSetWith(0, 1);
        while (getOrderedElementsOccupied(denseTombstoneSet) < getOrderedElementsLength(denseTombstoneSet))
        {
            denseTombstoneSet.remove(0);
            denseTombstoneSet.add(0);
            if (getOrderedElementsOccupied(denseTombstoneSet) < getOrderedElementsLength(denseTombstoneSet))
            {
                denseTombstoneSet.remove(1);
                denseTombstoneSet.add(1);
            }
        }
        assertEquals(8, getIndicesHashTableLength(denseTombstoneSet));
        assertEquals(5, getOrderedElementsOccupied(denseTombstoneSet));
        denseTombstoneSet.remove(1);
        denseTombstoneSet.add(1);
        assertEquals(8, getIndicesHashTableLength(denseTombstoneSet));
        assertEquals(5, getOrderedElementsLength(denseTombstoneSet));
        assertEquals(2, getIndicesHashTableOccupied(denseTombstoneSet));
        assertEquals(2, getOrderedElementsOccupied(denseTombstoneSet));

        // Remove/re-add churn compacts instead of growing unboundedly
        OrderedHashSet<String> churnSet = OrderedHashSet.newSetWith("A", "B");
        for (int i = 0; i < 10_000; i++)
        {
            assertTrue(churnSet.remove("A"));
            assertTrue(churnSet.add("A"));
            assertTrue(churnSet.remove("B"));
            assertTrue(churnSet.add("B"));
        }
        assertEquals(8, getIndicesHashTableLength(churnSet));
        assertEquals(5, getOrderedElementsLength(churnSet));
        assertEquals(List.of("A", "B"), new ArrayList<>(churnSet));

        // Growth after removals sizes the tables from the live elements only
        OrderedHashSet<Integer> capacitySet = new OrderedHashSet<>(10);
        for (int i = 0; i < 9; i++)
        {
            capacitySet.add(i);
        }
        capacitySet.remove(1);
        capacitySet.remove(3);
        capacitySet.remove(5);
        capacitySet.add(100);
        capacitySet.remove(7);
        assertEquals(6, capacitySet.size());
        assertEquals(10, getOrderedElementsOccupied(capacitySet));
        capacitySet.add(101);
        assertEquals(32, getIndicesHashTableLength(capacitySet));
        assertEquals(7, getOrderedElementsOccupied(capacitySet));
        assertEquals(List.of(0, 2, 4, 6, 8, 100, 101), new ArrayList<>(capacitySet));
    }

    @Override
    @Test
    public void Iterable_remove()
    {
        MutableOrderedSetTestCase.super.Iterable_remove();

        // Removing through the iterator in the middle and at the end, then re-adding, keeps the order consistent
        OrderedHashSet<Integer> set = OrderedHashSet.newSetWith(1, 2, 3, 4);
        Iterator<Integer> iterator = set.iterator();
        assertThrows(IllegalStateException.class, iterator::remove);
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        iterator.remove();
        assertThrows(IllegalStateException.class, iterator::remove);
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(Integer.valueOf(4), iterator.next());
        iterator.remove();
        assertFalse(iterator.hasNext());
        assertEquals(List.of(1, 3), new ArrayList<>(set));
        assertEquals(3, getOrderedElementsOccupied(set));
        assertTrue(set.add(2));
        assertEquals(List.of(1, 3, 2), new ArrayList<>(set));
    }

    @Override
    @Test
    public void Collection_clear()
    {
        MutableOrderedSetTestCase.super.Collection_clear();

        // Clearing an unallocated set keeps it unallocated
        OrderedHashSet<Integer> lazySet = new OrderedHashSet<>();
        lazySet.clear();
        assertNull(getField(lazySet, "indicesHashTable"));

        // Clearing keeps the allocated capacity and empties both arrays
        OrderedHashSet<Integer> set = OrderedHashSet.newSetWith(1, 2, 3, 4, 5, 6);
        set.remove(2);
        set.clear();
        assertEquals(16, getIndicesHashTableLength(set));
        assertEquals(0, getIndicesHashTableOccupied(set));
        assertEquals(0, getOrderedElementsOccupied(set));
        assertTrue(Arrays.stream((Object[]) getField(set, "orderedElements")).allMatch(each -> each == null));
        assertTrue(set.add(6));
        assertEquals(List.of(6), new ArrayList<>(set));
    }

    @Override
    @Test
    public void SetIterable_asParallel()
    {
        MutableOrderedSetTestCase.super.SetIterable_asParallel();

        // Batches cover the dense storage, skipping tombstones and unwrapping the null sentinel
        OrderedHashSet<Integer> set = OrderedHashSet.newSetWith(1, 2, 3, 4, 5, 6, 7, 8, null, 10);
        assertTrue(set.remove(1));
        assertTrue(set.remove(5));
        assertTrue(set.remove(6));
        assertEquals(10, getOrderedElementsOccupied(set));
        ParallelSetIterable<Integer> parallel = set.asParallel(ForkJoinPool.commonPool(), 3);
        assertEquals(Bags.mutable.with(2, 3, 4, 7, 8, null, 10), parallel.toBag());
        assertEquals(Sets.mutable.with(2, 4, 8, 10), parallel.select(each -> each != null && each % 2 == 0).toSet());
        assertEquals(6, parallel.count(each -> each != null));
        assertFalse(parallel.anySatisfy(each -> each != null && (each == 1 || each == 5 || each == 6)));
        assertTrue(parallel.anySatisfy(each -> each == null));
        assertTrue(parallel.allSatisfy(each -> each == null || each > 1));
        assertEquals(Integer.valueOf(10), parallel.detect(each -> each != null && each > 8));
    }
}
