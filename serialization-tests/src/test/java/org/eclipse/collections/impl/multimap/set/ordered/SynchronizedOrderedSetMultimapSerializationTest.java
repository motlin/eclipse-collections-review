/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

package org.eclipse.collections.impl.multimap.set.ordered;

import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.impl.multimap.SynchronizedMultimapSerializationTestCase;

public class SynchronizedOrderedSetMultimapSerializationTest
        extends SynchronizedMultimapSerializationTestCase
{
    @Override
    protected String getSerializedForm()
    {
        return "rO0ABXNyAExvcmcuZWNsaXBzZS5jb2xsZWN0aW9ucy5pbXBsLm11bHRpbWFwLlN5bmNocm9uaXpl\n"
                + "ZE11bHRpbWFwU2VyaWFsaXphdGlvblByb3h5AAAAAAAAAAEMAAB4cHNyAEhvcmcuZWNsaXBzZS5j\n"
                + "b2xsZWN0aW9ucy5pbXBsLm11bHRpbWFwLnNldC5vcmRlcmVkLk9yZGVyZWRIYXNoU2V0TXVsdGlt\n"
                + "YXAAAAAAAAAAAQwAAHhwdwQAAAACdAABQXcEAAAAAnEAfgAEdAABQnEAfgAFdwQAAAABcQB+AAR4\n"
                + "eA==";
    }

    @Override
    protected MutableMultimap<String, String> createEmpty()
    {
        return new OrderedHashSetMultimap<String, String>().asSynchronized();
    }
}
