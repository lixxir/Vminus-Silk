package net.lixir.vminus.client.definition.render

import net.lixir.vminus.Vminus
import net.minecraft.block.PlantBlock

class VminusRenderDefinitionGroupProvider : RenderDefinitionGroupProvider() {
    override fun run() {
        assignClasses(VminusRenderDefinitionGroups.PLANT, PlantBlock::class.java)
    }
}

