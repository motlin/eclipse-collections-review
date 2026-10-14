/*
 * Copyright (c) 2021 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test;

import org.eclipse.collections.api.ordered.SortedIterable;
import org.eclipse.collections.impl.block.factory.Comparators;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public interface SortedIterableTestCase extends OrderedIterableTestCase, NoDetectOptionalNullTestCase
{
    @Override
    <T> SortedIterable<T> newWith(T... elements);

    @Override
    default boolean supportsNonComparableElements()
    {
        return false;
    }

    @Test
    default void SortedIterable_comparator()
    {
        switch (this.getOrderingType())
        {
            case SORTED_NATURAL -> assertNull(this.newWith().comparator());
            case SORTED_REVERSE_NATURAL -> assertSame(Comparators.reverseNaturalOrder(), this.newWith().comparator());
            default -> throw new AssertionError("Unexpected ordering type for SortedIterable: " + this.getOrderingType());
        }
    }
}
