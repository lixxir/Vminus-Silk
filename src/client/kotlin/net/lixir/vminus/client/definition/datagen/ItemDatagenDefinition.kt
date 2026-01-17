package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.data.ItemModelType
import net.lixir.vminus.client.data.ItemModelTypes
import net.lixir.vminus.client.datagen.lang.Translation
import net.lixir.vminus.client.definition.Definition
import net.lixir.vminus.client.definition.registry.DefinitionRegistry

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.tag.TagKey
import java.util.*

open class ItemDatagenDefinition : DatagenDefinition<Item>(),
    DefinitionWithTags<ItemDatagenDefinition, Item> {

    val tags: MutableSet<TagKey<Item>> = HashSet()
    var modelType: ItemModelType= ItemModelTypes.UNSET
        protected set
    private var fromBlock = false

    override fun setDefault(of: Item): ItemDatagenDefinition {
        if (inherits) {
            val definition = DefinitionRegistry.getDefaultItemDefinition(of, category)
            this.merge(definition)
        }
        return this
    }

    override val empty: Boolean
        get() = this == EMPTY

    fun modelType(type: ItemModelType): ItemDatagenDefinition {
        this.modelType = type
        return this
    }

    override fun tags(vararg tags: TagKey<Item>): ItemDatagenDefinition {
        this.tags.addAll(tags.toList())
        return this
    }

    fun merge(other: BlockDatagenDefinition?): ItemDatagenDefinition {
        if (other == null) return this
        this.translation = if (translation.isUnset) Translation.NONE else this.translation
        val otherModel = other.modelType
        if (!otherModel.isEmpty) this.modelType = if (modelType.isUnset) otherModel.itemModelType!! else this.modelType
        this.fromBlock = true
        this.merge(other.itemDefinition)
        return this
    }

    override fun merge(other: Definition<Item>?): ItemDatagenDefinition {
        if (other == null || other !is ItemDatagenDefinition)
            return this

        tags.addAll(other.tags)
        this.modelType = if (modelType.isUnset) other.modelType else this.modelType

        if (!this.fromBlock) this.fromBlock = other.fromBlock

        return this
    }

    companion object {
        val EMPTY: ItemDatagenDefinition = ItemDatagenDefinition()

        fun of(item: Item): ItemDatagenDefinition {
            val definition = DatagenDefinitionRegistry.fromId(item.getCreatorNamespace(item.defaultStack))?.getItemDefinition(item) ?: return ItemDatagenDefinition()
            if (item is BlockItem) {
                val blockDefinition = BlockDatagenDefinition.of(item)
                if (!blockDefinition.empty) {
                    val blockItemDefinition = blockDefinition.itemDefinition
                    if (!blockItemDefinition.empty) {
                        return definition.merge(blockItemDefinition)
                    }
                }
            }
            return definition
        }

        fun ofInheriting(): ItemDatagenDefinition {
            val definition = ItemDatagenDefinition()
            definition.inherits = true
            return definition
        }
    }
}
