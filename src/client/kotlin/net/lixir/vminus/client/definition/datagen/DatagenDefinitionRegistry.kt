package net.lixir.vminus.client.definition.datagen

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.lixir.vminus.client.definition.registry.DefinitionRegistryWithBlocks
import net.lixir.vminus.client.definition.registry.DefinitionRegistryWithEntityTypes
import net.lixir.vminus.client.definition.registry.DefinitionRegistryWithItems
import net.lixir.vminus.client.definition.registry.DefinitionRegistry
import net.lixir.vminus.registry.VRegistry
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import java.util.concurrent.ConcurrentHashMap

open class DatagenDefinitionRegistry (
    modId: String,
) :
    DefinitionRegistry<DatagenDefinitionProvider<*>>(modId),
    DefinitionRegistryWithItems<ItemDatagenDefinition>,
    DefinitionRegistryWithBlocks<BlockDatagenDefinition>,
    DefinitionRegistryWithEntityTypes<EntityTypeDatagenDefinition> {

    override val itemDefinitions: Object2ObjectMap<Item, ItemDatagenDefinition> = Object2ObjectOpenHashMap()
    override val blockDefinitions: Object2ObjectMap<Block, BlockDatagenDefinition> = Object2ObjectOpenHashMap()
    override val entityTypeDefinitions: Object2ObjectMap<EntityType<*>, EntityTypeDatagenDefinition> = Object2ObjectOpenHashMap()

    init {
        REGISTRIES[modId] = this
    }

    override fun define(entityType: EntityType<*>, definition: EntityTypeDatagenDefinition) {
        var definition = definition
        definition = definition.setDefault(entityType)
        entityTypeDefinitions[entityType] = definition
    }

    override fun define(block: Block, definition: BlockDatagenDefinition) {
        var definition = definition
        definition = definition.setDefault(block)
        blockDefinitions[block] = definition
    }

    override fun define(item: Item, definition: ItemDatagenDefinition) {
        var definition = definition
        definition = definition.setDefault(item)
        itemDefinitions[item] = definition
    }

    override fun getItemDefinition(item: Item): ItemDatagenDefinition =
        itemDefinitions.getOrDefault(item, ItemDatagenDefinition())

    override fun getBlockDefinition(block: Block): BlockDatagenDefinition =
        blockDefinitions.getOrDefault(block, BlockDatagenDefinition())

    override fun getEntityTypeDefinition(entityType: EntityType<*>): EntityTypeDatagenDefinition =
        entityTypeDefinitions.getOrDefault(entityType, EntityTypeDatagenDefinition())

    companion object {
        private val REGISTRIES = ConcurrentHashMap<String, DatagenDefinitionRegistry>()

        fun fromId(id: String): DatagenDefinitionRegistry? = REGISTRIES[id]
    }

    override fun generateDefinitions(vRegistry: VRegistry) {
        for (block in vRegistry.blocks) define(block, BlockDatagenDefinition.ofInheritable())
        for (item in vRegistry.items) {
            var definition = ItemDatagenDefinition.ofInheriting()
            if (item is BlockItem) {
                val blockDefinition = this.getBlockDefinition(item.block)
                definition = definition.merge(blockDefinition)
            }
            define(item, definition)
        }
        for (entityType in vRegistry.entityTypes) define(entityType, EntityTypeDatagenDefinition.ofInheritable())
    }
}
