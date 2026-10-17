/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.jmh.set;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.eclipse.collections.api.set.ordered.MutableOrderedSet;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedHashSet;
import org.eclipse.collections.impl.set.ordered.mutable.OrderedSetAdapter;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Fork(2)
@Warmup(iterations = 10, time = 2)
@Measurement(iterations = 10, time = 2)
public class OrderedHashSetJMHTest
{
    private static final long RANDOM_SEED = 123456789L;
    private static final Integer FIRST_ELEMENT = 0;
    private static final Integer SECOND_ELEMENT = 1;

    @State(Scope.Thread)
    public static class SetState
    {
        @Param({"1000", "10000", "100000"})
        public int size;

        private Integer[] elements;
        private OrderedHashSet<Integer> orderedHashSet;
        private LinkedHashSet<Integer> linkedHashSet;
        private MutableOrderedSet<Integer> orderedSetAdapter;
        private UnifiedSet<Integer> unifiedSet;

        @Setup
        public void setUp()
        {
            this.elements = shuffledElements(this.size);
            this.orderedHashSet = newOrderedHashSet(this.elements);
            this.linkedHashSet = newLinkedHashSet(this.elements);
            this.orderedSetAdapter = newOrderedSetAdapter(this.elements);
            this.unifiedSet = newUnifiedSet(this.elements);
        }
    }

    @State(Scope.Thread)
    public static class RemoveAddChurnState
    {
        @Param({"1000", "10000", "100000"})
        public int churnCount;

        private OrderedHashSet<Integer> orderedHashSet;
        private LinkedHashSet<Integer> linkedHashSet;
        private MutableOrderedSet<Integer> orderedSetAdapter;
        private UnifiedSet<Integer> unifiedSet;

        @Setup(Level.Invocation)
        public void setUp()
        {
            this.orderedHashSet = new OrderedHashSet<>(2);
            this.orderedHashSet.add(FIRST_ELEMENT);
            this.orderedHashSet.add(SECOND_ELEMENT);

            this.linkedHashSet = new LinkedHashSet<>(2);
            this.linkedHashSet.add(FIRST_ELEMENT);
            this.linkedHashSet.add(SECOND_ELEMENT);

            this.orderedSetAdapter = OrderedSetAdapter.adapt(new LinkedHashSet<>(2));
            this.orderedSetAdapter.add(FIRST_ELEMENT);
            this.orderedSetAdapter.add(SECOND_ELEMENT);

            this.unifiedSet = UnifiedSet.newSet(2);
            this.unifiedSet.add(FIRST_ELEMENT);
            this.unifiedSet.add(SECOND_ELEMENT);
        }
    }

    @Benchmark
    public OrderedHashSet<Integer> orderedHashSetAdd(SetState state)
    {
        return newOrderedHashSet(state.elements);
    }

    @Benchmark
    public LinkedHashSet<Integer> linkedHashSetAdd(SetState state)
    {
        return newLinkedHashSet(state.elements);
    }

    @Benchmark
    public MutableOrderedSet<Integer> orderedSetAdapterAdd(SetState state)
    {
        return newOrderedSetAdapter(state.elements);
    }

    @Benchmark
    public UnifiedSet<Integer> unifiedSetAdd(SetState state)
    {
        return newUnifiedSet(state.elements);
    }

    @Benchmark
    public long orderedHashSetContains(SetState state)
    {
        OrderedHashSet<Integer> localSet = state.orderedHashSet;
        Integer[] localElements = state.elements;
        long result = 0L;
        for (Integer element : localElements)
        {
            if (!localSet.contains(element))
            {
                throw new AssertionError(element);
            }
            result += element;
        }
        return result;
    }

    @Benchmark
    public long linkedHashSetContains(SetState state)
    {
        LinkedHashSet<Integer> localSet = state.linkedHashSet;
        Integer[] localElements = state.elements;
        long result = 0L;
        for (Integer element : localElements)
        {
            if (!localSet.contains(element))
            {
                throw new AssertionError(element);
            }
            result += element;
        }
        return result;
    }

    @Benchmark
    public long orderedSetAdapterContains(SetState state)
    {
        MutableOrderedSet<Integer> localSet = state.orderedSetAdapter;
        Integer[] localElements = state.elements;
        long result = 0L;
        for (Integer element : localElements)
        {
            if (!localSet.contains(element))
            {
                throw new AssertionError(element);
            }
            result += element;
        }
        return result;
    }

