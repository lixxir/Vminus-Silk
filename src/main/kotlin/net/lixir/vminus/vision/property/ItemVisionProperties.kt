package net.lixir.vminus.vision.property

import com.mojang.serialization.Codec
import net.lixir.vminus.vision.VisionTypes
import net.lixir.vminus.vision.property.VisionPropertyRegistry.register
import net.lixir.vminus.item.ItemReplacement
import net.lixir.vminus.item.ItemStackSnapshot
import net.lixir.vminus.serialization.VminusCodecs
import net.lixir.vminus.serialization.VminusPacketCodecs
import net.minecraft.component.Component
import net.minecraft.component.ComponentChanges
import net.minecraft.component.ComponentMap
import net.minecraft.entity.EquipmentSlot
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.sound.SoundEvent
import net.minecraft.util.UseAction

object ItemVisionProperties {
    val COLLECT_SOUND: VisionProperty<SoundEvent> =
        register(VisionTypes.ITEM, VisionProperty("collect_sound", VminusCodecs.SOUND_EVENT, VminusPacketCodecs.SOUND_EVENT))
    val USE_ACTION: VisionProperty<UseAction> =
        register(VisionTypes.ITEM, VisionProperty("use_action", VminusCodecs.USE_ACTION, VminusPacketCodecs.USE_ACTION))
    val COMPONENTS: VisionProperty<ComponentChanges> =
        register(VisionTypes.ITEM, VisionProperty(
            "components",
            ComponentChanges.CODEC,
            ComponentChanges.PACKET_CODEC,
            listable = true
        ))
    val USE_TICKS: VisionProperty<Int> =
        register(VisionTypes.ITEM, VisionProperty("use_ticks", Codec.INT, PacketCodecs.VAR_INT))
    val MAX_USE_TICKS: VisionProperty<Int> =
        register(VisionTypes.ITEM, VisionProperty("max_use_ticks", Codec.INT, PacketCodecs.VAR_INT))
    val ENCHANTABILITY: VisionProperty<Int> =
        register(VisionTypes.ITEM, VisionProperty("enchantability", Codec.INT, PacketCodecs.VAR_INT))
    val GLINT: VisionProperty<Boolean> =
        register(VisionTypes.ITEM, VisionProperty("glint", Codec.BOOL, PacketCodecs.BOOL))
    val ENCHANTABLE: VisionProperty<Boolean> =
        register(VisionTypes.ITEM, VisionProperty("enchantable", Codec.BOOL, PacketCodecs.BOOL))
    val CAN_EQUIP: VisionProperty<Boolean> =
        register(VisionTypes.ITEM, VisionProperty("can_equip", Codec.BOOL, PacketCodecs.BOOL))
    val BAN: VisionProperty<Boolean> =
        register(VisionTypes.ITEM, VisionProperty("ban", Codec.BOOL, PacketCodecs.BOOL))
    val USE_REMAINDER: VisionProperty<ItemStackSnapshot> =
        register(VisionTypes.ITEM, VisionProperty("use_remainder", ItemStackSnapshot.CODEC, ItemStackSnapshot.PACKET_CODEC))
    val REPLACE: VisionProperty<ItemReplacement> =
        register(VisionTypes.ITEM, VisionProperty("replace", ItemReplacement.CODEC, ItemReplacement.PACKET_CODEC))
    val EQUIP_SLOT: VisionProperty<EquipmentSlot> =
        register(VisionTypes.ITEM, VisionProperty("equipment_slot", VminusCodecs.EQUIPMENT_SLOT, VminusPacketCodecs.EQUIPMENT_SLOT))

    fun initialize() {

    }
}