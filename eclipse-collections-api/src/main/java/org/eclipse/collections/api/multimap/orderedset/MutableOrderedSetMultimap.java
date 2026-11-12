/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.multimap.orderedset;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.multimap.bag.MutableBagMultimap;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.api.multimap.set.MutableSetIterableMultimap;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.api.tuple.Pair;

/**
 * A modifiable multimap whose values for each key are a {@link MutableOrderedSet}.
 *
 * @since 14.0
 */
public interface MutableOrderedSetMultimap<K, V>
        extends MutableSetIterableMultimap<K, V>, OrderedSetMultimap<K, V>
{
    @Override
    default MutableOrderedSetMultimap<K, V> withKeyValue(K key, V value)
    {
        this.put(key, value);
        return this;
    }

    @Override
    default MutableOrderedSetMultimap<K, V> withKeyMultiValues(K key, V... values)
    {
        return (MutableOrderedSetMultimap<K, V>) MutableSetIterableMultimap.super.withKeyMultiValues(key, values);
    }

    @Override
    MutableOrderedSet<V> replaceValues(K key, Iterable<? extends V> values);

    @Override
    MutableOrderedSet<V> removeAll(Object key);

    @Override
    MutableOrderedSetMultimap<K, V> newEmpty();

    @Override
    MutableOrderedSet<V> get(K key);

    @Override
    MutableOrderedSet<V> getIfAbsentPutAll(K key, Iterable<? extends V> values);

    @Override
    MutableSetMultimap<V, K> flip();

    @Override
    MutableOrderedSetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate);

    @Override
    MutableOrderedSetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate);

    @Override
    MutableOrderedSetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super RichIterable<V>> predicate);

    @Override
    MutableOrderedSetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super RichIterable<V>> predicate);

    @Override
    <K2, V2> MutableBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function);

    @Override
    <K2, V2> MutableBagMultimap<K2, V2> collectKeyMultiValues(Function<? super K, ? extends K2> keyFunction, Function<? super V, ? extends V2> valueFunction);

    @Override
    <V2> MutableListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function);

    @Override
    MutableOrderedSetMultimap<K, V> asSynchronized();
}
