package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.data.ItemModelTypes
import net.minecraft.item.BlockItem
import net.minecraft.item.Item

/**
 * Default definitions provided by VMinus.
 *
 */
open class VminusItemDatagenDefinitionProvider : ItemDatagenDefinitionProvider() {
    override fun run() {
        assign(BlockItem::class.java, ItemDatagenDefinition().modelType(ItemModelTypes.PARENT_BLOCK))
        assign(Item::class.java, ItemDatagenDefinition().modelType(ItemModelTypes.GENERATED))
    }
}

