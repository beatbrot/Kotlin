/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.gradle.plugin.mpp

import org.gradle.api.component.SoftwareComponent
import org.gradle.api.provider.Property
import org.gradle.api.publish.maven.MavenPublication
import org.jetbrains.kotlin.gradle.InternalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetComponent
import org.jetbrains.kotlin.gradle.plugin.mpp.external.DecoratedExternalKotlinTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.external.ExternalKotlinTargetImpl
import org.jetbrains.kotlin.tooling.core.HasMutableExtras

internal interface InternalKotlinTarget : KotlinTarget, HasMutableExtras {
    @InternalKotlinGradlePluginApi
    val isSourcesPublishableProperty: Property<Boolean>
    var isSourcesPublishable: Boolean
        get() = isSourcesPublishableProperty.get()
        set(value) = isSourcesPublishableProperty.set(value)

    val kotlinComponents: Set<KotlinTargetComponent>

    fun onPublicationCreated(publication: MavenPublication)
}

internal val KotlinTarget.internal: InternalKotlinTarget
    get() = (this as? InternalKotlinTarget) ?: throw IllegalArgumentException(
        "KotlinTarget($name) ${this::class} does not implement ${InternalKotlinTarget::class}"
    )

internal suspend fun InternalKotlinTarget.awaitComponents(): Set<SoftwareComponent> = when (this) {
    is AbstractKotlinTarget -> awaitComponents()
    is ExternalKotlinTargetImpl -> awaitComponents()
    is DecoratedExternalKotlinTarget -> delegate.awaitComponents()
    else -> components.also { kotlinComponents.forEach { it.awaitKotlinUsagesOrEmpty() } }
}