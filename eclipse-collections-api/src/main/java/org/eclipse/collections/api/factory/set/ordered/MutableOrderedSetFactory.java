/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.factory.set.ordered;

import org.eclipse.collections.api.set.ordered.MutableOrderedSet;

/**
 * @since 14.0
 */
public interface MutableOrderedSetFactory
{
    <T> MutableOrderedSet<T> empty();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableOrderedSet<T> of();

    /**
     * Same as {@link #empty()}.
     */
    <T> MutableOrderedSet<T> with();

    /**
     * Same as {@link #with(Object[])}.
     */
    <T> MutableOrderedSet<T> of(T... items);

    <T> MutableOrderedSet<T> with(T... items);

    /**
     * Same as {@link #withAll(Iterable)}.
     */
    <T> MutableOrderedSet<T> ofAll(Iterable<? extends T> items);

    <T> MutableOrderedSet<T> withAll(Iterable<? extends T> items);
}
