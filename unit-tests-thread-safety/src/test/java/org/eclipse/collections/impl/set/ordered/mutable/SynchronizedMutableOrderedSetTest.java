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

import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.impl.collection.mutable.SynchronizedMutableCollectionTestTrait;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.set.SynchronizedSetIterableTestTrait;
import org.junit.jupiter.api.Test;

public class SynchronizedMutableOrderedSetTest
        implements SynchronizedMutableCollectionTestTrait, SynchronizedSetIterableTestTrait
{
    private final MutableOrderedSet<String> classUnderTest = OrderedHashSet.newSetWith("1", "2", "3").asSynchronized();

    @Override
    public MutableOrderedSet<String> getClassUnderTest()
    {
        return this.classUnderTest;
    }

    @Test
    public void clone_synchronized()
    {
        this.assertSynchronized(this.getClassUnderTest()::clone);
    }

    @Test
    public void getFirstOptional_synchronized()
    {
        this.assertSynchronized(this.getClassUnderTest()::getFirstOptional);
    }

    @Test
    public void getLastOptional_synchronized()
    {
        this.assertSynchronized(this.getClassUnderTest()::getLastOptional);
    }

    @Test
    public void indexOf_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().indexOf("1"));
    }

    @Test
    public void detectIndex_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().detectIndex(value -> false));
    }

    @Test
    public void detectLastIndex_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().detectLastIndex(value -> false));
    }

    @Test
    public void reverseForEach_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().reverseForEach(value -> { }));
    }

    @Test
    public void reverseForEachWithIndex_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().reverseForEachWithIndex((value, index) -> { }));
    }

    @Test
    public void toReversed_synchronized()
    {
        this.assertSynchronized(this.getClassUnderTest()::toReversed);
    }

    @Test
    public void take_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().take(2));
    }

    @Test
    public void drop_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().drop(2));
    }

    @Test
    public void takeWhile_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().takeWhile(value -> true));
    }

    @Test
    public void dropWhile_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().dropWhile(value -> true));
    }

    @Test
    public void partitionWhile_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().partitionWhile(value -> true));
    }

    @Test
    public void distinct_synchronized()
    {
        this.assertSynchronized(this.getClassUnderTest()::distinct);
    }

    @Test
    public void corresponds_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().corresponds(FastList.newListWith("1", "2", "3"), String::equals));
    }

    @Test
    public void forEach_with_startIndex_endIndex_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().forEach(0, 1, value -> { }));
    }

    @Test
    public void forEachWithIndex_with_fromIndex_toIndex_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().forEachWithIndex(0, 1, (value, index) -> { }));
    }

    @Test
    public void toStack_synchronized()
    {
        this.assertSynchronized(this.getClassUnderTest()::toStack);
    }

    @Test
    public void collectWithIndex_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().collectWithIndex((value, index) -> value + index));
    }

    @Test
    public void collectWithIndex_with_target_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().collectWithIndex((value, index) -> value + index, FastList.newList()));
    }

    @Test
    public void selectWithIndex_with_target_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().selectWithIndex((value, index) -> true, FastList.newList()));
    }

    @Test
    public void rejectWithIndex_with_target_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().rejectWithIndex((value, index) -> false, FastList.newList()));
    }

    @Test
    public void injectIntoWithIndex_synchronized()
    {
        this.assertSynchronized(() -> this.getClassUnderTest().injectIntoWithIndex("", (result, value, index) -> result + value + index));
    }
}
