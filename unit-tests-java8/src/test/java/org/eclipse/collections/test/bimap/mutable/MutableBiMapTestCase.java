/*
 * Copyright (c) 2021 Goldman Sachs.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.bimap.mutable;

import org.eclipse.collections.api.bimap.MutableBiMap;
import org.eclipse.collections.test.map.mutable.MapTestCase;
import org.junit.jupiter.api.Test;

public interface MutableBiMapTestCase extends MutableUnsortedBiMapTestCase, MapTestCase
{
    @Override
    <T> MutableBiMap<Object, T> newWith(T... elements);

    @Override
    default boolean supportsNullKeys()
    {
        return MutableUnsortedBiMapTestCase.super.supportsNullKeys();
    }

    @Override
    default boolean supportsNullValues()
    {
        return MutableUnsortedBiMapTestCase.super.supportsNullValues();
    }

    @Override
    default boolean allowsPut()
    {
        return MutableUnsortedBiMapTestCase.super.allowsPut();
    }

    @Override
    default boolean supportsNonComparableKeys()
    {
        return MutableUnsortedBiMapTestCase.super.supportsNonComparableKeys();
    }

    @Override
    default boolean allowsDuplicateValues()
    {
        // Bimap values are the keys of the inverse map
        return false;
    }

    @Override
    @Test
    default void Iterable_toString()
    {
        // MapTestCase.Iterable_toString() assumes insertion order, which bimaps don't have
        MutableUnsortedBiMapTestCase.super.Iterable_toString();
    }

    @Override
    @Test
    default void Object_equalsAndHashCode()
    {
        MutableUnsortedBiMapTestCase.super.Object_equalsAndHashCode();
        MapTestCase.super.Object_equalsAndHashCode();
    }
}
