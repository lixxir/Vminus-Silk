package net.lixir.vminus.client.definition.datagen

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.lixir.vminus.client.definition.BlockApplicableDefinitionRegistry
import net.lixir.vminus.client.definition.EntityTypeApplicableDefinitionRegistry
import net.lixir.vminus.client.definition.ItemApplicableDefinitionRegistry
import net.lixir.vminus.client.definition.AbstractDefinitionRegistry
import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import java.util.concurrent.ConcurrentHashMap



open class DataGenDefinitionRegistry (
    modId: String,
    definitionGroupProvider: DataGenDefinitionGroupProvider
) :
    AbstractDefinitionRegistry<DataGenDefinitionGroupProvider>(modId, definitionGroupProvider),
    ItemApplicableDefinitionRegistry<ItemDataGenDefinition>,
    BlockApplicableDefinitionRegistry<BlockDataGenDefinition>,
    EntityTypeApplicableDefinitionRegistry<EntityTypeDataGenDefinition> {

    override val itemDefinitions: Object2ObjectMap<Item, ItemDataGenDefinition> = Object2ObjectOpenHashMap()
    override val blockDefinitions: Object2ObjectMap<Block, BlockDataGenDefinition> = Object2ObjectOpenHashMap()
    override val entityTypeDefinitions: Object2ObjectMap<EntityType<*>, EntityTypeDataGenDefinition> = Object2ObjectOpenHashMap()

    init {
        REGISTRIES[modId] = this
    }

    override fun define(entityType: EntityType<*>, definition: EntityTypeDataGenDefinition) {
        var definition = definition
        definition = definition.setDefault(entityType)
        entityTypeDefinitions[entityType] = definition
    }

    override fun define(block: Block, definition: BlockDataGenDefinition) {
        var definition = definition
        definition = definition.setDefault(block)
        blockDefinitions[block] = definition
    }

    override fun define(item: Item, definition: ItemDataGenDefinition) {
        var definition = definition
        definition = definition.setDefault(item)
        itemDefinitions[item] = definition
    }

    override fun getItemDefinition(item: Item): ItemDataGenDefinition
        = itemDefinitions.getOrDefault(item, ItemDataGenDefinition.of())

    override fun getBlockDefinition(block: Block): BlockDataGenDefinition
        = blockDefinitions.getOrDefault(block, BlockDataGenDefinition.of())

    override fun getEntityTypeDefinition(entityType: EntityType<*>): EntityTypeDataGenDefinition
        = entityTypeDefinitions.getOrDefault(entityType, EntityTypeDataGenDefinition.of())

    companion object {
        private val REGISTRIES = ConcurrentHashMap<String, DataGenDefinitionRegistry>()

        fun fromId(id: String): DataGenDefinitionRegistry? = REGISTRIES[id]
    }
}
