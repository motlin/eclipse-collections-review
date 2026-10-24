/*
 * Copyright (c) 2021 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.bimap.immutable;

import org.eclipse.collections.impl.bimap.immutable.AbstractImmutableBiMap;
import org.eclipse.collections.test.map.mutable.UnmodifiableMapTestCase;
import org.junit.jupiter.api.Test;

public interface ImmutableBiMapTestCase extends ImmutableUnsortedBiMapTestCase, UnmodifiableMapTestCase
{
    @Override
    <T> AbstractImmutableBiMap<Object, T> newWith(T... elements);

    @Override
    <K, V> AbstractImmutableBiMap<K, V> newWithKeysValues(Object... elements);

    @Override
    default boolean allowsPut()
    {
        return false;
    }

    @Override
    default boolean supportsNullKeys()
    {
        return ImmutableUnsortedBiMapTestCase.super.supportsNullKeys();
    }

    @Override
    default boolean supportsNullValues()
    {
        return ImmutableUnsortedBiMapTestCase.super.supportsNullValues();
    }

    @Override
    default boolean supportsNonComparableKeys()
    {
        return ImmutableUnsortedBiMapTestCase.super.supportsNonComparableKeys();
    }

    @Override
    default boolean allowsDuplicateValues()
    {
        // Bimap values are the keys of the inverse map
        return false;
    }

    @Override
    @Test
    default void Object_equalsAndHashCode()
    {
        ImmutableUnsortedBiMapTestCase.super.Object_equalsAndHashCode();
        UnmodifiableMapTestCase.super.Object_equalsAndHashCode();
    }

    @Override
    @Test
    default void Iterable_toString()
    {
        // MapTestCase.Iterable_toString() assumes insertion order
        ImmutableUnsortedBiMapTestCase.super.Iterable_toString();
    }
}
