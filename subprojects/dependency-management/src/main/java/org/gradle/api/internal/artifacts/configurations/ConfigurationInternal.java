/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.internal.artifacts.configurations;

import org.gradle.api.Action;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.ExcludeRule;
import org.gradle.api.internal.artifacts.ResolveContext;
import org.gradle.api.internal.artifacts.transform.ExtraExecutionGraphDependenciesResolverFactory;
import org.gradle.api.internal.attributes.AttributeContainerInternal;
import org.gradle.util.Path;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public interface ConfigurationInternal extends ResolveContext, Configuration, DependencyMetaDataProvider {
    enum InternalState {
        UNRESOLVED,
        BUILD_DEPENDENCIES_RESOLVED,
        GRAPH_RESOLVED,
        ARTIFACTS_RESOLVED}

    @Override
    ResolutionStrategyInternal getResolutionStrategy();

    @Override
    AttributeContainerInternal getAttributes();

    String getPath();

    Path getIdentityPath();

    /**
     * Runs any registered dependency actions for this Configuration, and any parent Configuration.
     * Actions may mutate the dependency set for this configuration.
     * After execution, all actions are de-registered, so execution will only occur once.
     */
    void runDependencyActions();

    void markAsObserved(InternalState requestedState);

    void addMutationValidator(MutationValidator validator);

    void removeMutationValidator(MutationValidator validator);

    /**
     * Converts this configuration to an {@link OutgoingVariant} view. The view may not necessarily be immutable.
     */
    OutgoingVariant convertToOutgoingVariant();

    /**
     * Registers an action to execute before locking for further mutation.
     */
    void beforeLocking(Action<? super ConfigurationInternal> action);

    void preventFromFurtherMutation();

    /**
     * Gets the complete set of exclude rules including those contributed by
     * superconfigurations.
     */
    Set<ExcludeRule> getAllExcludeRules();

    ExtraExecutionGraphDependenciesResolverFactory getDependenciesResolver();

    /**
     * @return alternative configurations that can be used to declare dependencies instead of this configuration. Returns 'null' if this configuration is not deprecated.
     */
    @Nullable
    List<String> getDeclarationAlternatives();

    /**
     * @return alternatives configurations that can be used to consume a component instead of consuming this configuration. Returns 'null' if this configuration is not deprecated for consumption.
     */
    @Nullable
    List<String> getConsumptionAlternatives();

    /**
     * @return true if this configuration is deprecated for removal
     */
    boolean isDeprecated();

    /**
     * @return true if the resolvability of this configuration is deprecated
     */
    boolean isDeprecatedForResolving();


    /**
     * Allows plugins to deprecate a configuration that will be removed in the next major Gradle version.
     *
     * @param alternativesForDeclaring alternative configurations that can be used to declare dependencies
     * @return this configuration
     */
    Configuration deprecate(String... alternativesForDeclaring);

    /**
     * Allows plugins to deprecate the consumability property (canBeConsumed() == true) of a configuration that will be changed in the next major Gradle version.
     *
     * @param alternativesForConsumption alternative configurations that can be used for consumption
     * @return this configuration
     */
    Configuration deprecateForConsuming(String... alternativesForConsumption);

    /**
     * Allows plugins to deprecate the resolvability property (canBeResolved() == true) of a configuration that will be changed in the next major Gradle version.
     *
     * @param alternativesForResolving alternative configurations that can be used for dependency resolution
     * @return this configuration
     */
    Configuration deprecateForResolving(String... alternativesForResolving);
}
