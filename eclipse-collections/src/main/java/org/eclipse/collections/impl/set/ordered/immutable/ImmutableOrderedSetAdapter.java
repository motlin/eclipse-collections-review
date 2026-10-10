/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.set.ordered.immutable;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.api.block.function.primitive.ByteFunction;
import org.eclipse.collections.api.block.function.primitive.CharFunction;
import org.eclipse.collections.api.block.function.primitive.DoubleFunction;
import org.eclipse.collections.api.block.function.primitive.FloatFunction;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.api.block.function.primitive.ShortFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.block.procedure.primitive.ObjectIntProcedure;
import org.eclipse.collections.api.collection.MutableCollection;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.primitive.ImmutableBooleanList;
import org.eclipse.collections.api.list.primitive.ImmutableByteList;
import org.eclipse.collections.api.list.primitive.ImmutableCharList;
import org.eclipse.collections.api.list.primitive.ImmutableDoubleList;
import org.eclipse.collections.api.list.primitive.ImmutableFloatList;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.api.list.primitive.ImmutableLongList;
import org.eclipse.collections.api.list.primitive.ImmutableShortList;
import org.eclipse.collections.api.multimap.orderedset.ImmutableOrderedSetMultimap;
import org.eclipse.collections.api.ordered.OrderedIterable;
import org.eclipse.collections.api.partition.set.ordered.PartitionImmutableOrderedSet;
import org.eclipse.collections.api.set.ParallelSetIterable;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.api.set.ordered.ImmutableOrderedSet;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.collection.immutable.AbstractImmutableCollection;
import org.eclipse.collections.impl.multimap.set.ordered.OrderedHashSetMultimap;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedSetAdapter;

/**
 * An {@link ImmutableOrderedSet} backed by an unmodifiable copy of a {@link LinkedHashSet}.
 *
 * @since 14.0
 */
