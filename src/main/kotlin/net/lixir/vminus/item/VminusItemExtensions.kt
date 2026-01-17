package net.lixir.vminus.item

import net.lixir.vminus.util.Identifiable
import net.minecraft.item.Item
import net.minecraft.util.Identifier

object VminusItemExtensions {
    fun Item.getIdentifier(): Identifier = (this as Identifiable).identifier
}