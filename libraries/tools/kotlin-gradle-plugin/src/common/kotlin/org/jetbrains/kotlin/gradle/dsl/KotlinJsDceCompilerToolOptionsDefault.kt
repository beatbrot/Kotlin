// DO NOT EDIT MANUALLY!
// Generated by org/jetbrains/kotlin/generators/arguments/GenerateGradleOptions.kt
// To regenerate run 'generateGradleOptions' task
@file:Suppress("RemoveRedundantQualifierName", "Deprecation", "Deprecation_Error", "DuplicatedCode")

package org.jetbrains.kotlin.gradle.dsl

internal abstract class KotlinJsDceCompilerToolOptionsDefault @javax.inject.Inject constructor(
    objectFactory: org.gradle.api.model.ObjectFactory
) : org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerToolOptionsDefault(objectFactory), org.jetbrains.kotlin.gradle.dsl.KotlinJsDceCompilerToolOptions {

    override val devMode: org.gradle.api.provider.Property<kotlin.Boolean> =
        objectFactory.property(kotlin.Boolean::class.java).convention(false)
}