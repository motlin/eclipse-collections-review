/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.multimap.set.ordered;

import java.io.Externalizable;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.Multimap;
import org.eclipse.collections.api.multimap.orderedset.ImmutableOrderedSetMultimap;
import org.eclipse.collections.api.multimap.orderedset.MutableOrderedSetMultimap;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.set.ordered.ImmutableOrderedSet;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.multimap.AbstractMutableMultimap;
import org.eclipse.collections.impl.multimap.bag.HashBagMultimap;
import org.eclipse.collections.impl.multimap.list.FastListMultimap;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedHashSet;
import org.eclipse.collections.impl.utility.Iterate;

/**
 * A {@link MutableOrderedSetMultimap} which uses an {@link OrderedHashSet} as its underlying store for the multiple values of a given key.
 *
 * @since 14.0
 */
public final class OrderedHashSetMultimap<K, V>
        extends AbstractMutableMultimap<K, V, MutableOrderedSet<V>>
        implements MutableOrderedSetMultimap<K, V>, Externalizable
{
    private static final long serialVersionUID = 1L;

    public OrderedHashSetMultimap()
    {
    }

    public OrderedHashSetMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        super(Math.max(multimap.sizeDistinct() * 2, 16));
        this.putAll(multimap);
    }

    public OrderedHashSetMultimap(Pair<K, V>... pairs)
    {
        super(pairs);
    }

    public OrderedHashSetMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        super(inputIterable);
    }

    public static <K, V> OrderedHashSetMultimap<K, V> newMultimap()
    {
        return new OrderedHashSetMultimap<>();
    }

    public static <K, V> OrderedHashSetMultimap<K, V> newMultimap(Multimap<? extends K, ? extends V> multimap)
    {
        return new OrderedHashSetMultimap<>(multimap);
    }

    public static <K, V> OrderedHashSetMultimap<K, V> newMultimap(Pair<K, V>... pairs)
    {
        return new OrderedHashSetMultimap<>(pairs);
    }

    public static <K, V> OrderedHashSetMultimap<K, V> newMultimap(Iterable<Pair<K, V>> inputIterable)
    {
        return new OrderedHashSetMultimap<>(inputIterable);
    }

    @Override
    protected MutableMap<K, MutableOrderedSet<V>> createMap()
    {
        return Maps.mutable.empty();
    }

    @Override
    protected MutableMap<K, MutableOrderedSet<V>> createMapWithKeyCount(int keyCount)
    {
        return UnifiedMap.newMap(keyCount);
    }

    @Override
    protected MutableOrderedSet<V> createCollection()
    {
        return new OrderedHashSet<>();
    }

    @Override
    public OrderedHashSetMultimap<K, V> newEmpty()
    {
        return new OrderedHashSetMultimap<>();
    }

    @Override
    public MutableOrderedSetMultimap<K, V> toMutable()
    {
        return new OrderedHashSetMultimap<>(this);
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> toImmutable()
    {
        MutableMap<K, ImmutableOrderedSet<V>> map = Maps.mutable.empty();

        this.map.forEachKeyValue((key, set) -> map.put(key, set.toImmutable()));
        return new ImmutableOrderedSetMultimapImpl<>(map);
    }

    @Override
    public MutableSetMultimap<V, K> flip()
    {
        return Iterate.flip(this);
    }

    @Override
    public OrderedHashSetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, this.newEmpty());
    }

    @Override
    public OrderedHashSetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, this.newEmpty());
    }

    @Override
    public OrderedHashSetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super RichIterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, this.newEmpty());
    }

    @Override
    public OrderedHashSetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super RichIterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, this.newEmpty());
    }

    @Override
    public <K2, V2> HashBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.collectKeysValues(function, HashBagMultimap.newMultimap());
    }

    @Override
    public <K2, V2> HashBagMultimap<K2, V2> collectKeyMultiValues(Function<? super K, ? extends K2> keyFunction, Function<? super V, ? extends V2> valueFunction)
    {
        return this.collectKeyMultiValues(keyFunction, valueFunction, HashBagMultimap.newMultimap());
    }

    @Override
    public <V2> FastListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function)
    {
        return this.collectValues(function, FastListMultimap.newMultimap());
    }

    @Override
    public MutableOrderedSetMultimap<K, V> asSynchronized()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".asSynchronized() not implemented yet");
    }
}
