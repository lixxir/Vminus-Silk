package net.lixir.vminus.client.data

import net.lixir.vminus.client.datagen.provider.VModelProvider
import net.lixir.vminus.client.definition.datagen.ItemDataGenDefinitionEntry
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.item.Item
import net.lixir.vminus.client.definition.datagen.ItemDataGenDefinition

/**
 * Represents a type of item model definition.
 *
 * Implementations (usually enums) define how models are generated from a [ItemDataGenDefinition]
 * in a subclass of [VModelProvider] during data generation.
 *
 * @see [ItemModelTypes]
 * @see [ItemDataGenDefinition]
 * @see [VModelProvider]
 */
interface ItemModelType : ModelType<Item, VModelProvider, ItemModelGenerator, ItemDataGenDefinitionEntry> {
    override val isEmpty: Boolean
        get() = this.asString() == ItemModelTypes.NONE.asString() || isUnset

    override val isUnset: Boolean
        get() = this.asString() == ItemModelTypes.UNSET.asString()
}
