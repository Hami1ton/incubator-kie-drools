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
package org.kie.pmml.compiler.commons.factories;

import java.util.List;

import org.dmg.pmml.NormDiscrete;
import org.kie.pmml.commons.model.KiePMMLExtension;
import org.kie.pmml.commons.model.expressions.KiePMMLNormDiscrete;

import static org.kie.pmml.compiler.commons.factories.KiePMMLExtensionInstanceFactory.getKiePMMLExtensions;

/**
 * Class meant to provide <i>helper</i> method to retrieve <code>KiePMMLNormDiscrete</code> instance
 * out of <code>NormDiscrete</code>s
 */
public class KiePMMLNormDiscreteInstanceFactory {

    private KiePMMLNormDiscreteInstanceFactory() {
        // Avoid instantiation
    }

    static KiePMMLNormDiscrete getKiePMMLNormDiscrete(final NormDiscrete normDiscrete) {
        List<KiePMMLExtension> extensions = getKiePMMLExtensions(normDiscrete.getExtensions());
        return new KiePMMLNormDiscrete(normDiscrete.getField(),
                                       extensions,
                                       normDiscrete.getValue().toString(),
                                       normDiscrete.getMapMissingTo());
    }
}
