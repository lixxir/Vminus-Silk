package net.lixir.vminus.client.definition.render

import net.lixir.vminus.client.definition.AbstractDefinitionGroup

open class RenderDefinitionGroup<T> protected constructor(definition: RenderDefinition<*, T>) :
    AbstractDefinitionGroup<T>(definition) {
    fun <D : RenderDefinition<D, T>> replace(definition: RenderDefinition<D, T>): RenderDefinitionGroup<T> {
        this.definition = of(definition).definition
        return this
    }

    companion object {
        fun <D : RenderDefinition<D, T>, T> of(definition: RenderDefinition<D, T>): RenderDefinitionGroup<T> {
            return RenderDefinitionGroup(definition)
        }
    }
}
