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
package org.drools.mvel.integrationtests.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.drools.commands.runtime.BatchExecutionCommandImpl;
import org.drools.mvel.compiler.Cheese;
import org.drools.mvel.compiler.Cheesery;
import org.drools.testcoverage.common.util.KieBaseTestConfiguration;
import org.drools.testcoverage.common.util.KieBaseUtil;
import org.drools.testcoverage.common.util.KieUtil;
import org.drools.testcoverage.common.util.TestParametersUtil2;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.api.KieBase;
import org.kie.api.builder.KieModule;
import org.kie.api.command.Command;
import org.kie.api.command.ExecutableCommand;
import org.kie.api.conf.SequentialOption;
import org.kie.api.io.Resource;
import org.kie.api.runtime.Channel;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.command.CommandFactory;
import org.kie.internal.io.ResourceFactory;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

public class StatelessSessionTest {

    public static Stream<KieBaseTestConfiguration> parameters() {
        return TestParametersUtil2.getKieBaseCloudConfigurations(true).stream();
    }

    final List list = new ArrayList();
    final Cheesery cheesery = new Cheesery();

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testSingleObjectAssert(KieBaseTestConfiguration kieBaseTestConfiguration) throws Exception {
        final StatelessKieSession session = getSession2(kieBaseTestConfiguration, "statelessSessionTest.drl" );

        final Cheese stilton = new Cheese( "stilton",
                                           5 );

        session.execute( stilton );

        assertThat(list.get(0)).isEqualTo("stilton");
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testArrayObjectAssert(KieBaseTestConfiguration kieBaseTestConfiguration) throws Exception {
        final StatelessKieSession session = getSession2(kieBaseTestConfiguration, "statelessSessionTest.drl" );

        final Cheese stilton = new Cheese( "stilton",
                                           5 );

        session.execute( Arrays.asList(stilton));

        assertThat(list.get(0)).isEqualTo("stilton");
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testCollectionObjectAssert(KieBaseTestConfiguration kieBaseTestConfiguration) throws Exception {
        final StatelessKieSession session = getSession2(kieBaseTestConfiguration, "statelessSessionTest.drl" );

        final Cheese stilton = new Cheese( "stilton",
                                           5 );

        final List collection = new ArrayList();
        collection.add( stilton );
        session.execute( collection );

        assertThat(list.get(0)).isEqualTo("stilton");
    }
    
    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testInsertObject(KieBaseTestConfiguration kieBaseTestConfiguration) throws Exception {
        String str = "";
        str += "package org.kie \n";
        str += "import org.drools.mvel.compiler.Cheese \n";
        str += "rule rule1 \n";
        str += "  when \n";
        str += "    $c : Cheese() \n";
        str += " \n";
        str += "  then \n";
        str += "    $c.setPrice( 30 ); \n";
        str += "end\n";
        
        Cheese stilton = new Cheese( "stilton", 5 );
        
        final StatelessKieSession ksession = getSession2( kieBaseTestConfiguration, ResourceFactory.newByteArrayResource( str.getBytes() ) );
        final ExecutableCommand cmd = (ExecutableCommand) CommandFactory.newInsert( stilton, "outStilton" );
        final BatchExecutionCommandImpl batch = new BatchExecutionCommandImpl(  Arrays.asList(cmd) );
        
        final ExecutionResults result = ksession.execute(batch);
        stilton = ( Cheese ) result.getValue( "outStilton" );
        assertThat(stilton.getPrice()).isEqualTo(30);
    }
    
    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testSetGlobal(KieBaseTestConfiguration kieBaseTestConfiguration) throws Exception {
        String str = "";
        str += "package org.kie \n";
        str += "import org.drools.mvel.compiler.Cheese \n";
        str += "global java.util.List list1 \n";
        str += "global java.util.List list2 \n";
        str += "global java.util.List list3 \n";
        str += "rule rule1 \n";
        str += "  when \n";
        str += "    $c : Cheese() \n";
        str += " \n";
        str += "  then \n";
        str += "    $c.setPrice( 30 ); \n";
        str += "    list1.add( $c ); \n";
        str += "    list2.add( $c ); \n";
        str += "    list3.add( $c ); \n";
        str += "end\n";
        
        final Cheese stilton = new Cheese( "stilton", 5 );
        final List list1 = new ArrayList();
        List list2 = new ArrayList();
        List list3 = new ArrayList();
        
        final StatelessKieSession ksession = getSession2(kieBaseTestConfiguration, ResourceFactory.newByteArrayResource( str.getBytes() ) );
        final Command setGlobal1 = CommandFactory.newSetGlobal( "list1", list1 );
        final Command setGlobal2 = CommandFactory.newSetGlobal( "list2", list2, true );
        final Command setGlobal3 = CommandFactory.newSetGlobal( "list3", list3, "outList3" );
        final Command insert = CommandFactory.newInsert( stilton  );
        
        final List cmds = new ArrayList();
        cmds.add( setGlobal1 );
        cmds.add( setGlobal2 );
        cmds.add( setGlobal3 );
        cmds.add(  insert );
        
        final ExecutionResults result = ksession.execute(CommandFactory.newBatchExecution(cmds));

        assertThat(stilton.getPrice()).isEqualTo(30);

        assertThat(result.getValue("list1")).isNull();
        
        list2 = ( List ) result.getValue( "list2" );
        assertThat(list2.size()).isEqualTo(1);
        assertThat(list2.get(0)).isSameAs(stilton);
        
          
        
        list3 = ( List ) result.getValue( "outList3" );
        assertThat(list3.size()).isEqualTo(1);
        assertThat(list3.get(0)).isSameAs(stilton);
    }
    
    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testQuery(KieBaseTestConfiguration kieBaseTestConfiguration) throws Exception {
        String str = "";
        str += "package org.kie.test  \n";
        str += "import org.drools.mvel.compiler.Cheese \n";
        str += "query cheeses \n";
        str += "    stilton : Cheese(type == 'stilton') \n";
        str += "    cheddar : Cheese(type == 'cheddar', price == stilton.price) \n";
        str += "end\n";

        KieBase kbase = KieBaseUtil.getKieBaseFromKieModuleFromDrl("test", kieBaseTestConfiguration, str);

        final StatelessKieSession ksession = kbase.newStatelessKieSession();
        final Cheese stilton1 = new Cheese( "stilton", 1);
        final Cheese cheddar1 = new Cheese( "cheddar", 1);
        final Cheese stilton2 = new Cheese( "stilton", 2);
        final Cheese cheddar2 = new Cheese( "cheddar", 2);
        final Cheese stilton3 = new Cheese( "stilton", 3);
        final Cheese cheddar3 = new Cheese( "cheddar", 3);
        
        final Set set = new HashSet();
        List list = new ArrayList();
        list.add(stilton1);
        list.add(cheddar1);
        set.add( list );
        
        list = new ArrayList();
        list.add(stilton2);
        list.add(cheddar2);
        set.add( list );
        
        list = new ArrayList();
        list.add(stilton3);
        list.add(cheddar3);
        set.add( list );
        
        final List<Command> cmds = new ArrayList<Command>();
        cmds.add( CommandFactory.newInsert( stilton1 ) );
        cmds.add( CommandFactory.newInsert( stilton2 ) );
        cmds.add( CommandFactory.newInsert( stilton3 ) );
        cmds.add( CommandFactory.newInsert( cheddar1 ) );
        cmds.add( CommandFactory.newInsert( cheddar2 ) );
        cmds.add( CommandFactory.newInsert( cheddar3 ) );
        
        cmds.add(  CommandFactory.newQuery( "cheeses", "cheeses" ) );
        
        final ExecutionResults batchResult = ksession.execute(CommandFactory.newBatchExecution(cmds));
        
        final org.kie.api.runtime.rule.QueryResults results = ( org.kie.api.runtime.rule.QueryResults) batchResult.getValue( "cheeses" );
        assertThat(results.size()).isEqualTo(3);
        assertThat(results.getIdentifiers().length).isEqualTo(2);
        final Set newSet = new HashSet();
        for ( final org.kie.api.runtime.rule.QueryResultsRow result : results ) {
            list = new ArrayList();
            list.add( result.get( "stilton" ) );
            list.add( result.get( "cheddar" ));
            newSet.add( list );
        }
        assertThat(newSet).isEqualTo(set);
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testNotInStatelessSession(KieBaseTestConfiguration kieBaseTestConfiguration) throws Exception {
        final KieModule kieModule = KieUtil.getKieModuleFromClasspathResources("test", getClass(), kieBaseTestConfiguration, "test_NotInStatelessSession.drl");
        final KieBase kbase = KieBaseUtil.newKieBaseFromKieModuleWithAdditionalOptions(kieModule, kieBaseTestConfiguration, SequentialOption.YES);
        final StatelessKieSession session = kbase.newStatelessKieSession();

        final List list = new ArrayList();
        session.setGlobal("list", list);
        session.execute("not integer");
        assertThat(list.get(0)).isEqualTo("not integer");
    }

    @ParameterizedTest(name = "KieBase type={0}")
    @MethodSource("parameters")
    public void testChannels(KieBaseTestConfiguration kieBaseTestConfiguration) throws Exception {
        String str = "";
        str += "package org.kie \n";
        str += "import org.drools.mvel.compiler.Cheese \n";
        str += "rule rule1 \n";
        str += "  when \n";
        str += "    $c : Cheese() \n";
        str += " \n";
        str += "  then \n";
        str += "    channels[\"x\"].send( $c ); \n";
        str += "end\n";

        final Cheese stilton = new Cheese("stilton", 5);
        final Channel channel = Mockito.mock(Channel.class);

        final StatelessKieSession ksession = getSession2(kieBaseTestConfiguration, ResourceFactory.newByteArrayResource(str.getBytes()));
        ksession.registerChannel("x", channel);

        assertThat(ksession.getChannels().size()).isEqualTo(1);
        assertThat(ksession.getChannels().get("x")).isEqualTo(channel);

        ksession.execute(stilton);

        Mockito.verify(channel).send(stilton);

        ksession.unregisterChannel("x");

        assertThat(ksession.getChannels().size()).isEqualTo(0);
        assertThat(ksession.getChannels().get("x")).isNull();
    }

    private StatelessKieSession getSession2(KieBaseTestConfiguration kieBaseTestConfiguration, final String fileName) throws Exception {
        return getSession2(kieBaseTestConfiguration, ResourceFactory.newClassPathResource( fileName, getClass() ) );
    }
        
    private StatelessKieSession getSession2(KieBaseTestConfiguration kieBaseTestConfiguration, final Resource resource) throws Exception {
        resource.setTargetPath("r1.drl");
        KieBase kbase = KieBaseUtil.getKieBaseFromResources(kieBaseTestConfiguration, resource);
        final StatelessKieSession session = kbase.newStatelessKieSession();

        session.setGlobal( "list",
                           this.list );
        session.setGlobal( "cheesery",
                           this.cheesery );
        return session;
    }
}
