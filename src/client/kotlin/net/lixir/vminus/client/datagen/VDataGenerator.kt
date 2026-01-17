package net.lixir.vminus.client.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.lixir.vminus.client.definition.datagen.BlockDatagenDefinition
import net.lixir.vminus.client.definition.datagen.DatagenDefinitionRegistry
import net.lixir.vminus.client.definition.datagen.EntityTypeDatagenDefinition
import net.lixir.vminus.client.definition.datagen.ItemDatagenDefinition
import net.lixir.vminus.registry.VRegistry
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item

abstract class VDataGenerator {
    protected lateinit var pack: FabricDataGenerator.Pack

    open fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        this.pack = fabricDataGenerator.createPack()
        definitionRegistry.generateDefinitions(vRegistry)
    }

    fun define(block: Block, definition: BlockDatagenDefinition) {
        definitionRegistry.define(block, definition)
    }

    fun define(entityType: EntityType<*>, definition: EntityTypeDatagenDefinition) {
        definitionRegistry.define(entityType, definition)
    }

    fun define(item: Item, definition: ItemDatagenDefinition) {
        definitionRegistry.define(item, definition)
    }

    abstract val vRegistry: VRegistry

    abstract val definitionRegistry: DatagenDefinitionRegistry
}
