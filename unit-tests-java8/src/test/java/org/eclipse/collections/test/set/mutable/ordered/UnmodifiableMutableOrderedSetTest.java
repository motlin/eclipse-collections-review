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

import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedHashSet;
import org.eclipse.collections.test.IterableTestCase;
import org.eclipse.collections.test.UnmodifiableMutableCollectionTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class UnmodifiableMutableOrderedSetTest implements MutableOrderedSetTestCase, UnmodifiableMutableCollectionTestCase
{
    @SafeVarargs
    @Override
    public final <T> MutableOrderedSet<T> newWith(T... elements)
    {
        MutableOrderedSet<T> result = new OrderedHashSet<>();
        IterableTestCase.addAllTo(elements, result);
        return result.asUnmodifiable();
    }

    @Override
    @Test
    public void Iterable_remove()
    {
        UnmodifiableMutableCollectionTestCase.super.Iterable_remove();
    }

    @Override
    @Test
    public void MutableOrderedSet_clone()
    {
        // An unmodifiable view is its own clone
        MutableOrderedSet<Integer> integers = this.newWith(3, 2, 1);
        assertSame(integers, integers.clone());
    }
}
