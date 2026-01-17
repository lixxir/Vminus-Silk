package net.lixir.vminus.client.definition.render

import net.lixir.vminus.client.definition.Definition
import net.lixir.vminus.client.definition.registry.DefinitionRegistry

import net.lixir.vminus.client.render.block.BlockRenderLayerType
import net.lixir.vminus.client.render.block.BlockRenderLayerTypes
import net.minecraft.block.Block
import net.minecraft.registry.Registries

open class BlockRenderDefinition : RenderDefinition<Block>() {
    var blockRenderLayerType: BlockRenderLayerType = BlockRenderLayerTypes.UNSET
        protected set

    fun renderLayerType(type: BlockRenderLayerType): BlockRenderDefinition {
        this.blockRenderLayerType = type
        return this
    }

    override fun setDefault(of: Block): BlockRenderDefinition {
        if (inherits) {
            val definition = DefinitionRegistry.getDefaultBlockDefinition(of, category)
            this.merge(definition)
        }
        return this
    }

    override fun merge(other: Definition<Block>?): Definition<Block> {
        if (other == null || other !is BlockRenderDefinition) return this

        blockRenderLayerType = if (blockRenderLayerType.isUnset) other.blockRenderLayerType else blockRenderLayerType
        return super.merge(other)
    }

    override fun toString(): String = "BlockRenderDefinition(blockRenderLayerType=$blockRenderLayerType)"

    override val empty: Boolean
        get() = this == EMPTY

    companion object {
        private val EMPTY = BlockRenderDefinition()

        fun of(block: Block): BlockRenderDefinition {
            val registry = RenderDefinitionRegistry.fromId(Registries.BLOCK.getId(block).namespace) ?: return BlockRenderDefinition()
            val definition = registry.getBlockDefinition(block)
            return definition
        }

        fun ofInheritable(): BlockRenderDefinition {
            val definition = BlockRenderDefinition()
            definition.inherits = true
            return definition
        }
    }
}
