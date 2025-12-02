package net.lixir.vminus.client.definition

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import net.minecraft.block.Block


interface BlockApplicableDefinitionRegistry<D : AbstractDefinition<D, *>> {
    val blockDefinitions: Object2ObjectMap<Block, D>

    fun getBlockDefinition(block: Block): D?

    fun define(block: Block, definition: D)
}
