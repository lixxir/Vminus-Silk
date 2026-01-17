package net.lixir.vminus.client.data

import net.lixir.vminus.client.datagen.provider.VModelProvider
import net.lixir.vminus.client.definition.Definition
import net.lixir.vminus.util.Identifiable
import net.lixir.vminus.util.UnsetAware

/**
 * 
 * 
 */
sealed interface ModelType<T, in P : VModelProvider, G, D: Definition<T>> : Identifiable,
    UnsetAware {
    val action: (value: T, definition: D, provider: P, generator: G) -> Unit
}
