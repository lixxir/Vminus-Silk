package net.lixir.vminus.serialization

import com.mojang.serialization.Codec
import net.minecraft.nbt.NbtOps
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec

object VminusCodecUtil {
    fun <E : Enum<E>> enumCodec(enumClass: Class<E>): Codec<E> {
        val values = enumClass.enumConstants

        return Codec.STRING.xmap(
            { str ->
                values.firstOrNull { it.name.equals(str, ignoreCase = true) }
                    ?: throw IllegalArgumentException("$str is not a valid value for enum ${enumClass.simpleName}")
            },
            { enumVal -> enumVal.name.lowercase() }
        )
    }
    fun <E : Enum<E>> enumPacketCodec(enumClass: Class<E>): PacketCodec<RegistryByteBuf, E> {
        val values = enumClass.enumConstants

        return PacketCodec.ofStatic(
            { buf, value ->
                buf.writeVarInt(value.ordinal)
            },
            { buf ->
                val ordinal = buf.readVarInt()
                if (ordinal !in values.indices)
                    throw IllegalStateException("Invalid enum ordinal $ordinal for ${enumClass.simpleName}")
                values[ordinal]
            }
        )
    }
}
