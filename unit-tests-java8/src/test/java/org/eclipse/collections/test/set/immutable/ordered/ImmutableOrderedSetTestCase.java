/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.set.immutable.ordered;

import org.eclipse.collections.api.collection.ImmutableCollection;
import org.eclipse.collections.api.set.ordered.ImmutableOrderedSet;
import org.eclipse.collections.test.collection.immutable.ImmutableCollectionUniqueTestCase;
import org.eclipse.collections.test.set.ordered.OrderedSetTestCase;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.test.IterableTestCase.assertIterablesEqual;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public interface ImmutableOrderedSetTestCase extends OrderedSetTestCase, ImmutableCollectionUniqueTestCase
{
    @Override
    <T> ImmutableOrderedSet<T> newWith(T... elements);

    @Override
    @Test
    default void Iterable_remove()
    {
        ImmutableCollectionUniqueTestCase.super.Iterable_remove();
    }

    @Override
    @Test
    default void ImmutableCollection_newWith()
    {
        ImmutableCollection<Integer> immutableCollection = this.newWith(3, 2, 1);
        ImmutableCollection<Integer> newWith = immutableCollection.newWith(4);

        assertIterablesEqual(this.newWith(3, 2, 1, 4), newWith);
        assertNotSame(immutableCollection, newWith);
        assertThat(newWith, instanceOf(ImmutableOrderedSet.class));

        ImmutableCollection<Integer> newWith2 = newWith.newWith(4);

        assertIterablesEqual(this.newWith(3, 2, 1, 4), newWith2);
        assertSame(newWith, newWith2);
    }
}
