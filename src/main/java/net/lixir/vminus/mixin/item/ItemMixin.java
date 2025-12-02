package net.lixir.vminus.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.lixir.vminus.duck.VisionDuck;
import net.lixir.vminus.duck.item.VminusItemDuck;
import net.lixir.vminus.util.ComponentUtils;
import net.lixir.vminus.vision.Vision;
import net.lixir.vminus.vision.property.ItemVisionProperties;
import net.lixir.vminus.vision.property.VisionPropertyRegistry;
import net.lixir.vminus.vision.VisionType;
import net.lixir.vminus.vision.VisionTypes;
import net.lixir.vminus.item.ItemReplacement;
import net.lixir.vminus.item.ItemStackSnapshot;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.UseAction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin implements VisionDuck, VminusItemDuck {
    @Unique
    private final Item vminus$self = (Item) (Object) this;
    @Unique
    private Identifier vminus$visionIdentifier = null;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Unique
    private static void addComponent(ComponentMap.@NotNull Builder builder, @NotNull Component<?> component) {
        builder.add((ComponentType) component.type(), component.value());
    }

    @Override
    public boolean vminus$isBanned() {
        Boolean ban = Vision.Companion.getValue(vminus$self, ItemVisionProperties.INSTANCE.getBAN(), false);
        ItemReplacement replacement = Vision.Companion.getValue(vminus$self, ItemVisionProperties.INSTANCE.getREPLACE());
        return Boolean.TRUE.equals(ban) && ItemReplacement.Companion.resolve(replacement).isEmpty();
    }

    @ModifyReturnValue(method = "finishUsing", at = @At("RETURN"))
    private ItemStack vminus$finishUsing(ItemStack original, @Local(ordinal = 0, argsOnly = true) LivingEntity user) {
        ItemStackSnapshot useRemainder = Vision.Companion.getValue(original, ItemVisionProperties.INSTANCE.getUSE_REMAINDER());
        if (useRemainder == null)
            return original;
        ItemStack useRemainderStack = useRemainder.get();
        if (useRemainderStack.isEmpty())
            return original;

        if (user instanceof PlayerEntity player && !player.getAbilities().creativeMode) {
            if (!player.getInventory().insertStack(useRemainderStack)) {
                player.dropItem(useRemainderStack, false);
            }
        }

        return original;
    }
    @ModifyReturnValue(method = "getComponents", at = @At("RETURN"))
    private ComponentMap vminus$applyVisionComponents(ComponentMap original) {
        List<ComponentChanges> componentChanges = Vision.Companion.getValues(
                vminus$self,
                ItemVisionProperties.INSTANCE.getCOMPONENTS()
        );

        if (componentChanges.isEmpty()) return original;

        return ComponentUtils.INSTANCE.mergeComponentChanges(original, componentChanges);
    }

    @ModifyReturnValue(method = "getUseAction", at = @At("RETURN"))
    private UseAction vminus$getUseAction(UseAction original) {
        return Vision.Companion.getValue(vminus$self, ItemVisionProperties.INSTANCE.getUSE_ACTION(), original);
    }

    @ModifyReturnValue(method = "getEnchantability", at = @At("RETURN"))
    private int vminus$getEnchantability(int original) {
        return Vision.Companion.getValue(vminus$self, ItemVisionProperties.INSTANCE.getENCHANTABILITY(), original);
    }

    public void vminus$setVisionIdentifier(Identifier id) {
        this.vminus$visionIdentifier = id;
    }

    public Identifier vminus$getVisionIdentifier() {
        return vminus$visionIdentifier;
    }

    public VisionType<?> vminus$getVisionType() {
        return VisionTypes.INSTANCE.getITEM();
    }

    @Override
    public Vision vminus$getVision() {
        return Vision.Companion.get(this);
    }
}
