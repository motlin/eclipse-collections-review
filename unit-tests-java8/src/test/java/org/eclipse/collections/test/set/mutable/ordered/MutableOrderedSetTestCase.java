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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.impl.set.ordered.mutable.SynchronizedMutableOrderedSet;
import org.eclipse.collections.impl.set.ordered.mutable.UnmodifiableMutableOrderedSet;
import org.eclipse.collections.test.MutableOrderedIterableTestCase;
import org.eclipse.collections.test.collection.mutable.MutableCollectionUniqueTestCase;
import org.eclipse.collections.test.set.ordered.OrderedSetTestCase;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.test.IterableTestCase.assertIterablesEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface MutableOrderedSetTestCase extends OrderedSetTestCase, MutableCollectionUniqueTestCase, MutableOrderedIterableTestCase
{
    @Override
    <T> MutableOrderedSet<T> newWith(T... elements);

    @Override
    @Test
    default void Iterable_remove()
    {
        Iterable<Integer> iterable = this.newWith(3, 2, 1);
        Iterator<Integer> iterator = iterable.iterator();
        iterator.next();
        iterator.remove();
        assertIterablesEqual(this.newWith(2, 1), iterable);
    }

    @Override
    @Test
    default void ReversibleIterable_toReversed()
    {
        OrderedSetTestCase.super.ReversibleIterable_toReversed();

        // The reversed set is a mutable copy
        MutableOrderedSet<Integer> integers = this.newWith(3, 2, 1);
        MutableOrderedSet<Integer> reversed = integers.toReversed();
        assertNotSame(integers, reversed);
        assertTrue(reversed.add(4));
        assertIterablesEqual(this.newWith(3, 2, 1), integers);
        assertIterablesEqual(this.newWith(1, 2, 3, 4), reversed);
    }

    @Test
    default void MutableOrderedSet_clone()
    {
        MutableOrderedSet<Integer> integers = this.newWith(3, 2, 1);
        MutableOrderedSet<Integer> clone = integers.clone();
        assertNotSame(integers, clone);
        assertSame(integers.getClass(), clone.getClass());
        assertIterablesEqual(integers, clone);

        // The clone is independent of the original
        assertTrue(clone.add(4));
        assertTrue(clone.remove(3));
        assertIterablesEqual(this.newWith(3, 2, 1), integers);
        assertIterablesEqual(this.newWith(2, 1, 4), clone);

        assertIterablesEqual(this.newWith(), this.newWith().clone());
    }

    @Test
    default void MutableCollection_asUnmodifiable()
    {
        MutableOrderedSet<Integer> set = this.newWith(3, 2, 1);

        MutableOrderedSet<Integer> unmodifiable = set.asUnmodifiable();
        assertInstanceOf(UnmodifiableMutableOrderedSet.class, unmodifiable);
        assertEquals(set, unmodifiable);
        assertEquals(List.of(3, 2, 1), new ArrayList<>(unmodifiable));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add(4));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.remove(3));
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.with(4));
        assertThrows(UnsupportedOperationException.class, unmodifiable::clear);
        assertEquals(List.of(3, 2, 1), new ArrayList<>(set));

        if (this.allowsAdd())
        {
            // The unmodifiable view reads through to the set
            assertTrue(set.add(4));
            assertEquals(List.of(3, 2, 1, 4), new ArrayList<>(unmodifiable));
        }
    }

    @Test
    default void MutableCollection_asSynchronized()
    {
        MutableOrderedSet<Integer> set = this.newWith(3, 2, 1);

        MutableOrderedSet<Integer> synchronizedSet = set.asSynchronized();
        assertInstanceOf(SynchronizedMutableOrderedSet.class, synchronizedSet);
        assertEquals(set, synchronizedSet);
        assertEquals(List.of(3, 2, 1), new ArrayList<>(synchronizedSet));

        if (this.allowsAdd())
        {
            // The synchronized view writes through to the set
            assertTrue(synchronizedSet.add(4));
            assertEquals(List.of(3, 2, 1, 4), new ArrayList<>(set));
        }
        else
        {
            assertThrows(UnsupportedOperationException.class, () -> synchronizedSet.add(4));
        }
    }
}
