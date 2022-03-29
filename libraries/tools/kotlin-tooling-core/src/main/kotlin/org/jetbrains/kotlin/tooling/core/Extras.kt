/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.tooling.core

import java.io.Serializable

interface Extras {
    class Id<T : Any> @PublishedApi internal constructor(
        internal val type: ReifiedTypeSignature<T>,
        val name: String? = null,
    ) : Serializable {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Id<*>) return false
            if (name != other.name) return false
            if (type != other.type) return false
            return true
        }

        override fun hashCode(): Int {
            var result = name?.hashCode() ?: 0
            result = 31 * result + type.hashCode()
            return result
        }

        override fun toString(): String {
            if (name == null) return type.toString()
            return "$name: $type"
        }

        internal companion object {
            private const val serialVersionUID = 0L
        }
    }

    class Key<T : Any> internal constructor(
        val id: Id<T>, @PublishedApi internal val capabilities: Set<Capability<T>>
    ) {
        constructor(id: Id<T>) : this(id, emptySet())

        interface Capability<T>

        override fun equals(other: Any?): Boolean {
            if (other === this) return true
            if (other !is Key<*>) return false
            if (other.id != this.id) return false
            if (other.capabilities != this.capabilities) return false
            return true
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + capabilities.hashCode()
            return result
        }

        override fun toString(): String = "Key($id)"

        operator fun plus(capability: Capability<T>) = Key(id = id, capabilities + capability)

        inline fun <reified C : Capability<T>> capability(): C? {
            return capabilities.lastOrNull { capability -> capability is C }?.let { it as C }
        }
    }

    class Entry<T : Any>(val key: Key<T>, val value: T) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Entry<*>) return false
            if (other.key != key) return false
            if (other.value != value) return false
            return true
        }

        override fun hashCode(): Int {
            var result = key.hashCode()
            result = 31 * result + value.hashCode()
            return result
        }

        override fun toString(): String = "$key=$value"

        operator fun component1() = key
        operator fun component2() = value
    }

    val ids: Set<Id<*>>
    operator fun <T : Any> get(key: Key<T>): T?
}

interface IterableExtras : Extras, Collection<Extras.Entry<*>> {
    val entries: Set<Extras.Entry<*>>
    fun isNotEmpty() = !isEmpty()
    override fun iterator(): Iterator<Extras.Entry<*>> = entries.iterator()
}

interface MutableExtras : IterableExtras {
    /**
     * @return The previous value or null if no previous value was set
     */
    operator fun <T : Any> set(key: Extras.Key<T>, value: T): T?

    /**
     * Removes the value from this container if *and only if* it was stored with this key
     */
    fun <T : Any> remove(key: Extras.Key<T>): T?

    /**
     * Removes the corresponding entry (regardless of which key was used) from this map
     */
    fun <T : Any> remove(id: Extras.Id<T>): Extras.Entry<T>?

    fun putAll(from: Iterable<Extras.Entry<*>>)

    fun clear()
}
