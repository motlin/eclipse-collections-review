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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
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
import org.eclipse.collections.impl.block.factory.Functions;
import org.eclipse.collections.impl.block.factory.Predicates;
import org.eclipse.collections.impl.block.procedure.PartitionPredicate2Procedure;
import org.eclipse.collections.impl.block.procedure.PartitionProcedure;
import org.eclipse.collections.impl.block.procedure.SelectInstancesOfProcedure;
import org.eclipse.collections.impl.collection.mutable.AbstractMutableCollection;
import org.eclipse.collections.impl.multimap.set.ordered.OrderedHashSetMultimap;
import org.eclipse.collections.impl.partition.set.ordered.PartitionOrderedSetAdapter;
import org.eclipse.collections.impl.utility.Iterate;
import org.eclipse.collections.impl.utility.internal.SetIterables;

/**
 * A memory-efficient, insertion-ordered set using a sparse index table with open addressing and dense
 * orderedElements storage. It is the set analog of
 * {@link org.eclipse.collections.impl.map.ordered.mutable.OrderedHashMap}: the sparse index table stores
 * integer indices that point into the dense storage, and the dense storage holds the elements in
 * insertion order. Adding an element that is already present does not change its position.
 * <p>
 * <b>Sizing and resizing.</b> A set created with the no-argument constructor allocates no tables until
 * its first insertion, which allocates the default index table capacity of 8.
 * The sparse index table always has a power-of-two capacity, and at most
 * two thirds of its slots may be occupied (by live elements or {@code REMOVED} tombstones). This usable
 * capacity, {@code (capacity * 2) / 3}, keeps probe chains short and guarantees the probe loop
 * terminates, because {@code EMPTY} slots always remain. The dense element storage is sized to exactly
 * the usable capacity, so it fills up at the same point the index table reaches its maximum load.
 * <p>
 * A rehash is triggered when an insert would overflow the dense storage, or would push the index
 * table's occupancy past the usable capacity without reusing a tombstone slot. The new capacity is
 * derived from the number of live elements alone, not from the current capacity: the smallest power of
 * two at least {@code max(size * 3, 8)}. Rehashing compacts tombstones out of both arrays. This has
 * consequences:
 * <ul>
 *     <li>With no removals, each rehash doubles the capacity: live elements fill two thirds of the old
 *     table, and three times that rounds up to twice the old capacity.</li>
 *     <li>After a rehash, the index table is at most one third full, leaving room for at least
 *     {@code size} more insertions before the next rehash.</li>
 *     <li>Removal-heavy workloads rehash at the same or a smaller capacity. Steady remove/re-add
 *     churn periodically compacts tombstones away instead of growing the arrays unboundedly.
 *     Removing the most recently added element reclaims its slot immediately. Removing any other
 *     element leaves a tombstone, and re-adding the same element appends it again, so repeatedly
 *     removing and re-adding one interior element triggers a compacting rehash about once every
 *     {@code size} such cycles even though the set of elements never changes.</li>
 * </ul>
 * For example, a set with the default index table capacity of 8 has a usable capacity of 5. The first
 * five insertions fit without rehashing. The sixth insertion triggers a rehash before it is stored:
 * with 5 live elements, the new index table capacity is 16 (the smallest power of two at least
 * {@code 5 * 3 = 15}), and the usable capacity becomes 10.
 * <p>
 * <b>Positional operations.</b> Because the dense storage is in insertion order, iteration, {@code getFirst},
 * {@code getLast}, reverse iteration, {@code take}, {@code drop}, and the other ordered operations walk the
 * dense storage directly, skipping tombstones. {@code indexOf} finds the element's dense index by hashing
 * and, when there are no interior tombstones, returns it directly.
 *
 * @since 14.0
 */
