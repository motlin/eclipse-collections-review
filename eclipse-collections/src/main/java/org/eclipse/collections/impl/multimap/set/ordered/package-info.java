/*
 * Copyright (c) 2026 Goldman Sachs and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 */

/**
 * This package contains implementations of the {@link org.eclipse.collections.api.multimap.orderedset.OrderedSetMultimap} interface.
 * <p>
 *     An {@link org.eclipse.collections.api.multimap.orderedset.OrderedSetMultimap} is a type of {@code Multimap} that uses an insertion-ordered set as its underlying store for the multiple values of a given key.
 * <p>
 *     This package contains the following implementations:
 * <ul>
 *     <li>
 *          {@link org.eclipse.collections.impl.multimap.set.ordered.OrderedHashSetMultimap} - a {@code MutableOrderedSetMultimap} which uses an {@link org.eclipse.collections.impl.set.ordered.mutable.OrderedHashSet} as its underlying store for the multiple values of a given key.
 *     </li>
 *     <li>
 *          {@link org.eclipse.collections.impl.multimap.set.ordered.ImmutableOrderedSetMultimapImpl} - the default {@code ImmutableOrderedSetMultimap} implementation.
 *     </li>
 * </ul>
 */
package org.eclipse.collections.impl.multimap.set.ordered;
