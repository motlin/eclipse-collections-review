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
import org.eclipse.collections.api.partition.set.ordered.PartitionImmutableOrderedSet;
import org.eclipse.collections.api.set.ImmutableSetIterable;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.api.tuple.Pair;

/**
 * An ImmutableOrderedSet is a non-modifiable set whose elements keep the order in which they were first added.
 *
 * @since 14.0
 */
public interface ImmutableOrderedSet<T>
        extends OrderedSet<T>, ImmutableSetIterable<T>
{
    @Override
    ImmutableOrderedSet<T> newWith(T element);

    @Override
    ImmutableOrderedSet<T> newWithout(T element);

    @Override
    ImmutableOrderedSet<T> newWithAll(Iterable<? extends T> elements);

    @Override
    ImmutableOrderedSet<T> newWithoutAll(Iterable<? extends T> elements);

    @Override
    ImmutableOrderedSet<T> tap(Procedure<? super T> procedure);

    @Override
    ImmutableOrderedSet<T> select(Predicate<? super T> predicate);

    @Override
    <P> ImmutableOrderedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    ImmutableOrderedSet<T> reject(Predicate<? super T> predicate);

    @Override
    <P> ImmutableOrderedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    PartitionImmutableOrderedSet<T> partition(Predicate<? super T> predicate);

    @Override
    <P> PartitionImmutableOrderedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    PartitionImmutableOrderedSet<T> partitionWhile(Predicate<? super T> predicate);

    @Override
    <S> ImmutableOrderedSet<S> selectInstancesOf(Class<S> clazz);

    @Override
    <V> ImmutableList<V> collect(Function<? super T, ? extends V> function);

    @Override
    default <V> ImmutableList<V> collectWithIndex(ObjectIntToObjectFunction<? super T, ? extends V> function)
    {
        int[] index = {0};
        return this.collect(each -> function.valueOf(each, index[0]++));
    }

    @Override
    ImmutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction);

    @Override
    ImmutableByteList collectByte(ByteFunction<? super T> byteFunction);

    @Override
    ImmutableCharList collectChar(CharFunction<? super T> charFunction);

    @Override
    ImmutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction);

    @Override
    ImmutableFloatList collectFloat(FloatFunction<? super T> floatFunction);

    @Override
    ImmutableIntList collectInt(IntFunction<? super T> intFunction);

    @Override
    ImmutableLongList collectLong(LongFunction<? super T> longFunction);

    @Override
    ImmutableShortList collectShort(ShortFunction<? super T> shortFunction);

    @Override
    <P, V> ImmutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    @Override
    <V> ImmutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    @Override
    <V> ImmutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    @Override
    default <P, V> ImmutableList<V> flatCollectWith(Function2<? super T, ? super P, ? extends Iterable<V>> function, P parameter)
    {
        return this.flatCollect(each -> function.apply(each, parameter));
    }

    @Override
    ImmutableOrderedSet<T> distinct();

    @Override
    ImmutableOrderedSet<T> takeWhile(Predicate<? super T> predicate);

    @Override
    ImmutableOrderedSet<T> dropWhile(Predicate<? super T> predicate);

    @Override
    <V> ImmutableOrderedSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function);

    @Override
    <V> ImmutableOrderedSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function);

    @Override
    <S> ImmutableList<Pair<T, S>> zip(Iterable<S> that);

    @Override
    ImmutableOrderedSet<Pair<T, Integer>> zipWithIndex();

    @Override
    ImmutableOrderedSet<T> toReversed();

    @Override
    ImmutableOrderedSet<T> take(int count);

    @Override
    ImmutableOrderedSet<T> drop(int count);

    @Override
    ImmutableOrderedSet<T> union(SetIterable<? extends T> set);

    @Override
    ImmutableOrderedSet<T> intersect(SetIterable<? extends T> set);

    @Override
    ImmutableOrderedSet<T> difference(SetIterable<? extends T> subtrahendSet);

    @Override
    ImmutableOrderedSet<T> symmetricDifference(SetIterable<? extends T> setB);
}
