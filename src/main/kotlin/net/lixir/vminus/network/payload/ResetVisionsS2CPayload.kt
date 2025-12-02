package net.lixir.vminus.network.payload

import net.lixir.vminus.Vminus
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

class ResetVisionsS2CPayload : CustomPayload {
    override fun getId(): CustomPayload.Id<out CustomPayload?> = ID

    companion object {
        private val RESET_VISIONS_ID: Identifier = Identifier.of(Vminus.ID, "clientbound_reset_visions")

        val ID: CustomPayload.Id<ResetVisionsS2CPayload?> = CustomPayload.Id(RESET_VISIONS_ID)
        val CODEC: PacketCodec<in RegistryByteBuf, ResetVisionsS2CPayload?> = PacketCodec.ofStatic(
            { _: RegistryByteBuf?, _: ResetVisionsS2CPayload? -> },
            { ResetVisionsS2CPayload() }
        )
    }
}
