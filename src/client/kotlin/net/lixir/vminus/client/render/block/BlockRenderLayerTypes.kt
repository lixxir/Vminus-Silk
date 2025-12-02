package net.lixir.vminus.client.render.block

import net.minecraft.client.render.RenderLayer

enum class BlockRenderLayerTypes(val id: String, override val renderLayer: RenderLayer?) : BlockRenderLayerType {
    UNSET("unset", null),
    NONE("none", null),
    CUTOUT("cutout", RenderLayer.getCutout()),
    CUTOUT_MIPPED("cutout_mipped", RenderLayer.getCutoutMipped()),
    TRANSLUCENT("translucent", RenderLayer.getTranslucent());

    override fun asString(): String = "BlockRenderLayerType($id)"
}
