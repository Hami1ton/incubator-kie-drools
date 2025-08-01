/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.efesto.common.api.utils;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CollectionUtils {

    private CollectionUtils() { }

    public static <T, X extends RuntimeException> Optional<T> findAtMostOne(Iterable<T> collection, BiFunction<T, T, X> multipleValuesExceptionSupplier) {
        T result = null;
        for (T t : collection) {
            if (result == null) {
                result = t;
            } else {
                throw multipleValuesExceptionSupplier.apply(result, t);
            }
        }
        return Optional.ofNullable(result);
    }

    public static <T, X extends RuntimeException> Optional<T> findAtMostOne(Iterable<T> collection, Predicate<T> filter, BiFunction<T, T, X> multipleValuesExceptionSupplier) {
        T result = null;
        for (T t : collection) {
            if (filter.test(t)) {
                if (result == null) {
                    result = t;
                } else {
                    throw multipleValuesExceptionSupplier.apply(result, t);
                }
            }
        }
        return Optional.ofNullable(result);
    }

    public static <T, X extends RuntimeException> T findExactlyOne(Iterable<T> collection, Predicate<T> filter, BiFunction<T, T, X> multipleValuesExceptionSupplier, Supplier<X> missingValueExceptionSupplier) {
        return findAtMostOne(collection, filter, multipleValuesExceptionSupplier).orElseThrow(missingValueExceptionSupplier);
    }
}
