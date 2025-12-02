package net.lixir.vminus.mixin.entity.effect;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.duck.VisionDuck;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.property.StatusEffectVisionProperties;
import net.lixir.vminus.vision.property.VisionPropertyRegistry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StatusEffect.class)
public class StatusEffectMixin implements VisionDuck {
    @Unique
    private final StatusEffect vminus$self = (StatusEffect) (Object) this;

    @Unique
    private Identifier vminus$visionIdentifier = null;

    @ModifyReturnValue(method = "getColor", at = @At("RETURN"))
    private int vMinus$getColor(int original) {
        return Vision.Companion.getValue(vminus$self, StatusEffectVisionProperties.INSTANCE.getCOLOR(), original);
    }

    @ModifyReturnValue(method = "getCategory", at = @At("RETURN"))
    private StatusEffectCategory vMinus$getCategory(StatusEffectCategory original) {
        return Vision.Companion.getValue(vminus$self, StatusEffectVisionProperties.INSTANCE.getCATEGORY(), original);
    }

    public void vminus$setVisionIdentifier(Identifier id) {
        this.vminus$visionIdentifier = id;
    }

    public Identifier vminus$getVisionIdentifier() {
        return vminus$visionIdentifier;
    }

    public VisionType<?> vminus$getVisionType() {
        return VisionTypes.INSTANCE.getSTATUS_EFFECT();
    }

    @Override
    public Vision vminus$getVision() {
        return Vision.Companion.get(this);
    }
}
