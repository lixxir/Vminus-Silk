package net.lixir.vminus.vision.property

import com.mojang.serialization.Codec
import net.lixir.vminus.vision.VisionTypes
import net.lixir.vminus.vision.property.VisionPropertyRegistry.register
import net.lixir.vminus.item.ItemGroupOrder
import net.lixir.vminus.item.ItemReplacement
import net.lixir.vminus.item.ItemStackSnapshot
import net.lixir.vminus.serialization.VminusPacketCodecs
import net.minecraft.network.codec.PacketCodecs

object ItemGroupVisionProperties {
    val ICON: VisionProperty<ItemStackSnapshot> =
        of(VisionProperty("icon", ItemStackSnapshot.CODEC, ItemStackSnapshot.PACKET_CODEC))
    val HIDE: VisionProperty<Boolean> =
        of(VisionProperty("hide", Codec.BOOL, PacketCodecs.BOOL))
    val ORDER: VisionProperty<ItemGroupOrder> =
        of(VisionProperty("order", ItemGroupOrder.CODEC, ItemGroupOrder.PACKET_CODEC, listable = true))
    val REMOVE: VisionProperty<ItemReplacement> =
        of(VisionProperty("remove", ItemReplacement.CODEC, ItemReplacement.PACKET_CODEC, listable = true))

    fun <T> of(visionProperty: VisionProperty<T>): VisionProperty<T> {
        return register(VisionTypes.ITEM_GROUP, visionProperty)
    }
    
    fun initialize() {

    }
}