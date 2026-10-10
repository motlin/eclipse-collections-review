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

import java.util.LinkedHashSet;

import org.eclipse.collections.api.set.ordered.ImmutableOrderedSet;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedSetAdapter;
import org.eclipse.collections.test.IterableTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImmutableOrderedSetAdapterTest implements ImmutableOrderedSetTestCase
{
    @SafeVarargs
    @Override
    public final <T> ImmutableOrderedSet<T> newWith(T... elements)
    {
        MutableOrderedSet<T> result = OrderedSetAdapter.adapt(new LinkedHashSet<>());
        IterableTestCase.addAllTo(elements, result);
        return result.toImmutable();
    }

    @Override
    @Test
    public void RichIterable_iterationOrder()
    {
        // TODO Support groupBy for ImmutableOrderedSetAdapter, then delete this override. Until then the inherited test runs up to its groupBy assertion.
        assertThrows(UnsupportedOperationException.class, () -> ImmutableOrderedSetTestCase.super.RichIterable_iterationOrder());
    }

    @Override
    @Test
    public void RichIterable_groupBy()
    {
        // TODO Support groupBy and groupByEach for ImmutableOrderedSetAdapter.
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).groupBy(each -> each));
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).groupByEach(each -> null));
    }
}
