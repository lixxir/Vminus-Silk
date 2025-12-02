package net.lixir.vminus.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.lixir.vminus.vision.Vision.Companion.getValue
import net.lixir.vminus.vision.condition.VisionContext
import net.lixir.vminus.vision.property.ItemVisionProperties
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.*
import java.util.function.Consumer

data class ItemReplacement(
    private val stackSnapshot: ItemStackSnapshot? = null,
    val tag: TagKey<Item>? = null,
    val conditions: Conditions? = null
) {
    constructor(item: Item) : this(stackSnapshot = ItemStackSnapshot(item.defaultStack))

    fun itemStack(): ItemStack = stackSnapshot?.get()?.copy() ?: ItemStack.EMPTY.copy()

    override fun toString(): String = "ItemReplacement[itemStack=$stackSnapshot, tag=$tag]"

    enum class ReplaceType {
        REPLACE,
        BAN,
        NONE
    }

    class Conditions private constructor() {
        private val blockDropsType = ReplaceType.REPLACE

        companion object {
            fun of(): Conditions {
                return Conditions()
            }
        }
    }

    companion object {
        private val NORMAL_CONDITIONS: Conditions = Conditions.of()

        val CODEC: Codec<ItemReplacement> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.optionalFieldOf("item")
                    .forGetter { rep -> Optional.ofNullable(Registries.ITEM.getId(rep.itemStack().item)) },
                Codec.INT.optionalFieldOf("count", 1)
                    .forGetter { rep -> rep.itemStack().count },
                Identifier.CODEC.optionalFieldOf("tag")
                    .forGetter { rep -> Optional.ofNullable(rep.tag?.id) }
            ).apply(instance) { idOpt, count, tagId ->
                val item = idOpt.map { Registries.ITEM.get(it) }
                    .orElseThrow { IllegalStateException("Item not found for replacement") }
                val tagKey: TagKey<Item>? = tagId?.orElse(null)?.let { TagKey.of(Registries.ITEM.key, it) }

                ItemReplacement(ItemStackSnapshot(ItemStack(item, count)), tagKey)
            }
        }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, ItemReplacement> =
            PacketCodec.ofStatic(
                { buf, replacement ->
                    val stack = replacement.itemStack()
                    val itemId = Registries.ITEM.getId(stack.item)
                    val tagId = replacement.tag?.id

                    buf.writeIdentifier(itemId)
                    buf.writeVarInt(stack.count)

                    buf.writeBoolean(tagId != null)
                    if (tagId != null) buf.writeIdentifier(tagId)
                },
                { buf ->
                    val item = Registries.ITEM.get(buf.readIdentifier())
                    val count = buf.readVarInt()

                    val hasTag = buf.readBoolean()
                    val tagKey = if (hasTag) {
                        val tagId = buf.readIdentifier()
                        TagKey.of(Registries.ITEM.key, tagId)
                    } else null

                    ItemReplacement(ItemStackSnapshot(ItemStack(item, count)), tagKey)
                }
            )

        fun from(stack: ItemStack, visionContext: VisionContext = VisionContext(stack)): ItemReplacement? {
            var replacement = getValue(stack, ItemVisionProperties.REPLACE, visionContext, null)
            if (replacement != null)
                replacement = ItemReplacement(ItemStackSnapshot(replacement.itemStack().also { itemStack ->
                    itemStack.count = stack.count
                }))
            return replacement
        }

        fun resolve(stack: ItemStack): ItemStack {
            return resolve(from(stack, VisionContext(stack)))
        }

        fun resolve(replacement: ItemReplacement?): ItemStack {
            if (replacement == null) return ItemStack.EMPTY

            val itemStack = replacement.itemStack()
            val tag = replacement.tag

            if (!itemStack.isEmpty) {
                return itemStack
            } else if (tag != null) {
                for (item in Registries.ITEM) {
                    if (item.registryEntry.isIn(tag)) {
                        return item.defaultStack
                    }
                }
                return ItemStack.EMPTY
            } else {
                return ItemStack.EMPTY
            }
        }

        fun tryReplace(original: ItemStack, apply: Consumer<ItemStack?>): Boolean {
            val replacement = getValue(original, ItemVisionProperties.REPLACE)
            val replaced = resolve(replacement)
            if (!replaced.isEmpty) {
                replaced.count = original.count
                apply.accept(replaced)
                return true
            }
            return false
        }
    }
}
