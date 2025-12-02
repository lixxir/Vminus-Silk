package net.lixir.vminus.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.duck.VisionDuck;
import net.lixir.vminus.registry.tag.VminusBlockTags;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionUtils;
import net.lixir.vminus.vision.property.BlockVisionProperties;
import net.lixir.vminus.vision.property.VisionPropertyRegistry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;

import net.lixir.vminus.vision.condition.VisionContext;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin implements VisionDuck {
    @Unique
    private final AbstractBlock.AbstractBlockState vminus$self = (AbstractBlock.AbstractBlockState) (Object) this;


    @Shadow
    public abstract Block getBlock();

    @ModifyReturnValue(method = "allowsSpawning", at = @At("RETURN"))
    private boolean vminus$allowsSpawning(boolean original) {
        return original || getBlock().getDefaultState().isIn(VminusBlockTags.INSTANCE.getVALID_SPAWN());
    }

    @ModifyReturnValue(method = "getLuminance", at = @At("RETURN"))
    private int vminus$getLuminance(int original) {
        return Vision.Companion.getValue(getBlock(), BlockVisionProperties.INSTANCE.getLUMINANCE(), original);
    }

    @ModifyReturnValue(method = "hasEmissiveLighting", at = @At("RETURN"))
    private boolean vminus$hasEmissiveLighting(boolean original) {
        return Vision.Companion.getValue(getBlock(), BlockVisionProperties.INSTANCE.getEMISSIVE_LIGHTING(), original);
    }

    @ModifyReturnValue(method = "isOpaque", at = @At("RETURN"))
    private boolean vMinus$canOcclude(boolean original) {
        return Vision.Companion.getValue(getBlock(), BlockVisionProperties.INSTANCE.getOPAQUE(), original);
    }

    @ModifyReturnValue(method = "isSolidBlock", at = @At("RETURN"))
    private boolean vminus$isSolidBlock(boolean original) {
        return Vision.Companion.getValue(getBlock(), BlockVisionProperties.INSTANCE.getSOLID(), original);
    }

    @ModifyReturnValue(method = "getHardness", at = @At("RETURN"))
    private float getDestroySpeed(float original) {
        return Vision.Companion.getValue(getBlock(), BlockVisionProperties.INSTANCE.getHARDNESS(), original);
    }

    public @Nullable Identifier vminus$getVisionIdentifier() {
        return getBlock().vminus$getVisionIdentifier();
    }

    @Override
    public @NotNull VisionType<?> vminus$getVisionType() {
        return VisionTypes.INSTANCE.getBLOCK();
    }

    @ModifyReturnValue(method = "getSoundGroup", at = @At("RETURN"))
    private BlockSoundGroup vMinus$getSoundType(BlockSoundGroup original) {
        BlockSoundGroup override = VisionUtils.INSTANCE.getOverrideValue(this, BlockVisionProperties.INSTANCE.getSOUND(), new VisionContext(vminus$self));

        if (override != null) {
            float volume = override.getVolume() != 0.0F ? override.getVolume() : original.getVolume();
            float pitch = override.getPitch() != 0.0F ? override.getPitch() : original.getPitch();
            return new BlockSoundGroup(volume, pitch, override.getBreakSound(), override.getStepSound(), override.getPlaceSound(),
                    override.getHitSound(), override.getFallSound());
        }
        return original;
    }

    @Override
    public Vision vminus$getVision() {
        return Vision.Companion.get(this);
    }
}
