package net.lixir.vminus.mixin.world;


import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.StructureWorldAccess;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkRegion.class)
public class ChunkRegionMixin {
    @Shadow
    @Final
    private ServerWorld world;

    @Inject(method = "setBlockState", at = @At("TAIL"))
    private void detour$setBlockState(BlockPos pos, @NotNull BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        int tick = state.getBlock().vminus$getWorldGenScheduledTick(world, state, pos);
        if (tick <= 0)
            return;
        ((StructureWorldAccess) this).scheduleBlockTick(pos, state.getBlock(), tick);
    }
}
