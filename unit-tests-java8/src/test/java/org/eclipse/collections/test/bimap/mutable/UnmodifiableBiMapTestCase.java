/*
 * Copyright (c) 2022 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.bimap.mutable;

import java.util.Map;

import org.eclipse.collections.test.FixedSizeIterableTestCase;
import org.eclipse.collections.test.map.mutable.UnmodifiableMapTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface UnmodifiableBiMapTestCase extends MutableBiMapTestCase, UnmodifiableMapTestCase, FixedSizeIterableTestCase
{
    @Override
    @Test
    default void Map_replaceAll()
    {
        // TODO: Throw UnsupportedOperationException before checking the arguments, like the other unmodifiable maps; UnmodifiableBiMap inherits the default java.util.Map method.
        Map<Integer, String> map = this.newWithKeysValues(1, "1", 2, "2", 3, "3");
        assertThrows(NullPointerException.class, () -> map.replaceAll(null));
        assertThrows(UnsupportedOperationException.class, () -> map.replaceAll((k, v) -> v + "modified"));
        assertEquals(this.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Override
    @Test
    default void Map_compute()
    {
        // TODO: Throw UnsupportedOperationException before checking the arguments, like the other unmodifiable maps; UnmodifiableBiMap inherits the default java.util.Map method.
        Map<Integer, String> map = this.newWithKeysValues(1, "1", 2, "2", 3, "3");
        assertThrows(NullPointerException.class, () -> map.compute(1, null));
        assertThrows(UnsupportedOperationException.class, () -> map.compute(1, (k, v) -> "modified"));
        assertThrows(UnsupportedOperationException.class, () -> map.compute(4, (k, v) -> "new"));
        assertEquals(this.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Override
    @Test
    default void Map_computeIfAbsent()
    {
        // TODO: Throw UnsupportedOperationException unconditionally, like the other unmodifiable maps; UnmodifiableBiMap inherits the default java.util.Map method, which checks the arguments first and returns without throwing when nothing would change.
        Map<Integer, String> map = this.newWithKeysValues(1, "1", 2, "2", 3, "3");
        assertThrows(NullPointerException.class, () -> map.computeIfAbsent(1, null));
        assertEquals("1", map.computeIfAbsent(1, k -> "modified"));
        assertThrows(UnsupportedOperationException.class, () -> map.computeIfAbsent(4, k -> "new"));
        assertEquals(this.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Override
    @Test
    default void Map_computeIfPresent()
    {
        // TODO: Throw UnsupportedOperationException unconditionally, like the other unmodifiable maps; UnmodifiableBiMap inherits the default java.util.Map method, which checks the arguments first and returns without throwing when nothing would change.
        Map<Integer, String> map = this.newWithKeysValues(1, "1", 2, "2", 3, "3");
        assertThrows(NullPointerException.class, () -> map.computeIfPresent(1, null));
        assertThrows(UnsupportedOperationException.class, () -> map.computeIfPresent(1, (k, v) -> "modified"));
        assertNull(map.computeIfPresent(4, (k, v) -> "new"));
        assertEquals(this.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Override
    @Test
    default void Map_putIfAbsent()
    {
        // TODO: Throw UnsupportedOperationException unconditionally, like the other unmodifiable maps; UnmodifiableBiMap inherits the default java.util.Map method, which returns without throwing when nothing would change.
        Map<Integer, String> map = this.newWithKeysValues(1, "1", 2, "2", 3, "3");
        assertEquals("1", map.putIfAbsent(1, "One"));
        assertThrows(UnsupportedOperationException.class, () -> map.putIfAbsent(4, "4"));
        assertEquals(this.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Override
    @Test
    default void Map_remove_key_value()
    {
        // TODO: Throw UnsupportedOperationException unconditionally, like the other unmodifiable maps; UnmodifiableBiMap inherits the default java.util.Map method, which returns without throwing when nothing would change.
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
        // TODO: Throw UnsupportedOperationException unconditionally, like the other unmodifiable maps; UnmodifiableBiMap inherits the default java.util.Map method, which returns without throwing when nothing would change.
        Map<Integer, String> map = this.newWithKeysValues(1, "1", 2, "2", 3, "3");
        assertThrows(UnsupportedOperationException.class, () -> map.replace(1, "One"));
        assertNull(map.replace(4, "Four"));
        assertThrows(UnsupportedOperationException.class, () -> map.replace(2, "2", "Two"));
        assertFalse(map.replace(3, "wrong", "Three"));
        assertFalse(map.replace(4, "4", "Four"));
        assertEquals(this.newWithKeysValues(1, "1", 2, "2", 3, "3"), map);
    }

    @Override
    default boolean allowsPut()
    {
        return false;
    }

    @Override
    @Test
    default void Iterable_remove()
    {
        FixedSizeIterableTestCase.super.Iterable_remove();
    }
}
