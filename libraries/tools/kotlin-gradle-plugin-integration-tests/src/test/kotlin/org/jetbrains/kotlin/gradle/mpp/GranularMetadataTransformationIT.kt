/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.mpp

import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.testbase.*
import org.jetbrains.kotlin.gradle.testbase.BuildOptions.ConfigurationCacheValue
import org.jetbrains.kotlin.test.TestMetadata
import org.junit.jupiter.api.DisplayName

@MppGradlePluginTests
@DisplayName("Tests for logic around granular metadata transformation")
class GranularMetadataTransformationIT : KGPBaseTest() {
    @DisplayName("KT-71328: no tasks instantiated at execution time during CInterop GMT")
    @TestMetadata("kt-71328")
    @GradleTest
    fun testNoTasksInstantiatedAtExecutionTimeCinteropGmt(gradleVersion: GradleVersion) {
        // configuration cache may hide the problem,
        // especially from Gradle 8.0 as it started to serialize the state even before the first execution
        // so disabling it in this test is mandatory
        val buildOptions = defaultBuildOptions.copy(configurationCache = ConfigurationCacheValue.DISABLED)
        project("kt-71328", gradleVersion, buildOptions = buildOptions) {
            if (gradleVersion < GradleVersion.version("8.4")) {
                build(":transformNativeMainCInteropDependenciesMetadata")
            } else {
                // fixme: KT-71764
                buildAndFail(":transformNativeMainCInteropDependenciesMetadata") {
                    assertOutputContains("The following tasks were instantiated at execution time: [:lib:allMetadataJar, :lib:commonizeCInterop]")
                }
            }
        }
    }
}