/*
 * Copyright 2017-2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.kubernetes.configuration;

import io.micronaut.context.condition.ConditionContext;
import io.micronaut.core.annotation.Internal;
import io.micronaut.kubernetes.KubernetesConfiguration;

/**
 * Condition evaluates when the {@link KubernetesConfigMapWatcherCondition} is enabled.
 *
 * @author Pavol Gressa
 * @since 3.1
 */
@Internal
public class KubernetesConfigMapWatcherCondition extends AbstractKubernetesConfigWatcherCondition {
    @Override
    KubernetesConfiguration.AbstractConfigConfiguration getConfig(ConditionContext context) {
        return context.getBean(KubernetesConfiguration.KubernetesConfigMapsConfiguration.class);
    }
}
