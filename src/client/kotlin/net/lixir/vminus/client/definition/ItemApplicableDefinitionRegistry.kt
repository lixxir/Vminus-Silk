package net.lixir.vminus.client.definition

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import net.minecraft.item.Item

interface ItemApplicableDefinitionRegistry<D : AbstractDefinition<*, *>> {
    val itemDefinitions: Object2ObjectMap<Item, D>

    fun getItemDefinition(item: Item): D?

    fun define(item: Item, definition: D)
}
