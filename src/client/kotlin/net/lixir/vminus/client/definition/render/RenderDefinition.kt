package net.lixir.vminus.client.definition.render

import net.lixir.vminus.client.definition.DefinitionCategory
import net.lixir.vminus.client.definition.DefinitionCategories
import net.lixir.vminus.client.definition.AbstractDefinition

abstract class RenderDefinition<D : RenderDefinition<D, T>, T> protected constructor() : AbstractDefinition<D, T>() {
    override val type: DefinitionCategory
        get() = DefinitionCategories.RENDER

    @Suppress("UNCHECKED_CAST")
    override fun merge(other: D?): D {
        if (other == null) return this as D

        return super.merge(other)
    }
}
