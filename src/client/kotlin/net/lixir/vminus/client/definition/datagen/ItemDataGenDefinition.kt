package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.data.ItemModelType
import net.lixir.vminus.client.data.ItemModelTypes
import net.lixir.vminus.client.datagen.lang.LangKey
import net.lixir.vminus.client.definition.AbstractDefinitionRegistry
import net.lixir.vminus.client.definition.TaggedDatagenDefinition
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.tag.TagKey
import java.util.*

open class ItemDataGenDefinition : DataGenDefinition<ItemDataGenDefinition, Item>(),
    TaggedDatagenDefinition<ItemDataGenDefinition, Item> {

    val tags: MutableSet<TagKey<Item>> = HashSet()
    var modelType: ItemModelType = ItemModelTypes.UNSET
        protected set
    private var fromBlock = false


    override fun setDefault(of: Item): ItemDataGenDefinition {
        if (isDefaulted) {
            val definition = AbstractDefinitionRegistry.getDefaultItemDefinition(of, type) as ItemDataGenDefinition
            this.merge(definition)
        }
        return this
    }

    override val isEmpty: Boolean
        get() = this == EMPTY

    fun modelType(type: ItemModelType): ItemDataGenDefinition {
        this.modelType = type
        return this
    }

    override fun tags(vararg tags: TagKey<Item>): ItemDataGenDefinition {
        this.tags.addAll(tags.toList())
        return this
    }

    fun merge(other: BlockDataGenDefinition?): ItemDataGenDefinition {
        if (other == null) return this
        this.langKey = if (langKey.isUnset) LangKey.NONE else this.langKey
        val otherModel = other.modelType
        if (!otherModel.isEmpty) this.modelType = if (modelType.isUnset) otherModel.itemModelType!! else this.modelType
        this.fromBlock = true
        this.merge(other.itemDefinition)
        return this
    }

    override fun merge(other: ItemDataGenDefinition?): ItemDataGenDefinition {
        if (other == null) return this

        tags.addAll(other.tags)
        this.modelType = if (modelType.isUnset) other.modelType else this.modelType

        if (!this.fromBlock) this.fromBlock = other.fromBlock

        return this
    }

    companion object {
        val EMPTY: ItemDataGenDefinition = of()

        fun of(item: Item): ItemDataGenDefinition {
            val definition = DataGenDefinitionRegistry.fromId(item.getCreatorNamespace(item.defaultStack))?.getItemDefinition(item) ?: return of()
            if (item is BlockItem) {
                val blockDefinition = BlockDataGenDefinition.of(item)
                if (!blockDefinition.isEmpty) {
                    val blockItemDefinition = blockDefinition.itemDefinition
                    if (!blockItemDefinition.isEmpty) {
                        return definition.merge(blockItemDefinition)
                    }
                }
            }
            return definition
        }

        fun of(): ItemDataGenDefinition = ItemDataGenDefinition()
        
        fun ofDefault(): ItemDataGenDefinition {
            val definition = ItemDataGenDefinition()
            definition.isDefaulted = true
            return definition
        }
    }
}
