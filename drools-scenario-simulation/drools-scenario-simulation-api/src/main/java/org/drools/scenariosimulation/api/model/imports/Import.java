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
package org.drools.scenariosimulation.api.model.imports;

public class Import {

    private String type;

    public Import() {

    }

    public Import(String t) {
        this.type = t;
    }

    public Import(Class<?> clazz) {
        this(clazz.getName());
    }

    public String getType() {
        return this.type;
    }   

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        // Must do instanceof check because there is a subtype of this class in drools-workbench-models-datamodel-api
        if (!(o instanceof Import)) {
            return false;
        }

        Import anImport = (Import) o;

        if (type != null ? !type.equals(anImport.type) : anImport.type != null) {
            return false;
        }

        return true;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }
}
