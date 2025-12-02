package net.lixir.vminus.entity

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier


data class EntityBaseAttribute(val value: Double, val attribute: RegistryEntry<EntityAttribute>) {
    companion object {
        val CODEC: Codec<EntityBaseAttribute> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.DOUBLE.fieldOf("value").forGetter { it.value },
                Identifier.CODEC.fieldOf("id")
                    .xmap(
                        { id ->
                            val entry = Registries.ATTRIBUTE.getEntry(Registries.ATTRIBUTE.get(id))
                                ?: throw IllegalArgumentException("Unknown attribute id: $id")
                            entry
                        },
                        { entry: RegistryEntry<EntityAttribute> ->
                            Registries.ATTRIBUTE.getId(entry.value())
                                ?: throw IllegalArgumentException("Unknown attribute registry entry: $entry")
                        }
                    ).forGetter { it.attribute }
            ).apply(instance) { value, attribute ->
                EntityBaseAttribute(value, attribute)
            }
        }
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, EntityBaseAttribute> = PacketCodec.ofStatic(
            { buf, attr ->
                buf.writeDouble(attr.value)
                val id = Registries.ATTRIBUTE.getId(attr.attribute.value())
                    ?: throw IllegalArgumentException("Unknown attribute registry entry: ${attr.attribute}")
                buf.writeIdentifier(id)
            },
            { buf ->
                val value = buf.readDouble()
                val id = buf.readIdentifier()
                val attribute = Registries.ATTRIBUTE.getEntry(Registries.ATTRIBUTE.get(id))
                    ?: throw IllegalArgumentException("Unknown attribute id: $id")

                EntityBaseAttribute(value, attribute)
            }
        )
    }
}
