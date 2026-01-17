package net.lixir.vminus.client.definition.render

import net.lixir.vminus.client.render.block.BlockRenderLayerTypes
import net.minecraft.block.PlantBlock

open class VminusBlockRenderDefinitionProvider : BlockRenderDefinitionProvider() {
    override fun run() {
        assign(PlantBlock::class.java, BlockRenderDefinition().renderLayerType(BlockRenderLayerTypes.CUTOUT))
    }
}

