package net.lixir.vminus.client.definition.datagen

import net.minecraft.block.Block
import net.minecraft.util.Identifier

data class BlockDataGenDefinitionEntry(
    val block: Block,
    val blockDefinition: BlockDataGenDefinition
) {
    val modelTextureSuffix: String
        get() = blockDefinition.modelTextureSuffix

    val modelTextureOverride: Identifier
        get() = blockDefinition.modelTextureOverride

    companion object {
        fun of(block: Block) = BlockDataGenDefinitionEntry(block, BlockDataGenDefinition.of(block))
    }
}
