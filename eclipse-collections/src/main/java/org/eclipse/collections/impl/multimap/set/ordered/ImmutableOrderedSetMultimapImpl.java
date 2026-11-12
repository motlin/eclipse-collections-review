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
import java.io.Serializable;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.factory.Maps;
import org.eclipse.collections.api.factory.OrderedSets;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.bag.ImmutableBagMultimap;
import org.eclipse.collections.api.multimap.bag.MutableBagMultimap;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.multimap.orderedset.ImmutableOrderedSetMultimap;
import org.eclipse.collections.api.multimap.orderedset.MutableOrderedSetMultimap;
import org.eclipse.collections.api.multimap.set.ImmutableSetMultimap;
import org.eclipse.collections.api.set.ordered.ImmutableOrderedSet;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.multimap.AbstractImmutableMultimap;
import org.eclipse.collections.impl.multimap.AbstractMutableMultimap;
import org.eclipse.collections.impl.multimap.ImmutableMultimapSerializationProxy;
import org.eclipse.collections.impl.multimap.bag.HashBagMultimap;
import org.eclipse.collections.impl.multimap.list.FastListMultimap;
import org.eclipse.collections.impl.utility.Iterate;

/**
 * The default ImmutableOrderedSetMultimap implementation.
 *
 * @since 14.0
 */
public final class ImmutableOrderedSetMultimapImpl<K, V>
        extends AbstractImmutableMultimap<K, V, ImmutableOrderedSet<V>>
        implements ImmutableOrderedSetMultimap<K, V>, Serializable
{
    private static final long serialVersionUID = 1L;

    public ImmutableOrderedSetMultimapImpl(MutableMap<K, ImmutableOrderedSet<V>> map)
    {
        super(map);
    }

    public ImmutableOrderedSetMultimapImpl(ImmutableMap<K, ImmutableOrderedSet<V>> map)
    {
        super(map);
    }

    @Override
    protected ImmutableOrderedSet<V> createCollection()
    {
        return OrderedSets.immutable.empty();
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> newEmpty()
    {
        return new ImmutableOrderedSetMultimapImpl<>(Maps.immutable.with());
    }

    @Override
    public MutableOrderedSetMultimap<K, V> toMutable()
    {
        return new OrderedHashSetMultimap<>(this);
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> toImmutable()
    {
        return this;
    }

    private Object writeReplace()
    {
        return new ImmutableOrderedSetMultimapSerializationProxy<>(this.map);
    }

    private static final class ImmutableOrderedSetMultimapSerializationProxy<K, V>
            extends ImmutableMultimapSerializationProxy<K, V, ImmutableOrderedSet<V>> implements Externalizable
    {
        private static final long serialVersionUID = 1L;

        public ImmutableOrderedSetMultimapSerializationProxy()
        {
            // For Externalizable use only
        }

        private ImmutableOrderedSetMultimapSerializationProxy(ImmutableMap<K, ImmutableOrderedSet<V>> map)
        {
            super(map);
        }

        @Override
        protected AbstractMutableMultimap<K, V, MutableOrderedSet<V>> createEmptyMutableMultimap()
        {
            return new OrderedHashSetMultimap<>();
        }
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> newWith(K key, V value)
    {
        return (ImmutableOrderedSetMultimap<K, V>) super.newWith(key, value);
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> newWithout(Object key, Object value)
    {
        return (ImmutableOrderedSetMultimap<K, V>) super.newWithout(key, value);
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> newWithAll(K key, Iterable<? extends V> values)
    {
        return (ImmutableOrderedSetMultimap<K, V>) super.newWithAll(key, values);
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> newWithoutAll(Object key)
    {
        return (ImmutableOrderedSetMultimap<K, V>) super.newWithoutAll(key);
    }

    @Override
    public ImmutableSetMultimap<V, K> flip()
    {
        return Iterate.flip(this).toImmutable();
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.selectKeysValues(predicate, OrderedHashSetMultimap.<K, V>newMultimap()).toImmutable();
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        return this.rejectKeysValues(predicate, OrderedHashSetMultimap.<K, V>newMultimap()).toImmutable();
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super RichIterable<V>> predicate)
    {
        return this.selectKeysMultiValues(predicate, OrderedHashSetMultimap.<K, V>newMultimap()).toImmutable();
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super RichIterable<V>> predicate)
    {
        return this.rejectKeysMultiValues(predicate, OrderedHashSetMultimap.<K, V>newMultimap()).toImmutable();
    }

    @Override
    public <K2, V2> ImmutableBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        return this.collectKeysValues(function, HashBagMultimap.newMultimap()).toImmutable();
    }

    @Override
    public <K2, V2> ImmutableBagMultimap<K2, V2> collectKeyMultiValues(Function<? super K, ? extends K2> keyFunction, Function<? super V, ? extends V2> valueFunction)
    {
        MutableBagMultimap<K2, V2> mutableMultimap = this.collectKeyMultiValues(keyFunction, valueFunction, HashBagMultimap.newMultimap());
        return mutableMultimap.toImmutable();
    }

    @Override
    public <V2> ImmutableListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function)
    {
        return this.collectValues(function, FastListMultimap.<K, V2>newMultimap()).toImmutable();
    }
}
