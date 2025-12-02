package net.lixir.vminus.client.render.block

import net.lixir.vminus.util.UnsetAware
import net.minecraft.client.render.RenderLayer
import net.minecraft.util.StringIdentifiable

interface BlockRenderLayerType : UnsetAware, StringIdentifiable {
    val renderLayer: RenderLayer?

    override val isEmpty: Boolean
        get() = asString() == BlockRenderLayerTypes.NONE.asString() || isUnset

    override val isUnset: Boolean
        get() = asString() == BlockRenderLayerTypes.UNSET.asString()
}
