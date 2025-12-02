package net.lixir.vminus.vision.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record VisionEntityAttribute(Boolean remove, Boolean replace, EntityAttributeModifier modifier,
                                    EntityAttribute attribute, @Nullable EquipmentSlot equipmentSlot) {

    public static @NotNull VisionEntityAttribute remove(EntityAttribute attribute) {
        return new VisionEntityAttribute(true, false, null, attribute, null);
    }

    public static @NotNull VisionEntityAttribute replace(EntityAttribute attribute) {
        return new VisionEntityAttribute(false, true, null, attribute, null);
    }

    public static @NotNull VisionEntityAttribute full(EntityAttributeModifier modifier, EntityAttribute attribute, EquipmentSlot slot) {
        return new VisionEntityAttribute(false, false, modifier, attribute, slot);
    }

    public static @NotNull VisionEntityAttribute full(EntityAttributeModifier modifier, EntityAttribute attribute) {
        return full(modifier, attribute, null);
    }

    public static @NotNull VisionEntityAttribute removeAndReplace(EntityAttributeModifier modifier, EntityAttribute attribute, EquipmentSlot slot) {
        return new VisionEntityAttribute(true, true, modifier, attribute, slot);
    }

    public static @NotNull VisionEntityAttribute removeAndReplace(EntityAttributeModifier modifier, EntityAttribute attribute) {
        return removeAndReplace(modifier, attribute, null);
    }

    public static @NotNull VisionEntityAttribute withModifier(EntityAttribute attribute, EquipmentSlot slot, Identifier identifier, double amount, EntityAttributeModifier.Operation operation) {
        EntityAttributeModifier modifier = new EntityAttributeModifier(identifier, amount, operation);
        return new VisionEntityAttribute(false, false, modifier, attribute, slot);
    }

    public static @NotNull VisionEntityAttribute withModifier(EntityAttribute attribute, EquipmentSlot slot, Identifier identifier, double amount) {
        return withModifier(attribute, slot, identifier, amount, EntityAttributeModifier.Operation.ADD_VALUE);
    }

    public static @NotNull VisionEntityAttribute withModifier(EntityAttribute attribute, Identifier identifier, double amount) {
        return withModifier(attribute, null, identifier, amount, EntityAttributeModifier.Operation.ADD_VALUE);
    }

    public static @NotNull VisionEntityAttribute removeAndReplaceWithModifier(EntityAttribute attribute, EquipmentSlot slot, Identifier identifier, double amount, EntityAttributeModifier.Operation operation) {
        EntityAttributeModifier modifier = new EntityAttributeModifier(identifier, amount, operation);
        return new VisionEntityAttribute(true, true, modifier, attribute, slot);
    }

    public static @NotNull VisionEntityAttribute removeAndReplaceWithModifier(EntityAttribute attribute, Identifier identifier, double amount, EntityAttributeModifier.Operation operation) {
        return removeAndReplaceWithModifier(attribute, null, identifier, amount, operation);
    }

    public static @NotNull VisionEntityAttribute removeAndReplaceWithModifier(EntityAttribute attribute, EquipmentSlot slot, Identifier identifier, double amount) {
        return removeAndReplaceWithModifier(attribute, slot, identifier, amount, EntityAttributeModifier.Operation.ADD_VALUE);
    }

    public static @NotNull VisionEntityAttribute removeAndReplaceWithModifier(EntityAttribute attribute, Identifier identifier, double amount) {
        return removeAndReplaceWithModifier(attribute, null, identifier, amount, EntityAttributeModifier.Operation.ADD_VALUE);
    }
}
