package net.lixir.vminus.client.definition.render

import net.lixir.vminus.client.definition.DefinitionCategory
import net.lixir.vminus.client.definition.DefinitionCategories
import net.lixir.vminus.client.definition.Definition

abstract class RenderDefinition<T> : Definition<T>() {
    override val category
        get() = DefinitionCategories.RENDER

    override fun merge(other: Definition<T>?): Definition<T> {
        if (other == null || other !is RenderDefinition<T>) return this
        return super.merge(other)
    }
}
