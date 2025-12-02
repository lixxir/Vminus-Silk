package net.lixir.vminus.client.render

import net.minecraft.item.ItemStack

fun interface ItemTintFunction {
    fun apply(stack: ItemStack?, tintIndex: Int?): Int
}
