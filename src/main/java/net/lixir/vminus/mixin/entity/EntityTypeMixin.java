package net.lixir.vminus.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.duck.VisionDuck;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.property.EntityTypeVisionProperties;
import net.lixir.vminus.vision.property.VisionPropertyRegistry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityType.class)
public class EntityTypeMixin implements VisionDuck {
    @Unique
    private final EntityType<?> vMinus$self = (EntityType<?>) (Object) this;
    @Unique
    private Identifier vminus$visionIdentifier = null;

    @ModifyReturnValue(method = "isSummonable", at = @At("RETURN"))
    private boolean vminus$isSummonable(boolean original) {
        if (Boolean.TRUE.equals(Vision.Companion.getValue(vMinus$self, EntityTypeVisionProperties.INSTANCE.getBAN())))
            return false;
        return original;
    }

    public void vminus$setVisionIdentifier(Identifier id) {
        this.vminus$visionIdentifier = id;
    }

    public Identifier vminus$getVisionIdentifier() {
        return vminus$visionIdentifier;
    }

    public VisionType<?> vminus$getVisionType() {
        return VisionTypes.INSTANCE.getENTITY_TYPE();
    }
}
