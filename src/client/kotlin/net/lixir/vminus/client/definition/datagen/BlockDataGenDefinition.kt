package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.data.BlockModelType
import net.lixir.vminus.client.data.BlockModelTypes
import net.lixir.vminus.client.datagen.block.BlockLootTableType
import net.lixir.vminus.client.datagen.block.BlockLootTableTypes
import net.lixir.vminus.client.definition.AbstractDefinitionRegistry
import net.lixir.vminus.client.definition.TaggedDatagenDefinition
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.*

open class BlockDataGenDefinition protected constructor() : DataGenDefinition<BlockDataGenDefinition, Block>(),
    TaggedDatagenDefinition<BlockDataGenDefinition, Block> {
    val tags: MutableSet<TagKey<Block>> = HashSet()
    var itemDefinition: ItemDataGenDefinition = ItemDataGenDefinition.of()
        protected set
    var modelTextureSuffix: String = "unset"
        protected set
    var modelType: BlockModelType = BlockModelTypes.UNSET
        protected set
    var lootTableType: BlockLootTableType = BlockLootTableTypes.UNSET
        protected set
    var modelTextureOverride: Identifier = UNSET_RESOURCE_LOCATION
        protected set

    override fun setDefault(of: Block): BlockDataGenDefinition {
        if (isDefaulted) {
            val definition = AbstractDefinitionRegistry.getDefaultBlockDefinition(of, type) as BlockDataGenDefinition
            this.merge(definition)
        }
        return this
    }

    override fun merge(other: BlockDataGenDefinition?): BlockDataGenDefinition {
        if (other == null) return this

        tags.addAll(other.tags)
        itemDefinition.merge(other.itemDefinition)
        modelType = if (modelType.isUnset) other.modelType else modelType
        lootTableType = if (lootTableType.isUnset) other.lootTableType else lootTableType
        modelTextureSuffix = if (modelTextureSuffix == "unset") other.modelTextureSuffix else modelTextureSuffix
        modelTextureOverride =
            if (modelTextureOverride == UNSET_RESOURCE_LOCATION) other.modelTextureOverride else modelTextureOverride

        return super.merge(other)
    }

    override val isEmpty: Boolean
        get() = this == EMPTY

    fun itemDefinition(itemDefinition: ItemDataGenDefinition): BlockDataGenDefinition {
        itemDefinition.setDefaulted()
        this.itemDefinition = itemDefinition
        return this
    }

    fun lootTableType(lootTableType: BlockLootTableType): BlockDataGenDefinition {
        this.lootTableType = lootTableType
        return this
    }

    override fun tags(vararg tags: TagKey<Block>): BlockDataGenDefinition {
        this.tags.addAll(tags.toList())
        return this
    }

    fun modelType(type: BlockModelType): BlockDataGenDefinition {
        this.modelType = type
        return this
    }

    fun modelTextureSuffix(suffix: String): BlockDataGenDefinition {
        this.modelTextureSuffix = suffix
        return this
    }

    fun modelTextureOverride(override: Identifier): BlockDataGenDefinition {
        this.modelTextureOverride = override
        return this
    }

    companion object {
        private val EMPTY = of()

        fun of(block: Block): BlockDataGenDefinition {
            val definition =
                DataGenDefinitionRegistry.fromId(Registries.BLOCK.getId(block).namespace)?.getBlockDefinition(block) ?: return of()
            return definition
        }

        fun of(item: BlockItem): BlockDataGenDefinition = of(item.block)

        fun of(): BlockDataGenDefinition = BlockDataGenDefinition()

        fun ofDefault(): BlockDataGenDefinition {
            val definition = BlockDataGenDefinition()
            definition.isDefaulted = true
            return definition
        }
    }
}
