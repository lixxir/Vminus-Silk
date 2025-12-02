package net.lixir.vminus.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.*


class ItemGroupOrder(
    val stackSnapshot: ItemStackSnapshot = ItemStackSnapshot(ItemStack.EMPTY),
    val targetSnapshot: ItemStackSnapshot = ItemStackSnapshot(ItemStack.EMPTY),
    val before: Boolean = false,
    val tag: TagKey<Item>? = null
) {

    companion object {
        val CODEC: Codec<ItemGroupOrder> = RecordCodecBuilder.create { instance ->
            instance.group(
                ItemStackSnapshot.CODEC.fieldOf("stack").forGetter { it.stackSnapshot },
                ItemStackSnapshot.CODEC.optionalFieldOf("target").forGetter { Optional.ofNullable(it.targetSnapshot) },
                Codec.BOOL.optionalFieldOf("before", false).forGetter { it.before },
                Identifier.CODEC.optionalFieldOf("tag").forGetter { Optional.ofNullable(it.tag?.id) }
            ).apply(instance) { stack, targetOpt, before, tagId ->
                val target = targetOpt.orElse(ItemStackSnapshot(ItemStack.EMPTY))
                val tagKey = tagId.orElse(null)?.let { TagKey.of(Registries.ITEM.key, it) }
                ItemGroupOrder(stack, target, before, tagKey)
            }
        }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, ItemGroupOrder> = PacketCodec.ofStatic(
            { buf, order ->
                ItemStackSnapshot.PACKET_CODEC.encode(buf, order.stackSnapshot)
                ItemStackSnapshot.PACKET_CODEC.encode(buf, order.targetSnapshot)

                buf.writeBoolean(order.before)

                order.tag?.id?.let {
                    buf.writeBoolean(true)
                    buf.writeIdentifier(it)
                } ?: buf.writeBoolean(false)
            },
            { buf ->
                val stackSnapshot = ItemStackSnapshot.PACKET_CODEC.decode(buf)
                val targetSnapshot = ItemStackSnapshot.PACKET_CODEC.decode(buf)

                val before = buf.readBoolean()

                val hasTag = buf.readBoolean()
                val tagKey = if (hasTag) {
                    val tagId = buf.readIdentifier()
                    TagKey.of(Registries.ITEM.key, tagId)
                } else null

                ItemGroupOrder(stackSnapshot, targetSnapshot, before, tagKey)
            }
        )


        fun before(item: Item, target: Item): ItemGroupOrder =
            ItemGroupOrder(ItemStackSnapshot(item), ItemStackSnapshot(target), before = true)

        fun after(item: Item, target: Item): ItemGroupOrder
            = ItemGroupOrder(ItemStackSnapshot(item), ItemStackSnapshot(target))

        fun tagBefore(tag: TagKey<Item>?, target: Item): ItemGroupOrder
            = ItemGroupOrder(targetSnapshot = ItemStackSnapshot(target), before = true, tag = tag)

        fun tagAfter(tag: TagKey<Item>?, target: Item): ItemGroupOrder
            = ItemGroupOrder(targetSnapshot = ItemStackSnapshot(target), tag = tag)

        fun beforeTag(item: Item, tag: TagKey<Item>?): ItemGroupOrder
            = ItemGroupOrder(stackSnapshot = ItemStackSnapshot(item), before = true, tag = tag)

        fun afterTag(item: Item, tag: TagKey<Item>?): ItemGroupOrder
            = ItemGroupOrder(stackSnapshot = ItemStackSnapshot(item), before = false, tag = tag)
    }
}
