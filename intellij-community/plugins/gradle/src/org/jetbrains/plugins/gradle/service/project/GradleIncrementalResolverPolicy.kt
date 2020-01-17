// Copyright 2000-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package org.jetbrains.plugins.gradle.service.project

import com.intellij.openapi.externalSystem.importing.IncrementalDataResolverPolicy
import org.jetbrains.annotations.ApiStatus
import java.util.function.Predicate

@ApiStatus.Experimental
class GradleIncrementalResolverPolicy(val extensionsFilter: Predicate<GradleProjectResolverExtension?>) : IncrementalDataResolverPolicy