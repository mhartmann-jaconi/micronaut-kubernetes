/*
 * Copyright 2017-2020 original authors
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

import io.kubernetes.client.openapi.models.V1ConfigMap;
import io.kubernetes.client.openapi.models.V1ConfigMapList;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.env.PropertySource;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.discovery.config.ConfigurationClient;
import io.micronaut.discovery.event.ServiceReadyEvent;
import io.micronaut.kubernetes.KubernetesConfiguration;
import io.micronaut.kubernetes.client.informer.Informer;
import io.micronaut.kubernetes.client.reactor.CoreV1ApiReactorClient;
import io.micronaut.kubernetes.util.KubernetesUtils;
import io.micronaut.runtime.context.scope.refresh.RefreshEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Inject;

/**
 * Watches for ConfigMap changes and makes the appropriate changes to the {@link Environment} by adding or removing
 * {@link PropertySource}s.
 *
 * @author Álvaro Sánchez-Mariscal
 * @since 1.0.0
 */
@Context
@Requires(env = Environment.KUBERNETES)
@Requires(beans = CoreV1ApiReactorClient.class)
@Requires(property = ConfigurationClient.ENABLED, value = "true", defaultValue = "false")
@Requires(condition = KubernetesConfigMapWatcherCondition.class)
@Informer(apiType = V1ConfigMap.class, apiListType = V1ConfigMapList.class, resourcePlural = "configmaps", apiGroup = "", labelSelectorSupplier = KubernetesConfigMapLabelSupplier.class)
public final class KubernetesConfigMapWatcher extends AbstractKubernetesConfigWatcher<V1ConfigMap> {

    private final KubernetesConfiguration configuration;

    @Inject
    public KubernetesConfigMapWatcher(Environment environment, KubernetesConfiguration configuration, ApplicationEventPublisher<RefreshEvent> eventPublisher) {
        super(environment, eventPublisher);

        this.configuration = configuration;
    }

    @EventListener
    public void onApplicationEvent(ServiceReadyEvent event) {
        serviceStarted.set(true);
    }

    @Override
    PropertySource readAsPropertySource(V1ConfigMap configMap) {
        return KubernetesUtils.configMapAsPropertySource(configMap);
    }

    @Override
    KubernetesConfiguration.AbstractConfigConfiguration getConfig() {
        return configuration.getConfigMaps();
    }
}
