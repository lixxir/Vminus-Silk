package net.lixir.vminus.client.datagen

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.lixir.vminus.client.definition.datagen.BlockDataGenDefinition
import net.lixir.vminus.client.definition.datagen.DataGenDefinitionRegistry
import net.lixir.vminus.client.definition.datagen.EntityTypeDataGenDefinition
import net.lixir.vminus.client.definition.datagen.ItemDataGenDefinition
import net.lixir.vminus.registry.VRegistry
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item

abstract class VDataGenerator {
    protected lateinit var pack: FabricDataGenerator.Pack

    open fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        this.pack = fabricDataGenerator.createPack()
        for (block in vRegistry.blocks) define(block, BlockDataGenDefinition.ofDefault())
        for (item in vRegistry.items) {
            var definition = ItemDataGenDefinition.ofDefault()
            if (item is BlockItem) {
                val blockDefinition = definitionRegistry.getBlockDefinition(item.block)
                definition = definition.merge(blockDefinition)
            }
            define(item, definition)
        }
        for (entityType in vRegistry.entityTypes) define(entityType, EntityTypeDataGenDefinition.ofDefault())
    }

    fun define(block: Block, definition: BlockDataGenDefinition) {
        definitionRegistry.define(block, definition)
    }

    fun define(entityType: EntityType<*>, definition: EntityTypeDataGenDefinition) {
        definitionRegistry.define(entityType, definition)
    }

    fun define(item: Item, definition: ItemDataGenDefinition) {
        definitionRegistry.define(item, definition)
    }

    abstract val vRegistry: VRegistry

    abstract val definitionRegistry: DataGenDefinitionRegistry
}
