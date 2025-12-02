package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.definition.AbstractDefinitionGroupProvider
import net.lixir.vminus.client.definition.DefinitionCategory
import net.lixir.vminus.client.definition.DefinitionCategories

abstract class DataGenDefinitionGroupProvider : AbstractDefinitionGroupProvider<DataGenDefinitionGroup<*>>() {
    override val type: DefinitionCategory = DefinitionCategories.DATAGEN
}
