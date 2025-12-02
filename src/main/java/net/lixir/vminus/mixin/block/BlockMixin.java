package net.lixir.vminus.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.duck.VisionDuck;
import net.lixir.vminus.duck.block.VminusBlockDuck;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.property.BlockVisionProperties;
import net.lixir.vminus.vision.property.VisionPropertyRegistry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.item.ItemReplacement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Block.class)
public class BlockMixin implements VisionDuck, VminusBlockDuck {
    @Unique
    private final Block vminus$self = (Block) (Object) this;

    @Unique
    private Identifier vminus$visionId = null;

    @Override
    public int vminus$getWorldGenScheduledTick(World world, BlockState state, BlockPos pos) {
        return VminusBlockDuck.super.vminus$getWorldGenScheduledTick(world, state, pos);
    }

    @ModifyReturnValue(method = "getPickStack", at = @At("RETURN"))
    private ItemStack vminus$getCloneItemStack(ItemStack original) {
        ItemStack replacement = ItemReplacement.Companion.resolve(original);
        if (!replacement.isEmpty()) {
            return replacement;
        }
        return original;
    }

    @ModifyReturnValue(method = "getVelocityMultiplier", at = @At("RETURN"))
    private float vminus$getVelocityMultiplier(float original) {
        return Vision.Companion.getValue(vminus$self, BlockVisionProperties.INSTANCE.getJUMP_VELOCITY_MULTIPLIER(), original);
    }

    @ModifyReturnValue(method = "getSlipperiness", at = @At("RETURN"))
    private float vminus$getSlipperiness(float original) {
        return Vision.Companion.getValue(vminus$self, BlockVisionProperties.INSTANCE.getSLIPPERINESS(), original);
    }

    @ModifyReturnValue(method = "getJumpVelocityMultiplier", at = @At("RETURN"))
    private float vminus$getJumpFactor(float original) {
        return Vision.Companion.getValue(vminus$self, BlockVisionProperties.INSTANCE.getJUMP_VELOCITY_MULTIPLIER(), original);
    }

    @ModifyReturnValue(method = "getBlastResistance", at = @At("RETURN"))
    private float vminus$getExplosionResistance(float original) {
        return Vision.Companion.getValue(vminus$self, BlockVisionProperties.INSTANCE.getBLAST_RESISTANCE(), original);
    }

    @Override
    public void vminus$setVisionIdentifier(Identifier id) {
        this.vminus$visionId = id;
    }

    public @Nullable Identifier vminus$getVisionIdentifier() {
        return vminus$visionId;
    }

    @Override
    public @NotNull VisionType<?> vminus$getVisionType() {
        return VisionTypes.INSTANCE.getBLOCK();
    }
}
