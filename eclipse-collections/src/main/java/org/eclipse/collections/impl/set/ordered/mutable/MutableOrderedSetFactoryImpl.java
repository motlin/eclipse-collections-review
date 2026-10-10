/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.set.ordered.mutable;

import org.eclipse.collections.api.factory.set.ordered.MutableOrderedSetFactory;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;

/**
 * @since 14.0
 */
@aQute.bnd.annotation.spi.ServiceProvider(MutableOrderedSetFactory.class)
public class MutableOrderedSetFactoryImpl implements MutableOrderedSetFactory
{
    public static final MutableOrderedSetFactory INSTANCE = new MutableOrderedSetFactoryImpl();

    @Override
    public <T> MutableOrderedSet<T> empty()
    {
        return new OrderedHashSet<>();
    }

    @Override
    public <T> MutableOrderedSet<T> of()
    {
        return this.empty();
    }

    @Override
    public <T> MutableOrderedSet<T> with()
    {
        return this.empty();
    }

    @Override
    public <T> MutableOrderedSet<T> of(T... items)
    {
        return this.with(items);
    }

    @Override
    public <T> MutableOrderedSet<T> with(T... items)
    {
        return OrderedHashSet.newSetWith(items);
    }

    @Override
    public <T> MutableOrderedSet<T> ofAll(Iterable<? extends T> items)
    {
        return this.withAll(items);
    }

    @Override
    public <T> MutableOrderedSet<T> withAll(Iterable<? extends T> items)
    {
        return OrderedHashSet.newSet(items);
    }
}
