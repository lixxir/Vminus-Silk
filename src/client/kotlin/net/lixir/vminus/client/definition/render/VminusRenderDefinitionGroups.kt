package net.lixir.vminus.client.definition.render

import net.lixir.vminus.client.render.block.BlockRenderLayerTypes
import net.minecraft.block.Block
import net.lixir.vminus.client.definition.render.BlockRenderDefinition.Companion.of as ofBlockDef
import net.lixir.vminus.client.definition.render.RenderDefinitionGroup.Companion.of as ofGroup

object VminusRenderDefinitionGroups {
    val PLANT: RenderDefinitionGroup<Block> = ofGroup(
        ofBlockDef().renderLayerType(BlockRenderLayerTypes.CUTOUT)
    )
}
