package net.lixir.vminus.client.data

import net.lixir.vminus.client.datagen.provider.VModelProvider
import net.lixir.vminus.client.definition.datagen.BlockDatagenDefinition
import net.minecraft.block.Block
import net.minecraft.data.client.BlockStateModelGenerator

/**
 * Represents a type of block model definition.
 * Implementations (usually enums) define how models are generated
 * for blocks and their associated items during data generation.
 */
interface BlockModelType : ModelType<Block, VModelProvider, BlockStateModelGenerator, BlockDatagenDefinition> {
    val itemModelType: ItemModelType?

    override val isEmpty: Boolean
        get() = this.identifier == BlockModelTypes.NONE.id || isUnset

    override val isUnset: Boolean
        get() = this.identifier == BlockModelTypes.UNSET.id
}
