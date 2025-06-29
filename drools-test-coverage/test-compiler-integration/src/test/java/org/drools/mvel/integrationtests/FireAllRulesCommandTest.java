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
package org.drools.mvel.integrationtests;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.drools.commands.runtime.rule.FireAllRulesCommand;
import org.drools.mvel.compiler.Cheese;
import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.TestParametersUtil2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.api.KieBase;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class FireAllRulesCommandTest {

    public static Stream<KieBaseTestConfiguration> parameters() {
        return TestParametersUtil2.getKieBaseCloudConfigurations(true).stream();
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void oneRuleFiredTest(KieBaseTestConfiguration kieBaseTestConfiguration) {
        String str = "";
        str += "package org.drools.mvel.integrationtests \n";
        str += "import " + Cheese.class.getCanonicalName() + " \n";
        str += "rule StringRule \n";
        str += " when \n";
        str += " $c : Cheese() \n";
        str += " then \n";
        str += " System.out.println($c); \n";
        str += "end \n";

        StatelessKieSession ksession = getSession(kieBaseTestConfiguration, str);

        List<Command<?>> commands = new ArrayList<Command<?>>();
        commands.add(CommandFactory.newInsert(new Cheese("stilton")));
        commands.add(CommandFactory.newFireAllRules("num-rules-fired"));

        ExecutionResults results = ksession.execute(CommandFactory.newBatchExecution(commands));
        int fired = Integer.parseInt(results.getValue("num-rules-fired").toString());

        assertThat(fired).isEqualTo(1);
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void fiveRulesFiredTest(KieBaseTestConfiguration kieBaseTestConfiguration) {
        String str = "";
        str += "package org.drools.mvel.integrationtests \n";
        str += "import " + Cheese.class.getCanonicalName() + " \n";
        str += "rule StringRule \n";
        str += " when \n";
        str += " $c : Cheese() \n";
        str += " then \n";
        str += " System.out.println($c); \n";
        str += "end \n";

        StatelessKieSession ksession = getSession(kieBaseTestConfiguration, str);

        List<Command<?>> commands = new ArrayList<Command<?>>();
        commands.add(CommandFactory.newInsert(new Cheese("stilton")));
        commands.add(CommandFactory.newInsert(new Cheese("gruyere")));
        commands.add(CommandFactory.newInsert(new Cheese("cheddar")));
        commands.add(CommandFactory.newInsert(new Cheese("stinky")));
        commands.add(CommandFactory.newInsert(new Cheese("limburger")));
        commands.add(CommandFactory.newFireAllRules("num-rules-fired"));

        ExecutionResults results = ksession.execute(CommandFactory.newBatchExecution(commands));
        int fired = Integer.parseInt(results.getValue("num-rules-fired").toString());

        assertThat(fired).isEqualTo(5);
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void zeroRulesFiredTest(KieBaseTestConfiguration kieBaseTestConfiguration) {
        String str = "";
        str += "package org.drools.mvel.integrationtests \n";
        str += "import " + Cheese.class.getCanonicalName() + " \n";
        str += "rule StringRule \n";
        str += " when \n";
        str += " $c : Cheese() \n";
        str += " then \n";
        str += " System.out.println($c); \n";
        str += "end \n";

        StatelessKieSession ksession = getSession(kieBaseTestConfiguration, str);

        List<Command<?>> commands = new ArrayList<Command<?>>();
        commands.add(CommandFactory.newInsert("not cheese"));
        commands.add(CommandFactory.newFireAllRules("num-rules-fired"));

        ExecutionResults results = ksession.execute(CommandFactory.newBatchExecution(commands));
        int fired = Integer.parseInt(results.getValue("num-rules-fired").toString());

        assertThat(fired).isEqualTo(0);
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void oneRuleFiredWithDefinedMaxTest(KieBaseTestConfiguration kieBaseTestConfiguration) {
        String str = "";
        str += "package org.drools.mvel.integrationtests \n";
        str += "import " + Cheese.class.getCanonicalName() + " \n";
        str += "rule StringRule \n";
        str += " when \n";
        str += " $c : Cheese() \n";
        str += " then \n";
        str += " System.out.println($c); \n";
        str += "end \n";

        StatelessKieSession ksession = getSession(kieBaseTestConfiguration, str);

        List<Command<?>> commands = new ArrayList<Command<?>>();
        commands.add(CommandFactory.newInsert(new Cheese("stilton")));
        FireAllRulesCommand farc = (FireAllRulesCommand) CommandFactory.newFireAllRules(10);
        farc.setOutIdentifier("num-rules-fired");
        commands.add(farc);

        ExecutionResults results = ksession.execute(CommandFactory.newBatchExecution(commands));
        int fired = Integer.parseInt(results.getValue("num-rules-fired").toString());

        assertThat(fired).isEqualTo(1);
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void infiniteLoopTerminatesAtMaxTest(KieBaseTestConfiguration kieBaseTestConfiguration) {
        String str = "";
        str += "package org.drools.mvel.integrationtests \n";
        str += "import " + Cheese.class.getCanonicalName() + " \n";
        str += "rule StringRule \n";
        str += " when \n";
        str += " $c : Cheese() \n";
        str += " then \n";
        str += " update($c); \n";
        str += "end \n";

        StatelessKieSession ksession = getSession(kieBaseTestConfiguration, str);

        List<Command<?>> commands = new ArrayList<Command<?>>();
        commands.add(CommandFactory.newInsert(new Cheese("stilton")));
        FireAllRulesCommand farc = (FireAllRulesCommand) CommandFactory.newFireAllRules(10);
        farc.setOutIdentifier("num-rules-fired");
        commands.add(farc);

        ExecutionResults results = ksession.execute(CommandFactory.newBatchExecution(commands));
        int fired = Integer.parseInt(results.getValue("num-rules-fired").toString());

        assertThat(fired).isEqualTo(10);
    }

    private StatelessKieSession getSession(KieBaseTestConfiguration kieBaseTestConfiguration, String drl) {
        KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("test", kieBaseTestConfiguration, drl);
        return kbase.newStatelessKieSession();
    }

}
