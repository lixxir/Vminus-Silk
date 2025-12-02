package net.lixir.vminus.client.data

import net.lixir.vminus.util.UnsetAware
import net.minecraft.util.StringIdentifiable

/**
 * 
 * 
 */
sealed interface ModelType<T, Provider, Generator, Entry> : StringIdentifiable,
    UnsetAware {
    val id: String

    val action: (entry: Entry, provider: Provider, generator: Generator) -> Unit

    fun apply(type: T, provider: Provider, generator: Generator)

    override fun toString(): String
}
