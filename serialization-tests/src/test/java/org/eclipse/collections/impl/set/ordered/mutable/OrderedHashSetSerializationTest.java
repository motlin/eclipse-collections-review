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
import org.eclipse.collections.impl.test.Verify;
import org.junit.jupiter.api.Test;

public class OrderedHashSetSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAD9vcmcuZWNsaXBzZS5jb2xsZWN0aW9ucy5pbXBsLnNldC5vcmRlcmVkLm11dGFibGUu\n"
                        + "T3JkZXJlZEhhc2hTZXQAAAAAAAAAAQwAAHhwdwQAAAAAeA==",
                OrderedHashSet.newSet());
    }

    @Test
    public void elementPreservation()
    {
        MutableOrderedSet<String> set = new OrderedHashSet<>();
        set.add("Charlie");
        set.add("Alpha");
        set.add("Bravo");

        Verify.assertPostSerializedEqualsAndHashCode(set);
    }
}
