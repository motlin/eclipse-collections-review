/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.partition.set.ordered;

import java.util.LinkedHashSet;

import org.eclipse.collections.api.partition.set.ordered.PartitionImmutableOrderedSet;
import org.eclipse.collections.api.partition.set.ordered.PartitionMutableOrderedSet;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedSetAdapter;

/**
 * @since 14.0
 */
public class PartitionOrderedSetAdapter<T> implements PartitionMutableOrderedSet<T>
{
    private final MutableOrderedSet<T> selected = OrderedSetAdapter.adapt(new LinkedHashSet<>());
    private final MutableOrderedSet<T> rejected = OrderedSetAdapter.adapt(new LinkedHashSet<>());

    @Override
    public MutableOrderedSet<T> getSelected()
    {
        return this.selected;
    }

    @Override
    public MutableOrderedSet<T> getRejected()
    {
        return this.rejected;
    }

    @Override
    public PartitionImmutableOrderedSet<T> toImmutable()
    {
        return new PartitionImmutableOrderedSetImpl<>(this);
    }
}
