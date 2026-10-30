/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.set.ordered.immutable;

import java.util.LinkedHashSet;

import org.eclipse.collections.impl.test.Verify;
import org.junit.jupiter.api.Test;

public class ImmutableOrderedSetAdapterSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyAE1vcmcuZWNsaXBzZS5jb2xsZWN0aW9ucy5pbXBsLnNldC5vcmRlcmVkLmltbXV0YWJs\n"
                        + "ZS5JbW11dGFibGVPcmRlcmVkU2V0QWRhcHRlcgAAAAAAAAABAgABTAAIZGVsZWdhdGV0ADtMb3Jn\n"
                        + "L2VjbGlwc2UvY29sbGVjdGlvbnMvYXBpL3NldC9vcmRlcmVkL011dGFibGVPcmRlcmVkU2V0O3hw\n"
                        + "c3IAQm9yZy5lY2xpcHNlLmNvbGxlY3Rpb25zLmltcGwuc2V0Lm9yZGVyZWQubXV0YWJsZS5PcmRl\n"
                        + "cmVkU2V0QWRhcHRlcgAAAAAAAAABAgABTAAIZGVsZWdhdGV0AA9MamF2YS91dGlsL1NldDt4cHNy\n"
                        + "ACVqYXZhLnV0aWwuQ29sbGVjdGlvbnMkVW5tb2RpZmlhYmxlU2V0gB2S0Y+bgFUCAAB4cgAsamF2\n"
                        + "YS51dGlsLkNvbGxlY3Rpb25zJFVubW9kaWZpYWJsZUNvbGxlY3Rpb24ZQgCAy173HgIAAUwAAWN0\n"
                        + "ABZMamF2YS91dGlsL0NvbGxlY3Rpb247eHBzcgAXamF2YS51dGlsLkxpbmtlZEhhc2hTZXTYbNda\n"
                        + "ld0qHgIAAHhyABFqYXZhLnV0aWwuSGFzaFNldLpEhZWWuLc0AwAAeHB3DAAAABA/QAAAAAAAAHg=\n",
                new ImmutableOrderedSetAdapter<>(new LinkedHashSet<Integer>()));
    }
}
