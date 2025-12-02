package net.lixir.vminus.item

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.lixir.vminus.util.ObjectSnapshot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import org.joml.Vector3f

data class ItemStackSnapshot(
    private val stack: ItemStack? = ItemStack.EMPTY
) : ObjectSnapshot<ItemStack> {
    constructor(item: Item) : this(item.defaultStack)

    fun isEmpty(): Boolean = stack?.isEmpty ?: true

    override fun get(): ItemStack = stack?.copy() ?: ItemStack.EMPTY

    companion object {
        val EMPTY: ItemStackSnapshot = ItemStackSnapshot()
        val CODEC: Codec<ItemStackSnapshot> = RecordCodecBuilder.create { instance ->
            instance.group(
                Identifier.CODEC.fieldOf("item")
                    .forGetter { Registries.ITEM.getId(it.get().item) },
                Codec.INT.optionalFieldOf("count", 1)
                    .forGetter { it.get().count },
            ).apply(instance) { id, count ->
                val item = Registries.ITEM.get(id)
                val stack = ItemStack(item, count)
                ItemStackSnapshot(stack)
            }
        }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, ItemStackSnapshot> =
            PacketCodec.ofStatic(
                { buf, snapshot ->
                    val stack = snapshot.get()
                    buf.writeIdentifier(Registries.ITEM.getId(stack.item))
                    buf.writeVarInt(stack.count)
                },
                { buf ->
                    val id = buf.readIdentifier()
                    val item = Registries.ITEM.get(id)
                    val count = buf.readVarInt()
                    ItemStackSnapshot(ItemStack(item, count))
                }
            )
    }
}
