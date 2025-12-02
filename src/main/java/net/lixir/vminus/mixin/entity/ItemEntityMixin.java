package net.lixir.vminus.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.property.ItemVisionProperties;
import net.lixir.vminus.vision.property.VisionPropertyRegistry;
import net.lixir.vminus.item.ItemReplacement;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Unique
    private final ItemEntity vminus$self = (ItemEntity) (Object) this;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyArg(
            method = "setStack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/data/DataTracker;set(Lnet/minecraft/entity/data/TrackedData;Ljava/lang/Object;)V"
            ),
            index = 1
    )
    private Object vMinus$replaceItem(Object original) {
        if (original instanceof ItemStack stack) {
            ItemStack replaced = ItemReplacement.Companion.resolve(stack);
            if (!replaced.isEmpty())
                return replaced;
            if (stack.vminus$isBanned())
                return ItemStack.EMPTY;
        }
        return original;
    }
}
