package net.lixir.vminus.client.data

import net.lixir.vminus.client.datagen.provider.VModelProvider
import net.lixir.vminus.client.definition.datagen.BlockDataGenDefinitionEntry
import net.minecraft.block.Block
import net.minecraft.data.client.BlockStateModelGenerator

/**
 * Default block model types provided by VMinus.
 */
enum class BlockModelTypes(
    override val id: String,
    override val action: (BlockDataGenDefinitionEntry, VModelProvider, BlockStateModelGenerator) -> Unit,
    override val itemModelType: ItemModelType
) : BlockModelType {

    UNSET("unset", { _, _, _ -> }, ItemModelTypes.UNSET),
    NONE("none", { _, _, _ -> }, ItemModelTypes.UNSET),
    CUBE_ALL(
        "cube_all",
        { entry, provider, generator ->
            provider.cubeAll(entry, generator)
        },
        ItemModelTypes.PARENT_BLOCK
    );

    override fun apply(type: Block, provider: VModelProvider, generator: BlockStateModelGenerator) {
        action(BlockDataGenDefinitionEntry.of(type), provider, generator)
    }

    override fun asString(): String = id

    override fun toString(): String = "BlockModelType[$id]"
}
