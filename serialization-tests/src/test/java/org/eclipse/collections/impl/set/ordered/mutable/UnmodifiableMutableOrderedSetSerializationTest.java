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

import org.eclipse.collections.api.factory.OrderedSets;
import org.eclipse.collections.impl.test.Verify;
import org.junit.jupiter.api.Test;

public class UnmodifiableMutableOrderedSetSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                "rO0ABXNyAFhvcmcuZWNsaXBzZS5jb2xsZWN0aW9ucy5pbXBsLmNvbGxlY3Rpb24ubXV0YWJsZS5V\n"
                        + "bm1vZGlmaWFibGVDb2xsZWN0aW9uU2VyaWFsaXphdGlvblByb3h5AAAAAAAAAAEMAAB4cHNyAD9v\n"
                        + "cmcuZWNsaXBzZS5jb2xsZWN0aW9ucy5pbXBsLnNldC5vcmRlcmVkLm11dGFibGUuT3JkZXJlZEhh\n"
                        + "c2hTZXQAAAAAAAAAAQwAAHhwdwQAAAAAeHg=",
                UnmodifiableMutableOrderedSet.of(OrderedSets.mutable.of()));
    }
}
