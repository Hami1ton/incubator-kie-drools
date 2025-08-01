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
package org.kie.dmn.backend.marshalling.v1_3.extensions;

import javax.xml.namespace.QName;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.QNameMap;
import java.util.Collections;
import java.util.Set;
import org.kie.dmn.api.marshalling.DMNExtensionRegister;

public class TrisoExtensionRegister implements DMNExtensionRegister {

    @Override
    public Set<String> allowedModelPackages() {
        return Collections.singleton("org.kie.dmn.trisotech.model.**");
    }

    @Override
    public void registerExtensionConverters(XStream xStream) {
        xStream.processAnnotations(ProjectCharter.class);
    }

    @Override
    public void beforeMarshal(Object o, QNameMap qmap) {
        qmap.registerMapping(new QName("http://www.trisotech.com/2015/triso/modeling", "ProjectCharter", "triso"), "ProjectCharter");
        qmap.registerMapping(new QName("http://www.trisotech.com/2015/triso/modeling", "projectGoals", "triso"), "projectGoals");
        qmap.registerMapping(new QName("http://www.trisotech.com/2015/triso/modeling", "projectChallenges", "triso"), "projectChallenges");
        qmap.registerMapping(new QName("http://www.trisotech.com/2015/triso/modeling", "projectStakeholders", "triso"), "projectStakeholders");
    }

}
