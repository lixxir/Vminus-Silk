package net.lixir.vminus.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.duck.VisionDuck;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.property.BlockVisionProperties;
import net.lixir.vminus.vision.property.VisionPropertyRegistry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin implements VisionDuck {
    @Shadow
    protected abstract Block asBlock();

    @ModifyReturnValue(method = "getLootTableKey", at = @At("RETURN"))
    private RegistryKey<LootTable> vminus$getLootTableKey(RegistryKey<LootTable> original) {
        Identifier identifier = Vision.Companion.getValue(asBlock(), BlockVisionProperties.INSTANCE.getLOOT_TABLE(), original.getValue());

        if (identifier != null)
            return RegistryKey.of(RegistryKeys.LOOT_TABLE, identifier);
        return original;
    }

    public @Nullable Identifier vminus$getVisionIdentifier() {
        return asBlock().vminus$getVisionIdentifier();
    }

    @Override
    public @NotNull VisionType<?> vminus$getVisionType() {
        return VisionTypes.INSTANCE.getBLOCK();
    }

    @Override
    public Vision vminus$getVision() {
        return Vision.Companion.get(this);
    }
}
