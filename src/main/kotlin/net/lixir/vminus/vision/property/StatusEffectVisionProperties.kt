package net.lixir.vminus.vision.property

import com.mojang.serialization.Codec
import net.lixir.vminus.serialization.VminusCodecs
import net.lixir.vminus.serialization.VminusPacketCodecs
import net.lixir.vminus.vision.VisionTypes
import net.lixir.vminus.vision.property.VisionPropertyRegistry.register
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.network.codec.PacketCodecs

object StatusEffectVisionProperties {
    val CATEGORY: VisionProperty<StatusEffectCategory> = register(
        VisionTypes.STATUS_EFFECT,
        VisionProperty("category", VminusCodecs.STATUS_EFFECT_CATEGORY, VminusPacketCodecs.STATUS_EFFECT_CATEGORY)
    )
    val COLOR: VisionProperty<Int> =
        register(VisionTypes.STATUS_EFFECT, VisionProperty("color", VminusCodecs.HEX_COLOR, VminusPacketCodecs.HEX_COLOR))
    val BAN: VisionProperty<Boolean> =
        register(VisionTypes.STATUS_EFFECT, VisionProperty("ban", Codec.BOOL, PacketCodecs.BOOL))

    fun initialize() {

    }
}