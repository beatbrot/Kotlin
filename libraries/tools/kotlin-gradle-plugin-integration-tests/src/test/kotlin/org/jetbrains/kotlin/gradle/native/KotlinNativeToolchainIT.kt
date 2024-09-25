/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.native

import org.gradle.util.GradleVersion
import org.jetbrains.kotlin.gradle.testbase.*
import org.jetbrains.kotlin.gradle.util.replaceText
import org.jetbrains.kotlin.test.TestMetadata
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.condition.OS

@DisplayName("This test class contains base scenarios for testing Kotlin Native toolchain feature")
@NativeGradlePluginTests
class KotlinNativeToolchainIT : KGPBaseTest() {

    @DisplayName(
        "KT-66750: check that disabled native toolchain flag in subproject does not affect root project"
    )
    @GradleTest
    fun checkCommonizeNativeDistributionWithPlatform(gradleVersion: GradleVersion) {
        nativeProject("commonize-kt-66750-with-subproject", gradleVersion) {
            val buildOptions = defaultBuildOptions.disableIsolatedProjects()
            build(":commonizeNativeDistribution", buildOptions = buildOptions)
        }
    }

    @DisplayName("KT-71722: project without multiplatform plugin should not add kotlinNativeBundleConfiguration")
    @TestMetadata("jvmAndJsProject")
    @GradleTest
    fun checkKNConfigurationIsNotCreatedWithoutMultiplatform(gradleVersion: GradleVersion) {
        project("jvmAndJsProject", gradleVersion) {
            build(":jvmLib:dependencies") {
                assertOutputDoesNotContain("kotlinNativeBundleConfiguration")
                assertOutputDoesNotContain("org.jetbrains.kotlin:kotlin-native-prebuilt")
            }
        }
    }

    @DisplayName("KT-71398: project with multiplatform plugin should not add kotlinNativeBundleConfiguration")
    @TestMetadata("KT-71398-native-with-one-target")
    @OsCondition(supportedOn = [OS.LINUX], enabledOnCI = [OS.LINUX])
    @GradleTest
    fun checkKNConfigurationIsNotCreateWithoutSupportedTarget(gradleVersion: GradleVersion) {
        project("KT-71398-native-with-one-target", gradleVersion) {
            // macosArm64 is not supported on linux machine, so we should not create `kotlinNativeBundleConfiguration`
            buildGradleKts.replaceText("<SingleNativeTarget>", "macosArm64")
            build("dependencies") {
                assertOutputDoesNotContain("kotlinNativeBundleConfiguration")
                assertOutputDoesNotContain("org.jetbrains.kotlin:kotlin-native-prebuilt")
            }
        }
    }

}