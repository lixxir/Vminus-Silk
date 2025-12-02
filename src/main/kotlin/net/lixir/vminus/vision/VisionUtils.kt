package net.lixir.vminus.vision

import net.lixir.vminus.data.server.bans.BannedRecipeManager
import net.lixir.vminus.duck.VisionDuck
import net.lixir.vminus.item.ItemReplacement
import net.lixir.vminus.vision.Vision.Companion.get
import net.lixir.vminus.vision.Vision.Companion.getValue
import net.lixir.vminus.vision.condition.VisionContext
import net.lixir.vminus.vision.property.ItemVisionProperties
import net.lixir.vminus.vision.property.VisionProperty
import net.lixir.vminus.vision.property.VisionPropertyRegistry
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.registry.DynamicRegistryManager
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.util.*

object VisionUtils {
    fun isRecipeBanned(recipe: Recipe<*>, id: Identifier?, dynamicRegistryManager: DynamicRegistryManager?): Boolean {
        if (recipe.ingredients.isEmpty()) return true

        if (BannedRecipeManager.INSTANCE.isBanned(id)) return true

        val result = recipe.getResult(dynamicRegistryManager)
        if (isBanned(result.item)) return true

        return recipe.ingredients.stream()
            .flatMap { ing -> Arrays.stream(ing.matchingStacks) }
            .map { stack -> stack.item }
            .allMatch { item -> isBanned(item) }
    }


    @Deprecated("")
    fun <T> getOverrideValue(
        visionDuck: VisionDuck,
        visionProperty: VisionProperty<T>,
        visionContext: VisionContext?
    ): T? {
        val vision = get(visionDuck)
        return vision.getValue(visionProperty, visionContext)
    }

    @Deprecated("")
    fun <T> tryOverride(firstType: T, otherType: T?): T {
        if (otherType != null) {
            return otherType
        }
        return firstType
    }

    @Deprecated("")
    fun <T> tryOverride(
        originalValue: T,
        visionDuck: VisionDuck,
        visionProperty: VisionProperty<T>,
        visionContext: VisionContext?
    ): T {
        val value = getOverrideValue(visionDuck, visionProperty, visionContext)
        return value ?: originalValue
    }

    @Deprecated("")
    fun <T> tryOverride(
        cir: CallbackInfoReturnable<T>,
        visionDuck: VisionDuck,
        visionProperty: VisionProperty<T>,
        visionContext: VisionContext?
    ): T? {
        val value = getOverrideValue(visionDuck, visionProperty, visionContext)
        if (value != null) {
            cir.returnValue = value
        }
        return value
    }

    fun isItemBannedOrReplaced(stack: ItemStack?, replacement: ItemReplacement?): Boolean {
        return isItemBannedOrReplaced(replacement, isBanned(stack))
    }

    fun isItemBannedOrReplaced(stack: ItemStack?, banned: Boolean): Boolean {
        val replacement = ItemReplacement.from(stack!!)
        return isItemBannedOrReplaced(replacement, banned)
    }

    fun isItemBannedOrReplaced(stack: ItemStack?): Boolean {
        val replacement = ItemReplacement.from(stack!!)
        return isItemBannedOrReplaced(replacement, isBanned(stack))
    }

    fun isItemBannedOrReplaced(replacement: ItemReplacement?, banned: Boolean): Boolean {
        return (replacement != null) || banned
    }

    @Deprecated("")
    fun <T> tryCancel(
        ci: CallbackInfo,
        visionDuck: VisionDuck,
        visionProperty: VisionProperty<T>,
        visionContext: VisionContext?
    ): T? {
        val value = getOverrideValue(visionDuck, visionProperty, visionContext)
        if (value != null) {
            ci.cancel()
        }
        return value
    }

    fun filterIngredient(stack: ItemStack, replacedItems: MutableList<ItemStack?>) {
        val replacement = ItemReplacement.from(stack)

        if (replacement != null) {
            replacedItems.add(replacement.itemStack())
            return
        }

        if (!isItemBannedOrReplaced(stack, replacement)) {
            replacedItems.add(stack.copy())
        }
    }

    @Deprecated("")
    fun isBanned(item: Item): Boolean {
        val ban = getValue(item, ItemVisionProperties.BAN)
        val replacement = getValue(item, ItemVisionProperties.REPLACE)
        return java.lang.Boolean.TRUE == ban && ItemReplacement.resolve(replacement).isEmpty
    }

    @Deprecated("")
    fun isBanned(stack: ItemStack?): Boolean {
        if (stack == null || stack.isEmpty) return false
        val ban = getValue(stack, ItemVisionProperties.BAN)
        val replacement = getValue(stack, ItemVisionProperties.REPLACE)
        return java.lang.Boolean.TRUE == ban && ItemReplacement.resolve(replacement).isEmpty
    }
}

