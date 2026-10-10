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

import org.eclipse.collections.api.partition.set.ordered.PartitionImmutableOrderedSet;
import org.eclipse.collections.api.partition.set.ordered.PartitionMutableOrderedSet;
import org.eclipse.collections.api.set.ordered.ImmutableOrderedSet;

/**
 * @since 14.0
 */
public class PartitionImmutableOrderedSetImpl<T> implements PartitionImmutableOrderedSet<T>
{
    private final ImmutableOrderedSet<T> selected;
    private final ImmutableOrderedSet<T> rejected;

    public PartitionImmutableOrderedSetImpl(PartitionMutableOrderedSet<T> partitionMutableOrderedSet)
    {
        this.selected = partitionMutableOrderedSet.getSelected().toImmutable();
        this.rejected = partitionMutableOrderedSet.getRejected().toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> getSelected()
    {
        return this.selected;
    }

    @Override
    public ImmutableOrderedSet<T> getRejected()
    {
        return this.rejected;
    }
}
