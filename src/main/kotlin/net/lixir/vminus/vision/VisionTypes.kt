package net.lixir.vminus.vision

import net.minecraft.block.Block
import net.minecraft.entity.EntityType
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.registry.Registries
import org.jetbrains.annotations.Contract

object VisionTypes {
    private val REGISTRY: MutableMap<String, VisionType<*>?> = HashMap()
    val ITEM: VisionType<Item> = register(VisionType("item", Registries.ITEM))
    val ITEM_GROUP: VisionType<ItemGroup> = register(VisionType("item_group", Registries.ITEM_GROUP))
    val ENTITY_TYPE: VisionType<EntityType<*>> = register(VisionType("entity_type", Registries.ENTITY_TYPE))
    val STATUS_EFFECT: VisionType<StatusEffect> = register(VisionType("status_effect", Registries.STATUS_EFFECT))
    val BLOCK: VisionType<Block> = register(VisionType("block", Registries.BLOCK))

    fun initialize() {}

    @Contract("_ -> param1")
    fun <T> register(visionType: VisionType<T>): VisionType<T> {
        REGISTRY[visionType.id] = visionType
        return visionType
    }

    @JvmStatic
    fun get(id: String): VisionType<*>? {
        return REGISTRY.getOrDefault(id, null)
    }

    val all: MutableCollection<VisionType<*>?> get() = REGISTRY.values
}
