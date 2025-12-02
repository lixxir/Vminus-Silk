package net.lixir.vminus.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.duck.VisionDuck;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.property.EntityTypeVisionProperties;
import net.lixir.vminus.vision.property.VisionPropertyRegistry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin implements VisionDuck {
    @Unique
    private final Entity vminus$self = (Entity) (Object) this;

    @Shadow
    public abstract EntityType<?> getType();

    @ModifyReturnValue(method = "isSilent", at = @At("RETURN"))
    private boolean vminus$isSilent(boolean original) {
        return Vision.Companion.getValue(vminus$self, EntityTypeVisionProperties.INSTANCE.getSILENT(), original);
    }

    @ModifyReturnValue(method = "occludeVibrationSignals", at = @At("RETURN"))
    private boolean vminus$occludeVibrationSignals(boolean original) {
        return Vision.Companion.getValue(vminus$self, EntityTypeVisionProperties.INSTANCE.getDAMPENS_VIBRATION(), original);
    }

    public Identifier vminus$getVisionIdentifier() {
        return this.getType().vminus$getVisionIdentifier();
    }

    public VisionType<?> vminus$getVisionType() {
        return VisionTypes.INSTANCE.getENTITY_TYPE();
    }
}
