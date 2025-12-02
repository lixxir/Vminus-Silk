package net.lixir.vminus.client.render.block

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.lixir.vminus.Vminus
import net.lixir.vminus.client.definition.RegistryDefinitionProvider
import net.lixir.vminus.client.definition.render.RenderDefinitionRegistry

abstract class BlockRenderLayerProvider(protected val modId: String) : RegistryDefinitionProvider<RenderDefinitionRegistry> {
    override fun run() {
        val registry = RenderDefinitionRegistry.fromId(modId) ?: return
        val definitions = registry.blockDefinitions
        definitions.forEach { (block, definition) ->
            val blockRenderLayerType = definition.blockRenderLayerType
            if (blockRenderLayerType.renderLayer != null)
                BlockRenderLayerMap.INSTANCE.putBlock(block, blockRenderLayerType.renderLayer)
        }
    }
}
