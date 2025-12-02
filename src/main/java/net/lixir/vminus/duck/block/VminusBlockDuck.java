package net.lixir.vminus.duck.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public interface VminusBlockDuck {
    default int vminus$getWorldGenScheduledTick(World world, BlockState state, BlockPos pos) {
        return -1;
    }
}
