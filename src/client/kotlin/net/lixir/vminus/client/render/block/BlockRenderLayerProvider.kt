package net.lixir.vminus.client.render.block

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap
import net.lixir.vminus.Vminus
import net.lixir.vminus.client.definition.SimpleProvider
import net.lixir.vminus.client.definition.render.RenderDefinitionRegistry

open class BlockRenderLayerProvider(modId: String) : SimpleProvider(modId) {
    override fun run() {
        Vminus.devLogger("Running BlockRenderLayerProvider for $modId")
        val registry = RenderDefinitionRegistry.fromId(modId) ?: return
        Vminus.devLogger("Got Registry")
        val definitions = registry.blockDefinitions
        Vminus.devLogger("Got definitions: $definitions")
        definitions.forEach { (block, definition) ->
            val blockRenderLayerType = definition.blockRenderLayerType
            Vminus.devLogger("test: $blockRenderLayerType")
            if (blockRenderLayerType.renderLayer != null)
                BlockRenderLayerMap.INSTANCE.putBlock(block, blockRenderLayerType.renderLayer)
        }
    }
}
