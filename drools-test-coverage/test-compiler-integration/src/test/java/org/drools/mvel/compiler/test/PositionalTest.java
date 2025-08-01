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
package org.drools.mvel.compiler.test;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.TestParametersUtil2;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;

import static org.assertj.core.api.Assertions.assertThat;

public class PositionalTest {

	public static Stream<KieBaseTestConfiguration> parameters() {
     // TODO: EM failed with ttestPositional. File JIRAs
        return TestParametersUtil2.getKieBaseCloudConfigurations(true).stream();
    }

    @ParameterizedTest(name = "KieBase type={0}")
	@MethodSource("parameters")
    public void testPositional(KieBaseTestConfiguration kieBaseTestConfiguration) {

        String drl =
                "import " + Man.class.getCanonicalName() + ";\n" +
                "\n" +
                "global java.util.List list;" +
                "\n" +
                "rule \"To be or not to be\"\n" +
                "when\n" +
                "    $m : Man( \"john\" , 18 , $w ; )\n" +
                "then\n" +
                "    list.add($w); " +
                "end";

        KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("test", kieBaseTestConfiguration, drl);
        KieSession kSession = kbase.newKieSession();

        java.util.ArrayList list = new ArrayList();
        kSession.setGlobal( "list",
                            list );

        kSession.insert( new Man( "john", 18, 84.2 ) );
        kSession.insert( new Man( "john", 19, 85.2 ) );
        kSession.fireAllRules();

        assertThat(list.contains(84.2)).isTrue();
        assertThat(list.contains(85.2)).isFalse();
    }


    @ParameterizedTest(name = "KieBase type={0}")
	@MethodSource("parameters")
    @Timeout(5000)
    public void testPositionalWithNull(KieBaseTestConfiguration kieBaseTestConfiguration) {
        // DROOLS-51
        String str =
                "declare Bean\n" +
                "  value : String\n" +
                "end\n" +
                "\n" +
                "rule \"Init\"\n" +
                "when\n" +
                "then\n" +
                "  insert( new Bean( null ) );\n" +
                "  insert( \"test\" );\n" +
                "end\n" +
                "\n" +
                "rule \"Bind\"\n" +
                "when\n" +
                "  $s : String(  )\n" +
                "  $b : Bean( null ; )\n" +
                "then\n" +
                "  modify ( $b ) { setValue( $s ); }\n" +
                "end";

        KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("test", kieBaseTestConfiguration, str);
        KieSession ksession = kbase.newKieSession();
        assertThat(ksession.fireAllRules()).isEqualTo(2);
    }
}
