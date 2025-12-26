package net.lixir.vminus.mixin.screen.slot;

import net.lixir.vminus.item.ItemReplacement;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @ModifyArg(method = "setStackNoCallbacks", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventory;setStack(ILnet/minecraft/item/ItemStack;)V"), index = 1)
    private ItemStack vminus$swapSetStack(ItemStack stack) {
        ItemStack replaced = ItemReplacement.Companion.resolve(stack);
        if (!replaced.isEmpty())
            return replaced;
        else if (stack.vminus$isBanned())
            return ItemStack.EMPTY.copy();
        return stack;
    }
}
