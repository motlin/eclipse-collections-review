/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.factory;

import java.util.Set;

import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedSetAdapter;

/**
 * @since 14.0
 */
@SuppressWarnings("ConstantNamingConvention")
public final class OrderedSets
{
    private OrderedSets()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> MutableOrderedSet<T> adapt(Set<T> set)
    {
        return OrderedSetAdapter.adapt(set);
    }
}
