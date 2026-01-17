package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.definition.DefinitionProvider
import net.lixir.vminus.client.definition.DefinitionCategory
import net.lixir.vminus.client.definition.DefinitionCategories
import net.minecraft.entity.EntityType

abstract class EntityTypeDatagenDefinitionProvider : DefinitionProvider<DatagenDefinition<EntityType<*>>, EntityType<*>>() {
    final override val category: DefinitionCategory = DefinitionCategories.DATAGEN
}
