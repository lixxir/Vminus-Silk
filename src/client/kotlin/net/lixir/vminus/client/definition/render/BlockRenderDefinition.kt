package net.lixir.vminus.client.definition.render

import net.lixir.vminus.Vminus
import net.lixir.vminus.client.definition.AbstractDefinitionRegistry
import net.lixir.vminus.client.render.block.BlockRenderLayerType
import net.lixir.vminus.client.render.block.BlockRenderLayerTypes
import net.minecraft.block.Block
import net.minecraft.registry.Registries

open class BlockRenderDefinition : RenderDefinition<BlockRenderDefinition, Block>() {
    var blockRenderLayerType: BlockRenderLayerType = BlockRenderLayerTypes.UNSET
        protected set

    fun renderLayerType(type: BlockRenderLayerType): BlockRenderDefinition {
        this.blockRenderLayerType = type
        return this
    }

    override fun setDefault(of: Block): BlockRenderDefinition {
        if (isDefaulted) {
            val definition = AbstractDefinitionRegistry.getDefaultBlockDefinition(of, type) as BlockRenderDefinition?
            this.merge(definition)
        }
        return this
    }

    override fun merge(other: BlockRenderDefinition?): BlockRenderDefinition {
        if (other == null) return this

        blockRenderLayerType = if (blockRenderLayerType.isUnset) other.blockRenderLayerType else blockRenderLayerType
        return super.merge(other)
    }

    override fun toString(): String = "BlockRenderDefinition(blockRenderLayerType=$blockRenderLayerType)"

    override val isEmpty: Boolean
        get() = this == EMPTY

    companion object {
        private val EMPTY = of()

        fun of(block: Block): BlockRenderDefinition {
            val registry = RenderDefinitionRegistry.fromId(Registries.BLOCK.getId(block).namespace) ?: return of()
            val definition = registry.getBlockDefinition(block)
            return definition
        }

        fun of(): BlockRenderDefinition = BlockRenderDefinition()

        fun ofDefault(): BlockRenderDefinition {
            val definition = BlockRenderDefinition()
            definition.isDefaulted = true
            return definition
        }
    }
}