public class OrderedHashSet<T>
        extends AbstractMutableCollection<T>
        implements MutableOrderedSet<T>, Externalizable
{
    private static final long serialVersionUID = 1L;

    private static final int MINIMUM_INDICES_HASH_TABLE_CAPACITY = 8;
    private static final int EMPTY = -1;
    private static final int REMOVED = -2;
    private static final int NO_SLOT = -1;

    private static final Object NULL_ELEMENT = new Object()
    {
        @Override
        public boolean equals(Object obj)
        {
            throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
        }

        @Override
        public int hashCode()
        {
            throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
        }

        @Override
        public String toString()
        {
            return "OrderedHashSet.NULL_ELEMENT";
        }
    };

    private static final Object REMOVED_ELEMENT = new Object()
    {
        @Override
        public boolean equals(Object obj)
        {
            throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
        }

        @Override
        public int hashCode()
        {
            throw new RuntimeException("Possible corruption through unsynchronized concurrent modification.");
        }

        @Override
        public String toString()
        {
            return "OrderedHashSet.REMOVED_ELEMENT";
        }
    };

    // Sparse open-addressed hash table. Each slot holds an orderedElements index, EMPTY, or REMOVED.
    // Both arrays are null until the first insertion into a set created with the no-argument constructor.
    private int[] indicesHashTable;
    // Counts live and REMOVED slots in indicesHashTable.
    private int indicesHashTableOccupied;
    // Dense elements in insertion order. Removed elements are replaced by REMOVED_ELEMENT tombstones.
    private Object[] orderedElements;
    // Counts live and interior REMOVED_ELEMENT tombstones in orderedElements.
    private int orderedElementsOccupied;
    // Number of live elements.
    private int size;

    public OrderedHashSet()
    {
    }

    public OrderedHashSet(int initialCapacity)
    {
        if (initialCapacity < 0)
        {
            throw new IllegalArgumentException("initial capacity cannot be less than 0");
        }
        int indicesHashTableCapacity = smallestIndicesHashTableCapacityForElementCount(initialCapacity);
        this.allocateTable(indicesHashTableCapacity);
    }

    public OrderedHashSet(Collection<? extends T> collection)
    {
        this(collection.size());
        this.addAllIterable(collection);
    }

    public static <T> OrderedHashSet<T> newSet()
    {
        return new OrderedHashSet<>();
    }

    public static <T> OrderedHashSet<T> newSet(int size)
    {
        return new OrderedHashSet<>(size);
    }

    public static <T> OrderedHashSet<T> newSet(Iterable<? extends T> source)
    {
        if (source instanceof Collection<?>)
        {
            return new OrderedHashSet<>((Collection<? extends T>) source);
        }
        OrderedHashSet<T> result = new OrderedHashSet<>();
        Iterate.addAllTo(source, result);
        return result;
    }

    public static <T> OrderedHashSet<T> newSetWith(T... elements)
    {
        OrderedHashSet<T> result = new OrderedHashSet<>(elements.length);
        for (T element : elements)
        {
            result.add(element);
        }
        return result;
    }

    private void allocateTable(int indicesHashTableCapacity)
    {
        this.indicesHashTable = new int[indicesHashTableCapacity];
        Arrays.fill(this.indicesHashTable, EMPTY);
        this.orderedElements = new Object[usableElementCapacity(indicesHashTableCapacity)];
        this.size = 0;
        this.orderedElementsOccupied = 0;
        this.indicesHashTableOccupied = 0;
    }

    private int spread(Object element)
    {
        int h = element == null ? 0 : element.hashCode();
        h ^= h >>> 20 ^ h >>> 12;
        h ^= h >>> 7 ^ h >>> 4;
        return h & (this.indicesHashTable.length - 1);
    }

    private int usableElementCapacity()
    {
        return this.indicesHashTable == null ? 0 : usableElementCapacity(this.indicesHashTable.length);
    }

    private int indexAt(int slot)
    {
        return slot == NO_SLOT ? EMPTY : this.indicesHashTable[slot];
    }

    private static int usableElementCapacity(int indicesHashTableCapacity)
    {
        return (indicesHashTableCapacity << 1) / 3;
    }

    private static int smallestIndicesHashTableCapacityForElementCount(int elementCount)
    {
        int capacity = MINIMUM_INDICES_HASH_TABLE_CAPACITY;
        while (usableElementCapacity(capacity) < elementCount)
        {
            capacity <<= 1;
        }
        return capacity;
    }

    private static int smallestIndicesHashTableCapacityForGrowth(int liveElements)
    {
        return smallestPowerOfTwoGreaterThan(Math.max(liveElements * 3, MINIMUM_INDICES_HASH_TABLE_CAPACITY));
    }

    private static int smallestPowerOfTwoGreaterThan(int n)
    {
        return n > 1 ? Integer.highestOneBit(n - 1) << 1 : 1;
    }

    private static boolean isLive(Object element)
    {
        return element != null && element != REMOVED_ELEMENT;
    }

    @SuppressWarnings("unchecked")
    private static <T> T toNonSentinel(Object element)
    {
        return element == NULL_ELEMENT ? null : (T) element;
    }

    private static Object toSentinelIfNull(Object element)
    {
        return element == null ? NULL_ELEMENT : element;
    }

    private T elementAt(int index)
    {
        return toNonSentinel(this.orderedElements[index]);
    }

    /**
     * Probes the indicesHashTable for the given element. Returns the slot index in {@code indicesHashTable[]}.
     * <p>
     * The returned slot will be one of:
     * <ul>
     *     <li>{@code indicesHashTable[slot] >= 0} — element was found at this orderedElements index</li>
     *     <li>{@code indicesHashTable[slot] == EMPTY} — element not found, insert here</li>
     *     <li>{@code indicesHashTable[slot] == REMOVED} — element not found, but this removed slot can be reused</li>
     * </ul>
     * Returns {@code NO_SLOT} when the tables have not been allocated yet.
     */
    private int probe(Object element)
    {
        if (this.indicesHashTable == null)
        {
            return NO_SLOT;
        }
        int slot = this.spread(element);
        int removedSlot = -1;

        int idx = this.indicesHashTable[slot];
        if (idx == REMOVED)
        {
            removedSlot = slot;
        }
        else if (idx == EMPTY)
        {
            return slot;
        }
        else if (Objects.equals(this.elementAt(idx), element))
        {
            return slot;
        }

        int probe = 17;
        while (true)
        {
            slot = (slot + probe) & (this.indicesHashTable.length - 1);
            probe += 17;

            idx = this.indicesHashTable[slot];
            if (idx == REMOVED)
            {
                if (removedSlot == -1)
                {
                    removedSlot = slot;
                }
            }
            else if (idx == EMPTY)
            {
                return removedSlot == -1 ? slot : removedSlot;
            }
            else if (Objects.equals(this.elementAt(idx), element))
            {
                return slot;
            }
        }
    }

    private void addElementAtSlot(T element, int slot)
    {
        boolean reusedRemovedHashTableSlot = this.indexAt(slot) == REMOVED;

        if (this.needsRehashBeforeAdd(reusedRemovedHashTableSlot))
        {
            this.rehashForGrowth();
            slot = this.probe(element);
            reusedRemovedHashTableSlot = false;
        }

        this.orderedElements[this.orderedElementsOccupied] = toSentinelIfNull(element);
        this.indicesHashTable[slot] = this.orderedElementsOccupied;
        this.orderedElementsOccupied++;
        if (!reusedRemovedHashTableSlot)
        {
            this.indicesHashTableOccupied++;
        }
        this.size++;
    }

    private boolean needsRehashBeforeAdd(boolean reusedRemovedHashTableSlot)
    {
        int usableElementCapacity = this.usableElementCapacity();
        return this.orderedElementsOccupied >= usableElementCapacity
                || (!reusedRemovedHashTableSlot && this.indicesHashTableOccupied >= usableElementCapacity);
    }

    private void rehashForGrowth()
    {
        this.rehash(smallestIndicesHashTableCapacityForGrowth(this.size));
    }

    private void rehash(int indicesHashTableCapacity)
    {
        Object[] oldOrderedElements = this.orderedElements;
        int oldOrderedElementsOccupied = this.orderedElementsOccupied;

        this.indicesHashTable = new int[indicesHashTableCapacity];
        Arrays.fill(this.indicesHashTable, EMPTY);
        this.orderedElements = new Object[usableElementCapacity(indicesHashTableCapacity)];

        int newIndex = 0;
        for (int i = 0; i < oldOrderedElementsOccupied; i++)
        {
            Object element = oldOrderedElements[i];
            if (isLive(element))
            {
                int slot = this.probeForRehash(toNonSentinel(element));
                this.indicesHashTable[slot] = newIndex;
                this.orderedElements[newIndex] = element;
                newIndex++;
            }
        }
        this.orderedElementsOccupied = newIndex;
        this.indicesHashTableOccupied = newIndex;
    }

    private int probeForRehash(Object element)
    {
        int slot = this.spread(element);
        if (this.indicesHashTable[slot] == EMPTY)
        {
            return slot;
        }
        int probe = 17;
        while (true)
        {
            slot = (slot + probe) & (this.indicesHashTable.length - 1);
            probe += 17;
            if (this.indicesHashTable[slot] == EMPTY)
            {
                return slot;
            }
        }
    }

    private void removeAtSlot(int slot, int idx)
    {
        this.indicesHashTable[slot] = REMOVED;
        this.orderedElements[idx] = REMOVED_ELEMENT;
        this.size--;
        this.reclaimTrailingTombstones();
    }

    private void reclaimTrailingTombstones()
    {
        while (this.orderedElementsOccupied > 0 && this.orderedElements[this.orderedElementsOccupied - 1] == REMOVED_ELEMENT)
        {
            this.orderedElementsOccupied--;
            this.orderedElements[this.orderedElementsOccupied] = null;
        }
    }

    @Override
    public boolean add(T element)
    {
        int slot = this.probe(element);
        if (this.indexAt(slot) >= 0)
        {
            return false;
        }
        this.addElementAtSlot(element, slot);
        return true;
    }

    @Override
    public boolean remove(Object element)
    {
        int slot = this.probe(element);
        int idx = this.indexAt(slot);
        if (idx >= 0)
        {
            this.removeAtSlot(slot, idx);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        int oldSize = this.size;
        for (Object each : iterable)
        {
            this.remove(each);
        }
        return this.size != oldSize;
    }

    @Override
    public boolean contains(Object element)
    {
        return this.indexAt(this.probe(element)) >= 0;
    }

    @Override
    public int size()
    {
        return this.size;
    }

    @Override
    public boolean isEmpty()
    {
        return this.size == 0;
    }

    @Override
    public void clear()
    {
        if (this.indicesHashTable == null)
        {
            return;
        }
        Arrays.fill(this.indicesHashTable, EMPTY);
        Arrays.fill(this.orderedElements, 0, this.orderedElementsOccupied, null);
        this.size = 0;
        this.orderedElementsOccupied = 0;
        this.indicesHashTableOccupied = 0;
    }

    @Override
    public Iterator<T> iterator()
    {
        return new OrderedHashSetIterator();
    }

    private class OrderedHashSetIterator implements Iterator<T>
    {
        private int position;
        private int count;
        private boolean lastReturned;

        @Override
        public boolean hasNext()
        {
            return this.count < OrderedHashSet.this.size;
        }

        @Override
        public T next()
        {
            if (!this.hasNext())
            {
                throw new NoSuchElementException();
            }
            Object[] localElements = OrderedHashSet.this.orderedElements;
            while (!isLive(localElements[this.position]))
            {
                this.position++;
            }
            T result = toNonSentinel(localElements[this.position]);
            this.count++;
            this.position++;
            this.lastReturned = true;
            return result;
        }

        @Override
        public void remove()
        {
            if (!this.lastReturned)
            {
                throw new IllegalStateException();
            }
            OrderedHashSet.this.remove(OrderedHashSet.this.elementAt(this.position - 1));
            this.count--;
            this.lastReturned = false;
        }
    }

    @Override
    public void each(Procedure<? super T> procedure)
    {
        int count = 0;
        for (int i = 0; count < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                procedure.value(this.elementAt(i));
                count++;
            }
        }
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        int count = 0;
        for (int i = 0; count < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                objectIntProcedure.value(this.elementAt(i), count);
                count++;
            }
        }
    }

    @Override
    public void forEach(int startIndex, int endIndex, Procedure<? super T> procedure)
    {
        this.forEachWithIndex(startIndex, endIndex, (each, index) -> procedure.value(each));
    }

    @Override
    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= this.size || toIndex >= this.size)
        {
            throw new IndexOutOfBoundsException("Index out of range: fromIndex=" + fromIndex + ", toIndex=" + toIndex + ", size=" + this.size);
        }

        if (fromIndex <= toIndex)
        {
            int count = 0;
            for (int i = 0; count <= toIndex; i++)
            {
                if (isLive(this.orderedElements[i]))
                {
                    if (count >= fromIndex)
                    {
                        objectIntProcedure.value(this.elementAt(i), count);
                    }
                    count++;
                }
            }
        }
        else
        {
            int count = this.size - 1;
            for (int i = this.orderedElementsOccupied - 1; count >= toIndex; i--)
            {
                if (isLive(this.orderedElements[i]))
                {
                    if (count <= fromIndex)
                    {
                        objectIntProcedure.value(this.elementAt(i), count);
                    }
                    count--;
                }
            }
        }
    }

    @Override
    public void reverseForEach(Procedure<? super T> procedure)
    {
        int count = 0;
        for (int i = this.orderedElementsOccupied - 1; count < this.size; i--)
        {
            if (isLive(this.orderedElements[i]))
            {
                procedure.value(this.elementAt(i));
                count++;
            }
        }
    }

    @Override
    public void reverseForEachWithIndex(ObjectIntProcedure<? super T> procedure)
    {
        int index = this.size - 1;
        for (int i = this.orderedElementsOccupied - 1; index >= 0; i--)
        {
            if (isLive(this.orderedElements[i]))
            {
                procedure.value(this.elementAt(i), index);
                index--;
            }
        }
    }

    @Override
    public T getFirst()
    {
        if (this.size == 0)
        {
            return null;
        }
        int i = 0;
        while (!isLive(this.orderedElements[i]))
        {
            i++;
        }
        return this.elementAt(i);
    }

    @Override
    public T getLast()
    {
        if (this.size == 0)
        {
            return null;
        }
        // Trailing tombstones are always reclaimed, so the last occupied element is live.
        return this.elementAt(this.orderedElementsOccupied - 1);
    }

    @Override
    public int indexOf(Object object)
    {
        int idx = this.indexAt(this.probe(object));
        if (idx < 0)
        {
            return -1;
        }
        if (this.orderedElementsOccupied == this.size)
        {
            return idx;
        }
        int tombstones = 0;
        for (int i = 0; i < idx; i++)
        {
            if (this.orderedElements[i] == REMOVED_ELEMENT)
            {
                tombstones++;
            }
        }
        return idx - tombstones;
    }

    @Override
    public int detectIndex(Predicate<? super T> predicate)
    {
        int count = 0;
        for (int i = 0; count < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                if (predicate.accept(this.elementAt(i)))
                {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    @Override
    public int detectLastIndex(Predicate<? super T> predicate)
    {
        int index = this.size - 1;
        for (int i = this.orderedElementsOccupied - 1; index >= 0; i--)
        {
            if (isLive(this.orderedElements[i]))
            {
                if (predicate.accept(this.elementAt(i)))
                {
                    return index;
                }
                index--;
            }
        }
        return -1;
    }

    @Override
    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        if (this.size != other.size())
        {
            return false;
        }
        Iterator<S> otherIterator = other.iterator();
        int count = 0;
        for (int i = 0; count < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                if (!predicate.accept(this.elementAt(i), otherIterator.next()))
                {
                    return false;
                }
                count++;
            }
        }
        return true;
    }

    @Override
    public OrderedHashSet<T> toReversed()
    {
        OrderedHashSet<T> result = new OrderedHashSet<>(this.size);
        this.reverseForEach(result::add);
        return result;
    }

    @Override
    public OrderedHashSet<T> take(int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        int resultSize = Math.min(count, this.size);
        OrderedHashSet<T> result = new OrderedHashSet<>(resultSize);
        int taken = 0;
        for (int i = 0; taken < resultSize; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                result.add(this.elementAt(i));
                taken++;
            }
        }
        return result;
    }

    @Override
    public OrderedHashSet<T> drop(int count)
    {
        if (count < 0)
        {
            throw new IllegalArgumentException("Count must be greater than zero, but was: " + count);
        }
        int start = Math.min(count, this.size);
        OrderedHashSet<T> result = new OrderedHashSet<>(this.size - start);
        int skipped = 0;
        for (int i = 0; skipped < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                if (skipped >= start)
                {
                    result.add(this.elementAt(i));
                }
                skipped++;
            }
        }
        return result;
    }

    @Override
    public OrderedHashSet<T> takeWhile(Predicate<? super T> predicate)
    {
        OrderedHashSet<T> result = new OrderedHashSet<>();
        int count = 0;
        for (int i = 0; count < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                T each = this.elementAt(i);
                if (!predicate.accept(each))
                {
                    break;
                }
                result.add(each);
                count++;
            }
        }
        return result;
    }

    @Override
    public OrderedHashSet<T> dropWhile(Predicate<? super T> predicate)
    {
        OrderedHashSet<T> result = new OrderedHashSet<>();
        boolean dropping = true;
        int count = 0;
        for (int i = 0; count < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                T each = this.elementAt(i);
                if (dropping && !predicate.accept(each))
                {
                    dropping = false;
                }
                if (!dropping)
                {
                    result.add(each);
                }
                count++;
            }
        }
        return result;
    }

    @Override
    public PartitionMutableOrderedSet<T> partitionWhile(Predicate<? super T> predicate)
    {
        PartitionMutableOrderedSet<T> result = new PartitionOrderedSetAdapter<>();
        boolean selecting = true;
        int count = 0;
        for (int i = 0; count < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                T each = this.elementAt(i);
                if (selecting && !predicate.accept(each))
                {
                    selecting = false;
                }
                if (selecting)
                {
                    result.getSelected().add(each);
                }
                else
                {
                    result.getRejected().add(each);
                }
                count++;
            }
        }
        return result;
    }

    @Override
    public OrderedHashSet<T> distinct()
    {
        return this.clone();
    }

    @Override
    public OrderedHashSet<T> newEmpty()
    {
        return new OrderedHashSet<>();
    }

    @Override
    public OrderedHashSet<T> clone()
    {
        OrderedHashSet<T> result = new OrderedHashSet<>();
        if (this.indicesHashTable != null)
        {
            result.indicesHashTable = this.indicesHashTable.clone();
            result.orderedElements = this.orderedElements.clone();
            result.indicesHashTableOccupied = this.indicesHashTableOccupied;
            result.orderedElementsOccupied = this.orderedElementsOccupied;
            result.size = this.size;
        }
        return result;
    }

    @Override
    public OrderedHashSet<T> with(T element)
    {
        this.add(element);
        return this;
    }

    @Override
    public OrderedHashSet<T> without(T element)
    {
        this.remove(element);
        return this;
    }

    @Override
    public OrderedHashSet<T> withAll(Iterable<? extends T> elements)
    {
        this.addAllIterable(elements);
        return this;
    }

    @Override
    public OrderedHashSet<T> withoutAll(Iterable<? extends T> elements)
    {
        this.removeAllIterable(elements);
        return this;
    }

    @Override
    public OrderedHashSet<T> tap(Procedure<? super T> procedure)
    {
        this.forEach(procedure);
        return this;
    }

    @Override
    public OrderedHashSet<T> select(Predicate<? super T> predicate)
    {
        return this.select(predicate, this.newEmpty());
    }

    @Override
    public <P> OrderedHashSet<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.select(Predicates.bind(predicate, parameter));
    }

    @Override
    public OrderedHashSet<T> reject(Predicate<? super T> predicate)
    {
        return this.reject(predicate, this.newEmpty());
    }

    @Override
    public <P> OrderedHashSet<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.reject(Predicates.bind(predicate, parameter));
    }

    @Override
    public PartitionMutableOrderedSet<T> partition(Predicate<? super T> predicate)
    {
        PartitionMutableOrderedSet<T> result = new PartitionOrderedSetAdapter<>();
        this.forEach(new PartitionProcedure<>(predicate, result));
        return result;
    }

    @Override
    public <P> PartitionMutableOrderedSet<T> partitionWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        PartitionMutableOrderedSet<T> result = new PartitionOrderedSetAdapter<>();
        this.forEach(new PartitionPredicate2Procedure<>(predicate, parameter, result));
        return result;
    }

    @Override
    public <S> OrderedHashSet<S> selectInstancesOf(Class<S> clazz)
    {
        OrderedHashSet<S> result = new OrderedHashSet<>();
        this.forEach(new SelectInstancesOfProcedure<>(clazz, result));
        return result;
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return this.collect(function, Lists.mutable.withInitialCapacity(this.size));
    }

    @Override
    public <P, V> MutableList<V> collectWith(Function2<? super T, ? super P, ? extends V> function, P parameter)
    {
        return this.collect(Functions.bind(function, parameter));
    }

    @Override
    public <V> MutableList<V> collectIf(Predicate<? super T> predicate, Function<? super T, ? extends V> function)
    {
        return this.collectIf(predicate, function, Lists.mutable.empty());
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.flatCollect(function, Lists.mutable.withInitialCapacity(this.size));
    }

    @Override
    public MutableBooleanList collectBoolean(BooleanFunction<? super T> booleanFunction)
    {
        return this.collectBoolean(booleanFunction, BooleanLists.mutable.withInitialCapacity(this.size));
    }

    @Override
    public MutableByteList collectByte(ByteFunction<? super T> byteFunction)
    {
        return this.collectByte(byteFunction, ByteLists.mutable.withInitialCapacity(this.size));
    }

    @Override
    public MutableCharList collectChar(CharFunction<? super T> charFunction)
    {
        return this.collectChar(charFunction, CharLists.mutable.withInitialCapacity(this.size));
    }

    @Override
    public MutableDoubleList collectDouble(DoubleFunction<? super T> doubleFunction)
    {
        return this.collectDouble(doubleFunction, DoubleLists.mutable.withInitialCapacity(this.size));
    }

    @Override
    public MutableFloatList collectFloat(FloatFunction<? super T> floatFunction)
    {
        return this.collectFloat(floatFunction, FloatLists.mutable.withInitialCapacity(this.size));
    }

    @Override
    public MutableIntList collectInt(IntFunction<? super T> intFunction)
    {
        return this.collectInt(intFunction, IntLists.mutable.withInitialCapacity(this.size));
    }

    @Override
    public MutableLongList collectLong(LongFunction<? super T> longFunction)
    {
        return this.collectLong(longFunction, LongLists.mutable.withInitialCapacity(this.size));
    }

    @Override
    public MutableShortList collectShort(ShortFunction<? super T> shortFunction)
    {
        return this.collectShort(shortFunction, ShortLists.mutable.withInitialCapacity(this.size));
    }

    @Override
    public <V> OrderedHashSetMultimap<V, T> groupBy(Function<? super T, ? extends V> function)
    {
        return this.groupBy(function, OrderedHashSetMultimap.newMultimap());
    }

    @Override
    public <V> OrderedHashSetMultimap<V, T> groupByEach(Function<? super T, ? extends Iterable<V>> function)
    {
        return this.groupByEach(function, OrderedHashSetMultimap.newMultimap());
    }

    @Override
    public <S> MutableList<Pair<T, S>> zip(Iterable<S> that)
    {
        return this.zip(that, Lists.mutable.withInitialCapacity(Math.min(this.size, Iterate.sizeOf(that))));
    }

    @Override
    public OrderedHashSet<Pair<T, Integer>> zipWithIndex()
    {
        return this.zipWithIndex(new OrderedHashSet<>(this.size));
    }

    @Override
    public OrderedHashSet<T> union(SetIterable<? extends T> set)
    {
        return this.unionInto(set, this.newEmpty());
    }

    @Override
    public <R extends Set<T>> R unionInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.unionInto(this, set, targetSet);
    }

    @Override
    public OrderedHashSet<T> intersect(SetIterable<? extends T> set)
    {
        return this.intersectInto(set, this.newEmpty());
    }

    @Override
    public <R extends Set<T>> R intersectInto(SetIterable<? extends T> set, R targetSet)
    {
        return SetIterables.intersectInto(this, set, targetSet);
    }

    @Override
    public OrderedHashSet<T> difference(SetIterable<? extends T> subtrahendSet)
    {
        return this.differenceInto(subtrahendSet, this.newEmpty());
    }

    @Override
    public <R extends Set<T>> R differenceInto(SetIterable<? extends T> subtrahendSet, R targetSet)
    {
        return SetIterables.differenceInto(this, subtrahendSet, targetSet);
    }

    @Override
    public OrderedHashSet<T> symmetricDifference(SetIterable<? extends T> setB)
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
    public MutableOrderedSet<T> asUnmodifiable()
    {
        return OrderedSetAdapter.adapt(Collections.unmodifiableSet(this));
    }

    @Override
    public MutableOrderedSet<T> asSynchronized()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".asSynchronized() not implemented yet");
    }

    @Override
    public ImmutableOrderedSet<T> toImmutable()
    {
        return OrderedSets.immutable.withAll(this);
    }

    @Override
    public boolean equals(Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (!(object instanceof Set))
        {
            return false;
        }
        Set<?> other = (Set<?>) object;
        if (this.size != other.size())
        {
            return false;
        }
        int count = 0;
        for (int i = 0; count < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                if (!other.contains(this.elementAt(i)))
                {
                    return false;
                }
                count++;
            }
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hashCode = 0;
        int count = 0;
        for (int i = 0; count < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                T each = this.elementAt(i);
                hashCode += each == null ? 0 : each.hashCode();
                count++;
            }
        }
        return hashCode;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeInt(this.size);
        int count = 0;
        for (int i = 0; count < this.size; i++)
        {
            if (isLive(this.orderedElements[i]))
            {
                out.writeObject(this.elementAt(i));
                count++;
            }
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        int deserializedSize = in.readInt();
        if (deserializedSize > 0)
        {
            this.allocateTable(smallestIndicesHashTableCapacityForElementCount(deserializedSize));
        }
        for (int i = 0; i < deserializedSize; i++)
        {
            this.add((T) in.readObject());
        }
    }
}
