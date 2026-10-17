/*
 * Copyright (c) 2021 Two Sigma.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.map.mutable.ordered;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.test.IterableTestCase.assertIterablesEqual;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class OrderedMapAdapterTest implements MutableOrderedMapTestCase
{
    @Override
    public <T> MutableOrderedMap<Object, T> newWith(T... elements)
    {
        int i = elements.length;
        MutableOrderedMap<Object, T> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (T each : elements)
        {
            assertNull(result.put(i, each));
            i--;
        }
        return result;
    }

    @Override
    public <K, V> MutableOrderedMap<K, V> newWithKeysValues(Object... elements)
    {
        if (elements.length % 2 != 0)
        {
            fail(String.valueOf(elements.length));
        }

        MutableOrderedMap<K, V> result = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (int i = 0; i < elements.length; i += 2)
        {
            assertNull(result.put((K) elements[i], (V) elements[i + 1]));
        }
        return result;
    }

    @Override
    @Test
    public void ReversibleIterable_detectLastIndex()
    {
        // TODO Support detectLastIndex for OrderedMapAdapter.
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).detectLastIndex(each -> true));
    }

    @Override
    @Test
    public void OrderedIterable_forEach_from_to()
    {
        MutableOrderedMap<Integer, Integer> map = this.newWithKeysValues(1, 10, 2, 20, 3, 30);

        MutableList<Integer> forward = Lists.mutable.empty();
        map.forEach(1, 2, forward::add);
        assertIterablesEqual(Lists.immutable.with(20, 30), forward);

        // TODO Support reverse ranges for delegates other than NavigableMap without copying.
        assertThrows(UnsupportedOperationException.class, () -> map.forEach(2, 1, each -> { }));

        // A NavigableMap delegate walks reverse ranges through its descending view
        MutableList<Integer> reverse = Lists.mutable.empty();
        OrderedMapAdapter.adapt(new TreeMap<>(Map.of(1, 10, 2, 20, 3, 30, 4, 40))).forEach(2, 1, reverse::add);
        assertIterablesEqual(Lists.immutable.with(30, 20), reverse);
    }

    @Override
    @Test
    public void OrderedIterable_forEachWithIndex_from_to()
    {
        MutableOrderedMap<Integer, Integer> map = this.newWithKeysValues(1, 10, 2, 20, 3, 30);

        MutableList<Integer> forward = Lists.mutable.empty();
        MutableList<Integer> forwardIndexes = Lists.mutable.empty();
        map.forEachWithIndex(1, 2, (each, index) ->
        {
            forward.add(each);
            forwardIndexes.add(index);
        });
        assertIterablesEqual(Lists.immutable.with(20, 30), forward);
        assertIterablesEqual(Lists.immutable.with(1, 2), forwardIndexes);

        // TODO Support reverse ranges for delegates other than NavigableMap without copying.
        assertThrows(UnsupportedOperationException.class, () -> map.forEachWithIndex(2, 1, (each, index) -> { }));

        // A NavigableMap delegate walks reverse ranges through its descending view
        MutableList<Integer> reverse = Lists.mutable.empty();
        MutableList<Integer> reverseIndexes = Lists.mutable.empty();
        OrderedMapAdapter.adapt(new TreeMap<>(Map.of(1, 10, 2, 20, 3, 30, 4, 40))).forEachWithIndex(2, 1, (each, index) ->
        {
            reverse.add(each);
            reverseIndexes.add(index);
        });
        assertIterablesEqual(Lists.immutable.with(30, 20), reverse);
        assertIterablesEqual(Lists.immutable.with(2, 1), reverseIndexes);
    }

    @Override
    @Test
    public void ReversibleIterable_reverseForEach()
    {
        // TODO Support reverseForEach for delegates other than NavigableMap (depends on reverse ranges in forEach(int, int)).
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).reverseForEach(each -> { }));

        // A NavigableMap delegate walks reverse ranges through its descending view
        MutableList<Integer> values = Lists.mutable.empty();
        OrderedMapAdapter.adapt(new TreeMap<>(Map.of(1, 10, 2, 20, 3, 30))).reverseForEach(values::add);
        assertIterablesEqual(Lists.immutable.with(30, 20, 10), values);
    }

    @Override
    @Test
    public void ReversibleIterable_reverseForEachWithIndex()
    {
        // TODO Support reverseForEachWithIndex for delegates other than NavigableMap (depends on reverse ranges in forEachWithIndex(int, int)).
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).reverseForEachWithIndex((each, index) -> { }));

        // A NavigableMap delegate walks reverse ranges through its descending view
        MutableList<Integer> values = Lists.mutable.empty();
        MutableList<Integer> indexes = Lists.mutable.empty();
        OrderedMapAdapter.adapt(new TreeMap<>(Map.of(1, 10, 2, 20, 3, 30))).reverseForEachWithIndex((each, index) ->
        {
            values.add(each);
            indexes.add(index);
        });
        assertIterablesEqual(Lists.immutable.with(30, 20, 10), values);
        assertIterablesEqual(Lists.immutable.with(2, 1, 0), indexes);
    }
}
