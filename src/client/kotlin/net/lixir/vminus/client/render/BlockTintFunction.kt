package net.lixir.vminus.client.render

import net.minecraft.block.BlockState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockRenderView

fun interface BlockTintFunction {
    fun apply(state: BlockState?, world: BlockRenderView?, pos: BlockPos?, index: Int): Int
}