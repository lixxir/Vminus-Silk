package net.lixir.vminus.client.render

import net.lixir.vminus.util.UnsetAware
import net.minecraft.util.StringIdentifiable

/**
 * Represents a tint that can be applied to items or blocks.
 *
 *
 * This interface is typically implemented by enums to define a set of available tint types.
 * Each tint type provides a unique ID and optional functions for applying tints to blocks and items.
 *
 */
interface TintType : StringIdentifiable, UnsetAware {
    val itemTintFunction: ItemTintFunction?
    val id: String
    val blockTintFunction: BlockTintFunction?

    override val isEmpty: Boolean
        get() = asString() == TintTypes.NONE.asString() || isUnset

    override val isUnset: Boolean
        get() = asString() == TintTypes.UNSET.asString()
}
