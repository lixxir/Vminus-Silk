package net.lixir.vminus.client.definition.render

import it.unimi.dsi.fastutil.objects.Object2ObjectMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.lixir.vminus.Vminus
import net.lixir.vminus.client.definition.AbstractDefinitionRegistry
import net.lixir.vminus.client.definition.BlockApplicableDefinitionRegistry
import net.lixir.vminus.client.definition.render.BlockRenderDefinition.Companion.ofDefault
import net.lixir.vminus.registry.VRegistry
import net.minecraft.block.Block
import java.util.concurrent.ConcurrentHashMap

open class RenderDefinitionRegistry (
    modId: String,
    definitionGroupProvider: RenderDefinitionGroupProvider
) :
    AbstractDefinitionRegistry<RenderDefinitionGroupProvider>(modId, definitionGroupProvider),
    BlockApplicableDefinitionRegistry<BlockRenderDefinition> {

    override val blockDefinitions: Object2ObjectMap<Block, BlockRenderDefinition> = Object2ObjectOpenHashMap()

    init {
        val registry = VRegistry.fromId(modId)
        for (block in registry.blocks) define(block, ofDefault())

        REGISTRIES[modId] = this
    }

    override fun getBlockDefinition(block: Block): BlockRenderDefinition
            = blockDefinitions.getOrDefault(block, BlockRenderDefinition.of())

    final override fun define(block: Block, definition: BlockRenderDefinition) {
        blockDefinitions[block] = definition.setDefault(block)
    }

    companion object {
        private val REGISTRIES = ConcurrentHashMap<String, RenderDefinitionRegistry>()

        fun fromId(id: String): RenderDefinitionRegistry? = REGISTRIES[id]
    }
}
