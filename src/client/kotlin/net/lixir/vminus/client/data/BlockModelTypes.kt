package net.lixir.vminus.client.data

import net.lixir.vminus.Vminus
import net.lixir.vminus.client.datagen.provider.VModelProvider
import net.lixir.vminus.client.datagen.provider.VminusModelProvider
import net.lixir.vminus.client.definition.datagen.BlockDatagenDefinition
import net.minecraft.block.Block
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.util.Identifier

/**
 * Default block model types provided by VMinus.
 */
enum class BlockModelTypes(
    val id: Identifier,
    override val action: (Block, BlockDatagenDefinition, VModelProvider, BlockStateModelGenerator) -> Unit,
    override val itemModelType: ItemModelType
) : BlockModelType {

    UNSET(Vminus.id("unset"), { _, _, _, _ -> }, ItemModelTypes.UNSET),
    NONE(Vminus.id("none"), { _, _, _, _ -> }, ItemModelTypes.UNSET),
    CUBE_ALL(
        Vminus.id("cube_all"),
        { block, definition, provider, generator ->
            provider.cubeAll(block, definition, generator)
        },
        ItemModelTypes.PARENT_BLOCK
    );

    override fun getIdentifier() = id

    override fun toString(): String = "BlockModelType{$id}"
}
