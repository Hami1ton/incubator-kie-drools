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
package org.drools.drlonyaml.model;

import java.util.Objects;

import org.drools.drl.ast.descr.ImportDescr;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Import {
    @JsonValue
    private String target;
    
    @JsonCreator
    public Import(final String target) {
        this.target = target;
    }
    
    private Import() {
        // no-arg.
    }

    public static Import from(ImportDescr i) {
        Objects.requireNonNull(i);
        Import result = new Import();
        result.target = i.getTarget();
        return result;
    }

    public String getTarget() {
        return target;
    }
}
