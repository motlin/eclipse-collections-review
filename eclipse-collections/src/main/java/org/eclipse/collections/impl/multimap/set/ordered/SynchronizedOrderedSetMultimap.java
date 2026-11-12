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

import java.io.Serializable;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.multimap.bag.MutableBagMultimap;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.api.multimap.orderedset.ImmutableOrderedSetMultimap;
import org.eclipse.collections.api.multimap.orderedset.MutableOrderedSetMultimap;
import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.multimap.AbstractSynchronizedMultimap;

/**
 * A synchronized view of a {@link MutableOrderedSetMultimap}.
 *
 * @see MutableOrderedSetMultimap#asSynchronized()
 * @since 14.0
 */
public class SynchronizedOrderedSetMultimap<K, V>
        extends AbstractSynchronizedMultimap<K, V>
        implements MutableOrderedSetMultimap<K, V>, Serializable
{
    private static final long serialVersionUID = 1L;

    public SynchronizedOrderedSetMultimap(MutableOrderedSetMultimap<K, V> multimap)
    {
        super(multimap);
    }

    public SynchronizedOrderedSetMultimap(MutableOrderedSetMultimap<K, V> multimap, Object newLock)
    {
        super(multimap, newLock);
    }

    /**
     * This method will take a Multimap and wrap it directly in a SynchronizedOrderedSetMultimap.
     */
    public static <K, V> SynchronizedOrderedSetMultimap<K, V> of(MutableOrderedSetMultimap<K, V> multimap)
    {
        if (multimap == null)
        {
            throw new IllegalArgumentException("cannot create a SynchronizedOrderedSetMultimap for null");
        }

        return new SynchronizedOrderedSetMultimap<>(multimap);
    }

    /**
     * This method will take a Multimap and wrap it directly in a SynchronizedOrderedSetMultimap.
     * Additionally, a developer specifies which lock to use with the collection.
     */
    public static <K, V> SynchronizedOrderedSetMultimap<K, V> of(MutableOrderedSetMultimap<K, V> multimap, Object lock)
    {
        if (multimap == null)
        {
            throw new IllegalArgumentException("cannot create a SynchronizedOrderedSetMultimap for null");
        }

        return new SynchronizedOrderedSetMultimap<>(multimap, lock);
    }

    @Override
    public MutableOrderedSetMultimap<K, V> withKeyValue(K key, V value)
    {
        synchronized (this.getLock())
        {
            this.getDelegate().withKeyValue(key, value);
            return this;
        }
    }

    @Override
    public MutableOrderedSetMultimap<K, V> withKeyMultiValues(K key, V... values)
    {
        synchronized (this.getLock())
        {
            this.getDelegate().withKeyMultiValues(key, values);
            return this;
        }
    }

    @Override
    public MutableOrderedSet<V> replaceValues(K key, Iterable<? extends V> values)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().replaceValues(key, values);
        }
    }

    @Override
    public MutableOrderedSet<V> removeAll(Object key)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().removeAll(key);
        }
    }

    @Override
    public MutableOrderedSetMultimap<K, V> newEmpty()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().newEmpty().asSynchronized();
        }
    }

    @Override
    public MutableOrderedSet<V> get(K key)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().get(key);
        }
    }

    @Override
    public MutableOrderedSet<V> getIfAbsentPutAll(K key, Iterable<? extends V> values)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().getIfAbsentPutAll(key, values);
        }
    }

    @Override
    public MutableSetMultimap<V, K> flip()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().flip();
        }
    }

    @Override
    public MutableOrderedSetMultimap<K, V> selectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().selectKeysValues(predicate);
        }
    }

    @Override
    public MutableOrderedSetMultimap<K, V> rejectKeysValues(Predicate2<? super K, ? super V> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().rejectKeysValues(predicate);
        }
    }

    @Override
    public MutableOrderedSetMultimap<K, V> selectKeysMultiValues(Predicate2<? super K, ? super RichIterable<V>> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().selectKeysMultiValues(predicate);
        }
    }

    @Override
    public MutableOrderedSetMultimap<K, V> rejectKeysMultiValues(Predicate2<? super K, ? super RichIterable<V>> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().rejectKeysMultiValues(predicate);
        }
    }

    @Override
    public <K2, V2> MutableBagMultimap<K2, V2> collectKeysValues(Function2<? super K, ? super V, Pair<K2, V2>> function)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().collectKeysValues(function);
        }
    }

    @Override
    public <K2, V2> MutableBagMultimap<K2, V2> collectKeyMultiValues(Function<? super K, ? extends K2> keyFunction, Function<? super V, ? extends V2> valueFunction)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().collectKeyMultiValues(keyFunction, valueFunction);
        }
    }

    @Override
    public <V2> MutableListMultimap<K, V2> collectValues(Function<? super V, ? extends V2> function)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().collectValues(function);
        }
    }

    @Override
    public MutableOrderedSetMultimap<K, V> asSynchronized()
    {
        return this;
    }

    @Override
    public MutableOrderedSetMultimap<K, V> toMutable()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().toMutable();
        }
    }

    @Override
    public ImmutableOrderedSetMultimap<K, V> toImmutable()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().toImmutable();
        }
    }

    @Override
    protected MutableOrderedSetMultimap<K, V> getDelegate()
    {
        return (MutableOrderedSetMultimap<K, V>) super.getDelegate();
    }
}
