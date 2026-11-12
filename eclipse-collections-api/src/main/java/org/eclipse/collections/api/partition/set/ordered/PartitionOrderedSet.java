/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.partition.set.ordered;

import org.eclipse.collections.api.partition.ordered.PartitionReversibleIterable;
import org.eclipse.collections.api.partition.set.PartitionSet;
import org.eclipse.collections.api.set.ordered.OrderedSet;

/**
 * @since 14.0
 */
public interface PartitionOrderedSet<T> extends PartitionSet<T>, PartitionReversibleIterable<T>
{
    @Override
    OrderedSet<T> getSelected();

    @Override
    OrderedSet<T> getRejected();
}
