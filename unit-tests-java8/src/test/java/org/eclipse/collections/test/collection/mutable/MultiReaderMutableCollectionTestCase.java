/*
 * Copyright (c) 2024 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.collection.mutable;

import org.eclipse.collections.impl.collection.mutable.AbstractMultiReaderMutableCollection;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public interface MultiReaderMutableCollectionTestCase extends MutableCollectionTestCase
{
    @Override
    <T> AbstractMultiReaderMutableCollection<T> newWith(T... elements);

    @Override
    @Test
    default void Iterable_remove()
    {
        // Multi-reader collections don't support iterator()
    }

    @Override
    @Test
    default void Iterable_next()
    {
        // Multi-reader collections don't support iterator() outside withReadLockAndDelegate
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).iterator());
    }

    @Override
    @Test
    default void Iterable_hasNext()
    {
        // Multi-reader collections don't support iterator() outside withReadLockAndDelegate
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).iterator());
    }

    @Override
    @Test
    default void RichIterable_getFirst()
    {
        // Does not support iterator outside withReadLockAndDelegate
    }

    @Override
    @Test
    default void RichIterable_getLast()
    {
        // getLast() works outside withReadLockAndDelegate, but iterator() does not, so check it against the ordering type only
        AbstractMultiReaderMutableCollection<Integer> collection = this.newWith(3, 2, 1);
        Integer last = collection.getLast();
        assertNotEquals(collection.getFirst(), last);
        switch (this.getOrderingType())
        {
            case UNORDERED -> assertThat(last, isOneOf(3, 2, 1));
            case INSERTION_ORDER -> assertThat(last, is(1));
            default -> fail("Unexpected value: " + this.getOrderingType());
        }
    }

    @Override
    @Test
    default void RichIterable_getOnly()
    {
        // Does not support iterator outside withReadLockAndDelegate
    }
}
