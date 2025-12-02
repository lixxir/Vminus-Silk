package net.lixir.vminus.duck;

import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.VisionType;
import net.minecraft.util.Identifier;

public interface VisionDuck {
    default void vminus$setVisionIdentifier(Identifier id) {

    }

    default Identifier vminus$getVisionIdentifier() {
        return null;
    }

    default VisionType<?> vminus$getVisionType() {
        return null;
    }

    default Vision vminus$getVision() {
        return Vision.Companion.get(this);
    }
}
