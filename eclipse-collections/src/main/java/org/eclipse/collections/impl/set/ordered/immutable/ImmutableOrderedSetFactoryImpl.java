/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.set.ordered.immutable;

import java.util.LinkedHashSet;

import org.eclipse.collections.api.factory.set.ordered.ImmutableOrderedSetFactory;
import org.eclipse.collections.api.set.ordered.ImmutableOrderedSet;
import org.eclipse.collections.impl.utility.ArrayIterate;
import org.eclipse.collections.impl.utility.Iterate;

/**
 * @since 14.0
 */
@aQute.bnd.annotation.spi.ServiceProvider(ImmutableOrderedSetFactory.class)
public class ImmutableOrderedSetFactoryImpl implements ImmutableOrderedSetFactory
{
    public static final ImmutableOrderedSetFactory INSTANCE = new ImmutableOrderedSetFactoryImpl();
    private static final ImmutableOrderedSet<?> EMPTY = new ImmutableOrderedSetAdapter<>(new LinkedHashSet<>());

    @Override
    public <T> ImmutableOrderedSet<T> empty()
    {
        return (ImmutableOrderedSet<T>) EMPTY;
    }

    @Override
    public <T> ImmutableOrderedSet<T> of()
    {
        return this.empty();
    }

    @Override
    public <T> ImmutableOrderedSet<T> with()
    {
        return this.empty();
    }

    @Override
    public <T> ImmutableOrderedSet<T> of(T... items)
    {
        return this.with(items);
    }

    @Override
    public <T> ImmutableOrderedSet<T> with(T... items)
    {
        if (items.length == 0)
        {
            return this.empty();
        }
        LinkedHashSet<T> set = new LinkedHashSet<>();
        ArrayIterate.addAllTo(items, set);
        return new ImmutableOrderedSetAdapter<>(set);
    }

    @Override
    public <T> ImmutableOrderedSet<T> ofAll(Iterable<? extends T> items)
    {
        return this.withAll(items);
    }

    @Override
    public <T> ImmutableOrderedSet<T> withAll(Iterable<? extends T> items)
    {
        LinkedHashSet<T> set = new LinkedHashSet<>();
        Iterate.addAllTo(items, set);
        if (set.isEmpty())
        {
            return this.empty();
        }
        return new ImmutableOrderedSetAdapter<>(set);
    }
}
