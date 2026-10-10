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

import java.util.Iterator;

import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.test.MutableOrderedIterableTestCase;
import org.eclipse.collections.test.collection.mutable.MutableCollectionUniqueTestCase;
import org.eclipse.collections.test.set.ordered.OrderedSetTestCase;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.test.IterableTestCase.assertIterablesEqual;
import static org.junit.jupiter.api.Assertions.assertNotSame;
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
}
