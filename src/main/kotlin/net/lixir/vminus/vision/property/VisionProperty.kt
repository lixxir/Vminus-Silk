package net.lixir.vminus.vision.property

import com.mojang.serialization.Codec
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec

class VisionProperty<T>(
    val id: String,
    val codec: Codec<T>,
    val packetCodec: PacketCodec<in RegistryByteBuf, T>,
    val syncToClient: Boolean = true,
    val listable: Boolean = false
)
