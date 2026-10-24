/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.set.ordered.mutable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function2;
import org.eclipse.collections.api.block.function.Function3;
import org.eclipse.collections.api.block.function.primitive.BooleanFunction;
import org.eclipse.collections.api.block.function.primitive.ByteFunction;
import org.eclipse.collections.api.block.function.primitive.CharFunction;
import org.eclipse.collections.api.block.function.primitive.DoubleFunction;
import org.eclipse.collections.api.block.function.primitive.FloatFunction;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.api.block.function.primitive.ObjectIntToObjectFunction;
import org.eclipse.collections.api.block.function.primitive.ShortFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
import org.eclipse.collections.api.block.predicate.primitive.ObjectIntPredicate;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.block.procedure.primitive.ObjectIntProcedure;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableBooleanList;
import org.eclipse.collections.api.list.primitive.MutableByteList;
import org.eclipse.collections.api.list.primitive.MutableCharList;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.list.primitive.MutableFloatList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.api.list.primitive.MutableShortList;
import org.eclipse.collections.api.multimap.orderedset.MutableOrderedSetMultimap;
import org.eclipse.collections.api.ordered.OrderedIterable;
import org.eclipse.collections.api.partition.set.ordered.PartitionMutableOrderedSet;
import org.eclipse.collections.api.set.ParallelSetIterable;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.api.set.ordered.ImmutableOrderedSet;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.collection.mutable.AbstractSynchronizedMutableCollection;
import org.eclipse.collections.impl.collection.mutable.SynchronizedCollectionSerializationProxy;

/**
 * A synchronized view of a {@link MutableOrderedSet}. It is imperative that the user manually synchronize on the collection when iterating over it using the
 * standard JDK iterator or JDK 5 for loop, as per {@link Collections#synchronizedCollection(Collection)}.
 *
 * @see MutableOrderedSet#asSynchronized()
 * @since 14.0
 */
