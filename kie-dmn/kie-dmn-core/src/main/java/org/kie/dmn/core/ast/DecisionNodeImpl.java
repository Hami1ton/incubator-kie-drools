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
package org.kie.dmn.core.ast;

import java.util.Objects;

import org.kie.dmn.api.core.DMNType;
import org.kie.dmn.api.core.ast.DecisionNode;
import org.kie.dmn.core.api.DMNExpressionEvaluator;
import org.kie.dmn.model.api.Decision;

public class DecisionNodeImpl
        extends DMNBaseNode implements DecisionNode {

    private Decision               decision;
    private DMNExpressionEvaluator evaluator;
    private DMNType                resultType;

    public DecisionNodeImpl() {
    }

    public DecisionNodeImpl(Decision decision) {
        this( decision, null );
    }

    public DecisionNodeImpl(Decision decision, DMNType resultType) {
        super( decision );
        this.decision = decision;
        this.resultType = resultType;
    }

    @Override
    public Decision getDecision() {
        return decision;
    }

    public DMNExpressionEvaluator getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(DMNExpressionEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public DMNType getResultType() {
        return resultType;
    }

    public void setResultType(DMNType resultType) {
        this.resultType = resultType;
    }

    // since DROOLS-1767 defined hashCode explicitly, this must be explicit too; there is no change in behaviour since DROOLS-1767, just explicit identity.
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public DMNType getType() {
        return getResultType();
    }

}
