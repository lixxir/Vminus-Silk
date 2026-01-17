package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.definition.DefinitionProvider
import net.lixir.vminus.client.definition.DefinitionCategory
import net.lixir.vminus.client.definition.DefinitionCategories

abstract class DatagenDefinitionProvider<T> : DefinitionProvider<DatagenDefinition<T>, T>() {
    final override val category: DefinitionCategory = DefinitionCategories.DATAGEN
}
