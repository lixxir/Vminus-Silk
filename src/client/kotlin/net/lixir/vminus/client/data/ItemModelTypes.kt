package net.lixir.vminus.client.data

import net.lixir.vminus.client.datagen.provider.VModelProvider
import net.lixir.vminus.client.definition.datagen.ItemDataGenDefinitionEntry
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.item.Item

/**
 * Built-in implementation of [ItemModelType].
 *
 * @see [ItemModelType]
 */
enum class ItemModelTypes(
    override val id: String,
    override val action: (
        entry: ItemDataGenDefinitionEntry,
        provider: VModelProvider,
        generator: ItemModelGenerator
            ) -> Unit
) : ItemModelType {

    UNSET("unset", { _, _, _ -> }),

    NONE("none", { _, _, _ -> }),

    BASIC("basic", { entry, provider, generator ->
        provider.basic(entry, generator)
    }),

    PANE("pane", { entry, provider, generator ->
        provider.pane(entry, generator)
    }),

    HANDHELD("handheld", { entry, provider, generator ->
        provider.handheld(entry, generator)
    }),

    PARENT_BLOCK("parent_block", { entry, provider, generator ->
        provider.parentBlock(entry, generator)
    });

    override fun apply(type: Item, provider: VModelProvider, generator: ItemModelGenerator) {
        action(ItemDataGenDefinitionEntry.of(type), provider, generator)
    }

    override fun asString() = id

    override fun toString() = "ItemModelType[$id]"
}
