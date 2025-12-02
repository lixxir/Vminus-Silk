package net.lixir.vminus.client.definition.datagen

import net.lixir.vminus.client.data.BlockModelTypes
import net.lixir.vminus.client.data.ItemModelTypes
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.lixir.vminus.client.definition.datagen.BlockDataGenDefinition.Companion.of as ofBlockDef
import net.lixir.vminus.client.definition.datagen.DataGenDefinitionGroup.Companion.of as ofGroup
import net.lixir.vminus.client.definition.datagen.ItemDataGenDefinition.Companion.of as ofItemDef

object VminusDataGenDefinitionGroups {
    val BLOCK_ITEM: DataGenDefinitionGroup<Item> = ofGroup(
        ofItemDef()
            .modelType(ItemModelTypes.PARENT_BLOCK)
    )
    val ITEM: DataGenDefinitionGroup<Item> = ofGroup(
        ofItemDef()
            .modelType(ItemModelTypes.BASIC)
    )
    val BLOCK: DataGenDefinitionGroup<Block> = ofGroup(
        ofBlockDef()
            .modelType(BlockModelTypes.CUBE_ALL)
    )
}
