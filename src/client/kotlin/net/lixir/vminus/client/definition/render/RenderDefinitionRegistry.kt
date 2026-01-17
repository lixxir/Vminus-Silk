package net.lixir.vminus.client.definition.render

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.lixir.vminus.client.definition.registry.DefinitionRegistry
import net.lixir.vminus.client.definition.registry.DefinitionRegistryWithBlocks

import net.lixir.vminus.registry.VRegistry
import net.minecraft.block.Block
import java.util.concurrent.ConcurrentHashMap

open class RenderDefinitionRegistry (
    modId: String
) :
    DefinitionRegistry<RenderDefinitionProvider<*>>(modId),
    DefinitionRegistryWithBlocks<BlockRenderDefinition> {

    override val blockDefinitions: Object2ObjectMap<Block, BlockRenderDefinition> = Object2ObjectOpenHashMap()

    init {
        REGISTRIES[modId] = this
    }

    override fun getBlockDefinition(block: Block): BlockRenderDefinition =
        blockDefinitions.getOrDefault(block, BlockRenderDefinition())

    final override fun define(block: Block, definition: BlockRenderDefinition) {
        blockDefinitions[block] = definition.setDefault(block)
    }

    companion object {
        private val REGISTRIES = ConcurrentHashMap<String, RenderDefinitionRegistry>()

        fun fromId(id: String): RenderDefinitionRegistry? = REGISTRIES[id]
    }

    override fun generateDefinitions(vRegistry: VRegistry) {
        for (block in vRegistry.blocks) define(block, BlockRenderDefinition.ofInheritable())
    }
}
