package net.lixir.vminus.block

import net.lixir.vminus.Vminus.Companion.REGISTRY
import net.minecraft.block.AbstractBlock.Settings
import net.minecraft.block.Block
import net.minecraft.block.ShortPlantBlock

object VminusBlocks {
    val TEST: Block = REGISTRY.block("test", ShortPlantBlock(Settings.create()))

    fun initialize() {

    }
}
