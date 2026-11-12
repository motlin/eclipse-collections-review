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
import org.eclipse.collections.api.block.function.primitive.ObjectIntToObjectFunction;
import org.eclipse.collections.api.block.function.primitive.ShortFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.Predicate2;
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
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.collection.mutable.AbstractUnmodifiableMutableCollection;
import org.eclipse.collections.impl.collection.mutable.UnmodifiableCollectionSerializationProxy;

/**
 * An unmodifiable view of a MutableOrderedSet.
 *
 * @see MutableOrderedSet#asUnmodifiable()
 * @since 14.0
 */
public class UnmodifiableMutableOrderedSet<T>
        extends AbstractUnmodifiableMutableCollection<T>
        implements MutableOrderedSet<T>, Serializable
{
    private static final long serialVersionUID = 1L;

    UnmodifiableMutableOrderedSet(MutableOrderedSet<? extends T> orderedSet)
    {
        super(orderedSet);
    }

    /**
     * This method will take a MutableOrderedSet and wrap it directly in a UnmodifiableMutableOrderedSet. It will
     * take any other non-Eclipse-Collections Set and first adapt it with an OrderedSetAdapter, and then return a
     * UnmodifiableMutableOrderedSet that wraps the adapter.
     */
    public static <E, S extends Set<E>> UnmodifiableMutableOrderedSet<E> of(S set)
    {
        if (set == null)
        {
            throw new IllegalArgumentException("cannot create an UnmodifiableMutableOrderedSet for null");
        }
        return new UnmodifiableMutableOrderedSet<>(OrderedSetAdapter.adapt(set));
    }

    @Override
    public MutableOrderedSet<T> with(T element)
    {
        throw new UnsupportedOperationException("Cannot call with() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableOrderedSet<T> without(T element)
    {
        throw new UnsupportedOperationException("Cannot call without() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableOrderedSet<T> withAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withAll() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableOrderedSet<T> withoutAll(Iterable<? extends T> elements)
    {
        throw new UnsupportedOperationException("Cannot call withoutAll() on " + this.getClass().getSimpleName());
    }

    @Override
    public MutableOrderedSet<T> newEmpty()
    {
        return this.getOrderedSet().newEmpty();
    }

    @Override
    public UnmodifiableMutableOrderedSet<T> clone()
    {
        return this;
    }

    @Override
    public MutableOrderedSet<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    @Override
    public MutableOrderedSet<T> select(Predicate<? super T> predicate)
    {
        return this.getOrderedSet().select(predicate);
    }

    @Override
    public <P> MutableOrderedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getOrderedSet().selectWith(predicate, parameter);
    }

    @Override
    public MutableOrderedSet<T> reject(Predicate<? super T> predicate)
    {
        return this.getOrderedSet().reject(predicate);
    }

    @Override
    public <P> MutableOrderedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getOrderedSet().rejectWith(predicate, parameter);
    }

    @Override
    public PartitionMutableOrderedSet<T> partition(Predicate<? super T> predicate)
    {
        return this.getOrderedSet().partition(predicate);
    }

    @Override
    public <P> PartitionMutableOrderedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.getOrderedSet().partitionWith(predicate, parameter);
    }

    @Override
    public PartitionMutableOrderedSet<T> partitionWhile(Predicate<? super T> predicate)
    {
        return this.getOrderedSet().partitionWhile(predicate);
    }

    @Override
    public <S> MutableOrderedSet<S> selectInstancesOf(Class<S> clazz)
    {
        return this.getOrderedSet().selectInstancesOf(clazz);
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return this.getOrderedSet().collect(function);
    }

    @Override
    public <V> MutableList<V> collectWithIndex(ObjectIntToObjectFunction<? super T, ? extends V> function)
    {
        return this.getOrderedSet().collectWithIndex(function);
    }

    @Override
    public MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.getOrderedSet().collectBoolean(booleanFunction);
    }

    @Override
    public MutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.getOrderedSet().collectByte(byteFunction);
    }

    @Override
    public MutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        return this.getOrderedSet().collectChar(charFunction);
    }

    @Override
    public MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.getOrderedSet().collectDouble(doubleFunction);
    }

    @Override
    public MutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.getOrderedSet().collectFloat(floatFunction);
    }

    @Override
    public MutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        return this.getOrderedSet().collectInt(intFunction);
    }

    @Override
    public MutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        return this.getOrderedSet().collectLong(longFunction);
    }

    @Override
    public MutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.getOrderedSet().collectShort(shortFunction);
    }

    @Override
    public <P, A> MutableList<A> collectWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.getOrderedSet().collectWith(function, parameter);
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.getOrderedSet().collectIf(predicate, function);
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getOrderedSet().flatCollect(function);
    }

    @Override
    public <P, V> MutableList<V> flatCollectWith(Function2<? super T, ? super P, ? extends Iterable<V>> function, P parameter)
    {
        return this.getOrderedSet().flatCollectWith(function, parameter);
    }

    @Override
    public MutableOrderedSet<T> distinct()
    {
        return this.getOrderedSet().distinct();
    }

    @Override
    public MutableOrderedSet<T> takeWhile(Predicate<? super T> predicate)
    {
        return this.getOrderedSet().takeWhile(predicate);
    }

    @Override
    public MutableOrderedSet<T> dropWhile(Predicate<? super T> predicate)
    {
        return this.getOrderedSet().dropWhile(predicate);
    }

    @Override
    public MutableOrderedSet<T> asUnmodifiable()
    {
        return this;
    }

    @Override
    public MutableOrderedSet<T> asSynchronized()
    {
        return SynchronizedMutableOrderedSet.of(this);
    }

    @Override
    public <V> MutableOrderedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.getOrderedSet().groupBy(function);
    }

    @Override
    public <V> MutableOrderedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.getOrderedSet().groupByEach(function);
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.getOrderedSet().zip(that);
    }

    @Override
    public MutableOrderedSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.getOrderedSet().zipWithIndex();
    }

    @Override
    public MutableOrderedSet<T> toReversed()
    {
        return this.getOrderedSet().toReversed();
    }

    @Override
    public MutableOrderedSet<T> take(int count)
    {
        return this.getOrderedSet().take(count);
    }

    @Override
    public MutableOrderedSet<T> drop(int count)
    {
        return this.getOrderedSet().drop(count);
    }

    @Override
    public MutableOrderedSet<T> union(SetIterable<? extends T> set)
    {
        return this.getOrderedSet().union(set);
    }

    @Override
    public MutableOrderedSet<T> intersect(SetIterable<? extends T> set)
    {
        return this.getOrderedSet().intersect(set);
    }

    @Override
    public MutableOrderedSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return this.getOrderedSet().difference(subtrahendSet);
    }

    @Override
    public MutableOrderedSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return this.getOrderedSet().symmetricDifference(setB);
    }

    @Override
    public ImmutableOrderedSet<T> toImmutable()
    {
        return this.getOrderedSet().toImmutable();
    }

    @Override
    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.getOrderedSet().unionInto(set, targetSet);
    }

    @Override
    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.getOrderedSet().intersectInto(set, targetSet);
    }

    @Override
    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return this.getOrderedSet().differenceInto(subtrahendSet, targetSet);
    }

    @Override
    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        return this.getOrderedSet().symmetricDifferenceInto(set, targetSet);
    }

    @Override
    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return this.getOrderedSet().isSubsetOf(candidateSuperset);
    }

    @Override
    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return this.getOrderedSet().isProperSubsetOf(candidateSuperset);
    }

    @Override
    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return this.getOrderedSet().cartesianProduct(set);
    }

    @Override
    public ParallelSetIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        return this.getOrderedSet().asParallel(executorService, batchSize);
    }

    @Override
    public void reverseForEach(Procedure<? super T> procedure)
    {
        this.getOrderedSet().reverseForEach(procedure);
    }

    @Override
    public void reverseForEachWithIndex(ObjectIntProcedure<? super T> procedure)
    {
        this.getOrderedSet().reverseForEachWithIndex(procedure);
    }

    @Override
    public LazyIterable<T> asReversed()
    {
        return this.getOrderedSet().asReversed();
    }

    @Override
    public int detectLastIndex(Predicate<? super T> predicate)
    {
        return this.getOrderedSet().detectLastIndex(predicate);
    }

    @Override
    public int indexOf(Object object)
    {
        return this.getOrderedSet().indexOf(object);
    }

    @Override
    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        return this.getOrderedSet().corresponds(other, predicate);
    }

    @Override
    public void forEach(int startIndex, int endIndex, Procedure<? super T> procedure)
    {
        this.getOrderedSet().forEach(startIndex, endIndex, procedure);
    }

    @Override
    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        this.getOrderedSet().forEachWithIndex(fromIndex, toIndex, objectIntProcedure);
    }

    @Override
    public <V, R extends Collection<V>> R collectWithIndex(ObjectIntToObjectFunction<? super T, ? extends V> function, R target)
    {
        return this.getOrderedSet().collectWithIndex(function, target);
    }

    @Override
    public int detectIndex(Predicate<? super T> predicate)
    {
        return this.getOrderedSet().detectIndex(predicate);
    }

    @Override
    public <S, R extends Collection<Pair<T, S>>> R zip(Iterable<S> that, R target)
    {
        return this.getOrderedSet().zip(that, target);
    }

    @Override
    public <R extends Collection<Pair<T, Integer>>> R zipWithIndex(R target)
    {
        return this.getOrderedSet().zipWithIndex(target);
    }

    protected MutableOrderedSet<T> getOrderedSet()
    {
        return (MutableOrderedSet<T>) this.getMutableCollection();
    }

    protected Object writeReplace()
    {
        return new UnmodifiableCollectionSerializationProxy<>(this.getOrderedSet());
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        return this.getOrderedSet().equals(obj);
    }

    @Override
    public int hashCode()
    {
        return this.getOrderedSet().hashCode();
    }
}
