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
package org.kie.drl.api.identifiers.data;

import org.kie.efesto.common.api.identifiers.LocalId;
import org.kie.efesto.common.api.identifiers.LocalUriId;

public class DataId extends LocalUriId implements LocalId {

    public static final String PREFIX = "data";
    private final DataSourceId parent;
    private final String dataId;

    public DataId(DataSourceId parent, String dataId) {
        super(parent.asLocalUri().append(PREFIX).append(dataId));
        this.parent = parent;
        this.dataId = dataId;
    }

    public DataSourceId dataSourceId() {
        return parent;
    }

    public String dataId() {
        return dataId;
    }
}
