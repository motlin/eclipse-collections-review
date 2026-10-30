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

import java.util.LinkedHashSet;

import org.eclipse.collections.impl.test.Verify;
import org.junit.jupiter.api.Test;

public class OrderedSetAdapterSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAEJvcmcuZWNsaXBzZS5jb2xsZWN0aW9ucy5pbXBsLnNldC5vcmRlcmVkLm11dGFibGUu\n"
                        + "T3JkZXJlZFNldEFkYXB0ZXIAAAAAAAAAAQIAAUwACGRlbGVnYXRldAAPTGphdmEvdXRpbC9TZXQ7\n"
                        + "eHBzcgAXamF2YS51dGlsLkxpbmtlZEhhc2hTZXTYbNdald0qHgIAAHhyABFqYXZhLnV0aWwuSGFz\n"
                        + "aFNldLpEhZWWuLc0AwAAeHB3DAAAABA/QAAAAAAAAHg=",
                OrderedSetAdapter.adapt(new LinkedHashSet<Integer>()));
    }
}