public class SynchronizedMutableOrderedSet<T>
        extends AbstractSynchronizedMutableCollection<T>
        implements MutableOrderedSet<T>, Serializable
{
    private static final long serialVersionUID = 1L;

    SynchronizedMutableOrderedSet(MutableOrderedSet<T> set)
    {
        super(set);
    }

    SynchronizedMutableOrderedSet(MutableOrderedSet<T> set, Object newLock)
    {
        super(set, newLock);
    }

    /**
     * This method will take a MutableOrderedSet and wrap it directly in a SynchronizedMutableOrderedSet. It will
     * take any other non-Eclipse-Collections Set and first adapt it with an OrderedSetAdapter, and then return a
     * SynchronizedMutableOrderedSet that wraps the adapter.
     */
    public static <E, S extends Set<E>> SynchronizedMutableOrderedSet<E> of(S set)
    {
        return new SynchronizedMutableOrderedSet<>(OrderedSetAdapter.adapt(set));
    }

    /**
     * This method will take a MutableOrderedSet and wrap it directly in a SynchronizedMutableOrderedSet. It will
     * take any other non-Eclipse-Collections Set and first adapt it with an OrderedSetAdapter, and then return a
     * SynchronizedMutableOrderedSet that wraps the adapter. Additionally, a developer specifies which lock to use
     * with the collection.
     */
    public static <E, S extends Set<E>> SynchronizedMutableOrderedSet<E> of(S set, Object lock)
    {
        return new SynchronizedMutableOrderedSet<>(OrderedSetAdapter.adapt(set), lock);
    }

    @Override
    public MutableOrderedSet<T> with(T element)
    {
        synchronized (this.getLock())
        {
            this.getDelegate().with(element);
            return this;
        }
    }

    @Override
    public MutableOrderedSet<T> without(T element)
    {
        synchronized (this.getLock())
        {
            this.getDelegate().without(element);
            return this;
        }
    }

    @Override
    public MutableOrderedSet<T> withAll(Iterable<? extends T> elements)
    {
        synchronized (this.getLock())
        {
            this.getDelegate().withAll(elements);
            return this;
        }
    }

    @Override
    public MutableOrderedSet<T> withoutAll(Iterable<? extends T> elements)
    {
        synchronized (this.getLock())
        {
            this.getDelegate().withoutAll(elements);
            return this;
        }
    }

    @Override
    public MutableOrderedSet<T> newEmpty()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().newEmpty().asSynchronized();
        }
    }

    @Override
    public MutableOrderedSet<T> clone()
    {
        synchronized (this.getLock())
        {
            return SynchronizedMutableOrderedSet.of(this.getDelegate().clone());
        }
    }

    @Override
    public MutableOrderedSet<T> tap(Procedure<? super T> procedure)
    {
        return (MutableOrderedSet<T>) super.tap(procedure);
    }

    @Override
    public MutableOrderedSet<T> select(Predicate<? super T> predicate)
    {
        return (MutableOrderedSet<T>) super.select(predicate);
    }

    @Override
    public <P> MutableOrderedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return (MutableOrderedSet<T>) super.selectWith(predicate, parameter);
    }

    @Override
    public MutableOrderedSet<T> reject(Predicate<? super T> predicate)
    {
        return (MutableOrderedSet<T>) super.reject(predicate);
    }

    @Override
    public <P> MutableOrderedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return (MutableOrderedSet<T>) super.rejectWith(predicate, parameter);
    }

    @Override
    public PartitionMutableOrderedSet<T> partition(Predicate<? super T> predicate)
    {
        return (PartitionMutableOrderedSet<T>) super.partition(predicate);
    }

    @Override
    public <P> PartitionMutableOrderedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return (PartitionMutableOrderedSet<T>) super.partitionWith(predicate, parameter);
    }

    @Override
    public PartitionMutableOrderedSet<T> partitionWhile(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().partitionWhile(predicate);
        }
    }

    @Override
    public <S> MutableOrderedSet<S> selectInstancesOf(Class<S> clazz)
    {
        return (MutableOrderedSet<S>) super.selectInstancesOf(clazz);
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return (MutableList<V>) super.<V>collect(function);
    }

    @Override
    public <V> MutableList<V> collectWithIndex(ObjectIntToObjectFunction<? super T, ? extends V> function)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().collectWithIndex(function);
        }
    }

    @Override
    public MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return (MutableBooleanList) super.collectBoolean(booleanFunction);
    }

    @Override
    public MutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        return (MutableByteList) super.collectByte(byteFunction);
    }

    @Override
    public MutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        return (MutableCharList) super.collectChar(charFunction);
    }

    @Override
    public MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return (MutableDoubleList) super.collectDouble(doubleFunction);
    }

    @Override
    public MutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        return (MutableFloatList) super.collectFloat(floatFunction);
    }

    @Override
    public MutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        return (MutableIntList) super.collectInt(intFunction);
    }

    @Override
    public MutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        return (MutableLongList) super.collectLong(longFunction);
    }

    @Override
    public MutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        return (MutableShortList) super.collectShort(shortFunction);
    }

    @Override
    public <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return (MutableList<V>) super.<P, V>collectWith(function, parameter);
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return (MutableList<V>) super.<V>collectIf(predicate, function);
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return (MutableList<V>) super.flatCollect(function);
    }

    @Override
    public <P, V> MutableList<V> flatCollectWith(Function2<? super T, ? super P, ? extends Iterable<V>> function, P parameter)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().flatCollectWith(function, parameter);
        }
    }

    @Override
    public MutableOrderedSet<T> distinct()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().distinct();
        }
    }

    @Override
    public MutableOrderedSet<T> takeWhile(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().takeWhile(predicate);
        }
    }

    @Override
    public MutableOrderedSet<T> dropWhile(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().dropWhile(predicate);
        }
    }

    @Override
    public MutableOrderedSet<T> asUnmodifiable()
    {
        synchronized (this.getLock())
        {
            return UnmodifiableMutableOrderedSet.of(this);
        }
    }

    @Override
    public MutableOrderedSet<T> asSynchronized()
    {
        return this;
    }

    @Override
    public <V> MutableOrderedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return (MutableOrderedSetMultimap<V, T>) super.<V>groupBy(function);
    }

    @Override
    public <V> MutableOrderedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return (MutableOrderedSetMultimap<V, T>) super.groupByEach(function);
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return (MutableList<Pair<T, S>>) super.zip(that);
    }

    @Override
    public MutableOrderedSet<Pair<T, Integer>> zipWithIndex()
    {
        return (MutableOrderedSet<Pair<T, Integer>>) super.zipWithIndex();
    }

    @Override
    public MutableOrderedSet<T> toReversed()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().toReversed();
        }
    }

    @Override
    public MutableOrderedSet<T> take(int count)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().take(count);
        }
    }

    @Override
    public MutableOrderedSet<T> drop(int count)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().drop(count);
        }
    }

    @Override
    public MutableOrderedSet<T> union(SetIterable<? extends T> set)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().union(set);
        }
    }

    @Override
    public MutableOrderedSet<T> intersect(SetIterable<? extends T> set)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().intersect(set);
        }
    }

    @Override
    public MutableOrderedSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().difference(subtrahendSet);
        }
    }

    @Override
    public MutableOrderedSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().symmetricDifference(setB);
        }
    }

    @Override
    public ImmutableOrderedSet<T> toImmutable()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().toImmutable();
        }
    }

    @Override
    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().unionInto(set, targetSet);
        }
    }

    @Override
    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().intersectInto(set, targetSet);
        }
    }

    @Override
    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().differenceInto(subtrahendSet, targetSet);
        }
    }

    @Override
    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().symmetricDifferenceInto(set, targetSet);
        }
    }

    @Override
    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().isSubsetOf(candidateSuperset);
        }
    }

    @Override
    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().isProperSubsetOf(candidateSuperset);
        }
    }

    @Override
    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().cartesianProduct(set);
        }
    }

    @Override
    public ParallelSetIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().asParallel(executorService, batchSize);
        }
    }

    @Override
    public void reverseForEach(Procedure<? super T> procedure)
    {
        synchronized (this.getLock())
        {
            this.getDelegate().reverseForEach(procedure);
        }
    }

    @Override
    public void reverseForEachWithIndex(ObjectIntProcedure<? super T> procedure)
    {
        synchronized (this.getLock())
        {
            this.getDelegate().reverseForEachWithIndex(procedure);
        }
    }

    @Override
    public LazyIterable<T> asReversed()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().asReversed();
        }
    }

    @Override
    public int detectLastIndex(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().detectLastIndex(predicate);
        }
    }

    @Override
    public int indexOf(Object object)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().indexOf(object);
        }
    }

    @Override
    public Optional<T> getFirstOptional()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().getFirstOptional();
        }
    }

    @Override
    public Optional<T> getLastOptional()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().getLastOptional();
        }
    }

    @Override
    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().corresponds(other, predicate);
        }
    }

    @Override
    public void forEach(int startIndex, int endIndex, Procedure<? super T> procedure)
    {
        synchronized (this.getLock())
        {
            this.getDelegate().forEach(startIndex, endIndex, procedure);
        }
    }

    @Override
    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        synchronized (this.getLock())
        {
            this.getDelegate().forEachWithIndex(fromIndex, toIndex, objectIntProcedure);
        }
    }

    @Override
    public MutableStack<T> toStack()
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().toStack();
        }
    }

    @Override
    public <V, R extends Collection<V>> R collectWithIndex(ObjectIntToObjectFunction<? super T, ? extends V> function, R target)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().collectWithIndex(function, target);
        }
    }

    @Override
    public <R extends Collection<T>> R selectWithIndex(ObjectIntPredicate<? super T> predicate, R target)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().selectWithIndex(predicate, target);
        }
    }

    @Override
    public <R extends Collection<T>> R rejectWithIndex(ObjectIntPredicate<? super T> predicate, R target)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().rejectWithIndex(predicate, target);
        }
    }

    @Override
    public <IV> IV injectIntoWithIndex(IV initialValue, Function3<? super IV, ? super T, Integer, ? extends IV> function)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().injectIntoWithIndex(initialValue, function);
        }
    }

    @Override
    public int detectIndex(Predicate<? super T> predicate)
    {
        synchronized (this.getLock())
        {
            return this.getDelegate().detectIndex(predicate);
        }
    }

    @Override
    protected MutableOrderedSet<T> getDelegate()
    {
        return (MutableOrderedSet<T>) super.getDelegate();
    }

    protected Object writeReplace()
    {
        return new SynchronizedCollectionSerializationProxy<>(this.getDelegate());
    }
}
