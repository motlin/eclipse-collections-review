/*
 * Copyright (c) 2022 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.map.immutable.sorted;

import java.util.Map;

import org.eclipse.collections.impl.map.sorted.immutable.AbstractImmutableSortedMap;
import org.eclipse.collections.test.FixedSizeIterableTestCase;
import org.eclipse.collections.test.map.SortedMapIterableTestCase;
import org.eclipse.collections.test.map.mutable.UnmodifiableMapTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public interface ImmutableSortedMapIterableTestCase extends SortedMapIterableTestCase, FixedSizeIterableTestCase, UnmodifiableMapTestCase
{
    @Override
    <T> AbstractImmutableSortedMap<Object, T> newWith(T... elements);

    @Override
    <K, V> AbstractImmutableSortedMap<K, V> newWithKeysValues(Object... elements);

    @Override
    default boolean allowsPut()
    {
        return false;
    }

    @Override
    default boolean supportsNullKeys()
    {
        return SortedMapIterableTestCase.super.supportsNullKeys();
    }

    @Override
    default boolean supportsNullValues()
    {
        return SortedMapIterableTestCase.super.supportsNullValues();
    }

    @Override
    default boolean supportsNonComparableKeys()
    {
        return false;
    }

    @Override
    @Test
    default void Iterable_remove()
    {
        FixedSizeIterableTestCase.super.Iterable_remove();
    }

    @Override
    @Test
    default void Object_equalsAndHashCode()
    {
        SortedMapIterableTestCase.super.Object_equalsAndHashCode();
        UnmodifiableMapTestCase.super.Object_equalsAndHashCode();
    }

    @Override
    @Test
    default void Iterable_toString()
    {
        // MapTestCase.Iterable_toString() assumes insertion order
        SortedMapIterableTestCase.super.Iterable_toString();
    }

    @Override
    @Test
    default void Map_merge()
    {
        // TODO: Throw before calling the function, like the unmodifiable maps do; AbstractImmutableSortedMap inherits the default java.util.Map.merge(), which calls the function before put() throws.
        Map<Integer, String> map = this.newWithKeysValues(1, "1", 2, "2", 3, "3");
        assertThrows(UnsupportedOperationException.class, () -> map.merge(3, "4", (v1, v2) -> v1 + v2));
        assertThrows(UnsupportedOperationException.class, () -> map.merge(4, "4", (v1, v2) -> {
            fail("Expected lambda not to be called for an absent key");
            return null;
        }));
        assertEquals(this.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Override
    @Test
    default void Map_putIfAbsent()
    {
        // TODO: Throw immediately instead of returning the existing value; AbstractImmutableSortedMap inherits the default java.util.Map.putIfAbsent().
        Map<Integer, String> map = this.newWithKeysValues(1, "1", 2, "2", 3, "3");
        assertEquals("1", map.putIfAbsent(1, "One"));
        assertThrows(UnsupportedOperationException.class, () -> map.putIfAbsent(4, "4"));
        assertEquals(this.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Override
    @Test
    default void Map_remove_key_value()
    {
        // TODO: Throw immediately instead of returning false for an absent entry; AbstractImmutableSortedMap inherits the default java.util.Map.remove(Object, Object).
        Map<Integer, String> map = this.newWithKeysValues(1, "1", 2, "2", 3, "3");
        assertThrows(UnsupportedOperationException.class, () -> map.remove(1, "1"));
        assertFalse(map.remove(2, "wrong"));
        assertFalse(map.remove(4, "4"));
        assertEquals(this.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Override
    @Test
    default void Map_replace()
    {
        // TODO: Throw immediately instead of returning for an absent key or a mismatched value; AbstractImmutableSortedMap inherits the default java.util.Map.replace() methods.
        Map<Integer, String> map = this.newWithKeysValues(1, "1", 2, "2", 3, "3");
        assertThrows(UnsupportedOperationException.class, () -> map.replace(1, "One"));
        assertNull(map.replace(4, "Four"));
        assertThrows(UnsupportedOperationException.class, () -> map.replace(2, "2", "Two"));
        assertFalse(map.replace(3, "wrong", "Three"));
        assertFalse(map.replace(4, "4", "Four"));
        assertEquals(this.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }
}
