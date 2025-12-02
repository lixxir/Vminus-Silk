package net.lixir.vminus.client.definition.render

import net.lixir.vminus.client.definition.AbstractDefinitionGroupProvider
import net.lixir.vminus.client.definition.DefinitionCategory
import net.lixir.vminus.client.definition.DefinitionCategories

abstract class RenderDefinitionGroupProvider : AbstractDefinitionGroupProvider<RenderDefinitionGroup<*>>() {
    override val type: DefinitionCategory = DefinitionCategories.RENDER
}
