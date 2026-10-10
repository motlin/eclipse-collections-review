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
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import org.eclipse.collections.api.LazyIterable;
import org.eclipse.collections.api.RichIterable;
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
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.OrderedSets;
import org.eclipse.collections.api.factory.primitive.BooleanLists;
import org.eclipse.collections.api.factory.primitive.ByteLists;
import org.eclipse.collections.api.factory.primitive.CharLists;
import org.eclipse.collections.api.factory.primitive.DoubleLists;
import org.eclipse.collections.api.factory.primitive.FloatLists;
import org.eclipse.collections.api.factory.primitive.IntLists;
import org.eclipse.collections.api.factory.primitive.LongLists;
import org.eclipse.collections.api.factory.primitive.ShortLists;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableBooleanList;
import org.eclipse.collections.api.list.primitive.MutableByteList;
import org.eclipse.collections.api.list.primitive.MutableCharList;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.list.primitive.MutableFloatList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.list.primitive.MutableLongList;
import org.eclipse.collections.api.list.primitive.MutableShortList;
import org.eclipse.collections.api.ordered.OrderedIterable;
import org.eclipse.collections.api.partition.set.ordered.PartitionMutableOrderedSet;
import org.eclipse.collections.api.set.ParallelSetIterable;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.api.set.ordered.ImmutableOrderedSet;
import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.block.procedure.PartitionPredicate2Procedure;
import org.eclipse.collections.impl.block.procedure.PartitionProcedure;
import org.eclipse.collections.impl.block.procedure.SelectInstancesOfProcedure;
import org.eclipse.collections.impl.collection.mutable.AbstractCollectionAdapter;
import org.eclipse.collections.impl.multimap.set.ordered.OrderedHashSetMultimap;
import org.eclipse.collections.impl.partition.set.ordered.PartitionOrderedSetAdapter;
import org.eclipse.collections.impl.utility.Iterate;
import org.eclipse.collections.impl.utility.OrderedIterate;
import org.eclipse.collections.impl.utility.internal.IterableIterate;
import org.eclipse.collections.impl.utility.internal.SetIterables;
import org.eclipse.collections.impl.utility.internal.SetIterate;

/**
 * This class provides a MutableOrderedSet wrapper around a JDK Collections Set interface instance whose iteration order
 * is the order of the set, normally a {@link LinkedHashSet}. All the MutableOrderedSet interface methods are supported
 * in addition to the JDK Set interface methods.
 * <p>
 * To create a new wrapper around an existing Set instance, use the {@link #adapt(Set)} factory method.
 *
 * @since 14.0
 */
