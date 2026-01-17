package net.lixir.vminus.client.data

import net.lixir.vminus.client.datagen.provider.VModelProvider
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.item.Item
import net.lixir.vminus.client.definition.datagen.ItemDatagenDefinition

/**
 * Represents a type of item model definition.
 *
 * Implementations (usually enums) define how models are generated from a [ItemDatagenDefinition]
 * in a subclass of [VModelProvider] during data generation.
 *
 * @see [ItemModelTypes]
 * @see [ItemDatagenDefinition]
 * @see [VModelProvider]
 */
interface ItemModelType : ModelType<Item, VModelProvider, ItemModelGenerator, ItemDatagenDefinition> {
    override val isEmpty: Boolean
        get() = this.identifier == ItemModelTypes.NONE.id || isUnset

    override val isUnset: Boolean
        get() = this.identifier == ItemModelTypes.UNSET.id
}
