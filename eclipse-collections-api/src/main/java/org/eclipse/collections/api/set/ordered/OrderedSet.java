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
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.list.primitive.BooleanList;
import org.eclipse.collections.api.list.primitive.ByteList;
import org.eclipse.collections.api.list.primitive.CharList;
import org.eclipse.collections.api.list.primitive.DoubleList;
import org.eclipse.collections.api.list.primitive.FloatList;
import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.api.list.primitive.LongList;
import org.eclipse.collections.api.list.primitive.ShortList;
import org.eclipse.collections.api.ordered.ReversibleIterable;
import org.eclipse.collections.api.partition.set.ordered.PartitionOrderedSet;
import org.eclipse.collections.api.set.SetIterable;
import org.eclipse.collections.api.tuple.Pair;

/**
 * An OrderedSet is a set whose elements keep the order in which they were first added, like
 * {@link java.util.LinkedHashSet}. Adding an element that is already present does not change its position.
 *
 * @since 14.0
 */
public interface OrderedSet<T>
        extends SetIterable<T>, ReversibleIterable<T>
{
    @Override
    OrderedSet<T> union(SetIterable<? extends T> set);

    @Override
    OrderedSet<T> intersect(SetIterable<? extends T> set);

    @Override
    OrderedSet<T> difference(SetIterable<? extends T> subtrahendSet);

    @Override
    OrderedSet<T> symmetricDifference(SetIterable<? extends T> setB);

    @Override
    OrderedSet<T> tap(Procedure<? super T> procedure);

    @Override
    OrderedSet<T> select(Predicate<? super T> predicate);

    @Override
    <P> OrderedSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    OrderedSet<T> reject(Predicate<? super T> predicate);

    @Override
    <P> OrderedSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    PartitionOrderedSet<T> partition(Predicate<? super T> predicate);

    @Override
    <P> PartitionOrderedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter);

    @Override
    PartitionOrderedSet<T> partitionWhile(Predicate<? super T> predicate);

    @Override
    <S> OrderedSet<S> selectInstancesOf(Class<S> clazz);

    @Override
    <V> ListIterable<V> collect(Function<? super T, ? extends V> function);

    @Override
    default <V> ListIterable<V> collectWithIndex(ObjectIntToObjectFunction<? super T, ? extends V> function)
    {
        int[] index = {0};
        return this.collect(each -> function.valueOf(each, index[0]++));
    }

    @Override
    BooleanList collectBoolean(BooleanFunction<? super T> booleanFunction);

    @Override
    ByteList collectByte(ByteFunction<? super T> byteFunction);

    @Override
    CharList collectChar(CharFunction<? super T> charFunction);

    @Override
    DoubleList collectDouble(DoubleFunction<? super T> doubleFunction);

    @Override
    FloatList collectFloat(FloatFunction<? super T> floatFunction);

    @Override
    IntList collectInt(IntFunction<? super T> intFunction);

    @Override
    LongList collectLong(LongFunction<? super T> longFunction);

    @Override
    ShortList collectShort(ShortFunction<? super T> shortFunction);

    @Override
    <P, V> ListIterable<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter);

    @Override
    <V> ListIterable<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function);

    @Override
    <V> ListIterable<V> flatCollect(Function<? super T, ? extends Iterable<V>> function);

    @Override
    default <P, V> ListIterable<V> flatCollectWith(Function2<? super T, ? super P, ? extends Iterable<V>> function, P parameter)
    {
        return this.flatCollect(each -> function.apply(each, parameter));
    }

    @Override
    OrderedSet<T> distinct();

    @Override
    OrderedSet<T> takeWhile(Predicate<? super T> predicate);

    @Override
    OrderedSet<T> dropWhile(Predicate<? super T> predicate);

    @Override
    OrderedSet<Pair<T, Integer>> zipWithIndex();

    @Override
    OrderedSet<T> toReversed();

    @Override
    OrderedSet<T> take(int count);

    @Override
    OrderedSet<T> drop(int count);
}