public final class OrderedSetAdapter<T>
        extends AbstractCollectionAdapter<T>
        implements Serializable, MutableOrderedSet<T>
{
    private static final long serialVersionUID = 1L;
    private final Set<T> delegate;

    OrderedSetAdapter(Set<T> newDelegate)
    {
        if (newDelegate == null)
        {
            throw new NullPointerException("OrderedSetAdapter may not wrap null");
        }
        this.delegate = newDelegate;
    }

    @Override
    protected Set<T> getDelegate()
    {
        return this.delegate;
    }

    @Override
    public MutableOrderedSet<T> asUnmodifiable()
    {
        return UnmodifiableMutableOrderedSet.of(this);
    }

    @Override
    public MutableOrderedSet<T> asSynchronized()
    {
        return SynchronizedMutableOrderedSet.of(this);
    }

    @Override
    public ImmutableOrderedSet<T> toImmutable()
    {
        return OrderedSets.immutable.withAll(this.delegate);
    }

    public static <T> MutableOrderedSet<T> adapt(Set<T> set)
    {
        if (set instanceof MutableOrderedSet<?>)
        {
            return (MutableOrderedSet<T>) set;
        }
        return new OrderedSetAdapter<>(set);
    }

    @Override
    public MutableOrderedSet<T> clone()
    {
        return new OrderedSetAdapter<>(new LinkedHashSet<>(this.delegate));
    }

    @Override
    public boolean contains(Object o)
    {
        return this.delegate.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return this.delegate.containsAll(collection);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        return this.delegate.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return this.delegate.hashCode();
    }

    @Override
    public OrderedSetAdapter<T> with(T element)
    {
        this.add(element);
        return this;
    }

    @Override
    public OrderedSetAdapter<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    @Override
    public OrderedSetAdapter<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    @Override
    public OrderedSetAdapter<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    @Override
    public MutableOrderedSet<T> newEmpty()
    {
        return new OrderedSetAdapter<>(new LinkedHashSet<>());
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        return SetIterate.removeAllIterable(this, iterable);
    }

    @Override
    public MutableOrderedSet<T> tap(Procedure<? super T> procedure)
    {
        Iterate.forEach(this.delegate, procedure);
        return this;
    }

    @Override
    public MutableOrderedSet<T> select(Predicate<? super T> predicate)
    {
        return Iterate.select(this.delegate, predicate, this.newEmpty());
    }

    @Override
    public MutableOrderedSet<T> reject(Predicate<? super T> predicate)
    {
        return Iterate.reject(this.delegate, predicate, this.newEmpty());
    }

    @Override
    public PartitionMutableOrderedSet<T> partition(Predicate<? super T> predicate)
    {
        PartitionMutableOrderedSet<T> partitionMutableOrderedSet = new PartitionOrderedSetAdapter<>();
        this.forEach(new PartitionProcedure<>(predicate, partitionMutableOrderedSet));
        return partitionMutableOrderedSet;
    }

    @Override
    public <P> PartitionMutableOrderedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        PartitionMutableOrderedSet<T> partitionMutableOrderedSet = new PartitionOrderedSetAdapter<>();
        this.forEach(new PartitionPredicate2Procedure<>(predicate, parameter, partitionMutableOrderedSet));
        return partitionMutableOrderedSet;
    }

    @Override
    public PartitionMutableOrderedSet<T> partitionWhile(Predicate<? super T> predicate)
    {
        PartitionOrderedSetAdapter<T> result = new PartitionOrderedSetAdapter<>();
        return IterableIterate.partitionWhile(this, predicate, result);
    }

    @Override
    public MutableOrderedSet<T> takeWhile(Predicate<? super T> predicate)
    {
        MutableOrderedSet<T> result = this.newEmpty();
        return IterableIterate.takeWhile(this, predicate, result);
    }

    @Override
    public MutableOrderedSet<T> dropWhile(Predicate<? super T> predicate)
    {
        MutableOrderedSet<T> result = this.newEmpty();
        return IterableIterate.dropWhile(this, predicate, result);
    }

    @Override
    public <S> MutableOrderedSet<S> selectInstancesOf(Class<S> clazz)
    {
        MutableOrderedSet<S> result = new OrderedSetAdapter<>(new LinkedHashSet<>());
        this.forEach(new SelectInstancesOfProcedure<>(clazz, result));
        return result;
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return Iterate.collect(this.delegate, function, Lists.mutable.withInitialCapacity(this.size()));
    }

    @Override
    public MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.collectBoolean(booleanFunction, BooleanLists.mutable.withInitialCapacity(this.size()));
    }

    @Override
    public MutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.collectByte(byteFunction, ByteLists.mutable.withInitialCapacity(this.size()));
    }

    @Override
    public MutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        return this.collectChar(charFunction, CharLists.mutable.withInitialCapacity(this.size()));
    }

    @Override
    public MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.collectDouble(doubleFunction, DoubleLists.mutable.withInitialCapacity(this.size()));
    }

    @Override
    public MutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.collectFloat(floatFunction, FloatLists.mutable.withInitialCapacity(this.size()));
    }

    @Override
    public MutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        return this.collectInt(intFunction, IntLists.mutable.withInitialCapacity(this.size()));
    }

    @Override
    public MutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        return this.collectLong(longFunction, LongLists.mutable.withInitialCapacity(this.size()));
    }

    @Override
    public MutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.collectShort(shortFunction, ShortLists.mutable.withInitialCapacity(this.size()));
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return Iterate.collectIf(this.delegate, predicate, function, Lists.mutable.empty());
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return Iterate.flatCollect(this.delegate, function, Lists.mutable.empty());
    }

    @Override
    public int detectIndex(Predicate<? super T> predicate)
    {
        return Iterate.detectIndex(this.delegate, predicate);
    }

    @Override
    public int detectLastIndex(Predicate<? super T> predicate)
    {
        int result = -1;
        int index = 0;
        for (T each : this.delegate)
        {
            if (predicate.accept(each))
            {
                result = index;
            }
            index++;
        }
        return result;
    }

    @Override
    public <V> OrderedHashSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return Iterate.groupBy(this.delegate, function, OrderedHashSetMultimap.newMultimap());
    }

    @Override
    public <V> OrderedHashSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return Iterate.groupByEach(this.delegate, function, OrderedHashSetMultimap.newMultimap());
    }

    @Override
    public <P> MutableOrderedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.selectWith(this.delegate, predicate, parameter, this.newEmpty());
    }

    @Override
    public <P> MutableOrderedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return Iterate.rejectWith(this.delegate, predicate, parameter, this.newEmpty());
    }

    @Override
    public <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return Iterate.collectWith(this.delegate, function, parameter, Lists.mutable.empty());
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        if (that instanceof Collection || that instanceof RichIterable)
        {
            int thatSize = Iterate.sizeOf(that);
            MutableList<Pair<T, S>> target = Lists.mutable.withInitialCapacity(Math.min(this.size(), thatSize));
            return Iterate.zip(this.delegate, that, target);
        }
        return Iterate.zip(this.delegate, that, Lists.mutable.empty());
    }

    @Override
    public MutableOrderedSet<Pair<T, Integer>> zipWithIndex()
    {
        return Iterate.zipWithIndex(this.delegate, new OrderedSetAdapter<>(new LinkedHashSet<>()));
    }

    @Override
    public MutableOrderedSet<T> distinct()
    {
        return this.clone();
    }

    @Override
    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        return OrderedIterate.corresponds(this, other, predicate);
    }

    @Override
    public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
    {
        this.forEachWithIndex(fromIndex, toIndex, (each, index) -> procedure.value(each));
    }

    @Override
    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        int size = this.size();
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= size || toIndex >= size)
        {
            throw new IndexOutOfBoundsException("Index out of range: fromIndex=" + fromIndex + ", toIndex=" + toIndex + ", size=" + size);
        }
        if (fromIndex <= toIndex)
        {
            Iterator<T> iterator = this.iterator();
            int i = 0;
            while (iterator.hasNext() && i <= toIndex)
            {
                T each = iterator.next();
                if (i >= fromIndex)
                {
                    objectIntProcedure.value(each, i);
                }
                i++;
            }
        }
        else
        {
            T[] array = (T[]) this.delegate.toArray();
            for (int i = fromIndex; i >= toIndex; i--)
            {
                objectIntProcedure.value(array[i], i);
            }
        }
    }

    @Override
    public MutableOrderedSet<T> toReversed()
    {
        T[] array = (T[]) this.delegate.toArray();
        MutableOrderedSet<T> result = new OrderedSetAdapter<>(new LinkedHashSet<>(array.length));
        for (int i = array.length - 1; i >= 0; i--)
        {
            result.add(array[i]);
        }
        return result;
    }

    @Override
    public MutableOrderedSet<T> union(SetIterable<? extends T> set)
    {
        return this.unionInto(set, this.newEmpty());
    }

    @Override
    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    @Override
    public MutableOrderedSet<T> intersect(SetIterable<? extends T> set)
    {
        return this.intersectInto(set, this.newEmpty());
    }

    @Override
    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    @Override
    public MutableOrderedSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return this.differenceInto(subtrahendSet, this.newEmpty());
    }

    @Override
    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    @Override
    public MutableOrderedSet<T> symmetricDifference(SetIterable<? extends T> setB)
    {
        return this.symmetricDifferenceInto(setB, this.newEmpty());
    }

    @Override
    public <R extends Set<T>> R symmetricDifferenceInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.symmetricDifferenceInto(this, set, targetSet);
    }

    @Override
    public boolean isSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return SetIterables.isSubsetOf(this, candidateSuperset);
    }

    @Override
    public boolean isProperSubsetOf(SetIterable<? extends T> candidateSuperset)
    {
        return SetIterables.isProperSubsetOf(this, candidateSuperset);
    }

    @Override
    public <B> LazyIterable<Pair<T, B>> cartesianProduct(SetIterable<B> set)
    {
        return SetIterables.cartesianProduct(this, set);
    }

    @Override
    public ParallelSetIterable<T> asParallel(ExecutorService executorService, int batchSize)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".asParallel() not implemented yet");
    }

    @Override
    public MutableOrderedSet<T> take(int count)
    {
        return IterableIterate.take(this.delegate, Math.min(this.size(), count), this.newEmpty());
    }

    @Override
    public MutableOrderedSet<T> drop(int count)
    {
        return IterableIterate.drop(this.delegate, count, this.newEmpty());
    }
}
