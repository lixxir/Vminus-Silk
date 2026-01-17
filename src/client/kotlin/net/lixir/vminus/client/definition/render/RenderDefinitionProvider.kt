package net.lixir.vminus.client.definition.render

import net.lixir.vminus.client.definition.DefinitionProvider
import net.lixir.vminus.client.definition.DefinitionCategory
import net.lixir.vminus.client.definition.DefinitionCategories

abstract class RenderDefinitionProvider<T> : DefinitionProvider<RenderDefinition<T>, T>() {
    final override val category: DefinitionCategory = DefinitionCategories.RENDER
}
