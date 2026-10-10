/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.api.set.ordered;

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
import org.eclipse.collections.api.partition.set.ordered.PartitionMutableOrderedSet;
import org.eclipse.collections.api.set.MutableSetIterable;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.api.tuple.Pair;

/**
 * A MutableOrderedSet is a modifiable set whose elements keep the order in which they were first added, like
 * {@link java.util.LinkedHashSet}.
 *
 * @since 14.0
 */
public interface MutableOrderedSet<T>
        extends MutableSetIterable<T>, OrderedSet<T>, Cloneable
{
    @Override
    default MutableOrderedSet<T> with(T element)
    {
        this.add(element);
        return this;
    }

    @Override
    default MutableOrderedSet<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    @Override
    default MutableOrderedSet<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    @Override
    default MutableOrderedSet<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    @Override
    MutableOrderedSet<T> newEmpty();

    MutableOrderedSet<T> clone();

    @Override
    MutableOrderedSet<T> tap(Procedure<? super T> procedure);

    @Override
    MutableOrderedSet<T> select(Predicate<? super T> predicate);

    @Override
    <P> MutableOrderedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    MutableOrderedSet<T> reject(Predicate<? super T> predicate);

    @Override
    <P> MutableOrderedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    PartitionMutableOrderedSet<T> partition(Predicate<? super T> predicate);

    @Override
    <P> PartitionMutableOrderedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    PartitionMutableOrderedSet<T> partitionWhile(Predicate<? super T> predicate);

    @Override
    <S> MutableOrderedSet<S> selectInstancesOf(Class<S> clazz);

    @Override
    <V> MutableList<V> collect(Function<? super T, ? extends V> function);

    @Override
    default <V> MutableList<V> collectWithIndex(ObjectIntToObjectFunction<? super T, ? extends V> function)
    {
        int[] index = {0};
        return this.collect(each -> function.valueOf(each, index[0]++));
    }

    @Override
    MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction);

    @Override
    MutableByteList collectByte(ByteFunction<? super T> byteFunction);

    @Override
    MutableCharList collectChar(CharFunction<? super T> charFunction);

    @Override
    MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction);

    @Override
    MutableFloatList collectFloat(FloatFunction<? super T> floatFunction);

    @Override
    MutableIntList collectInt(IntFunction<? super T> intFunction);

    @Override
    MutableLongList collectLong(LongFunction<? super T> longFunction);

    @Override
    MutableShortList collectShort(ShortFunction<? super T> shortFunction);

    @Override
    <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    @Override
    <V> MutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    @Override
    <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    @Override
    default <P, V> MutableList<V> flatCollectWith(Function2<? super T, ? super P, ? extends Iterable<V>> function, P parameter)
    {
        return this.flatCollect(each -> function.apply(each, parameter));
    }

    @Override
    MutableOrderedSet<T> distinct();

    @Override
    MutableOrderedSet<T> takeWhile(Predicate<? super T> predicate);

    @Override
    MutableOrderedSet<T> dropWhile(Predicate<? super T> predicate);

    @Override
    MutableOrderedSet<T> asUnmodifiable();

    @Override
    MutableOrderedSet<T> asSynchronized();

    @Override
    <V> MutableOrderedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    @Override
    <V> MutableOrderedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    @Override
    <S> MutableList<Pair<T, S>> zip(Iterable<S> that);

    @Override
    MutableOrderedSet<Pair<T, Integer>> zipWithIndex();

    @Override
    MutableOrderedSet<T> toReversed();

    @Override
    MutableOrderedSet<T> take(int count);

    @Override
    MutableOrderedSet<T> drop(int count);

    @Override
    MutableOrderedSet<T> union(SetIterable<? extends T> set);

    @Override
    MutableOrderedSet<T> intersect(SetIterable<? extends T> set);

    @Override
    MutableOrderedSet<T> difference(SetIterable<? extends T> subtrahendSet);

    @Override
    MutableOrderedSet<T> symmetricDifference(SetIterable<? extends T> setB);
}