    @Benchmark
    public long unifiedSetContains(SetState state)
    {
        UnifiedSet<Integer> localSet = state.unifiedSet;
        Integer[] localElements = state.elements;
        long result = 0L;
        for (Integer element : localElements)
        {
            if (!localSet.contains(element))
            {
                throw new AssertionError(element);
            }
            result += element;
        }
        return result;
    }

    @Benchmark
    public long orderedHashSetIteration(SetState state)
    {
        long result = 0L;
        for (Integer element : state.orderedHashSet)
        {
            result += element;
        }
        return result;
    }

    @Benchmark
    public long linkedHashSetIteration(SetState state)
    {
        long result = 0L;
        for (Integer element : state.linkedHashSet)
        {
            result += element;
        }
        return result;
    }

    @Benchmark
    public long orderedSetAdapterIteration(SetState state)
    {
        long result = 0L;
        for (Integer element : state.orderedSetAdapter)
        {
            result += element;
        }
        return result;
    }

    @Benchmark
    public long unifiedSetIteration(SetState state)
    {
        long result = 0L;
        for (Integer element : state.unifiedSet)
        {
            result += element;
        }
        return result;
    }

    @Benchmark
    public OrderedHashSet<Integer> orderedHashSetRemoveAddChurn(RemoveAddChurnState state)
    {
        OrderedHashSet<Integer> localSet = state.orderedHashSet;
        for (int i = 0; i < state.churnCount; i++)
        {
            localSet.remove(FIRST_ELEMENT);
            localSet.add(FIRST_ELEMENT);
            localSet.remove(SECOND_ELEMENT);
            localSet.add(SECOND_ELEMENT);
        }
        return localSet;
    }

    @Benchmark
    public LinkedHashSet<Integer> linkedHashSetRemoveAddChurn(RemoveAddChurnState state)
    {
        LinkedHashSet<Integer> localSet = state.linkedHashSet;
        for (int i = 0; i < state.churnCount; i++)
        {
            localSet.remove(FIRST_ELEMENT);
            localSet.add(FIRST_ELEMENT);
            localSet.remove(SECOND_ELEMENT);
            localSet.add(SECOND_ELEMENT);
        }
        return localSet;
    }

    @Benchmark
    public MutableOrderedSet<Integer> orderedSetAdapterRemoveAddChurn(RemoveAddChurnState state)
    {
        MutableOrderedSet<Integer> localSet = state.orderedSetAdapter;
        for (int i = 0; i < state.churnCount; i++)
        {
            localSet.remove(FIRST_ELEMENT);
            localSet.add(FIRST_ELEMENT);
            localSet.remove(SECOND_ELEMENT);
            localSet.add(SECOND_ELEMENT);
        }
        return localSet;
    }

    @Benchmark
    public UnifiedSet<Integer> unifiedSetRemoveAddChurn(RemoveAddChurnState state)
    {
        UnifiedSet<Integer> localSet = state.unifiedSet;
        for (int i = 0; i < state.churnCount; i++)
        {
            localSet.remove(FIRST_ELEMENT);
            localSet.add(FIRST_ELEMENT);
            localSet.remove(SECOND_ELEMENT);
            localSet.add(SECOND_ELEMENT);
        }
        return localSet;
    }

    private static OrderedHashSet<Integer> newOrderedHashSet(Integer[] elements)
    {
        OrderedHashSet<Integer> set = new OrderedHashSet<>(elements.length);
        for (Integer element : elements)
        {
            set.add(element);
        }
        return set;
    }

    private static LinkedHashSet<Integer> newLinkedHashSet(Integer[] elements)
    {
        LinkedHashSet<Integer> set = new LinkedHashSet<>(elements.length);
        for (Integer element : elements)
        {
            set.add(element);
        }
        return set;
    }

    private static MutableOrderedSet<Integer> newOrderedSetAdapter(Integer[] elements)
    {
        MutableOrderedSet<Integer> set = OrderedSetAdapter.adapt(new LinkedHashSet<>(elements.length));
        for (Integer element : elements)
        {
            set.add(element);
        }
        return set;
    }

    private static UnifiedSet<Integer> newUnifiedSet(Integer[] elements)
    {
        UnifiedSet<Integer> set = UnifiedSet.newSet(elements.length);
        for (Integer element : elements)
        {
            set.add(element);
        }
        return set;
    }

    private static Integer[] shuffledElements(int size)
    {
        Integer[] elements = new Integer[size];
        for (int i = 0; i < size; i++)
        {
            elements[i] = i;
        }
        Collections.shuffle(Arrays.asList(elements), new Random(RANDOM_SEED));
        return elements;
    }
}
