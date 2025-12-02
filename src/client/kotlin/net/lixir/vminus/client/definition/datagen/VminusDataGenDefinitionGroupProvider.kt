package net.lixir.vminus.client.definition.datagen

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item

/**
 * Default definition groups provided by VMinus.
 *
 *
 * Groups can be replaced with the
 * [replace][DataGenDefinitionGroup.replace]
 * method during [run].
 *
 */
class VminusDataGenDefinitionGroupProvider : DataGenDefinitionGroupProvider() {
    override fun run() {
        assignClasses(VminusDataGenDefinitionGroups.BLOCK, Block::class.java)
        assignClasses(VminusDataGenDefinitionGroups.BLOCK_ITEM, BlockItem::class.java)
        assignClasses(VminusDataGenDefinitionGroups.ITEM, Item::class.java)
    }
}

