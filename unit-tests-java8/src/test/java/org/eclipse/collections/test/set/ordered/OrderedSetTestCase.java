/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.set.ordered;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.OrderedSets;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.orderedset.OrderedSetMultimap;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.api.set.ordered.OrderedSet;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.tuple.Tuples;
import org.eclipse.collections.test.OrderedIterableTestCase;
import org.eclipse.collections.test.list.TransformsToListTrait;
import org.eclipse.collections.test.set.SetIterableTestCase;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.test.IterableTestCase.assertIterablesEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface OrderedSetTestCase extends SetIterableTestCase, OrderedIterableTestCase, TransformsToListTrait
{
    @Override
    <T> OrderedSet<T> newWith(T... elements);

    @Override
    default <T> OrderedSet<T> getExpectedFiltered(T... elements)
    {
        return OrderedSets.immutable.with(elements);
    }

    @Override
    default <T> MutableOrderedSet<T> newMutableForFilter(T... elements)
    {
        return OrderedSets.mutable.with(elements);
    }

    @Override
    @Test
    default void SetIterable_union()
    {
        SetIterable<Integer> union = this.newWith(3, 2, 1).union(this.newWith(5, 4, 3));
        assertIterablesEqual(this.newWith(3, 2, 1, 5, 4), union);
    }

    @Override
    @Test
    default void RichIterable_groupBy()
    {
        SetIterableTestCase.super.RichIterable_groupBy();

        // Each group is an ordered set that keeps the source iteration order
        OrderedSetMultimap<Boolean, Integer> groupBy = this.newWith(4, 3, 2, 1).groupBy(each -> each % 2 != 0);
        assertIterablesEqual(OrderedSets.immutable.with(3, 1), groupBy.get(true));
        assertIterablesEqual(OrderedSets.immutable.with(4, 2), groupBy.get(false));

        OrderedSetMultimap<Integer, Integer> groupByEach = this.newWith(4, 3, 2, 1).groupByEach(each -> Interval.fromTo(-1, -each));
        assertIterablesEqual(OrderedSets.immutable.with(4, 3, 2, 1), groupByEach.get(-1));
        assertIterablesEqual(OrderedSets.immutable.with(4, 3), groupByEach.get(-3));
    }

    @Test
    default void OrderedIterable_indexOf()
    {
        OrderedSet<Integer> integers = this.newWith(3, 2, 1);
        assertEquals(0, integers.indexOf(3));
        assertEquals(1, integers.indexOf(2));
        assertEquals(2, integers.indexOf(1));
        assertEquals(-1, integers.indexOf(4));
        assertEquals(-1, integers.indexOf(null));
        assertEquals(1, this.newWith(3, null, 1).indexOf(null));
    }

    @Test
    default void OrderedIterable_detectIndex()
    {
        OrderedSet<Integer> integers = this.newWith(3, 2, 1);
        assertEquals(0, integers.detectIndex(each -> each == 3));
        assertEquals(1, integers.detectIndex(each -> each == 2));
        assertEquals(2, integers.detectIndex(each -> each == 1));
        assertEquals(1, integers.detectIndex(each -> each < 3));
        assertEquals(-1, integers.detectIndex(each -> each == 99));
        assertEquals(-1, this.<Integer>newWith().detectIndex(each -> true));
    }

    @Test
    default void ReversibleIterable_detectLastIndex()
    {
        OrderedSet<Integer> integers = this.newWith(3, 2, 1);
        assertEquals(0, integers.detectLastIndex(each -> each == 3));
        assertEquals(1, integers.detectLastIndex(each -> each == 2));
        assertEquals(2, integers.detectLastIndex(each -> each == 1));
        assertEquals(1, integers.detectLastIndex(each -> each > 1));
        assertEquals(-1, integers.detectLastIndex(each -> each == 99));
        assertEquals(-1, this.<Integer>newWith().detectLastIndex(each -> true));
    }

    @Test
    default void OrderedIterable_forEachWithIndex_from_to()
    {
        OrderedSet<Integer> integers = this.newWith(9, 8, 7, 6, 5, 4, 3, 2, 1, 0);

        MutableList<Pair<Integer, Integer>> result = Lists.mutable.empty();
        integers.forEachWithIndex(5, 7, (each, index) -> result.add(Tuples.pair(each, index)));
        assertIterablesEqual(Lists.immutable.with(Tuples.pair(4, 5), Tuples.pair(3, 6), Tuples.pair(2, 7)), result);

        MutableList<Pair<Integer, Integer>> reverseResult = Lists.mutable.empty();
        integers.forEachWithIndex(7, 5, (each, index) -> reverseResult.add(Tuples.pair(each, index)));
        assertIterablesEqual(Lists.immutable.with(Tuples.pair(2, 7), Tuples.pair(3, 6), Tuples.pair(4, 5)), reverseResult);

        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEachWithIndex(-1, 0, (each, index) -> { }));
        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEachWithIndex(0, -1, (each, index) -> { }));
        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEachWithIndex(0, 10, (each, index) -> { }));
        assertThrows(IndexOutOfBoundsException.class, () -> integers.forEachWithIndex(10, 0, (each, index) -> { }));
    }

    @Test
    default void ReversibleIterable_reverseForEach()
    {
        MutableList<Integer> result = Lists.mutable.empty();
        this.newWith(3, 2, 1).reverseForEach(result::add);
        assertIterablesEqual(Lists.immutable.with(1, 2, 3), result);

        MutableList<Integer> empty = Lists.mutable.empty();
        this.<Integer>newWith().reverseForEach(empty::add);
        assertIterablesEqual(Lists.immutable.empty(), empty);
    }

    @Test
    default void ReversibleIterable_reverseForEachWithIndex()
    {
        MutableList<Pair<Integer, Integer>> result = Lists.mutable.empty();
        this.newWith(3, 2, 1).reverseForEachWithIndex((each, index) -> result.add(Tuples.pair(each, index)));
        assertIterablesEqual(Lists.immutable.with(Tuples.pair(1, 2), Tuples.pair(2, 1), Tuples.pair(3, 0)), result);

        MutableList<Pair<Integer, Integer>> empty = Lists.mutable.empty();
        this.<Integer>newWith().reverseForEachWithIndex((each, index) -> empty.add(Tuples.pair(each, index)));
        assertIterablesEqual(Lists.immutable.empty(), empty);
    }

    @Test
    default void ReversibleIterable_toReversed()
    {
        OrderedSet<Integer> integers = this.newWith(3, 2, 1);
        OrderedSet<Integer> reversed = integers.toReversed();
        assertIterablesEqual(this.getExpectedFiltered(1, 2, 3), reversed);
        assertIterablesEqual(integers, reversed.toReversed());
        assertIterablesEqual(this.getExpectedFiltered(), this.<Integer>newWith().toReversed());
    }

    @Test
    default void ReversibleIterable_take()
    {
        OrderedSet<Integer> integers = this.newWith(3, 2, 1);
        assertIterablesEqual(this.getExpectedFiltered(), integers.take(0));
        assertIterablesEqual(this.getExpectedFiltered(3), integers.take(1));
        assertIterablesEqual(this.getExpectedFiltered(3, 2), integers.take(2));
        assertIterablesEqual(this.getExpectedFiltered(3, 2, 1), integers.take(3));
        assertIterablesEqual(this.getExpectedFiltered(3, 2, 1), integers.take(Integer.MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> integers.take(-1));
    }

    @Test
    default void ReversibleIterable_drop()
    {
        OrderedSet<Integer> integers = this.newWith(3, 2, 1);
        assertIterablesEqual(this.getExpectedFiltered(3, 2, 1), integers.drop(0));
        assertIterablesEqual(this.getExpectedFiltered(2, 1), integers.drop(1));
        assertIterablesEqual(this.getExpectedFiltered(1), integers.drop(2));
        assertIterablesEqual(this.getExpectedFiltered(), integers.drop(3));
        assertIterablesEqual(this.getExpectedFiltered(), integers.drop(Integer.MAX_VALUE));
        assertThrows(IllegalArgumentException.class, () -> integers.drop(-1));
    }

    @Test
    default void OrderedIterable_corresponds()
    {
        OrderedSet<Integer> integers = this.newWith(3, 2, 1);
        assertTrue(integers.corresponds(Lists.mutable.with(3, 2, 1), Integer::equals));
        assertFalse(integers.corresponds(Lists.mutable.with(1, 2, 3), Integer::equals));
        assertFalse(integers.corresponds(Lists.mutable.with(3, 2), Integer::equals));
        assertTrue(this.<Integer>newWith().corresponds(Lists.mutable.empty(), Integer::equals));
    }

    @Test
    default void OrderedIterable_distinct()
    {
        assertIterablesEqual(this.getExpectedFiltered(3, 2, 1), this.newWith(3, 2, 1).distinct());
    }

    @Test
    default void OrderedIterable_toStack()
    {
        MutableStack<Integer> stack = this.newWith(3, 2, 1).toStack();
        assertIterablesEqual(Lists.immutable.with(1, 2, 3), stack.toList());
    }
}
