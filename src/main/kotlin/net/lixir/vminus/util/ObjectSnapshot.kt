package net.lixir.vminus.util

import net.minecraft.item.ItemStack

interface ObjectSnapshot<T> {
    fun get(): T
}