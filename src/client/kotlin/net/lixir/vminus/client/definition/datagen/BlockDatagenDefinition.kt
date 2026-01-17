package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.data.BlockModelType
import net.lixir.vminus.client.data.BlockModelTypes
import net.lixir.vminus.client.datagen.block.BlockLootTableType
import net.lixir.vminus.client.datagen.block.BlockLootTableTypes
import net.lixir.vminus.client.definition.Definition
import net.lixir.vminus.client.definition.registry.DefinitionRegistry
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import java.util.*

open class BlockDatagenDefinition : DatagenDefinition<Block>(),
    DefinitionWithTags<BlockDatagenDefinition, Block> {
    val tags: MutableSet<TagKey<Block>> = HashSet()
    var itemDefinition: ItemDatagenDefinition = ItemDatagenDefinition()
        protected set
    var textureSettings: DefinitionTextureSettings = DefinitionTextureSettings()
    var modelType: BlockModelType = BlockModelTypes.UNSET
        protected set
    var lootTableType: BlockLootTableType = BlockLootTableTypes.UNSET
        protected set


    override fun setDefault(of: Block): BlockDatagenDefinition {
        if (inherits) {
            val definition = DefinitionRegistry.getDefaultBlockDefinition(of, category)
            this.merge(definition)
        }
        return this
    }

    override fun merge(other: Definition<Block>?): Definition<Block> {
        if (other == null || other !is BlockDatagenDefinition) return this

        tags.addAll(other.tags)
        itemDefinition.merge(other.itemDefinition)
        textureSettings.merge(other.textureSettings)
        modelType = if (modelType.isUnset) other.modelType else modelType
        lootTableType = if (lootTableType.isUnset) other.lootTableType else lootTableType

        return super.merge(other)
    }

    override val empty: Boolean
        get() = this == EMPTY

    fun itemDefinition(itemDefinition: ItemDatagenDefinition): BlockDatagenDefinition {
        itemDefinition.inherits = true
        this.itemDefinition = itemDefinition
        return this
    }

    fun lootTableType(lootTableType: BlockLootTableType): BlockDatagenDefinition {
        this.lootTableType = lootTableType
        return this
    }

    override fun tags(vararg tags: TagKey<Block>): BlockDatagenDefinition {
        this.tags.addAll(tags.toList())
        return this
    }

    fun modelType(type: BlockModelType): BlockDatagenDefinition {
        this.modelType = type
        return this
    }

    fun textureSettings(settings: DefinitionTextureSettings): BlockDatagenDefinition {
        this.textureSettings = settings
        return this
    }

    companion object {
        private val EMPTY = BlockDatagenDefinition()

        fun of(block: Block): BlockDatagenDefinition {
            val definition =
                DatagenDefinitionRegistry.fromId(Registries.BLOCK.getId(block).namespace)?.getBlockDefinition(block) ?: return BlockDatagenDefinition()
            return definition
        }

        fun of(item: BlockItem): BlockDatagenDefinition = of(item.block)

        fun ofInheritable(): BlockDatagenDefinition {
            val definition = BlockDatagenDefinition()
            definition.inherits = true
            return definition
        }
    }
}
