/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.stack.immutable;

import java.util.EmptyStackException;

import org.eclipse.collections.api.factory.Stacks;
import org.eclipse.collections.api.stack.ImmutableStack;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.test.stack.immutable.ImmutableStackTestCase;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.test.IterableTestCase.assertIterablesEqual;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImmutableArrayStackTest implements ImmutableStackTestCase
{
    @SafeVarargs
    @Override
    public final <T> ImmutableStack<T> newWith(T... elements)
    {
        // Like ArrayStack.toImmutable(), use the ImmutableEmptyStack singleton for empty stacks.
        if (elements.length == 0)
        {
            return Stacks.immutable.empty();
        }
        return ImmutableArrayStack.newStackFromTopToBottom(elements);
    }

    @Override
    @Test
    public void ImmutableStack_pop()
    {
        ImmutableStack<Integer> immutableStack = this.newWith(5, 1, 4, 2, 3);
        ImmutableStack<Integer> poppedStack = immutableStack.pop();
        assertIterablesEqual(Stacks.immutable.withReversed(1, 4, 2, 3), poppedStack);
        assertIterablesEqual(Stacks.immutable.withReversed(5, 1, 4, 2, 3), immutableStack);

        // TODO: Popping the last element should return the ImmutableEmptyStack singleton.
        ImmutableStack<Integer> emptyStack = immutableStack.pop().pop().pop().pop().pop();
        assertEquals(Stacks.immutable.with(), emptyStack);
        assertNotSame(Stacks.immutable.with(), emptyStack);
        assertThrows(EmptyStackException.class, emptyStack::pop);
    }

    @Override
    @Test
    public void ImmutableStack_peekAndPop()
    {
        ImmutableStack<Integer> immutableStack = this.newWith(5, 1, 4, 2, 3);
        Pair<Integer, ImmutableStack<Integer>> elementAndStack = immutableStack.peekAndPop();
        assertEquals(Integer.valueOf(5), elementAndStack.getOne());
        assertIterablesEqual(Stacks.immutable.withReversed(1, 4, 2, 3), elementAndStack.getTwo());
        assertIterablesEqual(Stacks.immutable.withReversed(5, 1, 4, 2, 3), immutableStack);

        // TODO: Popping the last element should return the ImmutableEmptyStack singleton.
        ImmutableStack<Integer> emptyStack = immutableStack.peekAndPop().getTwo()
                .peekAndPop().getTwo()
                .peekAndPop().getTwo()
                .peekAndPop().getTwo()
                .peekAndPop().getTwo();
        assertEquals(Stacks.immutable.with(), emptyStack);
        assertNotSame(Stacks.immutable.with(), emptyStack);
        assertThrows(EmptyStackException.class, emptyStack::pop);
    }

    @Override
    @Test
    public void OrderedIterable_distinct()
    {
        // TODO: Support distinct() for ImmutableArrayStack.
        assertThrows(UnsupportedOperationException.class, () -> this.newWith(3, 2, 1).distinct());
    }
}
