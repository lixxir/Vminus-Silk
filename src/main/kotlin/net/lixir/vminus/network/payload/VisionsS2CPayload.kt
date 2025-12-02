package net.lixir.vminus.network.payload


import net.lixir.vminus.Vminus
import net.lixir.vminus.vision.Vision
import net.lixir.vminus.vision.VisionType
import net.lixir.vminus.vision.VisionTypes
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

data class VisionsS2CPayload(
    val visionTypeId: String,
    val groupedVisions: List<Pair<List<Identifier>, Vision>>
) : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload?> = ID

    companion object {
        private val VISIONS_ID: Identifier = Identifier.of(Vminus.ID, "clientbound_visions")

        val ID: CustomPayload.Id<VisionsS2CPayload?> = CustomPayload.Id(VISIONS_ID)

        val CODEC: PacketCodec<in RegistryByteBuf, VisionsS2CPayload?> = PacketCodec.ofStatic(
            { buf: RegistryByteBuf, payload: VisionsS2CPayload? ->
                if (payload == null) return@ofStatic

                buf.writeString(payload.visionTypeId)
                buf.writeVarInt(payload.groupedVisions.size)

                for ((ids, vision) in payload.groupedVisions) {
                    buf.writeVarInt(ids.size)
                    ids.forEach { buf.writeIdentifier(it) }

                    vision.writeToBuffer(buf, VisionTypes.get(payload.visionTypeId)!!)
                }
            },
            { buf: RegistryByteBuf ->
                val visionTypeId = buf.readString()
                val visionType = VisionTypes.get(visionTypeId)
                    ?: throw IllegalStateException("Unknown VisionType: $visionTypeId")

                val groupCount = buf.readVarInt()
                val grouped = mutableListOf<Pair<List<Identifier>, Vision>>()

                repeat(groupCount) {
                    val idCount = buf.readVarInt()
                    val ids = mutableListOf<Identifier>()
                    repeat(idCount) { ids.add(buf.readIdentifier()) }

                    val vision = Vision.readVisionFromBuffer(buf, visionType)
                    grouped.add(Pair(ids.toList(), vision))
                }

                VisionsS2CPayload(visionTypeId, grouped)
            }
        )
    }
}
