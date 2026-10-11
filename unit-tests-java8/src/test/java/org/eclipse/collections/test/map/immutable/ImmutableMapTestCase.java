/*
 * Copyright (c) 2021 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.map.immutable;

import org.eclipse.collections.impl.map.immutable.AbstractImmutableMap;
import org.eclipse.collections.test.ImmutableUnorderedIterableTestCase;
import org.eclipse.collections.test.map.UnsortedMapIterableTestCase;
import org.eclipse.collections.test.map.mutable.UnmodifiableMapTestCase;
import org.junit.jupiter.api.Test;

public interface ImmutableMapTestCase extends UnsortedMapIterableTestCase, ImmutableUnorderedIterableTestCase, UnmodifiableMapTestCase
{
    @Override
    <T> AbstractImmutableMap<Object, T> newWith(T... elements);

    @Override
    <K, V> AbstractImmutableMap<K, V> newWithKeysValues(Object... elements);

    @Override
    default boolean allowsPut()
    {
        return false;
    }

    @Override
    default boolean supportsNullKeys()
    {
        return UnsortedMapIterableTestCase.super.supportsNullKeys();
    }

    @Override
    default boolean supportsNullValues()
    {
        return UnsortedMapIterableTestCase.super.supportsNullValues();
    }

    @Override
    default boolean supportsNonComparableKeys()
    {
        return UnsortedMapIterableTestCase.super.supportsNonComparableKeys();
    }

    @Override
    @Test
    default void Iterable_remove()
    {
        ImmutableUnorderedIterableTestCase.super.Iterable_remove();
    }

    @Override
    @Test
    default void Object_equalsAndHashCode()
    {
        UnsortedMapIterableTestCase.super.Object_equalsAndHashCode();
        UnmodifiableMapTestCase.super.Object_equalsAndHashCode();
    }

    @Override
    @Test
    default void Iterable_toString()
    {
        // MapTestCase.Iterable_toString() assumes insertion order
        UnsortedMapIterableTestCase.super.Iterable_toString();
    }
}
