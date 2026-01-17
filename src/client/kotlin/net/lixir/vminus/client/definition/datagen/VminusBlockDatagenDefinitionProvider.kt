package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.data.BlockModelTypes
import net.minecraft.block.Block

/**
 * Default definitions provided by VMinus.
 *
 */
open class VminusBlockDatagenDefinitionProvider : BlockDatagenDefinitionProvider() {
    override fun run() {
        assign(Block::class.java, BlockDatagenDefinition().modelType(BlockModelTypes.CUBE_ALL))
    }
}

