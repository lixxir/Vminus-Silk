package net.lixir.vminus.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.duck.VisionDuck;
import net.lixir.vminus.duck.item.VminusItemStackDuck;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.property.ItemVisionProperties;
import net.lixir.vminus.vision.property.VisionPropertyRegistry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements VisionDuck, VminusItemStackDuck {
    @Unique
    private final ItemStack vminus$self = (ItemStack) (Object) this;

    @Override
    public boolean vminus$isBanned() {
        if (vminus$self == null || vminus$self.isEmpty())
            return false;
        return vminus$self.getItem().vminus$isBanned();
    }

    @Shadow
    public abstract Item getItem();

    public @Nullable Identifier vminus$getVisionIdentifier() {
        return getItem().vminus$getVisionIdentifier();
    }

    @Override
    public @NotNull VisionType<?> vminus$getVisionType() {
        return VisionTypes.INSTANCE.getITEM();
    }

    @Override
    public Vision vminus$getVision() {
        return Vision.Companion.get(this);
    }

    /*
    @ModifyReturnValue(method = "getBarWidth", at = @At("RETURN"))
    private int detour$getBarWidth(int original) {
        Integer maxDamage = Vision.getValue(vMinus$self, VisionProperties.Items.MAX_DAMAGE);
        Patches out an issue with damage bars not scaling properly when a new durability is applied.
         Only do it with vision applied durability to prevent altering vanilla behavior.

        if (vMinus$self.isDamageableItem() && maxDamage != null && maxDamage > 0) {
            float durabilityRatio = 1.0F - ((float) vMinus$self.getDamageValue() / vMinus$self.getMaxDamage());
            int barWidth = (int) Math.floor(13.0F * durabilityRatio);
            return Math.min(barWidth, 13);
        }
        return original;
    }


    @ModifyReturnValue(method = "getBarColor", at = @At("RETURN"))
    private int vMinus$getBarColor(int original) {
        Integer maxDamage = Vision.getValue(vMinus$self, VisionProperties.Items.MAX_DAMAGE);

        if (vMinus$self.isDamageableItem() && maxDamage != null && maxDamage > 0) {
            float f = Math.max(0.0F, (maxDamage - (float)getDamageValue()) / maxDamage);
            return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
        }
        return original;
    }
    */

    @ModifyReturnValue(method = "isEnchantable", at = @At("RETURN"))
    private boolean vminus$isEnchantable(boolean original) {
        return Vision.Companion.getValue(vminus$self, ItemVisionProperties.INSTANCE.getENCHANTABLE(), original);
    }

    @ModifyReturnValue(method = "getMaxUseTime", at = @At("RETURN"))
    private int vminus$getMaxUseTime(int original) {
        return Vision.Companion.getValue(vminus$self, ItemVisionProperties.INSTANCE.getMAX_USE_TICKS(), original);
    }
}
