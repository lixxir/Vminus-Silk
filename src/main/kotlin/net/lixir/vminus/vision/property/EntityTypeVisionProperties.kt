package net.lixir.vminus.vision.property

import com.mojang.serialization.Codec
import net.lixir.vminus.vision.VisionTypes
import net.lixir.vminus.vision.property.VisionPropertyRegistry.register
import net.lixir.vminus.entity.EntityBaseAttribute
import net.minecraft.network.codec.PacketCodecs

object EntityTypeVisionProperties {
    /*
    val VARIANT: VisionProperty<VisionEntityVariant> =
        register(VisionTypes.ENTITY_TYPE, VisionProperty("variant", VisionSerializers.ENTITY_VARIANT))

     */
    val BAN: VisionProperty<Boolean> =
        register(VisionTypes.ENTITY_TYPE, VisionProperty("ban", Codec.BOOL, PacketCodecs.BOOL))
    val SILENT: VisionProperty<Boolean> =
        register(VisionTypes.ENTITY_TYPE, VisionProperty("silent", Codec.BOOL, PacketCodecs.BOOL))
    val DAMPENS_VIBRATION: VisionProperty<Boolean> =
        register(VisionTypes.ENTITY_TYPE, VisionProperty("dampens_vibration", Codec.BOOL, PacketCodecs.BOOL))
    val BASE_ATTRIBUTE: VisionProperty<EntityBaseAttribute> =
        register(VisionTypes.ENTITY_TYPE, VisionProperty("base_attribute", EntityBaseAttribute.CODEC, EntityBaseAttribute.PACKET_CODEC, listable = true))

    fun initialize() {

    }
}