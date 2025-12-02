package net.lixir.vminus.client.definition.datagen

import net.minecraft.item.Item

data class ItemDataGenDefinitionEntry(val item: Item, val itemDefinition: ItemDataGenDefinition) {
    companion object {
        fun of(item: Item): ItemDataGenDefinitionEntry {
            return ItemDataGenDefinitionEntry(item, ItemDataGenDefinition.of(item))
        }
    }
}