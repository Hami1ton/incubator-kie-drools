/**
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
package org.drools.testcoverage.functional.parser;

import java.io.File;
import java.util.Collection;

import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieUtil;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;

public class DrlParserTest extends ParserTest {

    public DrlParserTest(final File file, final KieBaseTestConfiguration kieBaseTestConfiguration) {
        super(file, kieBaseTestConfiguration);
    }

    @Parameters(name = "{index}: {0}, {1}")
    public static Collection<Object[]> getParameters() {
        return getTestParamsFromFiles(getFiles("drl"));
    }

    @Test
    public void testParserSmoke() {
        final Resource fileResource = KieServices.Factory.get().getResources().newFileSystemResource(file);
        KieUtil.getKieBuilderFromResources(kieBaseTestConfiguration, true, fileResource);
    }
}
