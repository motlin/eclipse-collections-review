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
import org.eclipse.collections.impl.multimap.ImmutableMultimapSerializationTestCase;

public class ImmutableOrderedSetMultimapSerializationTest extends ImmutableMultimapSerializationTestCase
{
    @Override
    protected MutableMultimap<String, String> createEmpty()
    {
        return new OrderedHashSetMultimap<>();
    }

    @Override
    protected String getSerializedForm()
    {
        return "rO0ABXNyAH9vcmcuZWNsaXBzZS5jb2xsZWN0aW9ucy5pbXBsLm11bHRpbWFwLnNldC5vcmRlcmVk\n"
                + "LkltbXV0YWJsZU9yZGVyZWRTZXRNdWx0aW1hcEltcGwkSW1tdXRhYmxlT3JkZXJlZFNldE11bHRp\n"
                + "bWFwU2VyaWFsaXphdGlvblByb3h5AAAAAAAAAAEMAAB4cHcEAAAAAnQAAUF3BAAAAAJxAH4AAnQA\n"
                + "AUJxAH4AA3cEAAAAAXEAfgACeA==";
    }
}
