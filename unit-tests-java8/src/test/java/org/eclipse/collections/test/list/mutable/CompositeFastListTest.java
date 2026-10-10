/*
 * Copyright (c) 2022 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.test.list.mutable;

import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.CompositeFastList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.jupiter.api.Test;

import static org.eclipse.collections.test.IterableTestCase.assertIterablesEqual;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class CompositeFastListTest implements MutableListTestCase
{
    @SafeVarargs
    @Override
    public final <T> MutableList<T> newWith(T... elements)
    {
        RichIterable<RichIterable<T>> chunks = FastList.wrapCopy(elements).chunk(3);
        CompositeFastList<T> result = new CompositeFastList<>();
        // Empty inner lists before, between, and after the chunks make every contract test skip over them
        result.addComposited(FastList.newList());
        for (RichIterable<T> chunk : chunks)
        {
            result.addComposited(FastList.newList(chunk));
            result.addComposited(FastList.newList());
        }
        return result;
    }

    @Override
    @Test
    public void MutableList_clone()
    {
        MutableListTestCase.super.MutableList_clone();

        // A composite with no inner lists clones to an independent empty composite
        MutableList<Integer> empty = new CompositeFastList<>();
        MutableList<Integer> clone = empty.clone();
        assertNotSame(empty, clone);
        assertIterablesEqual(this.newWith(), clone);
        clone.add(1);
        assertIterablesEqual(this.newWith(), empty);
        assertIterablesEqual(this.newWith(1), clone);
    }
}
