package net.lixir.vminus.client.definition.registry

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import net.lixir.vminus.client.definition.Definition
import net.minecraft.block.Block


interface DefinitionRegistryWithBlocks<D : Definition<*>> {
    val blockDefinitions: Object2ObjectMap<Block, D>

    fun getBlockDefinition(block: Block): D?

    fun define(block: Block, definition: D)
}