public class ImmutableOrderedSetAdapter<T>
        extends AbstractImmutableCollection<T>
        implements ImmutableOrderedSet<T>, Set<T>, Serializable
{
    private static final long serialVersionUID = 1L;

    private final MutableOrderedSet<T> delegate;

    public ImmutableOrderedSetAdapter(Set<T> delegate)
    {
        this.delegate = OrderedSetAdapter.adapt(Collections.unmodifiableSet(new LinkedHashSet<>(delegate)));
    }

    @Override
    public int hashCode()
    {
        return this.delegate.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.delegate.equals(obj);
    }

    @Override
    public String toString()
    {
        return this.delegate.toString();
    }

    @Override
    protected MutableCollection<T> newMutable(int size)
    {
        return OrderedSetAdapter.adapt(new LinkedHashSet<>(size));
    }

    @Override
    public int size()
    {
        return this.delegate.size();
    }

    @Override
    public boolean contains(Object object)
    {
        return this.delegate.contains(object);
    }

    @Override
    public Iterator<T> iterator()
    {
        return this.delegate.iterator();
    }

    @Override
    public void each(Procedure<? super T> procedure)
    {
        this.delegate.each(procedure);
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.delegate.forEachWithIndex(objectIntProcedure);
    }

    @Override
    public T getFirst()
    {
        return this.delegate.getFirst();
    }

    @Override
    public T getLast()
    {
        return this.delegate.getLast();
    }

    @Override
    public ImmutableOrderedSet<T> newWith(T element)
    {
        if (this.contains(element))
        {
            return this;
        }
        MutableOrderedSet<T> result = this.delegate.clone();
        result.add(element);
        return result.toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> newWithout(T element)
    {
        if (!this.contains(element))
        {
            return this;
        }
        MutableOrderedSet<T> result = this.delegate.clone();
        result.remove(element);
        return result.toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> newWithAll(Iterable<? extends T> elements)
    {
        MutableOrderedSet<T> result = this.delegate.clone();
        result.addAllIterable(elements);
        return result.toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> newWithoutAll(Iterable<? extends T> elements)
    {
        MutableOrderedSet<T> result = this.delegate.clone();
        this.removeAllFrom(elements, result);
        return result.toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    @Override
    public ImmutableOrderedSet<T> select(Predicate<? super T> predicate)
    {
        return this.delegate.select(predicate).toImmutable();
    }

    @Override
    public <P> ImmutableOrderedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.delegate.selectWith(predicate, parameter).toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> reject(Predicate<? super T> predicate)
    {
        return this.delegate.reject(predicate).toImmutable();
    }

    @Override
    public <P> ImmutableOrderedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.delegate.rejectWith(predicate, parameter).toImmutable();
    }

    @Override
    public PartitionImmutableOrderedSet<T> partition(Predicate<? super T> predicate)
    {
        return this.delegate.partition(predicate).toImmutable();
    }

    @Override
    public <P> PartitionImmutableOrderedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.delegate.partitionWith(predicate, parameter).toImmutable();
    }

    @Override
    public PartitionImmutableOrderedSet<T> partitionWhile(Predicate<? super T> predicate)
    {
        return this.delegate.partitionWhile(predicate).toImmutable();
    }

    @Override
    public <S> ImmutableOrderedSet<S> selectInstancesOf(Class<S> clazz)
    {
        return this.delegate.selectInstancesOf(clazz).toImmutable();
    }

    @Override
    public <V> ImmutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return this.delegate.<V>collect(function).toImmutable();
    }

    @Override
    public ImmutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.delegate.collectBoolean(booleanFunction).toImmutable();
    }

    @Override
    public ImmutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.delegate.collectByte(byteFunction).toImmutable();
    }

    @Override
    public ImmutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        return this.delegate.collectChar(charFunction).toImmutable();
    }

    @Override
    public ImmutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.delegate.collectDouble(doubleFunction).toImmutable();
    }

    @Override
    public ImmutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.delegate.collectFloat(floatFunction).toImmutable();
    }

    @Override
    public ImmutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        return this.delegate.collectInt(intFunction).toImmutable();
    }

    @Override
    public ImmutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        return this.delegate.collectLong(longFunction).toImmutable();
    }

    @Override
    public ImmutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.delegate.collectShort(shortFunction).toImmutable();
    }

    @Override
    public <P, V> ImmutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.delegate.<P, V>collectWith(function, parameter).toImmutable();
    }

    @Override
    public <V> ImmutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.delegate.<V>collectIf(predicate, function).toImmutable();
    }

    @Override
    public <V> ImmutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.delegate.flatCollect(function).toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> distinct()
    {
        return this;
    }

    @Override
    public ImmutableOrderedSet<T> takeWhile(Predicate<? super T> predicate)
    {
        return this.delegate.takeWhile(predicate).toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> dropWhile(Predicate<? super T> predicate)
    {
        return this.delegate.dropWhile(predicate).toImmutable();
    }

    @Override
    public <V> ImmutableOrderedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, OrderedHashSetMultimap.<V, T>newMultimap()).toImmutable();
    }

    @Override
    public <V> ImmutableOrderedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, OrderedHashSetMultimap.<V, T>newMultimap()).toImmutable();
    }

    @Override
    public <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.delegate.zip(that).toImmutable();
    }

    @Override
    public ImmutableOrderedSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.delegate.zipWithIndex().toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> toReversed()
    {
        return this.delegate.toReversed().toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> take(int count)
    {
        return this.delegate.take(count).toImmutable();
    }

    @Override
    public ImmutableOrderedSet<T> drop(int count)
    {
        return this.delegate.drop(count).toImmutable();
    }

    @Override
    public int detectIndex(Predicate<? super T> predicate)
    {
        return this.delegate.detectIndex(predicate);
    }

    @Override
    public int detectLastIndex(Predicate<? super T> predicate)
    {
        return this.delegate.detectLastIndex(predicate);
    }

    @Override
    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        return this.delegate.corresponds(other, predicate);
    }

    @Override
    public void forEach(int startIndex, int endIndex, Procedure<? super T> procedure)
    {
        this.delegate.forEach(startIndex, endIndex, procedure);
    }

    @Override
    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.delegate.forEachWithIndex(fromIndex, toIndex, objectIntProcedure);
    }

    @Override
    public ImmutableOrderedSet<T> union(SetIterable<? extends T> set)
    {
        return this.delegate.union(set).toImmutable();
    }

    @Override
    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.delegate.unionInto(set, targetSet);
    }

    @Override
    public ImmutableOrderedSet<T> intersect(SetIterable<? extends T> set)
    {
        return this.delegate.intersect(set).toImmutable();
    }

    @Override
    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.delegate.intersectInto(set, targetSet);
    }

    @Override
    public ImmutableOrderedSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return this.delegate.difference(subtrahendSet).toImmutable();
    }

    @Override
    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return this.delegate.differenceInto(subtrahendSet, targetSet);
    }

    @Override
    public ImmutableOrderedSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return this.delegate.symmetricDifference(setB).toImmutable();
    }

    @Override
    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.delegate.symmetricDifferenceInto(set, targetSet);
    }

    @Override
    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return this.delegate.isSubsetOf(candidateSuperset);
    }

    @Override
    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return this.delegate.isProperSubsetOf(candidateSuperset);
    }

    @Override
    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return this.delegate.cartesianProduct(set);
    }

    @Override
    public ParallelSetIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        return this.delegate.asParallel(executorService, batchSize);
    }

    @Override
    public ImmutableOrderedSet<T> toImmutable()
    {
        return this;
    }
}
