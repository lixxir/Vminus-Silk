package net.lixir.vminus.client.data

import net.lixir.vminus.Vminus
import net.lixir.vminus.client.datagen.provider.VModelProvider
import net.lixir.vminus.client.definition.datagen.ItemDatagenDefinition
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.item.Item
import net.minecraft.util.Identifier

/**
 * Built-in implementation of [ItemModelType].
 *
 * @see [ItemModelType]
 */
enum class ItemModelTypes(
    val id: Identifier,
    override val action: (Item, ItemDatagenDefinition, VModelProvider, ItemModelGenerator) -> Unit
) : ItemModelType {

    UNSET(Vminus.id("unset"), { _, _, _, _ -> }),
    NONE(Vminus.id("none"), { _, _, _, _ -> }),
    GENERATED(Vminus.id("generated"), { item, definition, provider, generator -> provider.generatedItem(item, definition, generator) }),
    PANE(Vminus.id("pane"), { item, definition, provider, generator -> provider.paneItem(item, definition, generator) }),
    HANDHELD(Vminus.id("handheld"), { item, definition, provider, generator -> provider.handheldItem(item, definition,  generator) }),
    PARENT_BLOCK(Vminus.id("parent_block"), { item, definition, provider, generator -> provider.parentBlock(item, definition, generator) });

    override fun getIdentifier() = id
    
    override fun toString() = "ItemModelType{$id}"
}
