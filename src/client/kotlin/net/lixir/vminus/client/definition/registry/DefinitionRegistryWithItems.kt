package net.lixir.vminus.client.definition.registry

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import net.lixir.vminus.client.definition.Definition
import net.minecraft.item.Item

interface DefinitionRegistryWithItems<D : Definition<*>> {
    val itemDefinitions: Object2ObjectMap<Item, D>

    fun getItemDefinition(item: Item): D?

    fun define(item: Item, definition: D)
}
