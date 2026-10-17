/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.ordered.ReversibleIterable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public interface ReversibleIterableTestCase extends OrderedIterableTestCase
{
    @Test
    default void ReversibleIterable_toReversed()
    {
        ReversibleIterable<Integer> iterable = (ReversibleIterable<Integer>) this.newWith(3, 2, 1);
        ReversibleIterable<Integer> reversed = iterable.toReversed();
        switch (this.getOrderingType())
        {
            case INSERTION_ORDER, SORTED_REVERSE_NATURAL ->
            {
                assertEquals(Lists.immutable.with(1, 2, 3), reversed.toList());
                assertEquals(Lists.immutable.with(3, 2, 1), reversed.toReversed().toList());
            }
            case SORTED_NATURAL ->
            {
                assertEquals(Lists.immutable.with(3, 2, 1), reversed.toList());
                assertEquals(Lists.immutable.with(1, 2, 3), reversed.toReversed().toList());
            }
            default -> fail("Unexpected value: " + this.getOrderingType());
        }

        ReversibleIterable<Integer> empty = (ReversibleIterable<Integer>) this.<Integer>newWith();
        assertEquals(Lists.immutable.empty(), empty.toReversed().toList());
    }

    @Test
    default void ReversibleIterable_reverseForEach()
    {
        ReversibleIterable<Integer> iterable = (ReversibleIterable<Integer>) this.newWith(3, 2, 1);
        MutableList<Integer> result = Lists.mutable.empty();
        iterable.reverseForEach(result::add);
        switch (this.getOrderingType())
        {
            case INSERTION_ORDER, SORTED_REVERSE_NATURAL -> assertEquals(Lists.immutable.with(1, 2, 3), result);
            case SORTED_NATURAL -> assertEquals(Lists.immutable.with(3, 2, 1), result);
            default -> fail("Unexpected value: " + this.getOrderingType());
        }

        ReversibleIterable<Integer> empty = (ReversibleIterable<Integer>) this.<Integer>newWith();
        empty.reverseForEach(each -> fail());
    }

    @Test
    default void ReversibleIterable_detectLastIndex()
    {
        ReversibleIterable<Integer> iterable = (ReversibleIterable<Integer>) this.newWith(3, 2, 1);
        switch (this.getOrderingType())
        {
            case INSERTION_ORDER, SORTED_REVERSE_NATURAL ->
            {
                assertEquals(0, iterable.detectLastIndex(each -> each == 3));
                assertEquals(2, iterable.detectLastIndex(each -> each == 1));
            }
            case SORTED_NATURAL ->
            {
                assertEquals(2, iterable.detectLastIndex(each -> each == 3));
                assertEquals(0, iterable.detectLastIndex(each -> each == 1));
            }
            default -> fail("Unexpected value: " + this.getOrderingType());
        }
        assertEquals(1, iterable.detectLastIndex(each -> each == 2));
        assertEquals(2, iterable.detectLastIndex(each -> true));
        assertEquals(-1, iterable.detectLastIndex(each -> each == 99));
        assertEquals(-1, ((ReversibleIterable<Integer>) this.<Integer>newWith()).detectLastIndex(each -> true));

        if (!this.allowsDuplicates())
        {
            return;
        }

        ReversibleIterable<Integer> iterableWithDuplicates = (ReversibleIterable<Integer>) this.newWith(3, 3, 3, 2, 2, 1);
        switch (this.getOrderingType())
        {
            case INSERTION_ORDER, SORTED_REVERSE_NATURAL ->
            {
                assertEquals(2, iterableWithDuplicates.detectLastIndex(each -> each == 3));
                assertEquals(4, iterableWithDuplicates.detectLastIndex(each -> each == 2));
                assertEquals(5, iterableWithDuplicates.detectLastIndex(each -> each == 1));
            }
            case SORTED_NATURAL ->
            {
                assertEquals(5, iterableWithDuplicates.detectLastIndex(each -> each == 3));
                assertEquals(2, iterableWithDuplicates.detectLastIndex(each -> each == 2));
                assertEquals(0, iterableWithDuplicates.detectLastIndex(each -> each == 1));
            }
            default -> fail("Unexpected value: " + this.getOrderingType());
        }
    }

    @Test
    default void ReversibleIterable_take()
    {
        ReversibleIterable<Integer> iterable = (ReversibleIterable<Integer>) this.newWith(3, 2, 1);
        assertEquals(Lists.immutable.empty(), iterable.take(0).toList());
        switch (this.getOrderingType())
        {
            case INSERTION_ORDER, SORTED_REVERSE_NATURAL ->
            {
                assertEquals(Lists.immutable.with(3), iterable.take(1).toList());
                assertEquals(Lists.immutable.with(3, 2), iterable.take(2).toList());
                assertEquals(Lists.immutable.with(3, 2, 1), iterable.take(3).toList());
                assertEquals(Lists.immutable.with(3, 2, 1), iterable.take(Integer.MAX_VALUE).toList());
            }
            case SORTED_NATURAL ->
            {
                assertEquals(Lists.immutable.with(1), iterable.take(1).toList());
                assertEquals(Lists.immutable.with(1, 2), iterable.take(2).toList());
                assertEquals(Lists.immutable.with(1, 2, 3), iterable.take(3).toList());
                assertEquals(Lists.immutable.with(1, 2, 3), iterable.take(Integer.MAX_VALUE).toList());
            }
            default -> fail("Unexpected value: " + this.getOrderingType());
        }
        assertThrows(IllegalArgumentException.class, () -> iterable.take(-1));
    }

    @Test
    default void ReversibleIterable_drop()
    {
        ReversibleIterable<Integer> iterable = (ReversibleIterable<Integer>) this.newWith(3, 2, 1);
        switch (this.getOrderingType())
        {
            case INSERTION_ORDER, SORTED_REVERSE_NATURAL ->
            {
                assertEquals(Lists.immutable.with(3, 2, 1), iterable.drop(0).toList());
                assertEquals(Lists.immutable.with(2, 1), iterable.drop(1).toList());
                assertEquals(Lists.immutable.with(1), iterable.drop(2).toList());
            }
            case SORTED_NATURAL ->
            {
                assertEquals(Lists.immutable.with(1, 2, 3), iterable.drop(0).toList());
                assertEquals(Lists.immutable.with(2, 3), iterable.drop(1).toList());
                assertEquals(Lists.immutable.with(3), iterable.drop(2).toList());
            }
            default -> fail("Unexpected value: " + this.getOrderingType());
        }
        assertEquals(Lists.immutable.empty(), iterable.drop(3).toList());
        assertEquals(Lists.immutable.empty(), iterable.drop(Integer.MAX_VALUE).toList());
        assertThrows(IllegalArgumentException.class, () -> iterable.drop(-1));
    }

    @Test
    default void ReversibleIterable_asReversed()
    {
        ReversibleIterable<Integer> iterable = (ReversibleIterable<Integer>) this.newWith(3, 2, 1);
        switch (this.getOrderingType())
        {
            case INSERTION_ORDER, SORTED_REVERSE_NATURAL -> assertEquals(Lists.immutable.with(1, 2, 3), iterable.asReversed().toList());
            case SORTED_NATURAL -> assertEquals(Lists.immutable.with(3, 2, 1), iterable.asReversed().toList());
            default -> fail("Unexpected value: " + this.getOrderingType());
        }
        assertEquals(Lists.immutable.empty(), ((ReversibleIterable<Integer>) this.<Integer>newWith()).asReversed().toList());
    }
}
